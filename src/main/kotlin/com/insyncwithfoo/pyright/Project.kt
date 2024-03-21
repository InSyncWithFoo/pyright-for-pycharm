package com.insyncwithfoo.pyright

import com.insyncwithfoo.pyright.configuration.PyrightAllConfigurations
import com.insyncwithfoo.pyright.configuration.PyrightConfigurationService
import com.intellij.codeInsight.daemon.HighlightDisplayKey
import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.profile.codeInspection.InspectionProjectProfileManager
import com.intellij.psi.PsiFile
import java.nio.file.Path


internal val Project.path: Path?
    get() = basePath?.let { Path.of(it) }


private val Project.sdk: Sdk?
    get() = ProjectRootManager.getInstance(this).projectSdk


internal val Project.sdkPath: Path?
    get() = sdk?.homePath?.let { Path.of(it) }


internal val Project.pyrightConfigurations: PyrightAllConfigurations
    get() = PyrightConfigurationService.getInstance(this).configurations


internal fun Project.isPyrightEnabled(file: PsiFile): Boolean {
    val profileManager = InspectionProjectProfileManager.getInstance(this)
    val profile = profileManager.currentProfile
    val key = HighlightDisplayKey.find(PyrightInspection.SHORT_NAME)
    
    return key != null && profile.isToolEnabled(key, file)
}
