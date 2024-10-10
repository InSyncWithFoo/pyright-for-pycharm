package com.insyncwithfoo.pyright.icons

import com.intellij.openapi.util.IconLoader


private fun loadIcon(path: String) = IconLoader.getIcon(path, PyrightIcons::class.java)


internal object PyrightIcons {
    val TINY_18 = loadIcon("icons/18.svg")
    val TINY_16 = loadIcon("icons/16.svg")
}
