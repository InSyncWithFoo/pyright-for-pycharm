package com.insyncwithfoo.pyright.runner

import com.intellij.notification.BrowseNotificationAction
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.LocalFileSystem


private fun NotificationGroup.createErrorNotification(title: String, content: String) =
    createNotification(title, content, NotificationType.ERROR)


private class OpenFileAction(text: String, private val path: String) : NotificationAction(text) {
    
    override fun actionPerformed(event: AnActionEvent, notification: Notification) {
        val project = event.project ?: return somethingIsWrong()
        val fileEditorManager = FileEditorManager.getInstance(project)
        
        val fileSystem = LocalFileSystem.getInstance()
        val virtualFile = fileSystem.findFileByPath(path) ?: return somethingIsWrong()
        
        fileEditorManager.openFileEditor(OpenFileDescriptor(project, virtualFile), true)
    }
    
    fun somethingIsWrong() {
        val text = MESSAGE_TEXT.format(path)
        Messages.showErrorDialog(text, MESSAGE_TITLE)
    }
    
    companion object {
        const val MESSAGE_TITLE = "Something is wrong."
        const val MESSAGE_TEXT = "The file at %s cannot be opened. Please open it manually."
    }
    
}


internal sealed class PyrightException(
    val stdout: String,
    val stderr: String,
    message: String
) : Exception(message) {
    abstract fun createNotification(group: NotificationGroup): Notification
}


internal class FatalException(
    stdout: String,
    stderr: String,
    message: String = "Pyright reported a fatal error"
) : PyrightException(stdout, stderr, message) {
    
    override fun createNotification(group: NotificationGroup): Notification {
        val title = "Fatal error encountered"
        val content = """
            This is presumably a bug in Pyright.<br>
            Please try to narrow the problem as much as possible,
            then report it at Pyright's issue tracker.
        """.trimIndent()
        
        return group.createErrorNotification(title, content).run {
            addAction(BrowseNotificationAction(ACTION_TEXT, ACTION_LINK))
        }
    }
    
    companion object {
        private const val ACTION_TEXT = "Pyright issue tracker"
        private const val ACTION_LINK = "https://github.com/microsoft/pyright/issues"
    }
    
}


internal class InvalidConfigurationsException(
    stdout: String,
    stderr: String,
    message: String = "Pyright cannot parse configuration file"
) : PyrightException(stdout, stderr, message) {
    
    override fun createNotification(group: NotificationGroup): Notification {
        // Greedy is necessary since a Linux path may contain double quotes.
        // For example:
        //   Config file "/b"az/pyrightconfig.json" could not be parsed.
        val quoted = "\"(.*)\"".toRegex()
        
        val configurationFilePath = quoted.find(stderr)!!.groups[1]!!.value
        
        val title = "Cannot parse configuration file"
        val content = """The configuration file found at "$configurationFilePath" is invalid."""
        
        return group.createErrorNotification(title, content).run {
            addAction(OpenFileAction(ACTION_TEXT, configurationFilePath))
        }
    }
    
    companion object {
        private const val ACTION_TEXT = "View file"
    }
    
}


internal class InvalidParametersException(
    stdout: String,
    stderr: String,
    message: String = "Pyright does not recognize CLI options"
) : PyrightException(stdout, stderr, message) {
    
    override fun createNotification(group: NotificationGroup): Notification {
        val title = "Unrecognized CLI options"
        val content = """
            Presumably, you are using a new (or old) version of Pyright,
            which does not support the options this plugin uses.
            Please report this problem to the plugin's issue tracker.
        """.trimIndent()
        
        return group.createErrorNotification(title, content).run {
            addAction(BrowseNotificationAction(ACTION_TEXT, ACTION_LINK))
        }
    }
    
    companion object {
        private const val ACTION_TEXT = "Plugin issue tracker"
        private const val ACTION_LINK = "https://github.com/InSyncWithFoo/pyright-for-pycharm/issues"
    }
    
}
