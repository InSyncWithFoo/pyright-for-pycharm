package com.insyncwithfoo.pyright.annotations

import com.insyncwithfoo.pyright.message
import com.intellij.codeInsight.intention.FileModifier.SafeFieldForPreview
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiFile
import com.intellij.psi.util.startOffset
import com.jetbrains.python.psi.PyUtil
import com.jetbrains.python.psi.impl.PyPsiUtils


internal typealias PyrightErrorCode = String


private fun Document.appendToLine(line: Int, value: String) {
    val lineEndOffset = getLineEndOffset(line)
    replaceString(lineEndOffset, lineEndOffset, value)
}


private fun PsiFile.findSameLineComment(rangeOffset: Int): PsiComment? {
    return findElementAt(rangeOffset)?.let { PyPsiUtils.findSameLineComment(it) }
}


private fun PsiFile.edit(callback: (Document) -> Unit) {
    PyUtil.updateDocumentUnblockedAndCommitted(this, callback)
}


internal class SuppressQuickFix(
    private val code: String?,
    @SafeFieldForPreview
    private val range: TextRange
) : LocalQuickFix {
    
    private fun Document.appendNewCommentToLine() {
        val lineNumber = getLineNumber(range.startOffset)
        val lastCharacter = charsSequence[getLineEndOffset(lineNumber) - 1]
        
        val padding = when {
            lastCharacter == ' ' -> ""
            else -> "  "
        }
        
        appendToLine(lineNumber, padding + PyrightIgnoreComment.parse(code ?: ""))
    }
    
    private fun Document.removeCommentCodeList(comment: ExistingPyrightIgnoreComment) {
        val (start, end) = comment.codeListRemovalOffsets
        replaceString(start, end, "")
    }
    
    private fun Document.addNewCodeToExistingComment(comment: ExistingPyrightIgnoreComment) {
        val (start, end) = comment.codeListReplacementOffsets
        replaceString(start, end, comment.replacement)
    }
    
    private val ExistingPyrightIgnoreComment.replacement: String
        get() {
            val newCodes = codes + code!!
            val newComment = PyrightIgnoreComment(newCodes)
            
            val padding = when {
                codeListIsNotSpecified -> " "
                else -> ""
            }
            
            return padding + newComment.codeList
        }
    
    override fun getName() = when {
        code != null -> message("quickFixes.suppress.name", code)
        else -> message("quickFixes.suppress.name.noCode")
    }
    
    override fun getFamilyName() = message("quickFixes.suppress.familyName")
    
    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val psiFile = descriptor.psiElement.containingFile
        val existingComment = psiFile.findSameLineComment(range.startOffset)
            ?.let { ExistingPyrightIgnoreComment.create(it.text, it.startOffset) }
        
        when {
            existingComment == null -> psiFile.edit { it.appendNewCommentToLine() }
            code == null -> psiFile.edit { it.removeCommentCodeList(existingComment) }
            else -> psiFile.edit { it.addNewCodeToExistingComment(existingComment) }
        }
    }
    
}
