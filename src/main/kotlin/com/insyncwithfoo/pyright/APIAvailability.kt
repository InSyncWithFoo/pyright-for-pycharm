package com.insyncwithfoo.pyright

import com.intellij.execution.wsl.target.WslTargetEnvironmentConfiguration
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.util.SystemInfo
import com.intellij.platform.lsp.api.LspServerSupportProvider


// https://stackoverflow.com/q/79750919
private inline fun <reified C> load() {
    C::class.qualifiedName
}


internal val lsp4ijIsAvailable: Boolean
    get() {
        val pluginID = PluginId.getId("com.redhat.devtools.lsp4ij")
        return PluginManagerCore.run { isPluginInstalled(pluginID) && !isDisabled(pluginID) }
    }


internal val lspIsAvailable by lazy {
    try {
        load<LspServerSupportProvider>()
        true
    } catch (_: NoClassDefFoundError) {
        false
    }
}


internal val wslIsSupported by lazy {
    SystemInfo.isWindows && try {
        load<WslTargetEnvironmentConfiguration>()
        true
    } catch (_: NoClassDefFoundError) {
        false
    }
}
