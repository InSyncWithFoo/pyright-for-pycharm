package com.insyncwithfoo.pyright

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile


internal val VirtualFile.isPyrightConfig: Boolean
    get() = name == "pyrightconfig.json"


// https://github.com/InSyncWithFoo/ryecharm/issues/5
private val PsiFile.isReST: Boolean
    get() = virtualFile?.extension == "rst"


// TODO: Respect setting values
internal fun VirtualFile.isSupportedByPyright(project: Project? = null): Boolean {
    return extension == "py" || extension == "pyi"
}


internal val PsiFile.isSupportedByPyright: Boolean
    get() = virtualFile?.isSupportedByPyright(project) == true
