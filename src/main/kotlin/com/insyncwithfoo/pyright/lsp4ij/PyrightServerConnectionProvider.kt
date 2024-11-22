package com.insyncwithfoo.pyright.lsp4ij

import com.insyncwithfoo.pyright.configurations.Locale
import com.insyncwithfoo.pyright.configurations.pyrightConfigurations
import com.insyncwithfoo.pyright.configurations.pyrightLangserverExecutable
import com.insyncwithfoo.pyright.path
import com.intellij.openapi.project.Project
import com.redhat.devtools.lsp4ij.server.ProcessStreamConnectionProvider


internal class PyrightServerConnectionProvider(
    private val project: Project,
    commands: List<String>,
    workingDirectory: String?
) : ProcessStreamConnectionProvider(commands, workingDirectory) {
    
    init {
        val configurations = project.pyrightConfigurations
        
        userEnvironmentVariables = when {
            configurations.locale == Locale.DEFAULT -> emptyMap()
            else -> mapOf("LC_ALL" to configurations.locale.toString())
        }
    }
    
    companion object {
        fun create(project: Project): PyrightServerConnectionProvider {
            val executable = project.pyrightLangserverExecutable!!
            
            val commands: List<String> = listOf(executable.toString(), "--stdio")
            val workingDirectory = project.path?.toString()
            
            return PyrightServerConnectionProvider(project, commands, workingDirectory)
        }
    }
    
}
