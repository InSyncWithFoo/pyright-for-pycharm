package com.insyncwithfoo.pyright.configuration.application

import com.insyncwithfoo.pyright.PyrightDiagnosticSeverity
import com.insyncwithfoo.pyright.message
import com.intellij.openapi.components.BaseState


internal enum class RunningMode(val label: String) {
    CLI(message("configurations.runningMode.cli")),
    LSP4IJ(message("configurations.runningMode.lsp4ij")),;
}


internal class Configurations : BaseState() {
    var alwaysUseGlobal by property(false)
    var globalExecutable by string(null)
    var globalConfigurationFile by string(null)
    var useEditorFont by property(false)
    var addTooltipPrefix by property(false)
    var minimumSeverityLevel by enum(PyrightDiagnosticSeverity.INFORMATION)
    var processTimeout by property(10_000)
    var globalLangserverExecutable by string(null)
    var globalRunningMode by enum(RunningMode.CLI)
}
