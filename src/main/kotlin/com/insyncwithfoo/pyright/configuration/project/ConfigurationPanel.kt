package com.insyncwithfoo.pyright.configuration.project

import com.insyncwithfoo.pyright.configuration.Hint
import com.insyncwithfoo.pyright.configuration.NO_LABEL
import com.insyncwithfoo.pyright.configuration.bindText
import com.insyncwithfoo.pyright.configuration.configurationFilePathResolvingHint
import com.insyncwithfoo.pyright.configuration.displayPathHint
import com.insyncwithfoo.pyright.configuration.executablePathResolvingHint
import com.insyncwithfoo.pyright.configuration.makeCellReturnComponent
import com.insyncwithfoo.pyright.configuration.onInput
import com.insyncwithfoo.pyright.configuration.prefilledWithRandomPlaceholder
import com.insyncwithfoo.pyright.configuration.secondColumnPathInput
import com.insyncwithfoo.pyright.message
import com.insyncwithfoo.pyright.path
import com.insyncwithfoo.pyright.resolvedAgainst
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.emptyText
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.Row
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.panel


private fun unresolvablePathHint() =
    Hint.error(message("configurations.hint.unresolvablePath"))


private fun Row.makeProjectExecutableInput(block: Cell<TextFieldWithBrowseButton>.() -> Unit) =
    makeCellReturnComponent { secondColumnPathInput().apply(block) }


private fun Row.makeProjectConfigurationFileInput(block: Cell<TextFieldWithBrowseButton>.() -> Unit) =
    makeCellReturnComponent { secondColumnPathInput().apply(block) }


private fun Row.makeAutoSuggestExecutableInput() =
    checkBox(message("configurations.project.autoSuggestExecutable.label"))


internal fun Configurable.configurationPanel(state: Configurations) = panel {
    // FIXME: The onInput() callbacks are too deeply nested.
    
    row(message("configurations.project.projectExecutable.label")) {
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
    row(NO_LABEL) {
        makeAutoSuggestExecutableInput().bindSelected(state::autoSuggestExecutable)
    }
    row(message("configurations.project.projectConfigurationFile.label")) {
        makeProjectConfigurationFileInput {
            onInput(::displayPathHint) { path ->
                when {
                    project.path == null && !path.isAbsolute -> unresolvablePathHint()
                    else -> configurationFilePathResolvingHint(path.resolvedAgainst(project.path))
                }
            }
            
            prefilledWithRandomPlaceholder()
            bindText(state::projectConfigurationFile)
            
            component.emptyText.text = when (project.path) {
                null -> message("configurations.project.projectConfigurationFile.placeholder")
                else -> project.path.toString()
            }
        }
    }
}
