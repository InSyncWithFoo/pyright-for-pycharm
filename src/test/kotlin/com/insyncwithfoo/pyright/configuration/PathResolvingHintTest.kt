package com.insyncwithfoo.pyright.configuration

import com.insyncwithfoo.pyright.message
import junit.framework.TestCase
import java.nio.file.Path
import kotlin.io.path.div


class PathResolvingHintTest : TestCase() {
    
    private val cwd = Path.of(".")
    private val testRoot = cwd / "src" / "test"
    
    fun `test executablePathResolvingHint - non-existent`() {
        val path = cwd / "non-existent"
        val hint = executablePathResolvingHint(path)
        
        assertTrue(hint.icon === HintIcon.Warning)
        assertTrue(hint.text == message("configurations.hint.fileNotFound"))
    }
    
    fun `test executablePathResolvingHint - file`() {
        val path = cwd / Path.of("gradlew")
        val hint = executablePathResolvingHint(path)
        
        assertTrue(hint.icon === HintIcon.Info)
        assertTrue(hint.text == message("configurations.hint.unknownExecutable"))
    }
    
    fun `test executablePathResolvingHint - symlink`() {
        val path = cwd / ".idea" / "icon.svg"
        val hint = executablePathResolvingHint(path)
        
        assertTrue(hint.icon === HintIcon.Info)
        assertTrue(hint.text == message("configurations.hint.unknownExecutable"))
    }
    
    fun `test executablePathResolvingHint - directory`() {
        val path = cwd / "gradle"
        val hint = executablePathResolvingHint(path)
        
        assertTrue(hint.icon === HintIcon.Error)
        assertTrue(hint.text == message("configurations.hint.unexpectedDirectory"))
    }
    
    fun `test executablePathResolvingHint - valid`() {
        val parent = testRoot / "testData" / "configuration" / "PathResolvingHintTest1"
        val fileNames = listOf(
            "pyright", "pyright.exe", "pyright.js",
            "pyright-python", "pyright-python.exe", "pyright-python.rs"
        )
        
        fileNames.forEach { fileName ->
            val path = parent / fileName
            val hint = executablePathResolvingHint(path)
            
            assertTrue(hint.icon === HintIcon.Success)
            assertTrue(hint.text == message("configurations.hint.fileFound"))
        }
    }
    
    fun `test configurationFilePathResolvingHint - non-existent`() {
        val path = cwd / "non-existent"
        val hint = configurationFilePathResolvingHint(path)
        
        assertTrue(hint.icon === HintIcon.Warning)
        assertTrue(hint.text == message("configurations.hint.fileNotFound"))
    }
    
    fun `test configurationFilePathResolvingHint - unrecognized file`() {
        val path = cwd / "README.md"
        val hint = configurationFilePathResolvingHint(path)
        
        assertTrue(hint.icon === HintIcon.Error)
        assertTrue(hint.text == message("configurations.hint.unrecognizedConfigurationFile"))
    }
    
    fun `test configurationFilePathResolvingHint - directory, no configuration file`() {
        val path = cwd
        val hint = configurationFilePathResolvingHint(path)
        
        assertTrue(hint.icon === HintIcon.Warning)
        assertTrue(hint.text == message("configurations.hint.noRecognizedFileFound"))
    }
    
    fun `test configurationFilePathResolvingHint - directory, with pyrightconfig`() {
        val path = testRoot / "testData" / "configuration" / "PathResolvingHintTest1"
        val hint = configurationFilePathResolvingHint(path)
        
        assertTrue(hint.icon === HintIcon.Success)
        assertTrue(hint.text == message("configurations.hint.recognizedFileFound"))
    }
    
    fun `test configurationFilePathResolvingHint - directory, with pyproject`() {
        val path = testRoot / "testData" / "configuration" / "PathResolvingHintTest2"
        val hint = configurationFilePathResolvingHint(path)
        
        assertTrue(hint.icon === HintIcon.Success)
        assertTrue(hint.text == message("configurations.hint.recognizedFileFound"))
    }
    
    fun `test langserverExecutablePathResolvingHint - non-existent`() {
        val path = cwd / "non-existent"
        val hint = langserverExecutablePathResolvingHint(path)
        
        assertTrue(hint.icon === HintIcon.Warning)
        assertTrue(hint.text == message("configurations.hint.fileNotFound"))
    }
    
    fun `test langserverExecutablePathResolvingHint - file`() {
        val path = cwd / Path.of("gradlew")
        val hint = langserverExecutablePathResolvingHint(path)
        
        assertTrue(hint.icon === HintIcon.Info)
        assertTrue(hint.text == message("configurations.hint.unknownExecutable"))
    }
    
    fun `test langserverExecutablePathResolvingHint - symlink`() {
        val path = cwd / ".idea" / "icon.svg"
        val hint = langserverExecutablePathResolvingHint(path)
        
        assertTrue(hint.icon === HintIcon.Info)
        assertTrue(hint.text == message("configurations.hint.unknownExecutable"))
    }
    
    fun `test langserverExecutablePathResolvingHint - directory`() {
        val path = cwd / "gradle"
        val hint = langserverExecutablePathResolvingHint(path)
        
        assertTrue(hint.icon === HintIcon.Error)
        assertTrue(hint.text == message("configurations.hint.unexpectedDirectory"))
    }
    
    fun `test langserverExecutablePathResolvingHint - valid`() {
        val parent = testRoot / "testData" / "configuration" / "PathResolvingHintTest2"
        val fileNames = listOf(
            "pyright-langserver", "pyright-langserver.exe", "pyright-langserver.js",
            "pyright-python-langserver", "pyright-python-langserver.exe", "pyright-python-langserver.rs"
        )
        
        fileNames.forEach { fileName ->
            val path = parent / fileName
            val hint = langserverExecutablePathResolvingHint(path)
            
            assertTrue(hint.icon === HintIcon.Success)
            assertTrue(hint.text == message("configurations.hint.fileFound"))
        }
    }
    
}
