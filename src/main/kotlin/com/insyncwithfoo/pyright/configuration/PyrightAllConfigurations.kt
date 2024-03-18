package com.insyncwithfoo.pyright.configuration

import org.jetbrains.annotations.SystemDependent


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
