package com.insyncwithfoo.pyright.configuration.application

import com.insyncwithfoo.pyright.configuration.PyrightConfigurable
import com.insyncwithfoo.pyright.isNormal
import com.insyncwithfoo.pyright.message
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager


private val ProjectManager.undisposedProjects: Sequence<Project>
    get() = openProjects.asSequence().filter { it.isNormal }


internal class Configurable : PyrightConfigurable<Configurations>() {
    
    override val service = ConfigurationService.getInstance()
    override val state = service.state.copy()
    
    override val panel by lazy { configurationPanel(state) }
    
    override fun getDisplayName() = message("configurations.global.displayName")
    
    override fun apply() {
        super.apply()
        stopAllServersIfSoChoose()
    }
    
    private fun stopAllServersIfSoChoose() {
        ProjectManager.getInstance().undisposedProjects.forEach { project ->
            project.toggleServersAccordingly()
        }
    }
    
}
