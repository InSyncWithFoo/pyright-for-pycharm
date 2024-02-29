package com.insyncwithfoo.pyright.configuration.application

import com.insyncwithfoo.pyright.configuration.PyrightConfigurationPanel
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import javax.swing.JCheckBox
import javax.swing.JLabel
import javax.swing.JPanel


class ConfigurationPanel : PyrightConfigurationPanel<Configurations>() {
    
    override lateinit var panel: JPanel
    
    private lateinit var alwaysUseGlobalInput: JCheckBox
    
    private lateinit var globalExecutableLabel: JLabel
    private lateinit var globalExecutableInput: TextFieldWithBrowseButton
    
    private lateinit var globalConfigurationFileLabel: JLabel
    private lateinit var globalConfigurationFileInput: TextFieldWithBrowseButton
    
    private lateinit var useEditorFontInput: JCheckBox
    
    override val textFieldsWithBrowseButtons: List<TextFieldWithBrowseButton>
        get() = listOf(globalExecutableInput, globalConfigurationFileInput)
    
    init {
        setLabels()
        addBrowseButtonListeners()
        applyExistingConfigurations()
    }
    
    override var configurations: Configurations
        get() = Configurations.create(
            alwaysUseGlobal = alwaysUseGlobalInput.isSelected,
            globalExecutable = globalExecutableInput.text.takeIf { it.isNotBlank() },
            globalConfigurationFile = globalConfigurationFileInput.text.takeIf { it.isNotBlank() },
            useEditorFont = useEditorFontInput.isSelected
        )
        set(value) {
            alwaysUseGlobalInput.isSelected = value.alwaysUseGlobal
            globalExecutableInput.text = value.globalExecutable.orEmpty()
            globalConfigurationFileInput.text = value.globalConfigurationFile.orEmpty()
            useEditorFontInput.isSelected = value.useEditorFont
        }
    
    override fun getService() = ConfigurationService.getInstance()
    
    override fun setLabels() {
        alwaysUseGlobalInput.text = "Always use global executable"
        globalExecutableLabel.text = "Global executable:"
        globalConfigurationFileLabel.text = "Configuration file:"
        useEditorFontInput.text = "Use editor font"
    }
    
}
