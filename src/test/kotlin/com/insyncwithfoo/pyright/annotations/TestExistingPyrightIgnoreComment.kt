package com.insyncwithfoo.pyright.annotations

import junit.framework.TestCase


private fun String.firstPyrightIgnoreEndIndex(): Int {
    return """#\s*pyright:\s*ignore""".toRegex().find(this)!!.range.last + 1
}


private fun String.firstPyrightIgnoreEndIndexPair(): Pair<Int, Int> {
    val endIndex = firstPyrightIgnoreEndIndex()
    return endIndex to endIndex
}


private fun String.firstSquareBracketPairIndices(): Pair<Int, Int> {
    val opening = indexOf('[')
    val closing = indexOf(']', opening)
    
    return opening to closing + 1
}


private fun String.firstPyrightIgnoreEndToNextClosingBracketIndices(): Pair<Int, Int> {
    val pyrightIgnoreEnd = firstPyrightIgnoreEndIndex()
    val closingBracket = indexOf(']', pyrightIgnoreEnd)
    
    return pyrightIgnoreEnd to closingBracket + 1
}


class TestExistingPyrightIgnoreComment : TestCase() {
    
    fun `test create - no codes`() {
        val input = "# pyright: ignore"
        val parsed = ExistingPyrightIgnoreComment.create(input, 0)!!
        
        assertEquals(emptySet<String>(), parsed.codes)
        assertEquals(true, parsed.codeListIsNotSpecified)
        assertEquals(input.firstPyrightIgnoreEndIndexPair(), parsed.codeListReplacementOffsets)
        assertEquals(input.firstPyrightIgnoreEndIndexPair(), parsed.codeListRemovalOffsets)
    }
    
    fun `test create - empty list`() {
        val input = "# pyright: ignore []"
        val parsed = ExistingPyrightIgnoreComment.create(input, 0)!!
        
        assertEquals(emptySet<String>(), parsed.codes)
        assertEquals(false, parsed.codeListIsNotSpecified)
        assertEquals(input.firstSquareBracketPairIndices(), parsed.codeListReplacementOffsets)
        assertEquals(input.firstPyrightIgnoreEndToNextClosingBracketIndices(), parsed.codeListRemovalOffsets)
    }
    
    fun `test create - one code`() {
        val input = "# pyright: ignore [A]"
        val parsed = ExistingPyrightIgnoreComment.create(input, 0)!!
        
        assertEquals(setOf("A"), parsed.codes)
        assertEquals(false, parsed.codeListIsNotSpecified)
        assertEquals(input.firstSquareBracketPairIndices(), parsed.codeListReplacementOffsets)
        assertEquals(input.firstPyrightIgnoreEndToNextClosingBracketIndices(), parsed.codeListRemovalOffsets)
    }
    
    fun `test create - multiple codes`() {
        val input = "# pyright: ignore [A, B]"
        val parsed = ExistingPyrightIgnoreComment.create(input, 0)!!
        
        assertEquals(setOf("A", "B"), parsed.codes)
        assertEquals(false, parsed.codeListIsNotSpecified)
        assertEquals(input.firstSquareBracketPairIndices(), parsed.codeListReplacementOffsets)
        assertEquals(input.firstPyrightIgnoreEndToNextClosingBracketIndices(), parsed.codeListRemovalOffsets)
    }
    
    fun `test create - multiple duplicate codes`() {
        val input = "# pyright: ignore [A, A, B, , ,D, , C, D, ]"
        val parsed = ExistingPyrightIgnoreComment.create(input, 0)!!
        
        assertEquals(setOf("A", "B", "D", "C"), parsed.codes)
        assertEquals(false, parsed.codeListIsNotSpecified)
        assertEquals(input.firstSquareBracketPairIndices(), parsed.codeListReplacementOffsets)
        assertEquals(input.firstPyrightIgnoreEndToNextClosingBracketIndices(), parsed.codeListRemovalOffsets)
    }
    
    fun `test create - whitespace`() {
        val input = "#     pyright:\t\tignore[A,   ,\t,\t\t,,B,\t,  , D, C ,, E E,,,F]"
        val parsed = ExistingPyrightIgnoreComment.create(input, 0)!!
        
        assertEquals(setOf("A", "B", "D", "C", "E E", "F"), parsed.codes)
        assertEquals(false, parsed.codeListIsNotSpecified)
        assertEquals(input.firstSquareBracketPairIndices(), parsed.codeListReplacementOffsets)
        assertEquals(input.firstPyrightIgnoreEndToNextClosingBracketIndices(), parsed.codeListRemovalOffsets)
    }
    
    fun `test create - multiple hashes - no codes`() {
        val input = "# foo # pyright: ignore"
        val parsed = ExistingPyrightIgnoreComment.create(input, 0)!!
        
        assertEquals(emptySet<String>(), parsed.codes)
        assertEquals(true, parsed.codeListIsNotSpecified)
        assertEquals(input.firstPyrightIgnoreEndIndexPair(), parsed.codeListReplacementOffsets)
        assertEquals(input.firstPyrightIgnoreEndIndexPair(), parsed.codeListRemovalOffsets)
    }
    
