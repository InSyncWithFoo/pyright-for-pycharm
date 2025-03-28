package com.insyncwithfoo.pyright.configurations

import com.insyncwithfoo.pyright.bindItem
import com.insyncwithfoo.pyright.bindSelected
import com.insyncwithfoo.pyright.bindText
import com.insyncwithfoo.pyright.comboBox
import com.insyncwithfoo.pyright.commandline.DiagnosticSeverity
import com.insyncwithfoo.pyright.configurations.models.AdaptivePanel
import com.insyncwithfoo.pyright.configurations.models.Overrides
import com.insyncwithfoo.pyright.configurations.models.PanelBasedConfigurable
import com.insyncwithfoo.pyright.configurations.models.projectAndOverrides
import com.insyncwithfoo.pyright.emptyText
import com.insyncwithfoo.pyright.findExecutableInPath
import com.insyncwithfoo.pyright.findExecutableInVenv
import com.insyncwithfoo.pyright.lsp4ijIsAvailable
import com.insyncwithfoo.pyright.lspIsAvailable
import com.insyncwithfoo.pyright.makeFlexible
import com.insyncwithfoo.pyright.message
import com.insyncwithfoo.pyright.radioButtonFor
import com.insyncwithfoo.pyright.singleFileTextField
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.JBIntSpinner
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.fields.ExpandableTextField
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.Row
import com.intellij.ui.dsl.builder.bindIntValue
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.toNonNullableProperty


private class PyrightPanel(state: PyrightConfigurations, overrides: Overrides?, project: Project?) :
    AdaptivePanel<PyrightConfigurations>(state, overrides, project)


private fun Row.executableInput(block: Cell<TextFieldWithBrowseButton>.() -> Unit) =
    singleFileTextField().makeFlexible().apply(block)


private fun Row.smartExecutableResolutionInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.smartExecutableResolution.label")).apply(block)


private fun Row.languageServerExecutableInput(block: Cell<TextFieldWithBrowseButton>.() -> Unit) =
    singleFileTextField().makeFlexible().apply(block)


private fun Row.smartLanguageServerExecutableResolutionInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.smartLanguageServerExecutableResolution.label")).apply(block)


private fun Row.configurationFileInput(block: Cell<TextFieldWithBrowseButton>.() -> Unit) =
    singleFileTextField().makeFlexible().apply(block)


private fun Panel.runningModeInputGroup(block: Panel.() -> Unit) =
    buttonsGroup(init = block)


