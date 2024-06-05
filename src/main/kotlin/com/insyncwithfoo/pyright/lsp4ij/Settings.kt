package com.insyncwithfoo.pyright.lsp4ij


internal class Python {
    var pythonPath: String? = null
}


internal class Settings(block: Settings.() -> Unit) {
    
    private val python = Python()
    
    init {
        this.apply(block)
    }
    
    fun python(block: Python.() -> Unit) {
        python.apply(block)
    }
    
}
