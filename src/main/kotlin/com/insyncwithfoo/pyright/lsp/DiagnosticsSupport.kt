package com.insyncwithfoo.pyright.lsp

import com.insyncwithfoo.pyright.commandline.HighlightSeverity
import com.insyncwithfoo.pyright.commandline.pyrightInspection
import com.insyncwithfoo.pyright.configurations.pyrightConfigurations
import com.insyncwithfoo.pyright.shared.SuppressQuickFix
import com.insyncwithfoo.pyright.shared.codeAsString
import com.insyncwithfoo.pyright.shared.getFormattedTooltip
import com.insyncwithfoo.pyright.shared.isUnsuppressable
import com.insyncwithfoo.pyright.shared.suffixedMessage
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.platform.lsp.api.customization.LspDiagnosticsSupport
import org.eclipse.lsp4j.Diagnostic
import org.eclipse.lsp4j.DiagnosticSeverity


@Suppress("UnstableApiUsage")
internal class DiagnosticsSupport(private val project: Project) : LspDiagnosticsSupport() {
    
    override fun getHighlightSeverity(diagnostic: Diagnostic): HighlightSeverity? {
        val inspection = project.pyrightInspection
        
        return when (diagnostic.severity) {
            DiagnosticSeverity.Error -> HighlightSeverity(inspection.highlightSeverityForErrors)
            DiagnosticSeverity.Warning -> HighlightSeverity(inspection.highlightSeverityForWarnings)
            DiagnosticSeverity.Information -> HighlightSeverity(inspection.highlightSeverityForInformation)
            else -> super.getHighlightSeverity(diagnostic)
        }
    }
    
    override fun getMessage(diagnostic: Diagnostic) =
        diagnostic.suffixedMessage
    
    override fun getTooltip(diagnostic: Diagnostic) =
        diagnostic.getFormattedTooltip(project.pyrightConfigurations)
    
    override fun createAnnotation(
        holder: AnnotationHolder,
        diagnostic: Diagnostic,
        textRange: TextRange,
        quickFixes: List<IntentionAction>
    ) {
        val suppressFix = when {
            diagnostic.isUnsuppressable -> null
            else -> SuppressQuickFix(diagnostic.codeAsString, textRange)
        }
        val newQuickFixes = quickFixes + listOfNotNull(suppressFix)
        
        super.createAnnotation(holder, diagnostic, textRange, newQuickFixes)
    }
    
}
