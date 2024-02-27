package com.insyncwithfoo.pyright

import com.insyncwithfoo.pyright.configuration.PyrightAllConfigurations
import com.insyncwithfoo.pyright.configuration.PyrightConfigurationService
import com.intellij.codeInsight.daemon.HighlightDisplayKey
import com.intellij.lang.annotation.AnnotationBuilder
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.ExternalAnnotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.profile.codeInspection.InspectionProjectProfileManager
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.xml.util.XmlStringUtil
import com.jetbrains.python.PythonLanguage
import com.jetbrains.python.psi.PyFile
import com.jetbrains.python.psi.impl.PyFileImpl
import java.io.File


private val PsiFile.isInjected: Boolean
    get() {
        val manager = InjectedLanguageManager.getInstance(project)
        return manager.isInjectedFragment(this)
    }


private val PsiFile.languageIsPython: Boolean
    get() = language.isKindOf(PythonLanguage.getInstance())


private val PsiFile.isApplicable: Boolean
    get() {
        if (this !is PyFile || this.isInjected || !this.languageIsPython) {
            return false
        }
        
        if (this is PyFileImpl && !this.isAcceptedFor(PyrightInspection::class.java)) {
            return false
        }
        
        return true
    }


private fun Document.getOffset(endpoint: PyrightDiagnosticTextRangeEndpoint) =
    getLineStartOffset(endpoint.line) + endpoint.character


private fun Document.getStartEndRange(range: PyrightDiagnosticTextRange): TextRange {
    val start = getOffset(range.start)
    val end = getOffset(range.end)
    
    return TextRange(start, end)
}


private fun FileDocumentManager.saveAllUnsavedDocumentsAsIs() {
    unsavedDocuments.forEach { saveDocumentAsIs(it) }
}


private fun PyrightDiagnosticSeverity.toHighlightSeverity() = when (this) {
    PyrightDiagnosticSeverity.ERROR -> HighlightSeverity.WARNING
    PyrightDiagnosticSeverity.WARNING -> HighlightSeverity.WEAK_WARNING
    PyrightDiagnosticSeverity.INFORMATION -> HighlightSeverity.WEAK_WARNING
}


private fun String.toPreformattedTooltip(): String {
    val escapedLines = this.split("\n").map {
        XmlStringUtil.escapeString(it, true)
    }
    
    return escapedLines.joinToString("<br>")
}


private fun AnnotationHolder.makeBuilderForDiagnostic(diagnostic: PyrightDiagnostic): AnnotationBuilder {
    val (_, severity, message, rule) = diagnostic
    
    val tooltip = message.toPreformattedTooltip()
    val highlightSeverity = severity.toHighlightSeverity()
    
    val suffix = if (rule != null) " (${rule})" else ""
    val suffixedmessage = "$message$suffix"
    
    return newAnnotation(highlightSeverity, suffixedmessage).tooltip(tooltip)
}


private fun AnnotationHolder.applyDiagnostic(diagnostic: PyrightDiagnostic, document: Document) {
    val builder = makeBuilderForDiagnostic(diagnostic)
    val range = document.getStartEndRange(diagnostic.range)
    
    builder.annotateRange(range)
}


private fun AnnotationBuilder.annotateRange(range: TextRange) {
    this.needsUpdateOnTyping().range(range).create()
}


private fun Project.isPyrightEnabled(file: PsiFile): Boolean {
    val profileManager = InspectionProjectProfileManager.getInstance(this)
    val profile = profileManager.currentProfile
    val key = HighlightDisplayKey.find(PyrightInspection.SHORT_NAME)
    
    return key != null && profile.isToolEnabled(key, file)
}


private val Project.pyrightConfigurations: PyrightAllConfigurations
    get() = PyrightConfigurationService.getInstance(this).configurations


private fun String.toFileIfItExists(projectPath: String? = null) =
    File(this)
        .let { if (it.isAbsolute) it else File(projectPath, this).canonicalFile }
        .takeIf { it.exists() }


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
        val projectPath = file.project.basePath ?: return null
        
        val executable = configurations.executable?.toFileIfItExists(projectPath) ?: return null
        val target = file.virtualFile.path.toFileIfItExists() ?: return null
        val configurationFile = configurations.configurationFile
        
        val output = PyrightRunner(executable, target, configurationFile, projectPath).run()
        
        return Result(output)
    }
    
    override fun apply(file: PsiFile, annotationResult: Result?, holder: AnnotationHolder) {
        val project = file.project
        val documentManager = PsiDocumentManager.getInstance(project)
        val document = documentManager.getDocument(file) ?: return
        val output = annotationResult?.output ?: return
        
        output.generalDiagnostics.forEach {
            holder.applyDiagnostic(it, document)
        }
    }
    
    data class Info(
        val configurations: PyrightAllConfigurations,
        val file: PsiFile
    )
    
    data class Result(val output: PyrightOutput?)
    
}
