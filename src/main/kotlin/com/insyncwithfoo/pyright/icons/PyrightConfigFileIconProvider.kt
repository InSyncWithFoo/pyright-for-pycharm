package com.insyncwithfoo.pyright.icons

import com.intellij.ide.FileIconProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile


internal class PyrightConfigFileIconProvider : FileIconProvider, DumbAware {
    
    override fun getIcon(file: VirtualFile, flags: Int, project: Project?) =
        when (file.name) {
            "pyrightconfig.json" -> Icon.COLORED_TINY
            else -> null
        }
    
}
