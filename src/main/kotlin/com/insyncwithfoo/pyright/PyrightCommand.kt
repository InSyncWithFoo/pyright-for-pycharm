package com.insyncwithfoo.pyright

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessHandler
import com.intellij.execution.process.ProcessOutput


private val GeneralCommandLine.handler: CapturingProcessHandler
    get() = CapturingProcessHandler(this)


internal abstract class PyrightCommand {
    
    protected abstract val workingDirectory: String
    protected abstract val fragments: List<String>
    protected abstract val environmentVariables: Map<String, String>
    
    private val commandLine: GeneralCommandLine
        get() = GeneralCommandLine(fragments).apply {
            withWorkDirectory(this@PyrightCommand.workingDirectory)
            withCharset(Charsets.UTF_8)

            environmentVariables.forEach { (name, value) ->
                withEnvironment(name, value)
            }
        }
    
    fun asCopyableString() = commandLine.commandLineString
    
    fun run(timeoutInMilliseconds: Int): ProcessOutput =
        commandLine.handler.runProcess(timeoutInMilliseconds)
    
}
