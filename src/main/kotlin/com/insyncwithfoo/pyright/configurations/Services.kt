package com.insyncwithfoo.pyright.configurations

import com.insyncwithfoo.pyright.configurations.models.ConfigurationService
import com.insyncwithfoo.pyright.configurations.models.getMergedState
import com.intellij.openapi.components.RoamingType
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project


@State(name = "com.insyncwithfoo.pyright.configurations.Global", storages = [Storage("pyright.xml")])
@Service(Service.Level.APP)
internal class PyrightGlobalService : ConfigurationService<PyrightConfigurations>(PyrightConfigurations()) {
    
    companion object {
        fun getInstance() = service<PyrightGlobalService>()
    }
    
}


@State(name = "com.insyncwithfoo.pyright.configurations.Local", storages = [Storage("pyright.xml")])
@Service(Service.Level.PROJECT)
internal class PyrightLocalService : ConfigurationService<PyrightConfigurations>(PyrightConfigurations()) {
    
    companion object {
        fun getInstance(project: Project) = project.service<PyrightLocalService>()
    }
    
}


@State(
    name = "com.insyncwithfoo.pyright.configurations.Override",
    storages = [Storage("pyright-overrides.xml", roamingType = RoamingType.DISABLED)]
)
@Service(Service.Level.PROJECT)
internal class PyrightOverrideService : ConfigurationService<PyrightOverrides>(PyrightOverrides()) {
    
    companion object {
        fun getInstance(project: Project) = project.service<PyrightOverrideService>()
    }
    
}


internal val globalPyrightConfigurations: PyrightConfigurations
    get() = PyrightGlobalService.getInstance().state


internal val Project.pyrightConfigurations: PyrightConfigurations
    get() = getMergedState<PyrightGlobalService, PyrightLocalService, PyrightOverrideService, _>()
