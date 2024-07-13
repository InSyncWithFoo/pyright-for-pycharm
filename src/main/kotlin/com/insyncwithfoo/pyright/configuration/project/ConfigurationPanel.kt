package com.insyncwithfoo.pyright.configuration.project

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
import com.insyncwithfoo.pyright.path
import com.insyncwithfoo.pyright.resolvedAgainst
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.emptyText
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.ButtonsGroup
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.Row
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.toMutableProperty


private fun unresolvablePathHint() =
    Hint.error(message("configurations.hint.unresolvablePath"))


private fun Row.radioButtonFor(runningMode: RunningMode) =
    radioButton(runningMode.label, runningMode)


private fun Row.makeProjectExecutableInput(block: Cell<TextFieldWithBrowseButton>.() -> Unit) =
    secondColumnPathInput().apply(block)


private fun Row.makeProjectConfigurationFileInput(block: Cell<TextFieldWithBrowseButton>.() -> Unit) =
    secondColumnPathInput().apply(block)


private fun Row.makeAutoSuggestExecutableInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.autoSuggestExecutable.label")).apply(block)


private fun Row.makeProjectLangserverExecutableInput(block: Cell<TextFieldWithBrowseButton>.() -> Unit) =
    secondColumnPathInput().apply(block)


private fun Panel.makeProjectRunningModeInput(block: ButtonsGroup.() -> Unit) = run {
    val group = buttonsGroup {
        row(message("configurations.runningMode.label")) {
            radioButtonFor(RunningMode.USE_GLOBAL)
            radioButtonFor(RunningMode.CLI)
            radioButtonFor(RunningMode.LSP4IJ)
        }
    }
    group.apply(block)
}


internal fun Configurable.configurationPanel(state: Configurations) = panel {
    // FIXME: The onInput() callbacks are too deeply nested.
    
    row {
        makeAutoSuggestExecutableInput { bindSelected(state::autoSuggestExecutable) }
    }
    
    row(message("configurations.projectExecutable.label")) {
        makeProjectExecutableInput {
            onInput(::displayPathHint) { path ->
                when {
                    project.path == null && !path.isAbsolute -> unresolvablePathHint()
                    else -> executablePathResolvingHint(path.resolvedAgainst(project.path))
                }
            }
            
            prefilledWithRandomPlaceholder()
            bindText(state::projectExecutable)
        }
    }
    
    row(message("configurations.projectLangserverExecutable.label")) {
        makeProjectLangserverExecutableInput {
            onInput(::displayPathHint) { path ->
                when {
                    project.path == null && !path.isAbsolute -> unresolvablePathHint()
                    else -> langserverExecutablePathResolvingHint(path.resolvedAgainst(project.path))
                }
            }
            
            prefilledWithRandomPlaceholder()
            bindText(state::projectLangserverExecutable)
        }
    }
    
    row(message("configurations.projectConfigurationFile.label")) {
        makeProjectConfigurationFileInput {
            onInput(::displayPathHint) { path ->
                when {
                    project.path == null && !path.isAbsolute -> unresolvablePathHint()
                    else -> configurationFilePathResolvingHint(path.resolvedAgainst(project.path))
                }
            }
            
            prefilledWithRandomPlaceholder()
            bindText(state::projectConfigurationFile)
            
            component.emptyText.text =
                project.path?.toString() ?: message("configurations.projectConfigurationFile.placeholder")
        }
    }
    
    makeProjectRunningModeInput {
        bind(state::projectRunningMode.toMutableProperty(), RunningMode::class.java)
    }
    
}
