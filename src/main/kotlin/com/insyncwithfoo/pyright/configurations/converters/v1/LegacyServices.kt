package com.insyncwithfoo.pyright.configurations.converters.v1

import com.intellij.openapi.components.RoamingType
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage


@State(
    name = "ApplicationConfigurations",
    storages = [Storage("pyright.xml", roamingType = RoamingType.LOCAL)]
)
@Service(Service.Level.APP)
internal class LegacyGlobalService :
    SimplePersistentStateComponent<LegacyGlobalConfigurations>(LegacyGlobalConfigurations())


@State(
    name = "ProjectConfigurations",
    storages = [Storage("pyright.xml")]
)
@Service(Service.Level.PROJECT)
internal class LegacyLocalService :
    SimplePersistentStateComponent<LegacyLocalConfigurations>(LegacyLocalConfigurations())
