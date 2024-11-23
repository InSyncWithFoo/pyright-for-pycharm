package com.insyncwithfoo.pyright

import com.insyncwithfoo.pyright.configurations.pyrightConfigurations
import com.insyncwithfoo.pyright.configurations.targetedFileExtensionList
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile


internal val VirtualFile.isPyrightConfig: Boolean
    get() = name == "pyrightconfig.json"


// https://github.com/InSyncWithFoo/ryecharm/issues/5
private val PsiFile.isReST: Boolean
    get() = virtualFile?.extension == "rst"


internal fun VirtualFile.isSupportedByPyright(project: Project? = null): Boolean {
    val targeted = project?.pyrightConfigurations?.targetedFileExtensionList
    val fallback = listOf("py", "pyi", "pyw")
    
    return extension in (targeted ?: fallback)
}


internal val PsiFile.isSupportedByPyright: Boolean
    get() = virtualFile?.isSupportedByPyright(project) == true
