package com.insyncwithfoo.pyright.configuration

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import javax.swing.JPanel


private fun TextFieldWithBrowseButton.addBrowseButtonListener() {
    addBrowseFolderListener(
        null, null, null,
        FileChooserDescriptorFactory.createSingleFileDescriptor()
    )
}


abstract class PyrightConfigurationPanel<C> {
    
    protected abstract var panel: JPanel
    
    val component by ::panel
    
    protected abstract val textFieldsWithBrowseButtons: List<TextFieldWithBrowseButton>
    
    abstract var configurations: C
    
    abstract fun getService(): HasConfigurations<C>
    
    abstract fun setLabels()
    
    protected fun addBrowseButtonListeners() {
        textFieldsWithBrowseButtons.forEach { it.addBrowseButtonListener() }
    }
    
    protected fun applyExistingConfigurations() {
        configurations = getService().configurations
    }
    
}
