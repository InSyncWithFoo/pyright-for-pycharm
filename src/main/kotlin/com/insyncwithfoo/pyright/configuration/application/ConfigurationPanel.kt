package com.insyncwithfoo.pyright.configuration.application

import com.insyncwithfoo.pyright.configuration.Hint
import com.insyncwithfoo.pyright.configuration.bindText
import com.insyncwithfoo.pyright.configuration.configurationFilePathResolvingHint
import com.insyncwithfoo.pyright.configuration.displayPathHint
import com.insyncwithfoo.pyright.configuration.executablePathResolvingHint
import com.insyncwithfoo.pyright.configuration.makeCellReturnComponent
import com.insyncwithfoo.pyright.configuration.onInput
import com.insyncwithfoo.pyright.configuration.prefilledWithRandomPlaceholder
import com.insyncwithfoo.pyright.configuration.secondColumnPathInput
import com.insyncwithfoo.pyright.message
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.Row
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.panel


private fun relativePathHint() =
    Hint.error(message("configurations.hint.globalMustBeAbsolute"))


private fun Row.makeAlwaysUseGlobalInput() =
    checkBox(message("configurations.global.alwaysUseGlobal.label"))


private fun Row.makeGlobalExecutableInput(block: Cell<TextFieldWithBrowseButton>.() -> Unit) =
    makeCellReturnComponent { secondColumnPathInput().apply(block) }


private fun Row.makeGlobalConfigurationFileInput(block: Cell<TextFieldWithBrowseButton>.() -> Unit) =
    makeCellReturnComponent { secondColumnPathInput().apply(block) }


private fun Row.makeUseEditorFontInput() =
    checkBox(message("configurations.global.useEditorFont.label"))


private fun Row.makeAddTooltipPrefixInput() =
    checkBox(message("configurations.global.addTooltipPrefix.label"))


internal fun configurationPanel(state: Configurations) = panel {
    // FIXME: The onInput() callbacks are too deeply nested.
    
    row {
        makeAlwaysUseGlobalInput().bindSelected(state::alwaysUseGlobal)
    }
    
    row(message("configurations.global.globalExecutable.label")) {
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
    
    row(message("configurations.global.globalConfigurationFile.label")) {
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
    
    group(message("configurations.global.group.tooltips")) {
        row {
            makeUseEditorFontInput().bindSelected(state::useEditorFont)
        }
        row {
            makeAddTooltipPrefixInput().bindSelected(state::addTooltipPrefix)
        }
    }
    
}
