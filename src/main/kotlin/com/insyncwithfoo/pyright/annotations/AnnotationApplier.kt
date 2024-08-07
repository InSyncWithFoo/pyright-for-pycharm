package com.insyncwithfoo.pyright.annotations

import com.insyncwithfoo.pyright.HighlightSeverity
import com.insyncwithfoo.pyright.cli.PyrightDiagnostic
import com.insyncwithfoo.pyright.cli.PyrightDiagnosticSeverity
import com.insyncwithfoo.pyright.cli.PyrightDiagnosticTextRange
import com.insyncwithfoo.pyright.cli.PyrightDiagnosticTextRangeEndpoint
import com.insyncwithfoo.pyright.PyrightInspection
import com.insyncwithfoo.pyright.cli.PyrightOutput
import com.insyncwithfoo.pyright.configuration.AllConfigurations
import com.intellij.lang.annotation.AnnotationBuilder
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.ex.util.EditorUtil
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.util.text.HtmlChunk


private typealias ApplierCallback = (AnnotationBuilder, PyrightDiagnostic, TextRange) -> Unit


private fun <T> T.runIf(condition: Boolean, block: T.() -> T): T =
    if (condition) block() else this


private fun Document.getOffset(endpoint: PyrightDiagnosticTextRangeEndpoint) =
    getLineStartOffset(endpoint.line) + endpoint.character


private fun Document.getStartEndRange(range: PyrightDiagnosticTextRange): TextRange {
    val start = getOffset(range.start)
    val end = getOffset(range.end)
    
    return TextRange(start, end)
}


private fun String.toPreformatted(font: String? = null) =
    HtmlChunk.div()
        .runIf(font != null) { style("font-family: '$font'") }
        .child(HtmlChunk.text(this)).toString()


private val PyrightDiagnostic.suffixedMessage: String
    get() {
        val suffix = if (rule != null) " ($rule)" else ""
        return "$message$suffix"
    }


internal fun PyrightDiagnosticSeverity.toHighlightSeverity(inspection: PyrightInspection) = when (this) {
    PyrightDiagnosticSeverity.ERROR -> HighlightSeverity(inspection.highlightSeverityForErrors)
    PyrightDiagnosticSeverity.WARNING -> HighlightSeverity(inspection.highlightSeverityForWarnings)
    PyrightDiagnosticSeverity.INFORMATION -> HighlightSeverity(inspection.highlightSeverityForInformation)
}


internal class AnnotationApplier(
    private val configurations: AllConfigurations,
    private val inspection: PyrightInspection,
    private val holder: AnnotationHolder
) {
    
    private fun PyrightDiagnostic.makeBuilder(): AnnotationBuilder {
        var tooltipMessage = suffixedMessage
        
        val addTooltipPrefix = configurations.addTooltipPrefix
        if (addTooltipPrefix) {
            tooltipMessage = "Pyright: $tooltipMessage"
        }
        
        val useEditorFont = configurations.useEditorFont
        val font = when {
            useEditorFont -> EditorUtil.getEditorFont()
            else -> null
        }
        
        val tooltip = tooltipMessage.toPreformatted(font?.name)
        val highlightSeverity = severity.toHighlightSeverity(inspection)
        
        return holder.newAnnotation(highlightSeverity, message).tooltip(tooltip)
    }
    
    fun apply(document: Document, output: PyrightOutput, callback: ApplierCallback) {
        output.generalDiagnostics.forEach { diagnostic ->
            val builder = diagnostic.makeBuilder()
            val range = document.getStartEndRange(diagnostic.range)
            
            builder.needsUpdateOnTyping().range(range)
                .also { callback(builder, diagnostic, range) }
                .create()
        }
    }
    
}
