package com.insyncwithfoo.pyright.shared

import com.google.gson.JsonObject
import org.eclipse.lsp4j.CompletionItem
import org.eclipse.lsp4j.CompletionItemKind
import org.eclipse.lsp4j.InsertTextFormat
import org.eclipse.lsp4j.TextEdit


private const val CARET_POSITION = $$"$0"


internal val CompletionItem.isCallable: Boolean
    get() = kind in listOf(
        CompletionItemKind.Method,
        CompletionItemKind.Function,
        CompletionItemKind.Constructor
    )


internal val CompletionItem.isAutoImportCompletion: Boolean
    get() {
        val data = this.data
        return data is JsonObject && data.has("autoImportText")
    }


internal fun CompletionItem.completeWithParentheses() {
    val newInsertText = "$label($CARET_POSITION)"
    
    insertTextFormat = InsertTextFormat.Snippet
    
    when (val textEdit = this.textEdit?.get()) {
        null -> insertText = newInsertText
        is TextEdit -> textEdit.newText = newInsertText
        // InsertReplaceEdit must not be messed with.
    }
}
