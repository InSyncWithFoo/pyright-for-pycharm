package com.insyncwithfoo.pyright.shared


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
