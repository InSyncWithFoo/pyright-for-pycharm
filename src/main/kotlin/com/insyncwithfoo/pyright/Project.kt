package com.insyncwithfoo.pyright

import com.insyncwithfoo.pyright.configuration.AllConfigurations
import com.insyncwithfoo.pyright.configuration.ConfigurationService
import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.profile.codeInspection.InspectionProjectProfileManager
import java.nio.file.Path
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.nameWithoutExtension


internal val Project.path: Path?
    get() = basePath?.let { Path.of(it) }


private val Project.sdk: Sdk?
    get() = ProjectRootManager.getInstance(this).projectSdk


internal val Project.sdkPath: Path?
    get() = sdk?.homePath?.let { Path.of(it) }


internal val Project.pyrightConfigurations: AllConfigurations
    get() = ConfigurationService.getInstance(this).state


internal fun Project.isPyrightInspectionEnabled(): Boolean {
    val inspectionManager = InspectionProjectProfileManager.getInstance(this)
    val profile = inspectionManager.currentProfile
    val pyrightInspection = profile.allTools.find { it.tool.shortName == PyrightInspection.SHORT_NAME }
    
    return pyrightInspection?.isEnabled ?: false
}


internal fun Project.findPyrightExecutable(): Path? {
    val sdkDirectory = sdkPath?.parent ?: return null
    val children = sdkDirectory.listDirectoryEntries()
    
    return children.find { it.nameWithoutExtension == "pyright" }
}
