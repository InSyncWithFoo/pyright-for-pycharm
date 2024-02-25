package com.insyncwithfoo.pyright.configuration

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import javax.swing.JPanel


private fun TextFieldWithBrowseButton.addListener() {
    addBrowseFolderListener(
        null, null, null,
        FileChooserDescriptorFactory.createSingleFileDescriptor()
    )
}


abstract class PyrightConfigurationPanel<C> {
    
    protected abstract var panel: JPanel
    
    val component by ::panel
    
    abstract var configurations: C
    
    abstract fun getService(): HasConfigurations<C>
    
    abstract fun setLabels()
    
    protected fun addBrowseButtonListeners(browseButtons: List<TextFieldWithBrowseButton>) {
        browseButtons.forEach { it.addListener() }
    }
    
    protected fun applyExistingConfigurations() {
        configurations = getService().configurations
    }
    
}
