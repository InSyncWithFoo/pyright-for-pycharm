package com.insyncwithfoo.pyright.lsp4ij

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.redhat.devtools.lsp4ij.ServerStatus
import com.redhat.devtools.lsp4ij.client.LanguageClientImpl


private fun Project.createSettingsObject() = Settings().apply {
    python {
        pythonPath = onlyModuleOrNull?.interpreterPath?.toString()
    }
}


internal class PyrightServerClient(project: Project) : LanguageClientImpl(project) {
    
    override fun createSettings() =
        project.createSettingsObject().also { LOGGER.info(it.toString()) }
    
    override fun handleServerStatusChanged(serverStatus: ServerStatus?) {
        if (serverStatus == ServerStatus.started) {
            triggerChangeConfiguration()
        }
    }
    
    companion object {
        val LOGGER = Logger.getInstance(PyrightServerClient::class.java)
    }
    
}
