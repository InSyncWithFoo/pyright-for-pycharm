package com.insyncwithfoo.pyright.lsp4ij

import com.insyncwithfoo.pyright.commandline.highlightSeverityFor
import com.insyncwithfoo.pyright.commandline.pyrightInspection
import com.insyncwithfoo.pyright.configurations.pyrightConfigurations
import com.insyncwithfoo.pyright.shared.SuppressQuickFix
import com.insyncwithfoo.pyright.shared.codeAsString
import com.insyncwithfoo.pyright.shared.getFormattedTooltip
import com.insyncwithfoo.pyright.shared.getOffsetRange
import com.insyncwithfoo.pyright.shared.isUnsuppressable
import com.insyncwithfoo.pyright.shared.suffixedMessage
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiFile
import com.redhat.devtools.lsp4ij.client.features.LSPDiagnosticFeature
import org.eclipse.lsp4j.Diagnostic


@Suppress("UnstableApiUsage")
internal class DiagnosticFeature : LSPDiagnosticFeature() {
    
    private val configurations by lazy {
        project.pyrightConfigurations
    }
    
    override fun isEnabled(file: PsiFile) =
        configurations.diagnostics
    
    override fun getMessage(diagnostic: Diagnostic) =
        diagnostic.suffixedMessage
    
    override fun getTooltip(diagnostic: Diagnostic) =
        diagnostic.getFormattedTooltip(configurations)
    
    override fun getHighlightSeverity(diagnostic: Diagnostic) =
        project.pyrightInspection.highlightSeverityFor(diagnostic.severity)
            ?: super.getHighlightSeverity(diagnostic)
    
    override fun createAnnotation(
        diagnostic: Diagnostic,
        document: Document,
        fixes: MutableList<IntentionAction>,
        holder: AnnotationHolder
    ) {
        val newFixes = when (val range = document.getOffsetRange(diagnostic.range)) {
            null -> fixes
            
            else -> {
                val suppressFix = when {
                    diagnostic.isUnsuppressable -> null
                    else -> SuppressQuickFix(diagnostic.codeAsString, range)
                }
                
                fixes + listOfNotNull(suppressFix)
            }
        }
        
        super.createAnnotation(diagnostic, document, newFixes, holder)
    }
    
}
