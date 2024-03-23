package com.insyncwithfoo.pyright

import com.insyncwithfoo.pyright.configuration.PyrightAllConfigurations
import com.intellij.lang.annotation.AnnotationBuilder
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.ex.util.EditorUtil
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.util.text.HtmlChunk


private fun <T> T.applyIf(condition: Boolean, block: T.() -> T): T =
    if (condition) block() else this


private fun Document.getOffset(endpoint: PyrightDiagnosticTextRangeEndpoint) =
    getLineStartOffset(endpoint.line) + endpoint.character


private fun Document.getStartEndRange(range: PyrightDiagnosticTextRange): TextRange {
    val start = getOffset(range.start)
    val end = getOffset(range.end)
    
    return TextRange(start, end)
}


private fun PyrightDiagnosticSeverity.toHighlightSeverity() = when (this) {
    PyrightDiagnosticSeverity.ERROR -> HighlightSeverity.WARNING
    PyrightDiagnosticSeverity.WARNING -> HighlightSeverity.WEAK_WARNING
    PyrightDiagnosticSeverity.INFORMATION -> HighlightSeverity.WEAK_WARNING
}


private fun String.toPreformatted(font: String? = null) =
    HtmlChunk.div()
        .applyIf(font != null) { style("font-family: '$font'") }
        .child(HtmlChunk.text(this)).toString()


private val PyrightDiagnostic.suffixedMessage: String
    get() {
        val suffix = if (rule != null) " ($rule)" else ""
        return "$message$suffix"
    }


internal class PyrightAnnotationApplier(
    private val document: Document,
    private val output: PyrightOutput,
    private val configurations: PyrightAllConfigurations,
    private val holder: AnnotationHolder
) {
    
    fun apply() {
        output.generalDiagnostics.forEach {
            val builder = makeBuilder(it)
            val range = document.getStartEndRange(it.range)
            
            builder.needsUpdateOnTyping().range(range).create()
        }
    }
    
    private fun makeBuilder(diagnostic: PyrightDiagnostic): AnnotationBuilder {
        val (_, severity, message) = diagnostic
        var tooltipMessage = diagnostic.suffixedMessage
        
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
        val highlightSeverity = severity.toHighlightSeverity()
        
        return holder.newAnnotation(highlightSeverity, message).tooltip(tooltip)
    }
    
}
