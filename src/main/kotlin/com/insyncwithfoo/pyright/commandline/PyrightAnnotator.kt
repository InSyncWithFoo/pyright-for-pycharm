package com.insyncwithfoo.pyright.commandline

import com.insyncwithfoo.pyright.configurations.RunningMode
import com.insyncwithfoo.pyright.configurations.pyrightConfigurations
import com.insyncwithfoo.pyright.isSupportedByPyright
import com.insyncwithfoo.pyright.modules
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.ExternalAnnotator
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile


private fun FileDocumentManager.saveAllUnsavedDocumentsAsIs() {
    unsavedDocuments.forEach { saveDocumentAsIs(it) }
}


private val PsiElement.module: Module?
    get() = ModuleUtilCore.findModuleForPsiElement(this) ?: project.modules.singleOrNull()


internal class InitialInfo


internal class AnnotationResult


internal class PyrightAnnotator : ExternalAnnotator<InitialInfo, AnnotationResult>(), DumbAware {
    
    override fun getPairedBatchInspectionShortName() = PyrightInspection.SHORT_NAME
    
    override fun collectInformation(file: PsiFile, editor: Editor, hasErrors: Boolean): InitialInfo? {
        if (!file.isSupportedByPyright) {
            return null
        }
        
        val project = file.project
        val configurations = project.pyrightConfigurations
        
        if (configurations.runningMode != RunningMode.COMMAND_LINE) {
            return null
        }
        
        // TODO: Diagnostics setting
        
        val documentManager = FileDocumentManager.getInstance()
        
        // From https://intellij-support.jetbrains.com/hc/en-us/community/posts/17093296321426
        if (documentManager.unsavedDocuments.isNotEmpty()) {
            invokeLater { documentManager.saveAllUnsavedDocumentsAsIs() }
            return null
        }
        
        val module = file.module ?: return null
        
        return InitialInfo()  // TODO
    }
    
    override fun doAnnotate(collectedInfo: InitialInfo?): AnnotationResult? {
        
    }
    
    override fun apply(file: PsiFile, annotationResult: AnnotationResult?, holder: AnnotationHolder) {
        
    }
    
}
