package com.insyncwithfoo.pyright.configuration.application

import com.insyncwithfoo.pyright.configuration.common.ConfigurationPanel
import com.insyncwithfoo.pyright.message
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import javax.swing.JCheckBox
import javax.swing.JLabel
import javax.swing.JPanel


internal class ConfigurationPanel : ConfigurationPanel<Configurations>() {
    
    override lateinit var panel: JPanel
    
    private lateinit var alwaysUseGlobalInput: JCheckBox
    
    private lateinit var globalExecutableLabel: JLabel
    private lateinit var globalExecutableInput: TextFieldWithBrowseButton
    
    private lateinit var globalConfigurationFileLabel: JLabel
    private lateinit var globalConfigurationFileInput: TextFieldWithBrowseButton
    
    private lateinit var useEditorFontInput: JCheckBox
    
    private lateinit var addTooltipPrefixInput: JCheckBox
    
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
            useEditorFont = useEditorFontInput.isSelected,
            addTooltipPrefix = addTooltipPrefixInput.isSelected
        )
        set(value) {
            alwaysUseGlobalInput.isSelected = value.alwaysUseGlobal
            globalExecutableInput.text = value.globalExecutable.orEmpty()
            globalConfigurationFileInput.text = value.globalConfigurationFile.orEmpty()
            useEditorFontInput.isSelected = value.useEditorFont
            addTooltipPrefixInput.isSelected = value.addTooltipPrefix
        }
    
    override fun getService() = ConfigurationService.getInstance()
    
    override fun setLabels() {
        alwaysUseGlobalInput.text = message("configurations.global.alwaysUseGlobal.label")
        globalExecutableLabel.text = message("configurations.global.globalExecutable.label")
        globalConfigurationFileLabel.text = message("configurations.global.globalConfigurationFile.label")
        useEditorFontInput.text = message("configurations.global.useEditorFont.label")
        addTooltipPrefixInput.text = message("configurations.global.addTooltipPrefix.label")
    }
    
}
