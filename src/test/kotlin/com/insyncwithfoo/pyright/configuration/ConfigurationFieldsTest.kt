package com.insyncwithfoo.pyright.configuration

import com.insyncwithfoo.pyright.PyrightDiagnosticSeverity
import junit.framework.TestCase
import com.insyncwithfoo.pyright.configuration.application.Configurations as ApplicationConfigurations
import com.insyncwithfoo.pyright.configuration.project.Configurations as ProjectConfigurations


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
        
        assertEquals(6, applicationFields().size)
        
        configurations.run {
            assertEquals(false, alwaysUseGlobal)
            assertEquals(null, globalExecutable)
            assertEquals(null, globalConfigurationFile)
            assertEquals(false, useEditorFont)
            assertEquals(false, addTooltipPrefix)
            assertEquals(PyrightDiagnosticSeverity.INFORMATION, minimumSeverityLevel)
        }
    }
    
    fun `test defaults - project`() {
        val configurations = ProjectConfigurations()
        
        assertEquals(3, projectFields().size)
        
        configurations.run {
            assertEquals(null, projectExecutable)
            assertEquals(null, projectConfigurationFile)
            assertEquals(true, autoSuggestExecutable)
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
    
}
