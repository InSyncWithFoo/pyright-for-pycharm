package com.insyncwithfoo.pyright.configuration

import com.intellij.openapi.project.Project
import com.insyncwithfoo.pyright.configuration.application.ConfigurationService as ApplicationConfigurationService
import com.insyncwithfoo.pyright.configuration.project.ConfigurationService as ProjectConfigurationService


internal class PyrightConfigurationService private constructor(
    applicationService: ApplicationConfigurationService,
    projectService: ProjectConfigurationService
) {
    
    val configurations: PyrightAllConfigurations
    
    init {
        val applicationConfigurations = applicationService.configurations
        val projectConfigurations = projectService.configurations

        configurations = PyrightAllConfigurations(
            alwaysUseGlobal = applicationConfigurations.alwaysUseGlobal,
            globalExecutable = applicationConfigurations.globalExecutable,
            globalConfigurationFile = applicationConfigurations.globalConfigurationFile,
            useEditorFont = applicationConfigurations.useEditorFont,

            projectExecutable = projectConfigurations.projectExecutable,
            projectConfigurationFile = projectConfigurations.projectConfigurationFile
        )
    }
    
    companion object {
        fun getInstance(project: Project): PyrightConfigurationService {
            val applicationService = ApplicationConfigurationService.getInstance()
            val projectService = ProjectConfigurationService.getInstance(project)
            
            return PyrightConfigurationService(applicationService, projectService)
        }
    }
    
}
