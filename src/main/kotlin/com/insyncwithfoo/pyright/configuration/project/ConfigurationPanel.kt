package com.insyncwithfoo.pyright.configuration.project

import com.insyncwithfoo.pyright.configuration.Hint
import com.insyncwithfoo.pyright.configuration.PathHintState
import com.insyncwithfoo.pyright.configuration.configurationFilePathResolvingHint
import com.insyncwithfoo.pyright.configuration.executablePathResolvingHint
import com.insyncwithfoo.pyright.configuration.langserverExecutablePathResolvingHint
import com.insyncwithfoo.pyright.configuration.makeFlexible
import com.insyncwithfoo.pyright.configuration.reactiveLabel
import com.insyncwithfoo.pyright.configuration.triggerChange
import com.insyncwithfoo.pyright.message
import com.insyncwithfoo.pyright.path
import com.insyncwithfoo.pyright.resolvedAgainst
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.emptyText
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.ButtonsGroup
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.Row
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.toMutableProperty
import com.intellij.ui.dsl.builder.toNonNullableProperty
import java.nio.file.Path


private val unresolvablePathHint: Hint
    get() = Hint.error(message("configurations.hint.unresolvablePath"))


private fun Row.radioButtonFor(runningMode: RunningMode) =
    radioButton(runningMode.label, runningMode)


private fun Project.makeResolvablePathHintState(makeResolvablePathHint: (Path) -> Hint) = PathHintState { path ->
    when {
        this.path == null && !path.isAbsolute -> unresolvablePathHint
        else -> makeResolvablePathHint(path.resolvedAgainst(this.path))
    }
}


private fun Row.makeProjectExecutableInput(block: Cell<TextFieldWithBrowseButton>.() -> Unit) =
    textFieldWithBrowseButton().makeFlexible().apply(block)


private fun Row.makeProjectConfigurationFileInput(block: Cell<TextFieldWithBrowseButton>.() -> Unit) =
    textFieldWithBrowseButton().makeFlexible().apply(block)


private fun Row.makeAutoSuggestExecutableInput(block: Cell<JBCheckBox>.() -> Unit) =
    checkBox(message("configurations.autoSuggestExecutable.label")).apply(block)


private fun Row.makeProjectLangserverExecutableInput(block: Cell<TextFieldWithBrowseButton>.() -> Unit) =
    textFieldWithBrowseButton().makeFlexible().apply(block)


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
    val executablePathHintState = project.makeResolvablePathHintState(::executablePathResolvingHint)
    val configurationFilePathHintState = project.makeResolvablePathHintState(::configurationFilePathResolvingHint)
    val langserverExecutablePathHintState = project.makeResolvablePathHintState(::langserverExecutablePathResolvingHint)
    
    row {
        makeAutoSuggestExecutableInput { bindSelected(state::autoSuggestExecutable) }
    }
    
    row(message("configurations.projectExecutable.label")) {
        makeProjectExecutableInput {
            bindText(executablePathHintState.path)
            bindText(state::projectExecutable.toNonNullableProperty(""))
            triggerChange()
        }
    }
    row("") {
        reactiveLabel(executablePathHintState.hint)
    }
    
    row(message("configurations.projectConfigurationFile.label")) {
        makeProjectConfigurationFileInput {
            bindText(configurationFilePathHintState.path)
            bindText(state::projectConfigurationFile.toNonNullableProperty(""))
            triggerChange()
            
            component.emptyText.text =
                project.path?.toString() ?: message("configurations.projectConfigurationFile.placeholder")
        }
    }
    row("") {
        reactiveLabel(configurationFilePathHintState.hint)
    }
    
    row(message("configurations.projectLangserverExecutable.label")) {
        makeProjectLangserverExecutableInput {
            bindText(langserverExecutablePathHintState.path)
            bindText(state::projectLangserverExecutable.toNonNullableProperty(""))
            triggerChange()
        }
    }
    row("") {
        reactiveLabel(langserverExecutablePathHintState.hint)
    }
    
    makeProjectRunningModeInput {
        bind(state::projectRunningMode.toMutableProperty(), RunningMode::class.java)
    }
    
}
