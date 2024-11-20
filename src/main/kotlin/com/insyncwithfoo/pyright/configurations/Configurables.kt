package com.insyncwithfoo.pyright.configurations

import com.insyncwithfoo.pyright.commandline.pyrightInspectionisEnabled
import com.insyncwithfoo.pyright.configurations.models.PanelBasedConfigurable
import com.insyncwithfoo.pyright.configurations.models.ProjectBasedConfigurable
import com.insyncwithfoo.pyright.configurations.models.copy
import com.insyncwithfoo.pyright.lsp.PyrightServerSupportProvider
import com.insyncwithfoo.pyright.lsp4ij.SERVER_ID
import com.insyncwithfoo.pyright.message
import com.insyncwithfoo.pyright.openProjects
import com.insyncwithfoo.pyright.restartNativeServers
import com.insyncwithfoo.pyright.toggleLSP4IJServers
import com.intellij.openapi.project.Project


private fun Project.toggleServers() {
    val configurations = pyrightConfigurations
    
    if (configurations.autoRestartServers) {
        restartNativeServers<PyrightServerSupportProvider>()
        toggleLSP4IJServers(SERVER_ID, restart = configurations.runningMode == RunningMode.LSP4IJ)
    }
}


internal class PyrightConfigurable : PanelBasedConfigurable<PyrightConfigurations>() {
    
    private val service = PyrightGlobalService.getInstance()
    
    override val state = service.state.copy()
    override val panel by lazy { createPanel(state) }
    
    override fun getDisplayName() = message("configurations.displayName")
    
    override fun afterApply() {
        syncStateWithService(state, service.state)
        
        openProjects.forEach { project ->
            project.pyrightInspectionisEnabled = state.runningMode == RunningMode.COMMAND_LINE
            
            project.toggleServers()
        }
    }
    
}


internal class PyrightProjectConfigurable(override val project: Project) :
    PanelBasedConfigurable<PyrightConfigurations>(), ProjectBasedConfigurable {
    
    private val service = PyrightLocalService.getInstance(project)
    private val overrideService = PyrightOverrideService.getInstance(project)
    private val overrideState = overrideService.state.copy()
    
    override val state = service.state.copy()
    override val overrides by lazy { overrideState.names }
    override val panel by lazy { createPanel(state) }
    
    override fun afterApply() {
        syncStateWithService(state, service.state)
        syncStateWithService(overrideState, overrideService.state)
        
        project.pyrightInspectionisEnabled = state.runningMode == RunningMode.COMMAND_LINE
        
        project.toggleServers()
    }
    
}
