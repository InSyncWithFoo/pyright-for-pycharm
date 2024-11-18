package com.insyncwithfoo.pyright.commandline

import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange


internal fun Document.getOffset(endpoint: Endpoint) =
    getLineStartOffset(endpoint.line) + endpoint.character


internal fun Document.getOffsetRange(range: Range): TextRange {
    val start = getOffset(range.start)
    val end = getOffset(range.end)
    
    return TextRange(start, end)
}
