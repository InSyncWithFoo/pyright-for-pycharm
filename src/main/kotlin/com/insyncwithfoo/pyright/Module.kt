package com.insyncwithfoo.pyright

import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.psi.PsiElement
import com.jetbrains.python.sdk.PythonSdkUtil
import com.jetbrains.python.sdk.basePath
import java.nio.file.Path


internal val Module?.sdk: Sdk?
    get() = PythonSdkUtil.findPythonSdk(this)


internal val Module.rootManager: ModuleRootManager
    get() = ModuleRootManager.getInstance(this)


internal val Module.path: Path?
    get() = basePath?.toPathOrNull()


internal val Module.interpreterPath: Path?
    get() = sdk?.homePath?.let { Path.of(it) } ?: project.interpreterPath


internal val PsiElement.module: Module?
    get() = ModuleUtilCore.findModuleForPsiElement(this) ?: project.modules.singleOrNull()
