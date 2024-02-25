package com.insyncwithfoo.pyright.configuration.project

import com.insyncwithfoo.pyright.configuration.PyrightConfigurationPanel
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import javax.swing.JLabel
import javax.swing.JPanel


class ConfigurationPanel(private val project: Project) : PyrightConfigurationPanel<Configurations>() {
    
    override lateinit var panel: JPanel
    
    private lateinit var projectExecutableLabel: JLabel
    private lateinit var projectExecutableInput: TextFieldWithBrowseButton
    
    private lateinit var projectConfigurationFileLabel: JLabel
    private lateinit var projectConfigurationFileInput: TextFieldWithBrowseButton
    
    init {
        val browseButtons = listOf(projectExecutableInput, projectConfigurationFileInput)
        
        setLabels()
        addBrowseButtonListeners(browseButtons)
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
        projectExecutableLabel.text = "Project executable:"
        projectConfigurationFileLabel.text = "Configuration file:"
    }
    
}
