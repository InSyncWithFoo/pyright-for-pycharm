package com.insyncwithfoo.pyright.configurations

import com.insyncwithfoo.pyright.bindSelected
import com.insyncwithfoo.pyright.bindText
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
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.Row
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.panel


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


// TODO: Fix this
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
        configurationFileInput { bindText(state::configurationFile) }
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
    
    separator()
    
}


internal fun PanelBasedConfigurable<PyrightConfigurations>.createPanel(state: PyrightConfigurations): DialogPanel {
    val (project, overrides) = projectAndOverrides
    return PyrightPanel(state, overrides, project).makeComponent()
}
