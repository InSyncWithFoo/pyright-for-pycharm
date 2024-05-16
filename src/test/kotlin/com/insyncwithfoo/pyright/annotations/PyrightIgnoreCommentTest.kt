package com.insyncwithfoo.pyright.annotations

import junit.framework.TestCase


class PyrightIgnoreCommentTest : TestCase() {
    
    fun `test parse - empty`() {
        val input = ""
        val comment = PyrightIgnoreComment.parse(input)
        
        assertEquals(emptySet<PyrightErrorCode>(), comment.codes)
        assertEquals("[]", comment.codeList)
        assertEquals("# pyright: ignore", comment.toString())
    }
    
    fun `test parse - one code`() {
        val input = "A"
        val comment = PyrightIgnoreComment.parse(input)
        
        assertEquals(setOf("A"), comment.codes)
        assertEquals("[A]", comment.codeList)
        assertEquals("# pyright: ignore [A]", comment.toString())
    }
    
    fun `test parse - multiple codes`() {
        val input = "A, B, C"
        val comment = PyrightIgnoreComment.parse(input)
        
        assertEquals(setOf("A", "B", "C"), comment.codes)
        assertEquals("[A, B, C]", comment.codeList)
        assertEquals("# pyright: ignore [A, B, C]", comment.toString())
    }
    
    fun `test parse - multiple duplicate codes`() {
        val input = "A, A, B, , ,D, , C, D, "
        val comment = PyrightIgnoreComment.parse(input)
        
        assertEquals(setOf("A", "B", "D", "C"), comment.codes)
        assertEquals("[A, B, D, C]", comment.codeList)
        assertEquals("# pyright: ignore [A, B, D, C]", comment.toString())
    }
    
    fun `test parse - whitespace`() {
        val input = "   A ,B, C   "
        val comment = PyrightIgnoreComment.parse(input)
        
        assertEquals(setOf("A", "B", "C"), comment.codes)
        assertEquals("[A, B, C]", comment.codeList)
        assertEquals("# pyright: ignore [A, B, C]", comment.toString())
    }
    
}
