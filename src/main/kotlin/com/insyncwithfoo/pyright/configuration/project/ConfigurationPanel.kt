package com.insyncwithfoo.pyright.configuration.project

import com.insyncwithfoo.pyright.configuration.common.ConfigurationPanel
import com.insyncwithfoo.pyright.message
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import javax.swing.JLabel
import javax.swing.JPanel


internal class ConfigurationPanel(private val project: Project) : ConfigurationPanel<Configurations>() {
    
    override lateinit var panel: JPanel
    
    private lateinit var projectExecutableLabel: JLabel
    private lateinit var projectExecutableInput: TextFieldWithBrowseButton
    
    private lateinit var projectConfigurationFileLabel: JLabel
    private lateinit var projectConfigurationFileInput: TextFieldWithBrowseButton
    
    override val textFieldsWithBrowseButtons: List<TextFieldWithBrowseButton>
        get() = listOf(projectExecutableInput, projectConfigurationFileInput)
    
    init {
        setLabels()
        addBrowseButtonListeners()
        applyExistingConfigurations()
    }
    
    override var configurations: Configurations
        get() = Configurations.create(
            projectExecutable = projectExecutableInput.text.takeIf { it.isNotBlank() },
            projectConfigurationFile = projectConfigurationFileInput.text.takeIf { it.isNotBlank() }
        )
        set(value) {
            projectExecutableInput.text = value.projectExecutable.orEmpty()
            projectConfigurationFileInput.text = value.projectConfigurationFile.orEmpty()
        }
    
    override fun getService() = ConfigurationService.getInstance(project)
    
    override fun setLabels() {
        projectExecutableLabel.text = message("configurations.project.projectExecutable.label")
        projectConfigurationFileLabel.text = message("configurations.project.projectConfigurationFile.label")
    }
    
}
