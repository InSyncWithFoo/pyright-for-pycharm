package com.insyncwithfoo.pyright.shared

import com.insyncwithfoo.pyright.commandline.Endpoint
import com.insyncwithfoo.pyright.commandline.Range
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import org.eclipse.lsp4j.Position


private fun Document.getOffset(endpoint: Endpoint) =
    getLineStartOffset(endpoint.line) + endpoint.character


private fun Document.getOffset(endpoint: Position) =
    getLineStartOffset(endpoint.line) + endpoint.character


internal fun Document.getOffsetRange(range: Range): TextRange {
    val start = getOffset(range.start)
    val end = getOffset(range.end)
    
    return TextRange(start, end)
}


internal fun Document.getOffsetRange(range: org.eclipse.lsp4j.Range): TextRange {
    val start = getOffset(range.start)
    val end = getOffset(range.end)
    
    return TextRange(start, end)
}
