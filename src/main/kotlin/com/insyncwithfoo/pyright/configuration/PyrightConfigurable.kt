package com.insyncwithfoo.pyright.configuration

import com.insyncwithfoo.pyright.configuration.application.RunningMode
import com.insyncwithfoo.pyright.lsp4ij.SERVER_ID
import com.insyncwithfoo.pyright.pyrightConfigurations
import com.intellij.openapi.components.BaseState
import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.util.xmlb.XmlSerializerUtil
import com.redhat.devtools.lsp4ij.LanguageServerManager


private val Project.languageServerManager: LanguageServerManager
    get() = LanguageServerManager.getInstance(this)


private fun Project.stopServers(disable: Boolean = false) {
    val options = LanguageServerManager.StopOptions().apply {
        isWillDisable = disable
    }
    
    languageServerManager.stop(SERVER_ID, options)
}


private fun Project.startServers(enable: Boolean = false) {
    val options = LanguageServerManager.StartOptions().apply { 
        isWillEnable = enable
    }
    
    languageServerManager.start(SERVER_ID, options)
}


internal abstract class PyrightConfigurable<State : BaseState> : Configurable {
    
    protected abstract val service: SimplePersistentStateComponent<State>
    protected abstract val state: State
    
    protected abstract val panel: DialogPanel
    
    override fun createComponent() = panel
    
    override fun isModified() = panel.isModified()
    
    override fun apply() {
        panel.apply()
        XmlSerializerUtil.copyBean(state, service.state)
    }
    
    override fun reset() {
        panel.reset()
    }
    
    protected fun State.copy(): State {
        return XmlSerializerUtil.createCopy(this)
    }
    
    protected fun Project.toggleServersAccordingly() {
        stopServers()
        
        if (pyrightConfigurations.runningMode == RunningMode.LSP4IJ) {
            startServers()
        }
    }
    
}
