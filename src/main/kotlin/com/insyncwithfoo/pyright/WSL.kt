package com.insyncwithfoo.pyright

import com.intellij.execution.wsl.WSLDistribution
import com.intellij.execution.wsl.WslPath
import com.intellij.execution.wsl.target.WslTargetEnvironmentConfiguration
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.util.io.OSAgnosticPathUtil
import com.jetbrains.python.target.PyTargetAwareAdditionalData
import java.net.URI
import java.nio.file.Path


internal val Sdk.path: Path?
    get() = homePath?.toPathOrNull()


internal val Path.isUncPath: Boolean
    get() = WslPath.parseWindowsUncPath(this.toString()) != null


internal val URI.pathIsAbsoluteDos: Boolean
    get() = OSAgnosticPathUtil.isAbsoluteDosPath(Path.of(this).toString())


internal fun String.asFileURI(): String {
    val (scheme, host, fragment) = Triple("file", "", null)
    return URI(scheme, host, this, fragment).toASCIIString()
}


internal fun WSLDistribution?.getPureLinuxOrWindowsPath(path: Path) = when {
    this != null && path.isUncPath -> this.getWslPath(path)!!
    else -> path.toString()
}


internal val Project.wslDistribution: WSLDistribution?
    get() = sdk?.wslDistribution
        ?: this.path?.toString()?.let { WslPath.getDistributionByWindowsUncPath(it) }


internal val Module.wslDistribution: WSLDistribution?
    get() = sdk?.wslDistribution ?: project.wslDistribution


internal val Sdk.wslDistribution: WSLDistribution?
    get() {
        val additionalData = sdkAdditionalData as? PyTargetAwareAdditionalData ?: return null
        val configuration = additionalData.targetEnvironmentConfiguration as? WslTargetEnvironmentConfiguration
        
        return configuration?.distribution
    }
