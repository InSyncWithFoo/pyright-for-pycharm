package com.insyncwithfoo.pyright.configuration

import junit.framework.TestCase
import com.insyncwithfoo.pyright.configuration.application.Configurations as ApplicationConfigurations
import com.insyncwithfoo.pyright.configuration.project.Configurations as ProjectConfigurations


class ConfigurationFieldsTest : TestCase() {
    
    fun `test shape - application`() {
        val fields = applicationFields()
        val parameters = applicationParameters()
        
        assertEquals(fields.keys, parameters.keys)
        
        fields.forEach { (name, property) ->
            assertEquals(property.returnType, parameters[name]!!.type)
        }
    }
    
    fun `test shape - project`() {
        val fields = projectFields()
        val parameters = projectParameters()
        
        assertEquals(fields.keys, parameters.keys)
        
        fields.forEach { (name, property) ->
            assertEquals(property.returnType, parameters[name]!!.type)
        }
    }
    
    fun `test shape - all`() {
        val allFields = allFields()
        
        assertEquals(allFields.keys, applicationFields().keys + projectFields().keys)
        
        (applicationParameters() + projectParameters()).forEach { (name, parameter) ->
            assertEquals(parameter.type.annotations, allFields[name]!!.type.annotations)
            assertEquals(parameter.type, allFields[name]!!.type)
        }
    }
    
    fun `test defaults - application`() {
        val configurations = ApplicationConfigurations()
        
        assertEquals(5, applicationFields().size)
        
        assertEquals(false, configurations.alwaysUseGlobal)
        assertEquals(null, configurations.globalExecutable)
        assertEquals(null, configurations.globalConfigurationFile)
        assertEquals(false, configurations.useEditorFont)
        assertEquals(false, configurations.addTooltipPrefix)
    }
    
    fun `test defaults - project`() {
        val configurations = ProjectConfigurations()
        
        assertEquals(2, projectFields().size)
        
        assertEquals(null, configurations.projectExecutable)
        assertEquals(null, configurations.projectConfigurationFile)
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
