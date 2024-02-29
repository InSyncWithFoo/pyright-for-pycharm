package com.insyncwithfoo.pyright.configuration.application

import com.intellij.openapi.components.BaseState
import org.jetbrains.annotations.SystemDependent


class Configurations : BaseState() {
    
    var alwaysUseGlobal by property(false)
    var globalExecutable by string(null)
    var globalConfigurationFile by string(null)
    var useEditorFont by property(false)
    
    companion object {
        fun create(
            alwaysUseGlobal: Boolean,
            globalExecutable: @SystemDependent String?,
            globalConfigurationFile: @SystemDependent String?,
            useEditorFont: Boolean
        ) =
            Configurations().apply {
                this.alwaysUseGlobal = alwaysUseGlobal
                this.globalExecutable = globalExecutable
                this.globalConfigurationFile = globalConfigurationFile
                this.useEditorFont = useEditorFont
            }
    }
    
}
