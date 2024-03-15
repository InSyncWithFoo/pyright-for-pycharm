package com.insyncwithfoo.pyright.configuration.project

import com.insyncwithfoo.pyright.configuration.common.PyrightConfigurable
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil


internal class PyrightProjectConfigurable internal constructor(project: Project) :
    PyrightConfigurable<Configurations>() {
    
    override val service = ConfigurationService.getInstance(project)
    override val panel by lazy { ConfigurationPanel(project) }
    
    override val originalConfigurations: Configurations =
        XmlSerializerUtil.createCopy(service.configurations)
    
    override fun getDisplayName() = DISPLAY_NAME
    
    companion object {
        const val DISPLAY_NAME = "Pyright (Project)"
    }
    
}
