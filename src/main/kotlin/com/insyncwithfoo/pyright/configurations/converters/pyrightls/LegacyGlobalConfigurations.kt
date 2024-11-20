package com.insyncwithfoo.pyright.configurations.converters.pyrightls

import com.insyncwithfoo.pyright.configurations.DiagnosticMode
import com.insyncwithfoo.pyright.configurations.Locale
import com.insyncwithfoo.pyright.configurations.LogLevel
import com.insyncwithfoo.pyright.configurations.WorkspaceFolders
import com.insyncwithfoo.pyright.configurations.join
import com.intellij.openapi.components.BaseState


internal class LegacyGlobalConfigurations : BaseState() {
    var alwaysUseGlobal by property(false)
    var globalExecutable by string(null)
    var useEditorFont by property(false)
    var addTooltipPrefix by property(false)
    var linkErrorCodes by property(false)
    var hoverSupport by property(true)
    var completionSupport by property(false)
    var goToDefinitionSupport by property(false)
    var logLevel by enum(LogLevel.INFORMATION)
    var taggedHints by property(true)
    var autoImportCompletions by property(true)
    var autocompleteParentheses by property(false)
    var diagnosticsSupport by property(true)
    var autoRestartServer by property(false)
    var monkeypatchAutoImportDetails by property(true)
    var monkeypatchTrailingQuoteBug by property(true)
    var locale by enum(Locale.DEFAULT)
}


internal class LegacyLocalConfigurations : BaseState() {
    var projectExecutable by string(null)
    var autoSuggestExecutable by property(true)
    var workspaceFolders by enum(WorkspaceFolders.PROJECT_BASE)
    var targetedFileExtensions by string(listOf("py", "pyi").join())
    var diagnosticMode by enum(DiagnosticMode.OPEN_FILES_ONLY)
    var autoSearchPaths by property(true)
}
