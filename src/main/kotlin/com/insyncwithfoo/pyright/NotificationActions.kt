package com.insyncwithfoo.pyright

import com.insyncwithfoo.pyright.commandline.FileCommand
import com.insyncwithfoo.pyright.configurations.models.PanelBasedConfigurable
import com.intellij.execution.process.ProcessOutput
import com.intellij.notification.BrowseNotificationAction
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.util.ui.EmptyClipboardOwner
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection


internal fun Notification.addOpenPluginIssueTrackerAction(): Notification {
    val issueTrackerActionText = message("notificationActions.openPluginIssueTracker")
    val action = BrowseNotificationAction(
        issueTrackerActionText,
        "https://github.com/InSyncWithFoo/pyright-for-pycharm/issues"
    )
    
    return addAction(action)
}


internal fun Notification.addExpiringAction(text: String, action: () -> Unit) =
    addAction(NotificationAction.createSimpleExpiring(text, action))


internal fun Notification.addAction(text: String, action: () -> Unit) =
    addAction(NotificationAction.createSimple(text, action))


internal fun Notification.addCopyTextAction(text: String, content: String) {
    addAction(text) {
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(StringSelection(content), EmptyClipboardOwner.INSTANCE)
    }
}


internal fun Notification.addCopyTextAction(content: String) =
    addCopyTextAction(message("notificationActions.copyTextToClipboard"), content)


internal fun Notification.addCopyCommandAction(command: FileCommand) =
    addCopyTextAction(message("notificationActions.copyCommandToClipboard"), command.toString())


private fun <C : PanelBasedConfigurable<*>> Project.showSettingsDialog(toSelect: Class<C>) {
    ShowSettingsUtil.getInstance().showSettingsDialog(this, toSelect)
}


internal fun Notification.addOpenSettingsAction(
    project: Project,
    configurableClass: Class<out PanelBasedConfigurable<*>>
) {
    addExpiringAction(message("notificationActions.openSettings")) {
        project.showSettingsDialog(configurableClass)
    }
}


internal class OpenTemporaryFileAction(
    text: String,
    private val filename: String,
    private val content: String
) : NotificationAction(text) {
    
    override fun actionPerformed(event: AnActionEvent, notification: Notification) {
        event.project!!.openLightFile(filename, content)
    }
    
}


internal fun Notification.addOpenTemporaryFileAction(text: String, filename: String, content: String) {
    addAction(OpenTemporaryFileAction(text, filename, content))
}


private fun Notification.addOpenTemporaryFileActionIfNotBlank(text: String, filename: String, content: String) {
    if (content.isNotBlank()) {
        addOpenTemporaryFileAction(text, filename, content)
    }
}


private fun Notification.addSeeStdoutAction(content: String) {
    val text = message("notificationActions.seeStdoutInEditor")
    addOpenTemporaryFileActionIfNotBlank(text, "stdout.txt", content)
}


private fun Notification.addSeeStderrAction(content: String) {
    val text = message("notificationActions.seeStderrInEditor")
    addOpenTemporaryFileActionIfNotBlank(text, "stderr.txt", content)
}


internal fun Notification.addSeeOutputActions(processOutput: ProcessOutput) {
    addSeeStdoutAction(processOutput.stdout)
    addSeeStderrAction(processOutput.stderr)
}
