package com.insyncwithfoo.pyright.configuration.project

import com.intellij.openapi.components.BaseState


internal open class Configurations : BaseState() {
    var projectExecutable by string(null)
    var projectConfigurationFile by string(null)
    var autoSuggestExecutable by property(true)
}