private fun Row.autoRestartServers(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.autoRestartServers.label")).apply(block)


private fun Row.diagnosticsInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.diagnostics.label")).apply(block)


private fun Row.useEditorFontForTooltipsInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.useEditorFontForTooltips.label")).apply(block)


private fun Row.prefixTooltipMessagesInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.prefixTooltipMessages.label")).apply(block)


private fun Row.linkErrorCodesInTooltipsInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.linkErrorCodesInTooltips.label")).apply(block)


private fun Row.taggedHintsInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.taggedHints.label")).apply(block)


private fun Row.minimumSeverityLevelInput(block: Cell<ComboBox<DiagnosticSeverity>>.() -> Unit) =
    comboBox<DiagnosticSeverity>().apply(block)


private fun Row.hoverInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.hover.label")).apply(block)


private fun Row.completionInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.completion.label")).apply(block)


private fun Row.autoImportCompletionsInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.autoImportCompletions.label")).apply(block)


private fun Row.monkeypatchAutoImportDetailsInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.monkeypatchAutoImportDetails.label")).apply(block)


private fun Row.autocompleteParenthesesInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.autocompleteParentheses.label")).apply(block)


private fun Row.monkeypatchTrailingQuoteBugInput(block: Cell<JBCheckBox>.() -> Unit) = run {
    val comment = message("configurations.monkeypatchTrailingQuoteBug.comment")

    checkBox(message("configurations.monkeypatchTrailingQuoteBug.label")).comment(comment).apply(block)
}


private fun Row.autoSearchPathsInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.autoSearchPaths.label")).apply(block)


private fun Row.targetedFileExtensionsInput(block: Cell<ExpandableTextField>.() -> Unit) = run {
    val parser = DelimitedFileExtensionList::split
    val joiner = List<FileExtension>::join
    
    expandableTextField(parser, joiner).makeFlexible().apply(block)
}


private fun Row.workspaceFoldersInput(block: Cell<ComboBox<WorkspaceFolders>>.() -> Unit) =
    comboBox<WorkspaceFolders>().apply(block)


private fun Row.diagnosticModeInput(block: Cell<ComboBox<DiagnosticMode>>.() -> Unit) =
    comboBox<DiagnosticMode>().apply(block)


private fun Row.logLevelInput(block: Cell<ComboBox<LogLevel>>.() -> Unit) =
    comboBox<LogLevel>().apply(block)


private fun Row.localeInput(block: Cell<ComboBox<Locale>>.() -> Unit) =
    comboBox<Locale>().apply(block)


private fun Row.numberOfThreadsInput(block: Cell<JBIntSpinner>.() -> Unit) =
    spinner(0..1_000_000, step = 1).apply(block)


private fun Row.useSchemaFromStoreInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.useSchemaFromStore.label")).apply(block)


@Suppress("DialogTitleCapitalization")
private fun PyrightPanel.makeComponent() = panel {
    
    row(message("configurations.executable.label")) {
        executableInput {
            val detectedExecutable = project?.findExecutableInVenv("pyright")?.toString()
                ?: findExecutableInPath("pyright")?.toString()
            
            bindText(state::executable) { detectedExecutable.orEmpty() }
            emptyText = detectedExecutable ?: message("configurations.executable.placeholder")
        }
        overrideCheckbox(state::executable)
    }
    row("") {
        smartExecutableResolutionInput { bindSelected(state::smartExecutableResolution) }
        overrideCheckbox(state::smartExecutableResolution)
    }
    
    row(message("configurations.languageServerExecutable.label")) {
        languageServerExecutableInput {
            val detectedExecutable = project?.findExecutableInVenv("pyright-langserver")?.toString()
                ?: findExecutableInPath("pyright-langserver")?.toString()
            
            bindText(state::languageServerExecutable) { detectedExecutable.orEmpty() }
            emptyText = detectedExecutable ?: message("configurations.executable.placeholder")
        }
        overrideCheckbox(state::languageServerExecutable)
    }
    row("") {
        smartLanguageServerExecutableResolutionInput { bindSelected(state::smartLanguageServerExecutableResolution) }
        overrideCheckbox(state::smartLanguageServerExecutableResolution)
    }
    
    row(message("configurations.configurationFile.label")) {
        configurationFileInput { bindText(state::configurationFile.toNonNullableProperty("")) }
        overrideCheckbox(state::configurationFile)
    }
    
    val runningModeInputGroup = runningModeInputGroup {
        row(message("configurations.runningMode.label")) {
            radioButtonFor(RunningMode.COMMAND_LINE)
            radioButtonFor(RunningMode.LSP4IJ) { label ->
                message("configurations.runningMode.unavailable", label).takeUnless { lsp4ijIsAvailable }
            }
            radioButtonFor(RunningMode.LSP) { label ->
                message("configurations.runningMode.unavailable", label).takeUnless { lspIsAvailable }
            }
            
            overrideCheckbox(state::runningMode)
        }
    }
    runningModeInputGroup.bindSelected(state::runningMode)
    
    group(message("configurations.groups.main")) {
        
        row {
            autoRestartServers { bindSelected(state::autoRestartServers) }
            overrideCheckbox(state::autoRestartServers)
        }
        
        separator()
        
        row {
            diagnosticsInput { bindSelected(state::diagnostics) }
            overrideCheckbox(state::diagnostics)
        }
        indent {
            row {
                useEditorFontForTooltipsInput { bindSelected(state::useEditorFontForTooltips) }
                overrideCheckbox(state::useEditorFontForTooltips)
            }
            row {
                prefixTooltipMessagesInput { bindSelected(state::prefixTooltipMessages) }
                overrideCheckbox(state::prefixTooltipMessages)
            }
            row {
                linkErrorCodesInTooltipsInput { bindSelected(state::linkErrorCodesInTooltips) }
                overrideCheckbox(state::linkErrorCodesInTooltips)
            }
            row {
                taggedHintsInput { bindSelected(state::taggedHints) }
                overrideCheckbox(state::taggedHints)
            }
            row(message("configurations.minimumSeverityLevel.label")) {
                minimumSeverityLevelInput { bindItem(state::minimumSeverityLevel) }
                overrideCheckbox(state::minimumSeverityLevel)
            }
        }
        
        row {
            hoverInput { bindSelected(state::hover) }
            overrideCheckbox(state::hover)
        }
        
        row {
            completionInput { bindSelected(state::completion) }
            overrideCheckbox(state::completion)
        }
        indent {
            row {
                autoImportCompletionsInput { bindSelected(state::autoImportCompletions) }
                overrideCheckbox(state::autoImportCompletions)
            }
            indent {
                row {
                    monkeypatchAutoImportDetailsInput { bindSelected(state::monkeypatchAutoImportDetails) }
                    overrideCheckbox(state::monkeypatchAutoImportDetails)
                }
            }
            row {
                autocompleteParenthesesInput { bindSelected(state::autocompleteParentheses) }
                overrideCheckbox(state::autocompleteParentheses)
            }
            row {
                monkeypatchTrailingQuoteBugInput { bindSelected(state::monkeypatchTrailingQuoteBug) }
                overrideCheckbox(state::monkeypatchTrailingQuoteBug)
            }
        }
        
        separator()
        
        row {
            autoSearchPathsInput { bindSelected(state::autoSearchPaths) }
            overrideCheckbox(state::autoSearchPaths)
        }
        row(message("configurations.targetedFileExtensions.label")) {
            targetedFileExtensionsInput {
                bindText(
                    { state.targetedFileExtensions.orEmpty().deduplicate() },
                    { state.targetedFileExtensions = it.deduplicate() }
                )
            }
            overrideCheckbox(state::targetedFileExtensions)
        }
        row(message("configurations.workspaceFolders.label")) {
            workspaceFoldersInput { bindItem(state::workspaceFolders) }
            overrideCheckbox(state::workspaceFolders)
        }
        row(message("configurations.diagnosticMode.label")) {
            diagnosticModeInput { bindItem(state::diagnosticMode) }
            overrideCheckbox(state::diagnosticMode)
        }
        
        separator()
        
        row(message("configurations.logLevel.label")) {
            logLevelInput { bindItem(state::logLevel) }
            overrideCheckbox(state::logLevel)
        }
        row(message("configurations.locale.label")) {
            localeInput { bindItem(state::locale) }
            overrideCheckbox(state::locale)
        }
        row(message("configurations.numberOfThreads.label")) {
            numberOfThreadsInput { bindIntValue(state::numberOfThreads) }
            overrideCheckbox(state::numberOfThreads)
        }
        
    }
    
    advancedSettingsGroup {
        row {
            useSchemaFromStoreInput { bindSelected(state::useSchemaFromStore) }
            overrideCheckbox(state::useSchemaFromStore)
        }
    }
    
}


internal fun PanelBasedConfigurable<PyrightConfigurations>.createPanel(state: PyrightConfigurations): DialogPanel {
    val (project, overrides) = projectAndOverrides
    return PyrightPanel(state, overrides, project).makeComponent()
}
