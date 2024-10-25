package com.insyncwithfoo.pyright.lsp

import com.insyncwithfoo.pyright.commandline.pyrightInspectionisEnabled
import com.insyncwithfoo.pyright.configurations.pyrightConfigurations
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.ex.util.EditorUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.platform.lsp.api.customization.LspDiagnosticsSupport
import org.eclipse.lsp4j.Diagnostic
import org.eclipse.lsp4j.DiagnosticSeverity


@Suppress("UnstableApiUsage")
internal class DiagnosticsSupport(private val project: Project) : LspDiagnosticsSupport() {
    
    override fun getHighlightSeverity(diagnostic: Diagnostic): HighlightSeverity? {
        val inspection = project.pyrightInspectionisEnabled
        
        return when (diagnostic.severity) {
            DiagnosticSeverity.Error -> HighlightSeverity(inspection.highlightSeverityForErrors)
            DiagnosticSeverity.Warning -> HighlightSeverity(inspection.highlightSeverityForWarnings)
            DiagnosticSeverity.Information -> HighlightSeverity(inspection.highlightSeverityForInformation)
            DiagnosticSeverity.Hint -> super.getHighlightSeverity(diagnostic)
            else -> throw RuntimeException("This should not happen")
        }
    }
    
    override fun getMessage(diagnostic: Diagnostic) = diagnostic.suffixedMessage
    
    override fun getTooltip(diagnostic: Diagnostic): String {
        val configurations = project.pyrightConfigurations
        
        val descriptionHref = diagnostic.codeDescription?.href
            ?.takeIf { configurations.linkErrorCodesInTooltips }
        val font = EditorUtil.getEditorFont().name
            ?.takeIf { configurations.useEditorFontForTooltips }
        val tooltip = diagnostic.message
            .letIf(configurations.prefixTooltipMessages) { "Pyright: $it" }
        
        val codeSuffix = diagnostic.codeAsString?.toCodeSuffix(font, descriptionHref).orEmpty()
        
        return tooltip.toPreformattedBlock(font).addRaw(codeSuffix).toString()
    }
    
    override fun createAnnotation(
        holder: AnnotationHolder,
        diagnostic: Diagnostic,
        textRange: TextRange,
        quickFixes: List<IntentionAction>
    ) {
        val fix = when {
            diagnostic.isUnsuppressable -> null
            else -> SuppressQuickFix(diagnostic.codeAsString, textRange)
        }
        val newQuickFixes = quickFixes + listOfNotNull(fix)
        
        super.createAnnotation(holder, diagnostic, textRange, newQuickFixes)
    }
    
}
