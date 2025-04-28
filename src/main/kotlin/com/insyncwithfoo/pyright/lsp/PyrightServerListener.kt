package com.insyncwithfoo.pyright.lsp

import com.insyncwithfoo.pyright.shared.createLSPSettingsObject
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.platform.lsp.api.LspServerListener
import com.intellij.platform.lsp.api.LspServerManager
import org.eclipse.lsp4j.DidChangeConfigurationParams
import org.eclipse.lsp4j.InitializeResult


internal class Listener(val project: Project, private val module: Module? = null) : LspServerListener {
    
    override fun serverInitialized(params: InitializeResult) {
        val lspServerManager = LspServerManager.getInstance(project)
        val settings = project.createLSPSettingsObject(module)
        val parameters = DidChangeConfigurationParams(settings)
        
        lspServerManager.getServersForProvider(PyrightServerSupportProvider::class.java).forEach { lspServer ->
            lspServer.sendNotification {
                it.workspaceService.didChangeConfiguration(parameters)
            }
        }
    }
    
}
