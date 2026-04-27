package com.insyncwithfoo.pyright.lsp4ij

import com.insyncwithfoo.pyright.getPyrightWorkspaceFolders
import com.insyncwithfoo.pyright.toFileUriString
import com.redhat.devtools.lsp4ij.client.features.LSPClientFeatures
import org.eclipse.lsp4j.InitializeParams
import org.eclipse.lsp4j.WorkspaceFolder


@Suppress("UnstableApiUsage")
internal class PyrightClientFeatures : LSPClientFeatures() {

    override fun initializeParams(params: InitializeParams) {
        val workspaceRoots = project.getPyrightWorkspaceFolders()
        val workspaceFolders = workspaceRoots.map { folder ->
            WorkspaceFolder(folder.toFileUriString(), folder.name)
        }

        params.workspaceFolders = workspaceFolders

        @Suppress("DEPRECATION")
        if (workspaceRoots.size == 1) {
            val root = workspaceRoots.single()
            params.rootUri = root.toFileUriString()
            params.rootPath = root.path
        } else {
            params.rootUri = null
            params.rootPath = null
        }
    }

}
