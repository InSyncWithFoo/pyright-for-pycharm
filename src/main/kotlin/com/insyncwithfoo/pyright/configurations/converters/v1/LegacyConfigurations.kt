package com.insyncwithfoo.pyright.configurations.converters.v1

import com.insyncwithfoo.pyright.commandline.DiagnosticSeverity
import com.insyncwithfoo.pyright.configurations.Locale
import com.insyncwithfoo.pyright.configurations.RunningMode
import com.intellij.openapi.components.BaseState


internal class LegacyGlobalConfigurations : BaseState() {
    var alwaysUseGlobal by property(false)
    var globalExecutable by string(null)
    var globalConfigurationFile by string(null)
    var useEditorFont by property(false)
    var addTooltipPrefix by property(false)
    var minimumSeverityLevel by enum(DiagnosticSeverity.INFORMATION)
    var processTimeout by property(10_000)
    var globalLangserverExecutable by string(null)
    var globalRunningMode by enum(RunningMode.COMMAND_LINE)
    var numberOfThreads by property(0)
    var locale by enum(Locale.DEFAULT)
}


internal class LegacyLocalConfigurations : BaseState() {
    var projectExecutable by string(null)
    var projectConfigurationFile by string(null)
    var autoSuggestExecutable by property(true)
    var projectLangserverExecutable by string(null)
    var projectRunningMode by enum(RunningMode.COMMAND_LINE)
}
