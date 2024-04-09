package com.insyncwithfoo.pyright.configuration.project

import com.insyncwithfoo.pyright.configuration.PyrightConfigurable
import com.insyncwithfoo.pyright.message
import com.intellij.openapi.project.Project


internal class Configurable(internal val project: Project) : PyrightConfigurable<Configurations>() {
    
    override val service = ConfigurationService.getInstance(project)
    override val state = service.state.copy()
    
    override val panel by lazy { configurationPanel(state) }
    
    override fun getDisplayName() = message("configurations.project.displayName")
    
}
