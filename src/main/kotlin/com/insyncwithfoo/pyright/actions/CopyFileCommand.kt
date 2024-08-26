package com.insyncwithfoo.pyright.actions

import com.insyncwithfoo.pyright.cli.FileCommand
import com.insyncwithfoo.pyright.fileEditorManager
import com.insyncwithfoo.pyright.message
import com.insyncwithfoo.pyright.somethingIsWrong
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.ui.EmptyClipboardOwner
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection


private fun Project.getCurrentFile(): VirtualFile? {
    val currentDocument = fileEditorManager.selectedTextEditor?.document ?: return null
    
    return FileDocumentManager.getInstance().getFile(currentDocument)
}


internal class CopyFileCommand : DumbAwareAction() {
    
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project
        
        val currentFile = project?.getCurrentFile() ?: return project.cannotCreateCommand()
        val command = FileCommand.create(project, currentFile) ?: return project.cannotCreateCommand()
        
        try {
            copyToClipboard(command.asCopyableString())
        } catch (_: Exception) {
            project.cannotCopyToClipboard()
        }
    }
    
    private fun copyToClipboard(text: String) {
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(StringSelection(text), EmptyClipboardOwner.INSTANCE)
    }
    
    private fun Project?.cannotCreateCommand() {
        somethingIsWrong(
            message("messages.cannotCreateCommand.title"),
            message("messages.cannotCreateCommand.body"),
        )
    }
    
    private fun Project?.cannotCopyToClipboard() {
        somethingIsWrong(message("messages.cannotCopyCommandToClipboard.body"))
    }
    
}
