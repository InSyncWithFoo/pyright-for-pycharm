package com.insyncwithfoo.pyright.configuration.application

import com.insyncwithfoo.pyright.configuration.common.HasConfigurations
import com.intellij.openapi.components.RoamingType
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.util.xmlb.XmlSerializerUtil


@State(name = "ApplicationConfigurations", storages = [Storage("pyright.xml", roamingType = RoamingType.DISABLED)])
@Service(Service.Level.APP)
internal class ConfigurationService :
    SimplePersistentStateComponent<Configurations>(Configurations()),
    HasConfigurations<Configurations> {
    
    override var configurations: Configurations
        get() = state
        set(value) = XmlSerializerUtil.copyBean(value, state)
    
    companion object {
        fun getInstance() = service<ConfigurationService>()
    }
    
}
