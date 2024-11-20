package com.insyncwithfoo.pyright.configurations.converters.pyrightls

import com.intellij.openapi.components.RoamingType
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage


@State(
    name = "com.insyncwithfoo.pyrightls.configuration.application.ConfigurationService",
    storages = [Storage("pyright-langserver.xml", roamingType = RoamingType.LOCAL)]
)
@Service(Service.Level.APP)
internal class LegacyGlobalService :
    SimplePersistentStateComponent<LegacyGlobalConfigurations>(LegacyGlobalConfigurations())


@State(
    name = "com.insyncwithfoo.pyrightls.configuration.project.ConfigurationService",
    storages = [Storage("pyright-langserver.xml")]
)
@Service(Service.Level.PROJECT)
internal class LegacyLocalService :
    SimplePersistentStateComponent<LegacyLocalConfigurations>(LegacyLocalConfigurations())
