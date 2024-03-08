package com.insyncwithfoo.pyright.configuration.common

import com.intellij.openapi.options.Configurable


interface HasConfigurations<C> {
    var configurations: C
}


abstract class PyrightConfigurable<C> : Configurable {
    
    abstract val service: HasConfigurations<C>
    abstract val panel: ConfigurationPanel<C>
    
    abstract val originalConfigurations: C
    
    abstract override fun getDisplayName(): String
    
    override fun createComponent() = panel.component
    
    override fun isModified() =
        originalConfigurations != panel.configurations
    
    override fun apply() {
        service.configurations = panel.configurations
    }
    
    override fun reset() {
        panel.configurations = originalConfigurations
    }
    
}
