package com.insyncwithfoo.pyright.lsp4ij

import com.insyncwithfoo.pyright.configurations.pyrightLangserverExecutable
import com.insyncwithfoo.pyright.path
import com.intellij.openapi.project.Project
import com.redhat.devtools.lsp4ij.server.ProcessStreamConnectionProvider


internal class PyrightServerConnectionProvider(commands: List<String>, workingDirectory: String?) :
    ProcessStreamConnectionProvider(commands, workingDirectory) {
    
    companion object {
        fun create(project: Project): PyrightServerConnectionProvider {
            val executable = project.pyrightLangserverExecutable!!
            
            val commands: List<String> = listOf(executable.toString(), "--stdio")
            val workingDirectory = project.path?.toString()
            
            return PyrightServerConnectionProvider(commands, workingDirectory)
        }
    }
    
}
