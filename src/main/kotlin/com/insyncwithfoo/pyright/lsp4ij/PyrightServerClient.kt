package com.insyncwithfoo.pyright.lsp4ij

import com.insyncwithfoo.pyright.shared.createLSPSettingsObject
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.redhat.devtools.lsp4ij.ServerStatus
import com.redhat.devtools.lsp4ij.client.LanguageClientImpl


internal class PyrightServerClient(project: Project) : LanguageClientImpl(project) {
    
    override fun createSettings() =
        project.createLSPSettingsObject().also { thisLogger().info(it.toString()) }
    
    override fun handleServerStatusChanged(serverStatus: ServerStatus) {
        if (serverStatus == ServerStatus.started) {
            triggerChangeConfiguration()
        }
    }
    
}
