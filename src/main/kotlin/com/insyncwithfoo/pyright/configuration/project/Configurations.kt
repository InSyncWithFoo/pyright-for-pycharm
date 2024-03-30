package com.insyncwithfoo.pyright.configuration.project

import com.intellij.openapi.components.BaseState


internal class Configurations : BaseState() {
    var projectExecutable by string(null)
    var projectConfigurationFile by string(null)
    var autoSuggestExecutable by property(true)
}
