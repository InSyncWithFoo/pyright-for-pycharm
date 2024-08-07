package com.insyncwithfoo.pyright.icons

import com.intellij.openapi.util.IconLoader


private fun loadIcon(path: String) = IconLoader.getIcon(path, Icon::class.java)


@Suppress("unused")
internal object Icon {
    val COLORED_MEDIUM = loadIcon("icons/colored-medium.svg")
    val COLORED_SMALL = loadIcon("icons/colored-small.svg")
    val COLORED_TINY = loadIcon("icons/colored-tiny.svg")
    
    val OUTLINED_THIN_BLACK = loadIcon("icons/outline-thin-black.svg")
    val OUTLINED_THIN_WHITE = loadIcon("icons/outline-thin-white.svg")
    val OUTLINED_BOLD_BLACK = loadIcon("icons/outline-bold-black.svg")
    val OUTLINED_BOLD_WHITE = loadIcon("icons/outline-bold-white.svg")
}
