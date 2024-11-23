package com.insyncwithfoo.pyright.shared

import com.intellij.openapi.editor.ex.util.EditorUtil
import com.intellij.openapi.util.text.HtmlChunk


private typealias Message = String


private fun <T> T.letIf(condition: Boolean, block: (T) -> T): T =
    if (condition) block(this) else this


private fun <T> T.runIf(condition: Boolean, block: T.() -> T): T =
    if (condition) block() else this


private fun HtmlChunk.Element.withFont(font: String?) =
    this.runIf(font != null) { style("font-family: '$font'") }


private fun String.toPreformattedBlock(font: String?) =
    HtmlChunk.div().withFont(font).child(HtmlChunk.text(this))


private val PyrightErrorCode.documentationLink: String
    get() = "https://github.com/microsoft/pyright/blob/main/docs/configuration.md#$this"


private fun PyrightErrorCode.toFormatted(font: String?, linkErrorCode: Boolean) =
    HtmlChunk.text(this)
        .letIf(linkErrorCode) { HtmlChunk.link(documentationLink, it).withFont(font) }
        .toString()


internal fun Message.toFormattedTooltip(
    code: PyrightErrorCode?,
    useEditorFont: Boolean,
    addPrefix: Boolean,
    linkErrorCode: Boolean
): String {
    val font = EditorUtil.getEditorFont().takeIf { useEditorFont }
    
    val prefix = when (addPrefix) {
        true -> "Pyright: "
        else -> ""
    }
    
    val suffix = when (code) {
        null -> ""
        else -> " (${code.toFormatted(font?.name, linkErrorCode)})"
    }
    
    return "${prefix}${this}".toPreformattedBlock(font?.name).addRaw(suffix).toString()
}
