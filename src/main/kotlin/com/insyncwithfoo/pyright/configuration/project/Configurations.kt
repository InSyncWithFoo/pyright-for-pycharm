package com.insyncwithfoo.pyright.configuration.project

import com.intellij.openapi.components.BaseState
import org.jetbrains.annotations.SystemDependent


internal class Configurations : BaseState() {
    
    var projectExecutable by string(null)
    var projectConfigurationFile by string(null)
    var autoSuggestExecutable by property(true)
    
    companion object {
        fun create(
            projectExecutable: @SystemDependent String?,
            projectConfigurationFile: @SystemDependent String?,
            autoSuggestExecutable: Boolean
        ) =
            Configurations().apply {
                this.projectExecutable = projectExecutable
                this.projectConfigurationFile = projectConfigurationFile
                this.autoSuggestExecutable = autoSuggestExecutable
            }
    }
    
}