    fun `test create - multiple hashes - empty list`() {
        val input = "# foo # pyright: ignore []"
        val parsed = ExistingPyrightIgnoreComment.create(input, 0)!!
        
        assertEquals(emptySet<String>(), parsed.codes)
        assertEquals(false, parsed.codeListIsNotSpecified)
        assertEquals(input.firstSquareBracketPairIndices(), parsed.codeListReplacementOffsets)
        assertEquals(input.firstPyrightIgnoreEndToNextClosingBracketIndices(), parsed.codeListRemovalOffsets)
    }
    
    fun `test create - multiple hashes - one code`() {
        val input = "# foo # pyright: ignore [A]"
        val parsed = ExistingPyrightIgnoreComment.create(input, 0)!!
        
        assertEquals(setOf("A"), parsed.codes)
        assertEquals(false, parsed.codeListIsNotSpecified)
        assertEquals(input.firstSquareBracketPairIndices(), parsed.codeListReplacementOffsets)
        assertEquals(input.firstPyrightIgnoreEndToNextClosingBracketIndices(), parsed.codeListRemovalOffsets)
    }
    
    fun `test create - multiple hashes - multiple codes`() {
        val input = "# foo # pyright: ignore [A, B]"
        val parsed = ExistingPyrightIgnoreComment.create(input, 0)!!
        
        assertEquals(setOf("A", "B"), parsed.codes)
        assertEquals(false, parsed.codeListIsNotSpecified)
        assertEquals(input.firstSquareBracketPairIndices(), parsed.codeListReplacementOffsets)
        assertEquals(input.firstPyrightIgnoreEndToNextClosingBracketIndices(), parsed.codeListRemovalOffsets)
    }
    
    fun `test create - multiple hashes - multiple duplicate codes`() {
        val input = "# foo # pyright: ignore [A, A, B, , ,D, , C, D, ]"
        val parsed = ExistingPyrightIgnoreComment.create(input, 0)!!
        
        assertEquals(setOf("A", "B", "D", "C"), parsed.codes)
        assertEquals(false, parsed.codeListIsNotSpecified)
        assertEquals(input.firstSquareBracketPairIndices(), parsed.codeListReplacementOffsets)
        assertEquals(input.firstPyrightIgnoreEndToNextClosingBracketIndices(), parsed.codeListRemovalOffsets)
    }
    
    fun `test create - multiple hashes - multiple valid parts - no codes`() {
        val input = "# foo # pyright: ignore  # pyright: ignore []"
        val parsed = ExistingPyrightIgnoreComment.create(input, 0)!!
        
        assertEquals(emptySet<String>(), parsed.codes)
        assertEquals(true, parsed.codeListIsNotSpecified)
        assertEquals(input.firstPyrightIgnoreEndIndexPair(), parsed.codeListReplacementOffsets)
        assertEquals(input.firstPyrightIgnoreEndIndexPair(), parsed.codeListRemovalOffsets)
    }
    
    fun `test create - multiple hashes - multiple valid parts - one code`() {
        val input = "# foo # pyright: ignore [A]  # pyright: ignore []"
        val parsed = ExistingPyrightIgnoreComment.create(input, 0)!!
        
        assertEquals(setOf("A"), parsed.codes)
        assertEquals(false, parsed.codeListIsNotSpecified)
        assertEquals(input.firstSquareBracketPairIndices(), parsed.codeListReplacementOffsets)
        assertEquals(input.firstPyrightIgnoreEndToNextClosingBracketIndices(), parsed.codeListRemovalOffsets)
    }
    
    fun `test create - multiple hashes - multiple valid parts - multiple codes`() {
        val input = "# foo # pyright: ignore[A, B]  # pyright: ignore [C, D]"
        val parsed = ExistingPyrightIgnoreComment.create(input, 0)!!
        
        assertEquals(setOf("A", "B"), parsed.codes)
        assertEquals(false, parsed.codeListIsNotSpecified)
        assertEquals(input.firstSquareBracketPairIndices(), parsed.codeListReplacementOffsets)
        assertEquals(input.firstPyrightIgnoreEndToNextClosingBracketIndices(), parsed.codeListRemovalOffsets)
    }
    
    fun `test create - multiple hashes - multiple valid parts - multiple duplicate codes`() {
        val input = "# foo # pyright: ignore [A, A, B, , ,D, , C, D, ]  # pyright: ignore [A]"
        val parsed = ExistingPyrightIgnoreComment.create(input, 0)!!
        
        assertEquals(setOf("A", "B", "D", "C"), parsed.codes)
        assertEquals(false, parsed.codeListIsNotSpecified)
        assertEquals(input.firstSquareBracketPairIndices(), parsed.codeListReplacementOffsets)
        assertEquals(input.firstPyrightIgnoreEndToNextClosingBracketIndices(), parsed.codeListRemovalOffsets)
    }
    
    fun `test create - invalid - type ignore`() {
        val input = "# type: ignore [A, B]"
        val parsed = ExistingPyrightIgnoreComment.create(input, 0)
        
        assertEquals(null, parsed)
    }
    
    fun `test create - invalid - marker not immediately preceded by hash`() {
        val input = "# foo pyright: ignore [A, B]"
        val parsed = ExistingPyrightIgnoreComment.create(input, 0)
        
        assertEquals(null, parsed)
    }
    
    fun `test create - invalid - whitespace before colon`() {
        val input = "# pyright : ignore [A, B]"
        val parsed = ExistingPyrightIgnoreComment.create(input, 0)
        
        assertEquals(null, parsed)
    }
    
}
