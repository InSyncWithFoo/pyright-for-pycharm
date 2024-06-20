package com.insyncwithfoo.pyright.configuration

import com.insyncwithfoo.pyright.isNormal
import com.intellij.openapi.components.BaseState
import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.ui.DialogPanel
import com.intellij.util.xmlb.XmlSerializerUtil
import com.redhat.devtools.lsp4ij.LanguageServerManager


private val ProjectManager.undisposedProjects: Sequence<Project>
    get() = openProjects.asSequence().filter { it.isNormal }


private val Project.languageServerManager: LanguageServerManager
    get() = LanguageServerManager.getInstance(this)


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
    
    protected fun stopAllServers() {
        ProjectManager.getInstance().undisposedProjects.forEach { project ->
            project.languageServerManager.stop("pyright")
        }
    }
    
    protected fun startServers() {
        ProjectManager.getInstance().undisposedProjects.forEach { project ->
            project.languageServerManager.start("pyright")
        }
    }
    
    protected fun restartAllServersIfSoChoose() {
        stopAllServers()
        startServers()
    }
    
}
