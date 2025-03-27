package com.insyncwithfoo.pyright.lsp

import com.insyncwithfoo.pyright.configurations.RunningMode
import com.insyncwithfoo.pyright.configurations.pyrightConfigurations
import com.insyncwithfoo.pyright.configurations.pyrightLangserverExecutable
import com.insyncwithfoo.pyright.isSupportedByPyright
import com.insyncwithfoo.pyright.modules
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspServer
import com.intellij.platform.lsp.api.LspServerSupportProvider
import com.intellij.platform.lsp.api.LspServerSupportProvider.LspServerStarter
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.util.execution.ParametersListUtil


private val Project.psiManager: PsiManager
    get() = PsiManager.getInstance(this)


private val PsiElement.module: Module?
    get() = ModuleUtilCore.findModuleForPsiElement(this) ?: project.modules.singleOrNull()


@Suppress("UnstableApiUsage")
internal class PyrightServerSupportProvider : LspServerSupportProvider {
    
    override fun createLspServerWidgetItem(lspServer: LspServer, currentFile: VirtualFile?) =
        WidgetItem(lspServer, currentFile)
    
    override fun fileOpened(project: Project, file: VirtualFile, serverStarter: LspServerStarter) {
        val configurations = project.pyrightConfigurations
        val runningModeIsLSP = configurations.runningMode == RunningMode.LSP
        val psiFile = project.psiManager.findFile(file) ?: return
        
        if (!runningModeIsLSP || !file.isSupportedByPyright(project)) {
            return
        }
        
        val rawCommand = configurations.startLanguageServerCommand.orEmpty()
        val (keepQuotes, supportSingleQuotes, keepEmptyParameters) = Triple(false, true, true)
        val command = try {
            ParametersListUtil.parse(rawCommand, keepQuotes, supportSingleQuotes, keepEmptyParameters)
                .takeIf { it.isNotEmpty() }
        } catch (_: Exception) {
            null
        }
        
        val module = psiFile.module
        
        val descriptor = when {
            command != null -> PyrightServerDescriptor.fromCommand(project, module, command)
            else -> {
                val executable = project.pyrightLangserverExecutable ?: return
                PyrightServerDescriptor.fromExecutable(project, module, executable)
            }
        }
        
        serverStarter.ensureServerStarted(descriptor)
    }
    
}
