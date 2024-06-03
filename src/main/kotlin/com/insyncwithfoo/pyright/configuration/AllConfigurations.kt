package com.insyncwithfoo.pyright.configuration

import com.insyncwithfoo.pyright.PyrightDiagnosticSeverity
import org.jetbrains.annotations.SystemDependent
import com.insyncwithfoo.pyright.configuration.application.Configurations as ApplicationConfigurations
import com.insyncwithfoo.pyright.configuration.project.Configurations as ProjectConfigurations


internal infix fun ApplicationConfigurations.mergeWith(other: ProjectConfigurations) =
    AllConfigurations(
        alwaysUseGlobal = this.alwaysUseGlobal,
        globalExecutable = this.globalExecutable,
        globalConfigurationFile = this.globalConfigurationFile,
        useEditorFont = this.useEditorFont,
        addTooltipPrefix = this.addTooltipPrefix,
        minimumSeverityLevel = this.minimumSeverityLevel,
        processTimeout = this.processTimeout,
        
        projectExecutable = other.projectExecutable,
        projectConfigurationFile = other.projectConfigurationFile,
        autoSuggestExecutable = other.autoSuggestExecutable
    )


internal data class AllConfigurations(
    val alwaysUseGlobal: Boolean,
    val globalExecutable: @SystemDependent String?,
    val globalConfigurationFile: @SystemDependent String?,
    val useEditorFont: Boolean,
    val addTooltipPrefix: Boolean,
    val minimumSeverityLevel: PyrightDiagnosticSeverity,
    val processTimeout: Int,
    
    val projectExecutable: @SystemDependent String?,
    val projectConfigurationFile: @SystemDependent String?,
    val autoSuggestExecutable: Boolean
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
