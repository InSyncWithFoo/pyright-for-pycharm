package com.insyncwithfoo.pyright.configuration.application

import com.insyncwithfoo.pyright.cli.PyrightDiagnosticSeverity
import com.insyncwithfoo.pyright.configuration.Hint
import com.insyncwithfoo.pyright.configuration.PathHintState
import com.insyncwithfoo.pyright.configuration.configurationFilePathResolvingHint
import com.insyncwithfoo.pyright.configuration.executablePathResolvingHint
import com.insyncwithfoo.pyright.configuration.langserverExecutablePathResolvingHint
import com.insyncwithfoo.pyright.configuration.makeFlexible
import com.insyncwithfoo.pyright.configuration.reactiveLabel
import com.insyncwithfoo.pyright.configuration.triggerChange
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
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.toMutableProperty
import com.intellij.ui.dsl.builder.toNonNullableProperty
import com.intellij.ui.dsl.builder.toNullableProperty
import java.nio.file.Path


private val relativePathHint: Hint
    get() = Hint.error(message("configurations.hint.globalMustBeAbsolute"))


private val PyrightDiagnosticSeverity.label: String
    get() = when (this) {
        PyrightDiagnosticSeverity.ERROR -> message("configurations.minimumSeverityLevel.error")
        PyrightDiagnosticSeverity.WARNING -> message("configurations.minimumSeverityLevel.warning")
        PyrightDiagnosticSeverity.INFORMATION -> message("configurations.minimumSeverityLevel.information")
    }


private fun makeAbsolutePathHintState(makeAbsolutePathHint: (Path) -> Hint) = PathHintState { path ->
    when {
        !path.isAbsolute -> relativePathHint
        else -> makeAbsolutePathHint(path)
    }
}


private fun Row.radioButtonFor(runningMode: RunningMode) {
    radioButton(runningMode.label, runningMode)
}


private fun Row.makeAlwaysUseGlobalInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.alwaysUseGlobal.label")).apply(block)


private fun Row.makeGlobalExecutableInput(block: Cell<TextFieldWithBrowseButton>.() -> Unit) =
    textFieldWithBrowseButton().makeFlexible().apply(block)


private fun Row.makeGlobalConfigurationFileInput(block: Cell<TextFieldWithBrowseButton>.() -> Unit) =
    textFieldWithBrowseButton().makeFlexible().apply(block)


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
    textFieldWithBrowseButton().makeFlexible().apply(block)


private fun Panel.makeGlobalRunningModeInput(block: ButtonsGroup.() -> Unit) = run {
    val group = buttonsGroup {
        row(message("configurations.runningMode.label")) {
            radioButtonFor(RunningMode.CLI)
            radioButtonFor(RunningMode.LSP4IJ)
        }
    }
    group.apply(block)
}


private fun Row.makeNumberOfThreadsInput(block: Cell<JBIntSpinner>.() -> Unit): Cell<JBIntSpinner> = run {
    val comment = message("configurations.numberOfThreads.comment")
    spinner(0..1_000_000, step = 1).comment(comment).apply(block)
}


@Suppress("DialogTitleCapitalization")
internal fun configurationPanel(state: Configurations) = panel {
    val executablePathHintState = makeAbsolutePathHintState(::executablePathResolvingHint)
    val configurationFilePathHintState = makeAbsolutePathHintState(::configurationFilePathResolvingHint)
    val langserverExecutablePathHintState = makeAbsolutePathHintState(::langserverExecutablePathResolvingHint)
    
    row {
        makeAlwaysUseGlobalInput { bindSelected(state::alwaysUseGlobal) }
    }
    
    row(message("configurations.globalExecutable.label")) {
        makeGlobalExecutableInput {
            bindText(executablePathHintState.path)
            bindText(state::globalExecutable.toNonNullableProperty(""))
            triggerChange()
        }
    }
    row("") {
        reactiveLabel(executablePathHintState.hint)
    }
    
    row(message("configurations.globalConfigurationFile.label")) {
        makeGlobalConfigurationFileInput {
            bindText(configurationFilePathHintState.path)
            bindText(state::globalConfigurationFile.toNonNullableProperty(""))
            triggerChange()
        }
    }
    row("") {
        reactiveLabel(configurationFilePathHintState.hint)
    }
    
    row(message("configurations.globalLangserverExecutable.label")) {
        makeGlobalLangserverExecutableInput {
            bindText(langserverExecutablePathHintState.path)
            bindText(state::globalLangserverExecutable.toNonNullableProperty(""))
            triggerChange()
        }
    }
    row("") {
        reactiveLabel(langserverExecutablePathHintState.hint)
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
        row(message("configurations.numberOfThreads.label")) {
            makeNumberOfThreadsInput { bindIntValue(state::numberOfThreads) }
        }
        row(message("configurations.minimumSeverityLevel.label")) {
            makeMinimumSeverityLevelInput { bindItem(state::minimumSeverityLevel.toNullableProperty()) }
        }
    }
    
}
