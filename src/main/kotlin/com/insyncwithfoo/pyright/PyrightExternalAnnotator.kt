package com.insyncwithfoo.pyright

import com.insyncwithfoo.pyright.configuration.PyrightAllConfigurations
import com.insyncwithfoo.pyright.configuration.PyrightConfigurationService
import com.insyncwithfoo.pyright.runner.PyrightCommand
import com.insyncwithfoo.pyright.runner.PyrightRunner
import com.intellij.codeInsight.daemon.HighlightDisplayKey
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.ExternalAnnotator
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.profile.codeInspection.InspectionProjectProfileManager
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.jetbrains.python.PythonLanguage
import com.jetbrains.python.psi.PyFile
import com.jetbrains.python.psi.impl.PyFileImpl


private val PsiFile.isInjected: Boolean
    get() {
        val manager = InjectedLanguageManager.getInstance(project)
        return manager.isInjectedFragment(this)
    }


private val PsiFile.languageIsPython: Boolean
    get() = language.isKindOf(PythonLanguage.getInstance())


private val PsiFile.isApplicable: Boolean
    get() = when {
        this !is PyFile || this.isInjected || !this.languageIsPython -> false
        this is PyFileImpl && !this.isAcceptedFor(PyrightInspection::class.java) -> false
        else -> true
    }


private fun FileDocumentManager.saveAllUnsavedDocumentsAsIs() {
    unsavedDocuments.forEach { saveDocumentAsIs(it) }
}


private fun Project.isPyrightEnabled(file: PsiFile): Boolean {
    val profileManager = InspectionProjectProfileManager.getInstance(this)
    val profile = profileManager.currentProfile
    val key = HighlightDisplayKey.find(PyrightInspection.SHORT_NAME)
    
    return key != null && profile.isToolEnabled(key, file)
}


private val Project.pyrightConfigurations: PyrightAllConfigurations
    get() = PyrightConfigurationService.getInstance(this).configurations


class PyrightExternalAnnotator :
    ExternalAnnotator<PyrightExternalAnnotator.Info, PyrightExternalAnnotator.Result>() {
    
    override fun getPairedBatchInspectionShortName() =
        PyrightInspection.SHORT_NAME
    
    override fun collectInformation(file: PsiFile): Info? {
        if (!file.isApplicable) {
            return null
        }
        
        val documentManager = FileDocumentManager.getInstance()
        
        // From https://intellij-support.jetbrains.com/hc/en-us/community/posts/17093296321426
        if (documentManager.unsavedDocuments.isNotEmpty()) {
            invokeLater { documentManager.saveAllUnsavedDocumentsAsIs() }
            return null
        }
        
        val project = file.project
        
        if (!project.isPyrightEnabled(file)) {
            return null
        }
        
        val configurations = project.pyrightConfigurations
        
        if (configurations.executable == null) {
            return null
        }
        
        return Info(configurations, file)
    }
    
    override fun doAnnotate(collectedInfo: Info?): Result? {
        val (configurations, file) = collectedInfo ?: return null
        
        val command = PyrightCommand.create(configurations, file) ?: return null
        val output = PyrightRunner(command).run() ?: return null
        
        return Result(configurations, output)
    }
    
    override fun apply(file: PsiFile, annotationResult: Result?, holder: AnnotationHolder) {
        val (configurations, output) = annotationResult ?: return
        
        val project = file.project
        val documentManager = PsiDocumentManager.getInstance(project)
        val document = documentManager.getDocument(file) ?: return
        
        PyrightAnnotationApplier(document, output, configurations, holder).apply()
    }
    
    data class Info(
        val configurations: PyrightAllConfigurations,
        val file: PsiFile
    )
    
    data class Result(
        val configurations: PyrightAllConfigurations,
        val output: PyrightOutput
    )
    
}
