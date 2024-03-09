package com.insyncwithfoo.pyright

import com.intellij.openapi.util.IconLoader


private fun loadIcon(path: String) = IconLoader.getIcon(path, PyrightIcon::class.java)


private fun loadOutlinedIcon(name: String) = loadIcon("icons/outline-$name.svg")


private fun loadColoredIcon(size: String) = loadIcon("icons/colored-$size.svg")


@Suppress("unused")
object PyrightIcon {
    val COLORED_MEDIUM = loadColoredIcon("medium")
    val COLORED_SMALL = loadColoredIcon("small")
    
    val OUTLINED_THIN_BLACK = loadOutlinedIcon("thin-black")
    val OUTLINED_THIN_WHITE = loadOutlinedIcon("thin-white")
    val OUTLINED_BOLD_BLACK = loadOutlinedIcon("bold-black")
    val OUTLINED_BOLD_WHITE = loadOutlinedIcon("bold-white")
}
