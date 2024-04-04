package com.insyncwithfoo.pyright.configuration

import junit.framework.TestCase
import java.nio.file.Path
import kotlin.io.path.div


class PathResolvingHintTest : TestCase() {
    
    private val cwd = Path.of(".")
    private val testRoot = cwd / "src" / "test"
    
    fun `test executablePathResolvingHint - file`() {
        val path = cwd / Path.of("gradlew")
        val hint = executablePathResolvingHint(path)
        
        assertTrue(hint.icon === HintIcon.Success)
    }
    
    fun `test executablePathResolvingHint - symlink`() {
        val path = cwd / ".idea" / "icon.svg"
        val hint = executablePathResolvingHint(path)
        
        assertTrue(hint.icon === HintIcon.Success)
    }
    
    fun `test executablePathResolvingHint - directory`() {
        val path = cwd / "gradle"
        val hint = executablePathResolvingHint(path)
        
        assertTrue(hint.icon === HintIcon.Error)
    }
    
    fun `test configurationFilePathResolvingHint - non-existent`() {
        val path = cwd / "non-existent"
        val hint = configurationFilePathResolvingHint(path)
        
        assertTrue(hint.icon === HintIcon.Warning)
    }
    
    fun `test configurationFilePathResolvingHint - unrecognized file`() {
        val path = cwd / "README.md"
        val hint = configurationFilePathResolvingHint(path)
        
        assertTrue(hint.icon === HintIcon.Error)
    }
    
    fun `test configurationFilePathResolvingHint - directory, no configuration file`() {
        val path = cwd
        val hint = configurationFilePathResolvingHint(path)
        
        assertTrue(hint.icon === HintIcon.Warning)
    }
    
    fun `test configurationFilePathResolvingHint - directory, with pyrightconfig`() {
        val path = testRoot / "testData" / "configuration" / "PathResolvingHintTest1"
        val hint = configurationFilePathResolvingHint(path)
        
        assertTrue(hint.icon === HintIcon.Success)
    }
    
    fun `test configurationFilePathResolvingHint - directory, with pyproject`() {
        val path = testRoot / "testData" / "configuration" / "PathResolvingHintTest2"
        val hint = configurationFilePathResolvingHint(path)
        
        assertTrue(hint.icon === HintIcon.Success)
    }
    
}
