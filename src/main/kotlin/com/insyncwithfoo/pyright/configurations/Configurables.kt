package com.insyncwithfoo.pyright.configurations

import com.insyncwithfoo.pyright.configurations.models.PanelBasedConfigurable
import com.insyncwithfoo.pyright.configurations.models.ProjectBasedConfigurable
import com.insyncwithfoo.pyright.configurations.models.copy
import com.insyncwithfoo.pyright.message
import com.intellij.openapi.project.Project


internal class PyrightConfigurable : PanelBasedConfigurable<PyrightConfigurations>() {
    
    private val service = PyrightGlobalService.getInstance()
    
    override val state = service.state.copy()
    override val panel by lazy { createPanel(state) }
    
    override fun getDisplayName() = message("configurations.displayName")
    
    override fun afterApply() {
        syncStateWithService(state, service.state)
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
    }
    
}
