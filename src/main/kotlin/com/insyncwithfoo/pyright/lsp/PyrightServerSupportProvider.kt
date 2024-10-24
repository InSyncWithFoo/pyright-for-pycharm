package com.insyncwithfoo.pyright.lsp

import com.insyncwithfoo.pyright.commandline.PyrightInspection
import com.insyncwithfoo.pyright.commandline.pyrightInspectionisEnabled
import com.insyncwithfoo.pyright.configurations.RunningMode
import com.insyncwithfoo.pyright.configurations.pyrightConfigurations
import com.insyncwithfoo.pyright.configurations.pyrightLangserverExecutable
import com.insyncwithfoo.pyright.isSupportedByPyright
import com.insyncwithfoo.pyright.modules
import com.intellij.codeInspection.ex.InspectionToolRegistrar
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.project.Project
import com.intellij.openapi.module.Module
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspServerSupportProvider
import com.intellij.platform.lsp.api.LspServerSupportProvider.LspServerStarter
import com.intellij.profile.codeInspection.ProjectInspectionProfileManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager


private val Project.psiManager: PsiManager
    get() = PsiManager.getInstance(this)


private val PsiElement.module: Module?
    get() = ModuleUtilCore.findModuleForPsiElement(this) ?: project.modules.singleOrNull()


@Suppress("UnstableApiUsage")
internal class PyrightServerSupportProvider : LspServerSupportProvider {
    
    // TODO: Add widget
    
    override fun fileOpened(project: Project, file: VirtualFile, serverStarter: LspServerStarter) {
        val configurations = project.pyrightConfigurations
        val runningModeIsLSP = configurations.runningMode == RunningMode.LSP
        val psiFile = project.psiManager.findFile(file) ?: return
        
        if (runningModeIsLSP && file.isSupportedByPyright(project)) {
            val executable = project.pyrightLangserverExecutable ?: return
            val descriptor = PyrightServerDescriptor(project, psiFile.module, executable)
            
            serverStarter.ensureServerStarted(descriptor)
        }
    }
    
}
