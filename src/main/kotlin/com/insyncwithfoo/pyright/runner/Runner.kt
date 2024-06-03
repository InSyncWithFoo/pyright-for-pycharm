package com.insyncwithfoo.pyright.runner

import com.insyncwithfoo.pyright.PyrightOutput
import com.insyncwithfoo.pyright.addSimpleExpiringAction
import com.insyncwithfoo.pyright.configuration.AllConfigurations
import com.insyncwithfoo.pyright.configuration.PyrightConfigurable
import com.insyncwithfoo.pyright.createErrorNotification
import com.insyncwithfoo.pyright.fileEditorManager
import com.insyncwithfoo.pyright.message
import com.insyncwithfoo.pyright.pyrightConfigurations
import com.intellij.execution.RunCanceledByUserException
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.BaseState
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.platform.ide.progress.withBackgroundProgress
import com.intellij.testFramework.LightVirtualFile
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.io.path.name
import com.insyncwithfoo.pyright.configuration.application.Configurable as ApplicationConfigurable
import com.insyncwithfoo.pyright.configuration.project.Configurable as ProjectConfigurable


private typealias PyrightConfigurableClass = PyrightConfigurable<out BaseState>


private class TimeoutException(message: String = "Process timed out") : RuntimeException(message)


private class OpenTemporaryFileAction(
    text: String,
    private val fileName: String,
    private val content: String
) : NotificationAction(text) {
    
    override fun actionPerformed(event: AnActionEvent, notification: Notification) {
        val project = event.project!!
        val fileType = FileTypeManager.getInstance().getFileTypeByFileName(fileName)
        
        val openFileDescriptor = OpenFileDescriptor(
            project,
            LightVirtualFile(fileName, fileType, content)
        )
        
        project.fileEditorManager.openEditor(openFileDescriptor, true)
    }
    
}


@Serializable
private data class PyrightExceptionInfo(
    val type: String,
    val stdout: String,
    val stderr: String,
    val message: String
) {
    override fun toString() = Json.encodeToString(this)
}


private val PyrightException.info: PyrightExceptionInfo
    get() = PyrightExceptionInfo(
        type = this::class.simpleName!!,
        stdout = stdout,
        stderr = stderr,
        message = message!!
    )


private fun FileCommand.toJson() = Json.encodeToString(this)


private val AllConfigurations.configurableClassWithExecutable: Class<out PyrightConfigurableClass>
    get() = when {
        alwaysUseGlobal -> ProjectConfigurable::class.java
        projectExecutable != null -> ProjectConfigurable::class.java
        else -> ApplicationConfigurable::class.java
    }

private fun <T : PyrightConfigurableClass> Project.showSettingsDialog(toSelect: Class<T>) {
    ShowSettingsUtil.getInstance().showSettingsDialog(this, toSelect)
}


private fun Notification.addOpenSettingsAction(
    project: Project,
    configurableClass: Class<out PyrightConfigurableClass>
) {
    addSimpleExpiringAction(message("notifications.error.action.openSettings")) {
        project.showSettingsDialog(configurableClass)
    }
}


private fun Notification.addOpenTemporaryFileAction(text: String, fileName: String, content: String) {
    addAction(OpenTemporaryFileAction(text, fileName, content))
}


private fun Notifier.notifyPyrightException(exception: PyrightException) =
    notify { group, _ -> exception.createNotification(group) }


private fun Notifier.notifySerializationException(output: String) = notify { group, project ->
    val configurations = project.pyrightConfigurations
    val notification = group.createErrorNotification(
        title = message("notifications.error.invalidOutput.title"),
        content = when {
            output.isBlank() -> message("notifications.error.invalidOutput.body.blank")
            else -> message("notifications.error.invalidOutput.body")
        }
    )
    
    if (output.isNotBlank()) {
        notification.addOpenTemporaryFileAction(
            text = message(key = "notifications.error.action.seeOutputInEditor"),
            fileName = "output.txt",
            content = output
        )
    }
    notification.addOpenSettingsAction(project, configurations.configurableClassWithExecutable)
    
    notification
}


private fun Notifier.notifyProcessTimeout() = notify { group, project ->
    val newNotification = group.createErrorNotification(
        title = message("notifications.error.processTimeout.title"),
        content = message("notifications.error.processTimeout.body")
    )
    
    newNotification.addOpenSettingsAction(project, ApplicationConfigurable::class.java)
    
    newNotification
}


internal class PyrightRunner(private val project: Project) {
    
    private val notifier = Notifier(project)
    
    fun run(command: FileCommand): PyrightOutput? {
        LOGGER.info("Running: ${command.toJson()}")
        
        val output = runWithIndicator(command) ?: return null
        val parsed = parseOutputIfPossible(output) ?: return null
        
        return parsed.also { logMinified(it) }
    }
    
    private fun runWithIndicator(command: FileCommand) = runBlocking {
        val title = message("progress.runOnFile.title", command.target.name)
        
        withBackgroundProgress(project, title, cancellable = false) {
            command.getOutputGracefully()
        }
    }
    
    private fun FileCommand.getStdout(): String {
        val timeout = project.pyrightConfigurations.processTimeout
        val processOutput = this.run(timeout)
        
        return processOutput.run {
            if (isCancelled) {
                throw RunCanceledByUserException()
            }
            
            if (isTimeout) {
                throw TimeoutException()
            }
            
            when (PyrightExitCode.fromInt(exitCode)) {
                PyrightExitCode.FATAL -> throw FatalException(stdout, stderr)
                PyrightExitCode.INVALID_CONFIG -> throw InvalidConfigurationsException(stdout, stderr)
                PyrightExitCode.INVALID_PARAMETERS -> throw InvalidParametersException(stdout, stderr)
                else -> stdout
            }
        }
    }
    
    private fun FileCommand.getOutputGracefully(): String? {
        return try {
            this.getStdout()
        } catch (_: RunCanceledByUserException) {
            null
        } catch (exception: TimeoutException) {
            report(exception)
            null
        } catch (exception: PyrightException) {
            report(exception)
            
            when (exception) {
                is InvalidConfigurationsException -> exception.stdout
                is FatalException, is InvalidParametersException -> null
            }
        }
    }
    
    private fun logMinified(parsedOutput: PyrightOutput) {
        val minified = Json.encodeToString(parsedOutput)
        LOGGER.info("Output: $minified")
    }
    
    private fun parseOutputIfPossible(rawOutput: String): PyrightOutput? {
        return try {
            Json.decodeFromString<PyrightOutput>(rawOutput)
        } catch (exception: SerializationException) {
            report(exception, rawOutput)
            null
        }
    }
    
    private fun report(exception: PyrightException) {
        LOGGER.warn(exception)
        LOGGER.info("Exception properties: ${exception.info}")
        
        notifier.notifyPyrightException(exception)
    }
    
    private fun report(exception: SerializationException, rawOutput: String) {
        LOGGER.warn(exception)
        
        notifier.notifySerializationException(rawOutput)
    }
    
    private fun report(exception: TimeoutException) {
        LOGGER.warn(exception)
        
        notifier.notifyProcessTimeout()
    }
    
    companion object {
        private val LOGGER = Logger.getInstance(PyrightRunner::class.java)
    }
    
}
