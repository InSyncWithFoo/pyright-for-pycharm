package com.insyncwithfoo.pyright.shared

import org.jetbrains.annotations.VisibleForTesting


// From https://github.com/microsoft/pyright/blob/a4d165e/packages/pyright-internal/src/parser/tokenizer.ts#L1302
private val pyrightIgnoreSyntax = """(?x)
    (?<prefix>\#\s*pyright:\s*ignore)
    (?:
        (?<padding>\s*)
        (?<codeList>\[[\s\w-,]*])
    )?
""".toRegex()


private fun MatchGroup.toFragment(baseOffset: Int): PyrightIgnoreCommentFragment {
    val selfRelativeOffset = range.first
    return PyrightIgnoreCommentFragment(value, baseOffset + selfRelativeOffset)
}


private fun String.stripBrackets() = this.substring(1, length - 1)


internal class PyrightIgnoreCommentFragment(val content: String, baseOffset: Int) {
    val start = baseOffset
    val end = baseOffset + content.length
}


internal class PyrightIgnoreComment(val codes: Set<PyrightErrorCode>) {
    
    val codeList: String
        get() = "[${codes.joinToString(", ")}]"
    
    override fun toString(): String {
        val affix = when {
            codes.isEmpty() -> ""
            else -> " $codeList"
        }
        return "# pyright: ignore${affix}"
    }
    
    companion object {
        
        fun parse(codeList: String): PyrightIgnoreComment {
            val codes = codeList.split(",")
                .mapNotNullTo(mutableSetOf()) { code ->
                    code.trim().takeIf { it.isNotEmpty() }
                }
            
            return PyrightIgnoreComment(codes)
        }
        
        fun parse(fragment: PyrightIgnoreCommentFragment): PyrightIgnoreComment {
            return parse(fragment.content.stripBrackets())
        }
        
    }
    
}


internal class ExistingPyrightIgnoreComment(
    private val prefix: PyrightIgnoreCommentFragment,
    private val padding: PyrightIgnoreCommentFragment?,
    private val codeList: PyrightIgnoreCommentFragment?
) {
    
    val codes by lazy { codeList?.let { PyrightIgnoreComment.parse(it).codes } ?: emptySet() }
    
    val codeListIsNotSpecified: Boolean
        get() = codeList == null
    
    val codeListRemovalOffsets: Pair<Int, Int>
        get() = when {
            codeList != null -> padding!!.start to codeList.end
            else -> prefix.end to prefix.end
        }
    
    val codeListReplacementOffsets: Pair<Int, Int>
        get() = when {
            codeList != null -> codeList.start to codeList.end
            else -> prefix.end to prefix.end
        }
    
    companion object {
        @VisibleForTesting
        fun create(text: String, baseOffset: Int): ExistingPyrightIgnoreComment? {
            val pyrightIgnorePart = pyrightIgnoreSyntax.find(text) ?: return null
            val groups = pyrightIgnorePart.groups
            
            val prefix = groups["prefix"]!!.toFragment(baseOffset)
            val padding = groups["padding"]?.toFragment(baseOffset)
            val codeList = groups["codeList"]?.toFragment(baseOffset)
            
            return ExistingPyrightIgnoreComment(prefix, padding, codeList)
        }
    }
    
}
