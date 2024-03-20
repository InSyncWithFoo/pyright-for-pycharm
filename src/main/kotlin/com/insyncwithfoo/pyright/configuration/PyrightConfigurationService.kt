package com.insyncwithfoo.pyright.configuration

import com.intellij.openapi.project.Project
import com.insyncwithfoo.pyright.configuration.application.ConfigurationService as ApplicationConfigurationService
import com.insyncwithfoo.pyright.configuration.project.ConfigurationService as ProjectConfigurationService


internal class PyrightConfigurationService private constructor(
    applicationService: ApplicationConfigurationService,
    val projectService: ProjectConfigurationService
) {
    
    val configurations = applicationService.configurations mergeWith projectService.configurations
    
    companion object {
        fun getInstance(project: Project): PyrightConfigurationService {
            val applicationService = ApplicationConfigurationService.getInstance()
            val projectService = ProjectConfigurationService.getInstance(project)
            
            return PyrightConfigurationService(applicationService, projectService)
        }
    }
    
}
