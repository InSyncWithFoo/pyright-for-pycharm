package com.insyncwithfoo.pyright.lsp4ij


import com.insyncwithfoo.pyright.configuration.application.RunningMode
import com.insyncwithfoo.pyright.pyrightConfigurations
import com.insyncwithfoo.pyright.pyrightInspectionIsEnabled
import com.insyncwithfoo.pyright.pyrightLSExecutable
import com.intellij.openapi.project.Project
import com.redhat.devtools.lsp4ij.LanguageServerEnablementSupport
import com.redhat.devtools.lsp4ij.LanguageServerFactory
import com.redhat.devtools.lsp4ij.client.LanguageClientImpl
import com.redhat.devtools.lsp4ij.server.StreamConnectionProvider
import com.insyncwithfoo.pyright.configuration.application.ConfigurationService as ApplicationConfigurationService
import com.insyncwithfoo.pyright.configuration.project.ConfigurationService as ProjectConfigurationService
import com.insyncwithfoo.pyright.configuration.project.RunningMode as ProjectRunningMode


internal const val SERVER_ID = "pyright"


internal class PyrightLanguageServerFactory : LanguageServerFactory, LanguageServerEnablementSupport {
    
    override fun isEnabled(project: Project): Boolean {
        val configurations = project.pyrightConfigurations
        val runningModeIsLSP4IJ = configurations.runningMode == RunningMode.LSP4IJ
        val executable = project.pyrightLSExecutable
        
        return project.pyrightInspectionIsEnabled && runningModeIsLSP4IJ && executable != null
    }
    
    override fun setEnabled(enabled: Boolean, project: Project) {
        val configurations = project.pyrightConfigurations

        val globalConfigurationService = ApplicationConfigurationService.getInstance()
        val projectConfigurationService = ProjectConfigurationService.getInstance(project)
        
        when (configurations.projectRunningMode) {
            ProjectRunningMode.USE_GLOBAL ->
                globalConfigurationService.state.globalRunningMode = when {
                    enabled -> RunningMode.LSP4IJ
                    else -> RunningMode.CLI
                }
            else ->
                projectConfigurationService.state.projectRunningMode = when {
                    enabled -> ProjectRunningMode.LSP4IJ
                    else -> ProjectRunningMode.CLI
                }
        }
    }
    
    override fun createConnectionProvider(project: Project): StreamConnectionProvider {
        return ConnectionProvider.create(project)
    }
    
    override fun createLanguageClient(project: Project): LanguageClientImpl {
        return Client(project)
    }
    
}


