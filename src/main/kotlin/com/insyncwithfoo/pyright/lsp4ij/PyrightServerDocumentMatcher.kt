package com.insyncwithfoo.pyright.lsp4ij

import com.insyncwithfoo.pyright.isSupportedByPyright
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.redhat.devtools.lsp4ij.AbstractDocumentMatcher


internal class PyrightServerDocumentMatcher : AbstractDocumentMatcher() {
    
    override fun match(file: VirtualFile, project: Project) =
        file.isSupportedByPyright(project)
    
}
