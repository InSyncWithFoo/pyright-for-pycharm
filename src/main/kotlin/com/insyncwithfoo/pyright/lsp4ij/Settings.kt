package com.insyncwithfoo.pyright.lsp4ij


private interface Builder


internal operator fun <T : Builder> T.invoke(block: T.() -> Unit) {
    this.apply(block)
}


internal data class Python(
    var pythonPath: String? = null
) : Builder


internal data class Settings(
    val python: Python = Python()
)
