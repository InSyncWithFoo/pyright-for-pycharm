package com.insyncwithfoo.pyright.configuration

import org.jetbrains.annotations.SystemDependent
import com.insyncwithfoo.pyright.configuration.application.Configurations as ApplicationConfigurations
import com.insyncwithfoo.pyright.configuration.project.Configurations as ProjectConfigurations


internal infix fun ApplicationConfigurations.mergeWith(other: ProjectConfigurations) =
    PyrightAllConfigurations(
        alwaysUseGlobal = this.alwaysUseGlobal,
        globalExecutable = this.globalExecutable,
        globalConfigurationFile = this.globalConfigurationFile,
        useEditorFont = this.useEditorFont,
        addTooltipPrefix = this.addTooltipPrefix,
        
        projectExecutable = other.projectExecutable,
        projectConfigurationFile = other.projectConfigurationFile
    )


data class PyrightAllConfigurations(
    val alwaysUseGlobal: Boolean = false,
    val globalExecutable: @SystemDependent String? = null,
    val globalConfigurationFile: @SystemDependent String? = null,
    val useEditorFont: Boolean = false,
    val addTooltipPrefix: Boolean = false,
    
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
