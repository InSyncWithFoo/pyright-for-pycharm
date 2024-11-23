package com.insyncwithfoo.pyright.lsp4ij

import com.insyncwithfoo.pyright.configurations.Locale
import com.insyncwithfoo.pyright.configurations.pyrightConfigurations
import com.insyncwithfoo.pyright.configurations.pyrightLangserverExecutable
import com.insyncwithfoo.pyright.path
import com.intellij.openapi.project.Project
import com.redhat.devtools.lsp4ij.server.ProcessStreamConnectionProvider


internal class PyrightServerConnectionProvider(
    commands: List<String>,
    workingDirectory: String?,
    environmentVariables: Map<String, String>
) : ProcessStreamConnectionProvider(commands, workingDirectory, environmentVariables) {
    
    companion object {
        fun create(project: Project): PyrightServerConnectionProvider {
            val configurations = project.pyrightConfigurations
            val executable = project.pyrightLangserverExecutable!!
            
            val commands: List<String> = listOf(executable.toString(), "--stdio")
            val workingDirectory = project.path?.toString()
            
            val environmentVariables = when {
                configurations.locale == Locale.DEFAULT -> emptyMap()
                else -> mapOf("LC_ALL" to configurations.locale.toString())
            }
            
            return PyrightServerConnectionProvider(commands, workingDirectory, environmentVariables)
        }
    }
    
}
