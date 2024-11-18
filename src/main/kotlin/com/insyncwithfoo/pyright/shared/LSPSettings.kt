package com.insyncwithfoo.pyright.shared

import com.insyncwithfoo.pyright.configurations.pyrightConfigurations
import com.insyncwithfoo.pyright.interpreterPath
import com.insyncwithfoo.pyright.wslDistribution
import com.intellij.openapi.project.Project
import com.intellij.openapi.module.Module


private interface Builder


internal operator fun <T : Builder> T.invoke(block: T.() -> Unit) {
    this.apply(block)
}


internal data class Analysis(
    var logLevel: String? = null,
    var autoImportCompletions: Boolean? = null,
    var diagnosticMode: String? = null,
    var autoSearchPaths: Boolean = true
) : Builder


internal data class Python(
    var pythonPath: String? = null,
    val analysis: Analysis = Analysis()
) : Builder


internal class Pyright(
    var disableTaggedHints: Boolean? = null
) : Builder


internal data class Settings(
    val python: Python = Python(),
    val pyright: Pyright = Pyright()
)
