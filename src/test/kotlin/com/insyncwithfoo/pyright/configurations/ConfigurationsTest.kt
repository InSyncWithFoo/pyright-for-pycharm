package com.insyncwithfoo.pyright.configurations

import com.insyncwithfoo.pyright.configurations.models.fields
import com.insyncwithfoo.pyright.message
import com.intellij.openapi.components.BaseState
import org.junit.Assert.assertNotEquals
import org.junit.Before
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createInstance


internal abstract class ConfigurationsTest<C : BaseState> {

    abstract val configurationClass: KClass<C>
    
    protected lateinit var state: C
    protected lateinit var fields: Map<String, KProperty1<C, *>>
    
    @Before
    fun setUp() {
        state = configurationClass.createInstance()
        fields = configurationClass.fields
    }
    
    protected fun doMessagesTest(getKeyForName: (String) -> String) {
        fields.forEach { (name, _) ->
            val key = getKeyForName(name)
            
            assertNotEquals("!$key!", message(key))
        }
    }
    
}
