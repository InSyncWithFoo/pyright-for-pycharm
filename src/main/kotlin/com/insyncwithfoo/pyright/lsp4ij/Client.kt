package com.insyncwithfoo.pyright.lsp4ij

import com.insyncwithfoo.pyright.sdkPath
import com.intellij.openapi.project.Project
import com.redhat.devtools.lsp4ij.ServerStatus
import com.redhat.devtools.lsp4ij.client.LanguageClientImpl


private fun Project.createSettingsObject() = Settings().apply {
    python {
        pythonPath = sdkPath?.toString()
    }
}


internal class Client(project: Project) : LanguageClientImpl(project) {
    
    override fun createSettings() =
        project.createSettingsObject()
    
    override fun handleServerStatusChanged(serverStatus: ServerStatus?) {
        if (serverStatus == ServerStatus.started) {
            triggerChangeConfiguration()
        }
    }
    
}
