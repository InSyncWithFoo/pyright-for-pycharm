package com.insyncwithfoo.pyright

import com.intellij.openapi.util.IconLoader


private fun loadIcon(path: String) = IconLoader.getIcon(path, PyrightIcons::class.java)


internal object PyrightIcons {
    val SMALL_32 = loadIcon("icons/32.svg")
    val TINY_18 = loadIcon("icons/18.svg")
    val TINY_16 = loadIcon("icons/16.svg")
    
    val TINY_16_WHITE = loadIcon("icons/16-white.svg")
}
