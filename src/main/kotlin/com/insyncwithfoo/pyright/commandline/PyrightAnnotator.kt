package com.insyncwithfoo.pyright.commandline

import com.insyncwithfoo.pyright.configurations.PyrightConfigurations
import com.insyncwithfoo.pyright.configurations.RunningMode
import com.insyncwithfoo.pyright.configurations.pyrightConfigurations
import com.insyncwithfoo.pyright.fileDocumentManager
import com.insyncwithfoo.pyright.invokeLater
import com.insyncwithfoo.pyright.isSupportedByPyright
import com.insyncwithfoo.pyright.message
import com.insyncwithfoo.pyright.modules
import com.insyncwithfoo.pyright.shared.SuppressQuickFix
import com.insyncwithfoo.pyright.shared.getFormattedTooltip
import com.insyncwithfoo.pyright.shared.getOffsetRange
import com.insyncwithfoo.pyright.shared.isUnsuppressable
import com.insyncwithfoo.pyright.shared.suffixedMessage
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.AnnotationBuilder
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.ExternalAnnotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.progress.runBlockingCancellable
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.toNioPathOrNull
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.nio.file.Path
import kotlin.io.path.name


private val Project.inspectionManager: InspectionManager
    get() = InspectionManager.getInstance(this)


private fun FileDocumentManager.saveAllUnsavedDocumentsAsIs() {
    unsavedDocuments.forEach { saveDocumentAsIs(it) }
}


private val PsiElement.module: Module?
    get() = ModuleUtilCore.findModuleForPsiElement(this) ?: project.modules.singleOrNull()


private fun HighlightSeverity.toProblemHighlightType() =
    ProblemHighlightType.valueOf(this.name.replace(" ", "_"))


internal data class InitialInfo(
    val module: Module,
    val configurations: PyrightConfigurations,
    val inspection: PyrightInspection,
    val path: Path
)


internal data class AnnotationResult(
    val configurations: PyrightConfigurations,
    val inspection: PyrightInspection,
    val result: Result
)


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
        
        if (!configurations.diagnostics) {
            return null
        }
        
        // From https://intellij-support.jetbrains.com/hc/en-us/community/posts/17093296321426
        if (fileDocumentManager.unsavedDocuments.isNotEmpty()) {
            invokeLater { fileDocumentManager.saveAllUnsavedDocumentsAsIs() }
            return null
        }
        
        val module = file.module ?: return null
        val path = file.virtualFile?.toNioPathOrNull() ?: return null
        val inspection = file.pyrightInspection
        
        return InitialInfo(module, configurations, inspection, path)
    }
    
    @Suppress("UnstableApiUsage")
    override fun doAnnotate(collectedInfo: InitialInfo?): AnnotationResult? {
        val (module, configurations, inspection, path) = collectedInfo ?: return null
        val command = FileCommand.create(module, path) ?: return null
        val project = module.project
        
        val output = runBlockingCancellable {
            project.runInBackground(message("progresses.runOnFile.title", path.name)) {
                command.run()
            }
        }
        
        project.reportErrorIfNecessary(output)
        
        if (output.isTimeout || output.isCancelled) {
            return null
        }
        
        val result = try {
            Json.decodeFromString<Result>(output.stdout.ifBlank { output.stderr })
        } catch (_: SerializationException) {
            return null
        }
        
        return AnnotationResult(configurations, inspection, result)
    }
    
    override fun apply(file: PsiFile, annotationResult: AnnotationResult?, holder: AnnotationHolder) {
        val (configurations, inspection, result) = annotationResult ?: return
        val document = file.viewProvider.document ?: return
        
        result.generalDiagnostics.forEach { diagnostic ->
            val message = diagnostic.suffixedMessage
            
            val range = document.getOffsetRange(diagnostic.range) ?: return@forEach
            val tooltip = diagnostic.getFormattedTooltip(configurations)
            
            val highlightSeverity = inspection.highlightSeverityFor(diagnostic.severity)
            val problemHighlightType = highlightSeverity.toProblemHighlightType()
            val builder = holder.newAnnotation(highlightSeverity, message)
            
            builder.needsUpdateOnTyping()
            builder.tooltip(tooltip)
            builder.range(range)
            
            diagnostic.makeSuppressFix(range)?.let {
                builder.registerQuickFix(file, message, it, problemHighlightType)
            }
            
            builder.create()
        }
    }
    
    private fun Diagnostic.makeSuppressFix(range: TextRange) = when {
        this.isUnsuppressable -> null
        else -> SuppressQuickFix(rule, range)
    }
    
    private fun AnnotationBuilder.registerQuickFix(
        file: PsiFile,
        message: String,
        fix: LocalQuickFix,
        problemHighlightType: ProblemHighlightType
    ) {
        val problemDescriptor = file.createProblemDescriptor(message, fix, problemHighlightType)
        newLocalQuickFix(fix, problemDescriptor).registerFix()
    }
    
    private fun PsiFile.createProblemDescriptor(
        message: String,
        fix: LocalQuickFix,
        problemHighlightType: ProblemHighlightType
    ): ProblemDescriptor {
        val onTheFly = true
        
        return project.inspectionManager.createProblemDescriptor(this, message, fix, problemHighlightType, onTheFly)
    }
    
}
