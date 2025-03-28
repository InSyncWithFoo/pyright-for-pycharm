package com.insyncwithfoo.pyright.configurations

import com.insyncwithfoo.pyright.commandline.DiagnosticSeverity
import org.junit.Assert.assertEquals
import org.junit.Test


internal class PyrightConfigurationsTest : ConfigurationsTest<PyrightConfigurations>() {
    
    override val configurationClass = PyrightConfigurations::class
    
    @Test
    fun `test shape`() {
        assertEquals(27, fields.size)
        
        state.apply {
            assertEquals(null, executable)
            assertEquals(false, smartExecutableResolution)
            assertEquals(null, languageServerExecutable)
            assertEquals(false, smartLanguageServerExecutableResolution)
            assertEquals(null, configurationFile)
            assertEquals(RunningMode.COMMAND_LINE, runningMode)
            
            assertEquals(true, autoRestartServers)
            
            assertEquals(true, diagnostics)
            assertEquals(false, useEditorFontForTooltips)
            assertEquals(false, prefixTooltipMessages)
            assertEquals(true, linkErrorCodesInTooltips)
            assertEquals(true, taggedHints)
            assertEquals(DiagnosticSeverity.INFORMATION, minimumSeverityLevel)
            
            assertEquals(true, hover)
            
            assertEquals(true, completion)
            assertEquals(true, autoImportCompletions)
            assertEquals(true, monkeypatchAutoImportDetails)
            assertEquals(true, autocompleteParentheses)
            assertEquals(true, monkeypatchTrailingQuoteBug)
            
            assertEquals(true, autoSearchPaths)
            assertEquals("py|pyi|pyw", targetedFileExtensions)
            assertEquals(WorkspaceFolders.PROJECT_BASE, workspaceFolders)
            assertEquals(DiagnosticMode.OPEN_FILES_ONLY, diagnosticMode)
            
            assertEquals(LogLevel.INFORMATION, logLevel)
            assertEquals(Locale.DEFAULT, locale)
            assertEquals(0, numberOfThreads)
            
            assertEquals(false, useSchemaFromStore)
        }
    }
    
    @Test
    fun `test messages`() {
        doMessagesTest { name -> "configurations.$name.label" }
    }
    
}
