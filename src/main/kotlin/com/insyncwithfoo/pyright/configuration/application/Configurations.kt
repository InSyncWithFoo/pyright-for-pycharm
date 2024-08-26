package com.insyncwithfoo.pyright.configuration.application

import com.insyncwithfoo.pyright.cli.PyrightDiagnosticSeverity
import com.insyncwithfoo.pyright.message
import com.intellij.openapi.components.BaseState


internal enum class RunningMode(val label: String) {
    CLI(message("configurations.runningMode.cli")),
    LSP4IJ(message("configurations.runningMode.lsp4ij"));
}


// https://github.com/microsoft/pyright/blob/acc52c7420/packages/pyright-internal/src/localization/localize.ts#L12-L26
internal enum class Locale(private val value: String) {
    DEFAULT(message("configurations.locale.default")),
    CS("cs"),
    DE("de"),
    EN_US("en-us"),
    ES("es"),
    FR("fr"),
    IT("it"),
    JA("ja"),
    KO("ko"),
    PL("pl"),
    PT_BR("pt-br"),
    RU("ru"),
    TR("tr"),
    ZH_CN("zh-cn"),
    ZH_TW("zh-tw");

    override fun toString() = value
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
    var numberOfThreads by property(0)
    var locale by enum(Locale.DEFAULT)
}
