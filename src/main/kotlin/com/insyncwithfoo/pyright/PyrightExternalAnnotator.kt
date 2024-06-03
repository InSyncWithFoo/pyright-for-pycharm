package com.insyncwithfoo.pyright

import com.insyncwithfoo.pyright.annotations.AnnotationApplier
import com.insyncwithfoo.pyright.annotations.SuppressQuickFix
import com.insyncwithfoo.pyright.annotations.toHighlightSeverity
import com.insyncwithfoo.pyright.configuration.AllConfigurations
import com.insyncwithfoo.pyright.runner.FileCommand
import com.insyncwithfoo.pyright.runner.PyrightRunner
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.AnnotationBuilder
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.ExternalAnnotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.python.PythonLanguage
import com.jetbrains.python.psi.PyFile


private fun AnnotationBuilder.registerFix(createProblemDescriptor: () -> ProblemDescriptor?) {
    val problemDescriptor = createProblemDescriptor() ?: return
    val fix = problemDescriptor.fixes?.firstOrNull() as LocalQuickFix? ?: return
    
    newLocalQuickFix(fix, problemDescriptor).registerFix()
}


private fun FileDocumentManager.saveAllUnsavedDocumentsAsIs() {
    unsavedDocuments.forEach { saveDocumentAsIs(it) }
}


private fun HighlightSeverity.toProblemHighlightType() =
    ProblemHighlightType.valueOf(this.name.replace(" ", "_"))


private fun InspectionManager.createProblemDescriptor(
    psiElement: PsiElement,
    fix: LocalQuickFix,
    highlightType: ProblemHighlightType
) =
    createProblemDescriptor(psiElement, fix.familyName, fix, highlightType, true)


private val Project.inspectionManager: InspectionManager
    get() = InspectionManager.getInstance(this)


private val Project.psiDocumentManager: PsiDocumentManager
    get() = PsiDocumentManager.getInstance(this)


private val PsiFile.isInjected: Boolean
    get() {
        val manager = InjectedLanguageManager.getInstance(project)
        return manager.isInjectedFragment(this)
    }


private val PsiFile.languageIsPython: Boolean
    get() = language.isKindOf(PythonLanguage.getInstance())


private val PsiFile.isApplicable: Boolean
    get() = this is PyFile && !this.isInjected && this.languageIsPython


private fun PsiFile.getPyrightInspection(): PyrightInspection {
    val profile = project.inspectionProfileManager.currentProfile
    
    return profile.getUnwrappedTool(PyrightInspection.SHORT_NAME, this) as PyrightInspection
}


private fun PsiFile.getElementAtRange(range: TextRange): PsiElement? {
    val psiElement = findElementAtRange(range)
    
    return when {
        psiElement != null -> psiElement
        range.isEmpty && range.atEndOfFile(this) -> findElementAt(range.endOffset - 1)
        else -> findElementAt(range.startOffset)
    }
}


private fun PsiFile.findElementAtRange(range: TextRange): PsiElement? {
    val (start, end) = range.startOffset to range.endOffset
    return PsiTreeUtil.findElementOfClassAtRange(this, start, end, PsiElement::class.java)
}


private val PyrightDiagnostic.isUnsuppressable: Boolean
    get() {
        val unsuppressableErrorCodes = listOf("reportUnnecessaryTypeIgnoreComment")
        
        return rule in unsuppressableErrorCodes
    }


private fun TextRange.atEndOfFile(file: PsiFile) = endOffset == file.textLength


internal data class AnnotationInfo(
    val configurations: AllConfigurations,
    val inspection: PyrightInspection,
    val file: PsiFile
)


internal data class AnnotationResult(
    val configurations: AllConfigurations,
    val inspection: PyrightInspection,
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
        
        return AnnotationInfo(configurations, file.getPyrightInspection(), file)
    }
    
    override fun doAnnotate(collectedInfo: AnnotationInfo?): AnnotationResult? {
        val (configurations, inspection, file) = collectedInfo ?: return null
        
        val command = FileCommand.create(file) ?: return null
        val output = PyrightRunner(file.project).run(command) ?: return null
        
        return AnnotationResult(configurations, inspection, output)
    }
    
    override fun apply(file: PsiFile, annotationResult: AnnotationResult?, holder: AnnotationHolder) {
        val (configurations, inspection, output) = annotationResult ?: return
        
        val project = file.project
        val document = project.psiDocumentManager.getDocument(file) ?: return
        
        val annotationApplier = AnnotationApplier(configurations, inspection, holder)
        
        annotationApplier.apply(document, output) { builder, diagnostic, range ->
            builder.registerFix {
                if (diagnostic.isUnsuppressable) {
                    return@registerFix null
                }
                
                val element = file.getElementAtRange(range) ?: return@registerFix null
                val fix = SuppressQuickFix(diagnostic.rule, range)
                
                val problemHighlightType = diagnostic.severity
                    .toHighlightSeverity(inspection)
                    .toProblemHighlightType()
                
                project.inspectionManager.createProblemDescriptor(element, fix, problemHighlightType)
            }
        }
    }
    
}
