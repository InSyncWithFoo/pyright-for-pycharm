package com.insyncwithfoo.pyright.configuration.project

import com.intellij.openapi.components.BaseState
import org.jetbrains.annotations.SystemDependent


class Configurations : BaseState() {
    var projectExecutable by string(null)
    var projectConfigurationFile by string(null)
    
    companion object {
        fun create(
            projectExecutable: @SystemDependent String?,
            projectConfigurationFile: @SystemDependent String?
        ) =
            Configurations().apply {
                this.projectExecutable = projectExecutable
                this.projectConfigurationFile = projectConfigurationFile
            }
    }
}
