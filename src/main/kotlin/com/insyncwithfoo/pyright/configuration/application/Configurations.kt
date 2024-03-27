package com.insyncwithfoo.pyright.configuration.application

import com.intellij.openapi.components.BaseState


internal class Configurations : BaseState() {
    var alwaysUseGlobal by property(false)
    var globalExecutable by string(null)
    var globalConfigurationFile by string(null)
    var useEditorFont by property(false)
    var addTooltipPrefix by property(false)
}
