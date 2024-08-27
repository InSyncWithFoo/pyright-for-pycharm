package com.insyncwithfoo.pyright.lsp4ij

import com.insyncwithfoo.pyright.path
import com.insyncwithfoo.pyright.pyrightConfigurations
import com.insyncwithfoo.pyright.pyrightLSExecutable
import com.intellij.openapi.project.Project
import com.redhat.devtools.lsp4ij.server.ProcessStreamConnectionProvider


internal class ConnectionProvider(
    commands: List<String>,
    workingDirectory: String?
) : ProcessStreamConnectionProvider(commands, workingDirectory) {
    
    companion object {
        fun create(project: Project): ConnectionProvider {
            val configurations = project.pyrightConfigurations
            val executable = project.pyrightLSExecutable!!
            
            val commands: List<String> = listOf(executable.toString(), "--stdio")
            val workingDirectory = configurations.workingDirectory ?: project.path?.toString()
            
            return ConnectionProvider(commands, workingDirectory)
        }
    }
    
}
