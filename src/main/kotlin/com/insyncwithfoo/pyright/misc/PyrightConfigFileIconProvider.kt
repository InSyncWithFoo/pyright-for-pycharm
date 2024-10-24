package com.insyncwithfoo.pyright.misc

import com.insyncwithfoo.pyright.PyrightIcons
import com.intellij.ide.FileIconProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile


internal class PyrightConfigFileIconProvider : FileIconProvider, DumbAware {
    
    override fun getIcon(file: VirtualFile, flags: Int, project: Project?) =
        when (file.name) {
            "pyrightconfig.json" -> PyrightIcons.TINY_16
            else -> null
        }
    
}
