package com.insyncwithfoo.pyright.configuration.application

import com.insyncwithfoo.pyright.PyrightDiagnosticSeverity
import com.insyncwithfoo.pyright.configuration.Hint
import com.insyncwithfoo.pyright.configuration.bindText
import com.insyncwithfoo.pyright.configuration.configurationFilePathResolvingHint
import com.insyncwithfoo.pyright.configuration.displayPathHint
import com.insyncwithfoo.pyright.configuration.executablePathResolvingHint
import com.insyncwithfoo.pyright.configuration.langserverExecutablePathResolvingHint
import com.insyncwithfoo.pyright.configuration.onInput
import com.insyncwithfoo.pyright.configuration.prefilledWithRandomPlaceholder
import com.insyncwithfoo.pyright.configuration.secondColumnPathInput
import com.insyncwithfoo.pyright.message
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.JBIntSpinner
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.ButtonsGroup
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.Row
import com.intellij.ui.dsl.builder.bindIntValue
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.toMutableProperty
import com.intellij.ui.dsl.builder.toNullableProperty


private fun relativePathHint() =
    Hint.error(message("configurations.hint.globalMustBeAbsolute"))


private val PyrightDiagnosticSeverity.label: String
    get() = when (this) {
        PyrightDiagnosticSeverity.ERROR -> message("configurations.minimumSeverityLevel.error")
        PyrightDiagnosticSeverity.WARNING -> message("configurations.minimumSeverityLevel.warning")
        PyrightDiagnosticSeverity.INFORMATION -> message("configurations.minimumSeverityLevel.information")
    }


private fun Row.makeAlwaysUseGlobalInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.alwaysUseGlobal.label")).apply(block)


private fun Row.makeGlobalExecutableInput(block: Cell<TextFieldWithBrowseButton>.() -> Unit) =
    secondColumnPathInput().apply(block)


private fun Row.makeGlobalConfigurationFileInput(block: Cell<TextFieldWithBrowseButton>.() -> Unit) =
    secondColumnPathInput().apply(block)


private fun Row.makeUseEditorFontInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.useEditorFont.label")).apply(block)


private fun Row.makeAddTooltipPrefixInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.addTooltipPrefix.label")).apply(block)


private fun Row.makeProcessTimeoutInput(block: Cell<JBIntSpinner>.() -> Unit) =
    spinner(-1..3_600_000, step = 100).apply(block)


private fun Row.makeMinimumSeverityLevelInput(block: Cell<ComboBox<PyrightDiagnosticSeverity>>.() -> Unit) = run {
    val renderer = SimpleListCellRenderer.create<PyrightDiagnosticSeverity> { label, value, _ ->
        label.text = value.label
    }
    
    comboBox(PyrightDiagnosticSeverity.entries, renderer).apply(block)
}


private fun Row.makeGlobalLangserverExecutableInput(block: Cell<TextFieldWithBrowseButton>.() -> Unit) =
    secondColumnPathInput().apply(block)


private fun Panel.makeGlobalRunningModeInput(block: ButtonsGroup.() -> Unit) = run {
    val group = buttonsGroup {
        row(message("configurations.runningMode.label")) {
            radioButton(message("configurations.runningMode.cli"), RunningMode.CLI)
            radioButton(message("configurations.runningMode.lsp4ij"), RunningMode.LSP4IJ)
        }
    }
    group.apply(block)
}


@Suppress("DialogTitleCapitalization")
internal fun configurationPanel(state: Configurations) = panel {
    // FIXME: The onInput() callbacks are too deeply nested.
    
    row {
        makeAlwaysUseGlobalInput { bindSelected(state::alwaysUseGlobal) }
    }
    
    row(message("configurations.globalExecutable.label")) {
        makeGlobalExecutableInput {
            onInput(::displayPathHint) { path ->
                when {
                    !path.isAbsolute -> relativePathHint()
                    else -> executablePathResolvingHint(path)
                }
            }
            
            prefilledWithRandomPlaceholder()
            bindText(state::globalExecutable)
        }
    }
    
    row(message("configurations.globalLangserverExecutable.label")) {
        makeGlobalLangserverExecutableInput {
            onInput(::displayPathHint) { path ->
                when {
                    !path.isAbsolute -> relativePathHint()
                    else -> langserverExecutablePathResolvingHint(path)
                }
            }
            
            prefilledWithRandomPlaceholder()
            bindText(state::globalLangserverExecutable)
        }
    }
    
    row(message("configurations.globalConfigurationFile.label")) {
        makeGlobalConfigurationFileInput {
            onInput(::displayPathHint) { path ->
                when {
                    !path.isAbsolute -> relativePathHint()
                    else -> configurationFilePathResolvingHint(path)
                }
            }
            
            prefilledWithRandomPlaceholder()
            bindText(state::globalConfigurationFile)
        }
    }
    
    makeGlobalRunningModeInput {
        bind(state::globalRunningMode.toMutableProperty(), RunningMode::class.java)
    }
    
    group(message("configurations.group.tooltips")) {
        row {
            makeUseEditorFontInput { bindSelected(state::useEditorFont) }
        }
        row {
            makeAddTooltipPrefixInput { bindSelected(state::addTooltipPrefix) }
        }
    }
    
    group(message("configurations.group.others")) {
        row(message("configurations.processTimeout.label")) {
            makeProcessTimeoutInput { bindIntValue(state::processTimeout) }
        }
        row(message("configurations.minimumSeverityLevel.label")) {
            makeMinimumSeverityLevelInput { bindItem(state::minimumSeverityLevel.toNullableProperty()) }
        }
    }
    
}
