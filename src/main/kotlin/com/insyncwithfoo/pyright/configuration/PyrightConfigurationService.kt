package com.insyncwithfoo.pyright.configuration

import com.intellij.openapi.project.Project
import org.jetbrains.annotations.SystemDependent
import com.insyncwithfoo.pyright.configuration.application.ConfigurationService as ApplicationConfigurationService
import com.insyncwithfoo.pyright.configuration.project.ConfigurationService as ProjectConfigurationService


data class PyrightAllConfigurations(
    val alwaysUseGlobal: Boolean = false,
    val globalExecutable: @SystemDependent String? = null,
    val globalConfigurationFile: @SystemDependent String? = null,

    val projectExecutable: @SystemDependent String? = null,
    val projectConfigurationFile: @SystemDependent String? = null
) {
    
    val executable: @SystemDependent String?
        get() = when {
            alwaysUseGlobal -> globalExecutable
            else -> projectExecutable ?: globalExecutable
        }
    
    val configurationFile: @SystemDependent String?
        get() = when {
            executable == null -> null
            alwaysUseGlobal -> globalConfigurationFile
            projectExecutable != null -> projectConfigurationFile
            else -> projectConfigurationFile ?: globalConfigurationFile
        }
    
}


class PyrightConfigurationService private constructor(
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
