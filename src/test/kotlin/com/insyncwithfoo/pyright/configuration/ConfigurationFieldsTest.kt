package com.insyncwithfoo.pyright.configuration

import com.insyncwithfoo.pyright.cli.PyrightDiagnosticSeverity
import com.insyncwithfoo.pyright.configuration.application.Locale
import junit.framework.TestCase
import kotlin.test.assertContains
import com.insyncwithfoo.pyright.configuration.application.Configurations as ApplicationConfigurations
import com.insyncwithfoo.pyright.configuration.application.RunningMode as ApplicationRunningMode
import com.insyncwithfoo.pyright.configuration.project.Configurations as ProjectConfigurations
import com.insyncwithfoo.pyright.configuration.project.RunningMode as ProjectRunningMode


class ConfigurationFieldsTest : TestCase() {
    
    fun `test shape - all`() {
        val allFields = allFields()
        
        assertEquals(allFields.keys, applicationFields().keys + projectFields().keys)
        
        (applicationFields() + projectFields()).forEach { (name, field) ->
            val correspondingFieldInAll = allFields[name]!!
            
            assertEquals(field.returnType, correspondingFieldInAll.type)
        }
    }
    
    fun `test defaults - application`() {
        val configurations = ApplicationConfigurations()
        
        assertEquals(11, applicationFields().size)
        
        configurations.run {
            assertEquals(false, alwaysUseGlobal)
            assertEquals(null, globalExecutable)
            assertEquals(null, globalConfigurationFile)
            assertEquals(false, useEditorFont)
            assertEquals(false, addTooltipPrefix)
            assertEquals(PyrightDiagnosticSeverity.INFORMATION, minimumSeverityLevel)
            assertEquals(10_000, processTimeout)
            assertEquals(null, globalLangserverExecutable)
            assertEquals(ApplicationRunningMode.CLI, globalRunningMode)
            assertEquals(0, numberOfThreads)
            assertEquals(Locale.DEFAULT, locale)
        }
    }
    
    fun `test defaults - project`() {
        val configurations = ProjectConfigurations()
        
        assertEquals(5, projectFields().size)
        
        configurations.run {
            assertEquals(null, projectExecutable)
            assertEquals(null, projectConfigurationFile)
            assertEquals(true, autoSuggestExecutable)
            assertEquals(null, projectLangserverExecutable)
            assertEquals(ProjectRunningMode.USE_GLOBAL, projectRunningMode)
        }
    }
    
    fun `test delegate - application`() {
        val receiver = ApplicationConfigurations()
        
        applicationFields().forEach { (_, property) ->
            assertNotNull(property.getDelegate(receiver))
        }
    }
    
    fun `test delegate - project`() {
        val receiver = ProjectConfigurations()
        
        projectFields().forEach { (_, property) ->
            assertNotNull(property.getDelegate(receiver))
        }
    }
    
    fun `test RunningMode`() {
        val applicationRunningModes = applicationRunningModes()
        val projectRunningModes = projectRunningModes()
        
        assertEquals(projectRunningModes.size, applicationRunningModes.size + 1)
        
        applicationRunningModes.forEach { (name, _) ->
            assertContains(projectRunningModes, name)
        }
        
        assertContains(projectRunningModes, "USE_GLOBAL")
    }
    
}
