package com.insyncwithfoo.pyright.configuration.application

import com.insyncwithfoo.pyright.configuration.PyrightConfigurable
import com.insyncwithfoo.pyright.message


internal class Configurable : PyrightConfigurable<Configurations>() {
    
    override val service = ConfigurationService.getInstance()
    override val state = service.state.copy()
    override val originalState = state.copy()
    
    override val panel by lazy { configurationPanel(state) }
    
    override fun getDisplayName() = message("configurations.global.displayName")
    
}
