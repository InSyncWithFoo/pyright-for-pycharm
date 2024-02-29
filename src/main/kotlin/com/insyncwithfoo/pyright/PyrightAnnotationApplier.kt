package com.insyncwithfoo.pyright

import com.insyncwithfoo.pyright.configuration.PyrightAllConfigurations
import com.intellij.lang.annotation.AnnotationBuilder
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.xml.util.XmlStringUtil


private fun Document.getOffset(endpoint: PyrightDiagnosticTextRangeEndpoint) =
    getLineStartOffset(endpoint.line) + endpoint.character


private fun Document.getStartEndRange(range: PyrightDiagnosticTextRange): TextRange {
    val start = getOffset(range.start)
    val end = getOffset(range.end)
    
    return TextRange(start, end)
}


private fun AnnotationHolder.makeBuilderForDiagnostic(diagnostic: PyrightDiagnostic): AnnotationBuilder {
    val (_, severity, message, rule) = diagnostic
    
    val tooltip = message.toPreformattedTooltip()
    val highlightSeverity = severity.toHighlightSeverity()
    
    val suffix = if (rule != null) " (${rule})" else ""
    val suffixedmessage = "$message$suffix"
    
    return newAnnotation(highlightSeverity, suffixedmessage).tooltip(tooltip)
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


private fun AnnotationHolder.applyDiagnostic(diagnostic: PyrightDiagnostic, document: Document) {
    val builder = makeBuilderForDiagnostic(diagnostic)
    val range = document.getStartEndRange(diagnostic.range)
    
    builder.annotateRange(range)
}


private fun AnnotationBuilder.annotateRange(range: TextRange) {
    this.needsUpdateOnTyping().range(range).create()
}


internal class PyrightAnnotationApplier(
    private val document: Document,
    private val output: PyrightOutput,
    private val configurations: PyrightAllConfigurations,
    private val holder: AnnotationHolder
) {
    fun apply() {
        output.generalDiagnostics.forEach {
            holder.applyDiagnostic(it, document)
        }
    }
}
