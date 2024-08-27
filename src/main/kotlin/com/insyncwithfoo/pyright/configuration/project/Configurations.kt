package com.insyncwithfoo.pyright.configuration.project

import com.insyncwithfoo.pyright.message
import com.intellij.openapi.components.BaseState


internal enum class RunningMode(val label: String) {
    USE_GLOBAL(message("configurations.runningMode.useGlobal")),
    CLI(message("configurations.runningMode.cli")),
    LSP4IJ(message("configurations.runningMode.lsp4ij"));
}


internal class Configurations : BaseState() {
    var projectExecutable by string(null)
    var projectConfigurationFile by string(null)
    var autoSuggestExecutable by property(true)
    var projectLangserverExecutable by string(null)
    var projectRunningMode by enum(RunningMode.USE_GLOBAL)
    var workingDirectory by string(null)
}
