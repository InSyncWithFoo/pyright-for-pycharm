package com.insyncwithfoo.pyright

import com.intellij.execution.wsl.WSLDistribution
import com.intellij.execution.wsl.target.WslTargetEnvironmentConfiguration
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.Sdk
import com.jetbrains.python.target.PyTargetAwareAdditionalData


internal val Project.wslDistribution: WSLDistribution?
    get() = sdk?.wslDistribution


internal val Module.wslDistribution: WSLDistribution?
    get() = sdk?.wslDistribution ?: project.wslDistribution


internal val Sdk.wslDistribution: WSLDistribution?
    get() {
        val additionalData = sdkAdditionalData as? PyTargetAwareAdditionalData ?: return null
        val configuration = additionalData.targetEnvironmentConfiguration as? WslTargetEnvironmentConfiguration
        
        return configuration?.distribution
    }
