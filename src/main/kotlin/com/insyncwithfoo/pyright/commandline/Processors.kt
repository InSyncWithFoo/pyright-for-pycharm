package com.insyncwithfoo.pyright.commandline

import com.insyncwithfoo.pyright.addOpenFileAction
import com.insyncwithfoo.pyright.addOpenPluginIssueTrackerAction
import com.insyncwithfoo.pyright.addOpenPyrightIssueTrackerAction
import com.insyncwithfoo.pyright.addSeeOutputActions
import com.insyncwithfoo.pyright.error
import com.insyncwithfoo.pyright.errorNotificationGroup
import com.insyncwithfoo.pyright.message
import com.insyncwithfoo.pyright.runThenNotify
import com.insyncwithfoo.pyright.warning
import com.intellij.execution.process.ProcessOutput
import com.intellij.openapi.project.Project
import com.intellij.platform.ide.progress.withBackgroundProgress
import kotlinx.coroutines.CoroutineScope


internal suspend fun <T> Project.runInBackground(
    title: String,
    cancellable: Boolean = true,
    action: suspend CoroutineScope.() -> T
) =
    withBackgroundProgress(this, title, cancellable, action)


internal enum class ExitCode(val value: Int) {
    SUCCESS(0),
    ERRORS(1),
    FATAL(2),
    INVALID_CONFIG(3),
    INVALID_PARAMETERS(4);
    
    companion object {
        fun fromInt(value: Int) = entries.firstOrNull { it.value == value }
    }
}


internal fun Project.reportErrorIfNecessary(output: ProcessOutput) {
    when (ExitCode.fromInt(output.exitCode)) {
        ExitCode.SUCCESS, ExitCode.ERRORS, null -> return
        ExitCode.FATAL -> reportFatalError(output)
        ExitCode.INVALID_CONFIG -> warnInvalidConfiguration(output)
        ExitCode.INVALID_PARAMETERS -> reportInvalidParameters(output)
    }
}


internal fun Project.reportFatalError(output: ProcessOutput) {
    val title = message("notifications.error.fatal.title")
    val content = message("notifications.error.fatal.body")
    
    errorNotificationGroup.error(title, content).runThenNotify(this) {
        addSeeOutputActions(output)
        addOpenPyrightIssueTrackerAction()
    }
}


internal fun Project.warnInvalidConfiguration(output: ProcessOutput) {
    // Greedy is necessary since a Linux path may contain double quotes:
    //   Config file "/b"az/pyrightconfig.json" could not be parsed.
    val quoted = "\"(.*)\"".toRegex()
    val configurationFilePath = quoted.find(output.stderr)!!.groups[1]!!.value
    
    val title = message("notifications.error.invalidConfigurations.title")
    val body = message("notifications.error.invalidConfigurations.body", configurationFilePath)
    
    errorNotificationGroup.warning(title, body).runThenNotify(this) {
        val actionText = message("notificationActions.openConfigurationFile")
        addOpenFileAction(actionText, configurationFilePath)
    }
}


internal fun Project.reportInvalidParameters(output: ProcessOutput) {
    val title = message("notifications.error.invalidCliOptions.title")
    val content = message("notifications.error.invalidCliOptions.body")
    
    errorNotificationGroup.error(title, content).runThenNotify(this) {
        addSeeOutputActions(output)
        addOpenPluginIssueTrackerAction()
    }
}
