package com.insyncwithfoo.pyright.configuration

import com.insyncwithfoo.pyright.PyrightDiagnosticSeverity
import org.jetbrains.annotations.SystemDependent
import com.insyncwithfoo.pyright.configuration.application.Configurations as ApplicationConfigurations
import com.insyncwithfoo.pyright.configuration.application.RunningMode as ApplicationRunningMode
import com.insyncwithfoo.pyright.configuration.project.Configurations as ProjectConfigurations
import com.insyncwithfoo.pyright.configuration.project.RunningMode as ProjectRunningMode


internal infix fun ApplicationConfigurations.mergeWith(other: ProjectConfigurations) =
    AllConfigurations(
        alwaysUseGlobal = this.alwaysUseGlobal,
        globalExecutable = this.globalExecutable,
        globalConfigurationFile = this.globalConfigurationFile,
        useEditorFont = this.useEditorFont,
        addTooltipPrefix = this.addTooltipPrefix,
        minimumSeverityLevel = this.minimumSeverityLevel,
        processTimeout = this.processTimeout,
        globalLangserverExecutable = this.globalLangserverExecutable,
        globalRunningMode = this.globalRunningMode,
        
        projectExecutable = other.projectExecutable,
        projectConfigurationFile = other.projectConfigurationFile,
        autoSuggestExecutable = other.autoSuggestExecutable,
        projectLangserverExecutable = other.projectLangserverExecutable,
        projectRunningMode = other.projectRunningMode
    )


internal data class AllConfigurations(
    val alwaysUseGlobal: Boolean,
    val globalExecutable: @SystemDependent String?,
    val globalConfigurationFile: @SystemDependent String?,
    val useEditorFont: Boolean,
    val addTooltipPrefix: Boolean,
    val minimumSeverityLevel: PyrightDiagnosticSeverity,
    val processTimeout: Int,
    val globalLangserverExecutable: @SystemDependent String?,
    val globalRunningMode: ApplicationRunningMode,
    
    val projectExecutable: @SystemDependent String?,
    val projectConfigurationFile: @SystemDependent String?,
    val autoSuggestExecutable: Boolean,
    val projectLangserverExecutable: @SystemDependent String?,
    val projectRunningMode: ProjectRunningMode
) {
    
    val executable: @SystemDependent String?
        get() = when {
            alwaysUseGlobal -> globalExecutable
            else -> projectExecutable ?: globalExecutable
        }
    
    val langserverExecutable: @SystemDependent String?
        get() = when {
            alwaysUseGlobal -> globalLangserverExecutable
            else -> projectLangserverExecutable ?: globalLangserverExecutable
        }
    
    val configurationFile: @SystemDependent String?
        get() = when {
            executable == null -> null
            alwaysUseGlobal -> globalConfigurationFile
            projectExecutable != null -> projectConfigurationFile
            else -> projectConfigurationFile ?: globalConfigurationFile
        }
    
    val runningMode: ApplicationRunningMode
        get() = when {
            projectRunningMode == ProjectRunningMode.USE_GLOBAL -> globalRunningMode
            else -> ApplicationRunningMode.valueOf(projectRunningMode.name)
        }
    
}
