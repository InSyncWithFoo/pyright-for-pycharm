package com.insyncwithfoo.pyright

import com.intellij.openapi.module.Module
import com.intellij.openapi.projectRoots.Sdk
import com.jetbrains.python.sdk.PythonSdkUtil
import java.nio.file.Path


private val Module?.sdk: Sdk?
    get() = PythonSdkUtil.findPythonSdk(this)


internal val Module.interpreterPath: Path?
    get() = sdk?.homePath?.let { Path.of(it) } ?: project.interpreterPath
