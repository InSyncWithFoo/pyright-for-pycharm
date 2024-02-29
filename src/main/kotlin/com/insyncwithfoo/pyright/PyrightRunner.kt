package com.insyncwithfoo.pyright

import com.insyncwithfoo.pyright.configuration.PyrightAllConfigurations
import com.intellij.execution.RunCanceledByUserException
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessHandler
import com.intellij.execution.process.ProcessOutput
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiFile
import com.jetbrains.python.packaging.IndicatedProcessOutputListener
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.jetbrains.annotations.SystemDependent
import java.io.File


private fun String.toFileIfItExists(projectPath: String? = null) =
    File(this)
        .let { if (it.isAbsolute) it else File(projectPath, this).canonicalFile }
        .takeIf { it.exists() }


private sealed class PyrightException(message: String) : Exception(message)


private class FatalException(message: String) : PyrightException(message)


private class InvalidConfigurationsException(message: String) : PyrightException(message)


private class InvalidParametersException(message: String) : PyrightException(message)


private enum class PyrightExitCode(val value: Int) {
    SUCCESS(0),
    ERRORS(1),
    FATAL(2),
    INVALID_CONFIG(3),
    INVALID_PARAMETERS(4);
    
    companion object {
        fun fromInt(value: Int) = entries.first { it.value == value }
    }
}


internal data class Command(
    val executable: @SystemDependent File,
    val target: File,
    val projectPath: String,
    val extraArguments: List<String>
) {
    
    private val fragments: List<String>
        get() = listOf(
            executable.path.toString(),
            *extraArguments.toTypedArray(),
            target.toString()
        )
    
    private val handler: CapturingProcessHandler
        get() = CapturingProcessHandler(getGeneralCommandLine())
    
    private fun getGeneralCommandLine(): GeneralCommandLine =
        GeneralCommandLine(fragments)
            .withWorkDirectory(projectPath)
            .withCharset(Charsets.UTF_8)
    
    private fun runWithIndicator(): ProcessOutput {
        val indicator = ProgressManager.getInstance().progressIndicator
        
        return with(handler) {
            when {
                indicator != null -> {
                    addProcessListener(IndicatedProcessOutputListener(indicator))
                    runProcessWithProgressIndicator(indicator)
                }
                
                else -> runProcess()
            }
        }
    }
    
    fun run(): String {
        val processOutput = runWithIndicator()
        
        return processOutput.run {
            if (isCancelled) {
                throw RunCanceledByUserException()
            }
            
            when (PyrightExitCode.fromInt(exitCode)) {
                PyrightExitCode.FATAL -> throw FatalException(stdout)
                PyrightExitCode.INVALID_CONFIG -> throw InvalidConfigurationsException(stdout)
                PyrightExitCode.INVALID_PARAMETERS -> throw InvalidParametersException(stdout)
                else -> stdout
            }
        }
    }
    
    companion object {
        fun create(
            configurations: PyrightAllConfigurations,
            file: PsiFile
        ): Command? {
            val project = file.project
            
            val projectPath = project.basePath ?: return null
            
            val executable = configurations.executable?.toFileIfItExists(projectPath) ?: return null
            val target = file.virtualFile.path.toFileIfItExists() ?: return null
            val configurationFile = configurations.configurationFile
            
            val projectSdk = ProjectRootManager.getInstance(project).projectSdk
            val pythonExecutable = projectSdk?.homePath ?: return null
            
            val extraArguments = listOf(
                "--outputjson",
                "--project", configurationFile ?: projectPath,
                "--pythonpath", pythonExecutable
            )
            
            return Command(executable, target, projectPath, extraArguments)
        }
    }
    
}


private class PyrightErrorReporter(private val logger: Logger) {
    
    fun reportException(exception: PyrightException) {
        logger.error(exception)
        Messages.showErrorDialog(ERROR_MESSAGE, ERROR_MESSAGE_TITLE)
    }
    
    companion object {
        private const val ISSUE_TRACKER_LINK = "https://github.com/insyncwithfoo/pyright-plugin/issues"
        private const val ERROR_MESSAGE_TITLE = "Pyright"
        private val ERROR_MESSAGE = """
            An error is encountered. Please report it at ${ISSUE_TRACKER_LINK}.
            See your <code>idea.log</code> file for raw output.
        """.trimIndent()
    }
    
}


internal class PyrightRunner(private val command: Command) {
    
    fun run(): PyrightOutput? {
        LOGGER.info("Running: $command")
        
        return try {
            runAndLog()
        } catch (_: RunCanceledByUserException) {
            null
        } catch (exception: FatalException) {
            ERROR_REPORTER.reportException(exception)
            null
        } catch (exception: InvalidParametersException) {
            ERROR_REPORTER.reportException(exception)
            null
        }
    }
    
    private fun runAndLog(): PyrightOutput {
        val output = command.run()
        val parsed = parseOutput(output)
        
        val minified = Json.encodeToString(serializer(), parsed)
        LOGGER.info("Output: $minified")
        
        return parsed
    }
    
    private fun parseOutput(response: String) =
        Json.decodeFromString<PyrightOutput>(response)
    
    companion object {
        private val LOGGER = Logger.getInstance(PyrightRunner::class.java)
        private val ERROR_REPORTER = PyrightErrorReporter(LOGGER)
    }
    
}
