package com.insyncwithfoo.pyright

import com.insyncwithfoo.pyright.configuration.AllConfigurations
import com.insyncwithfoo.pyright.runner.PyrightCommand
import com.insyncwithfoo.pyright.runner.PyrightRunner
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.ExternalAnnotator
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.fileEditor.FileDocumentManager
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


internal data class AnnotationInfo(
    val configurations: AllConfigurations,
    val file: PsiFile
)


internal data class AnnotationResult(
    val configurations: AllConfigurations,
    val output: PyrightOutput
)


internal class PyrightExternalAnnotator : ExternalAnnotator<AnnotationInfo, AnnotationResult>() {
    
    override fun getPairedBatchInspectionShortName() =
        PyrightInspection.SHORT_NAME
    
    override fun collectInformation(file: PsiFile): AnnotationInfo? {
        if (!file.isApplicable) {
            return null
        }
        
        val project = file.project
        val configurations = project.pyrightConfigurations
        
        if (configurations.executable == null) {
            return null
        }
        
        val documentManager = FileDocumentManager.getInstance()
        
        // From https://intellij-support.jetbrains.com/hc/en-us/community/posts/17093296321426
        if (documentManager.unsavedDocuments.isNotEmpty()) {
            invokeLater { documentManager.saveAllUnsavedDocumentsAsIs() }
            return null
        }
        
        return AnnotationInfo(configurations, file)
    }
    
    override fun doAnnotate(collectedInfo: AnnotationInfo?): AnnotationResult? {
        val (configurations, file) = collectedInfo ?: return null
        
        val command = PyrightCommand.create(configurations, file) ?: return null
        val output = PyrightRunner(file.project).run(command) ?: return null
        
        return AnnotationResult(configurations, output)
    }
    
    override fun apply(file: PsiFile, annotationResult: AnnotationResult?, holder: AnnotationHolder) {
        val (configurations, output) = annotationResult ?: return
        
        val project = file.project
        val documentManager = PsiDocumentManager.getInstance(project)
        val document = documentManager.getDocument(file) ?: return
        
        AnnotationApplier(document, output, configurations, holder).apply()
    }
    
}
