package com.insyncwithfoo.pyright.lsp4ij

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.redhat.devtools.lsp4ij.AbstractDocumentMatcher


internal class PyrightDocumentMatcher : AbstractDocumentMatcher() {
    
    override fun match(file: VirtualFile, project: Project): Boolean {
        return file.extension in listOf("py", "pyi")
    }
    
}
