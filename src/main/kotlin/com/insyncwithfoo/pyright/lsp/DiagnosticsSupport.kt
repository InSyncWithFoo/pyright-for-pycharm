package com.insyncwithfoo.pyright.lsp

import com.insyncwithfoo.pyright.configurations.pyrightConfigurations
import com.insyncwithfoo.pyright.shared.SuppressQuickFix
import com.insyncwithfoo.pyright.shared.codeAsString
import com.insyncwithfoo.pyright.shared.getFormattedTooltip
import com.insyncwithfoo.pyright.shared.isUnsuppressable
import com.insyncwithfoo.pyright.shared.suffixedMessage
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.platform.lsp.api.customization.LspDiagnosticsSupport
import org.eclipse.lsp4j.Diagnostic


internal class DiagnosticsSupport(private val project: Project) : LspDiagnosticsSupport() {
    
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
