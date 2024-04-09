package com.insyncwithfoo.pyright.configuration

import com.intellij.openapi.components.BaseState
import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.util.xmlb.XmlSerializerUtil


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
    
}
