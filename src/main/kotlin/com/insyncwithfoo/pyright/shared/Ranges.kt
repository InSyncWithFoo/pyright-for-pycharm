package com.insyncwithfoo.pyright.shared

import com.insyncwithfoo.pyright.commandline.Endpoint
import com.insyncwithfoo.pyright.commandline.Range
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import org.eclipse.lsp4j.Position


// From https://github.com/redhat-developer/lsp4ij/blob/c352be8146/src/main/java/com/redhat/devtools/lsp4ij/LSPIJUtils.java#L669-L682
private fun Document.getOffset(line: Int, character: Int): Int {
    if (line >= lineCount) {
        return textLength
    }
    
    if (line < 0) {
        return 0
    }
    
    val lineOffset = getLineStartOffset(line)
    val nextLineOffset = getLineEndOffset(line)
    
    val offset = lineOffset + character
    
    return offset.coerceIn(lineOffset, nextLineOffset)
}


private fun Document.getOffset(endpoint: Endpoint) =
    getOffset(endpoint.line, endpoint.character)


private fun Document.getOffset(endpoint: Position) =
    getOffset(endpoint.line, endpoint.character)


// From https://github.com/redhat-developer/lsp4ij/blob/c352be8146/src/main/java/com/redhat/devtools/lsp4ij/LSPIJUtils.java#L801-L824
private fun Document.getOffsetRange(start: Int, end: Int): TextRange? {
    try {
        if (end !in start..textLength) {
            return null
        }
        
        if (start == end) {
            return null
        }
        
        return TextRange(start, end)
    } catch (_: IndexOutOfBoundsException) {
        return null
    }
}


internal fun Document.getOffsetRange(range: Range): TextRange? {
    val start = getOffset(range.start)
    val end = getOffset(range.end)
    
    return getOffsetRange(start, end)
}


internal fun Document.getOffsetRange(range: org.eclipse.lsp4j.Range): TextRange? {
    val start = getOffset(range.start)
    val end = getOffset(range.end)
    
    return getOffsetRange(start, end)
}
