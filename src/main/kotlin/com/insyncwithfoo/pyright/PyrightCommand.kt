package com.insyncwithfoo.pyright

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessHandler
import com.intellij.execution.process.ProcessOutput


private val GeneralCommandLine.handler: CapturingProcessHandler
    get() = CapturingProcessHandler(this)


internal abstract class PyrightCommand {
    
    protected abstract val workingDirectory: String
    protected abstract val fragments: List<String>
    
    private val commandLine: GeneralCommandLine
        get() = GeneralCommandLine(fragments).apply {
            withWorkDirectory(workingDirectory)
            withCharset(Charsets.UTF_8)
        }
    
    fun asCopyableString() = commandLine.commandLineString
    
    fun run(timeoutInMilliseconds: Int): ProcessOutput =
        commandLine.handler.runProcess(timeoutInMilliseconds)
    
}
