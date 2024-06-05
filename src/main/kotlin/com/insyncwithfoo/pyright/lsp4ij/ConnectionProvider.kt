package com.insyncwithfoo.pyright.lsp4ij

import com.insyncwithfoo.pyright.configuration.application.RunningMode
import com.insyncwithfoo.pyright.path
import com.insyncwithfoo.pyright.pyrightConfigurations
import com.insyncwithfoo.pyright.sdkPath
import com.insyncwithfoo.pyright.toPathIfItExists
import com.intellij.openapi.project.Project
import com.redhat.devtools.lsp4ij.server.ProcessStreamConnectionProvider
import java.net.URI
import java.nio.file.Path


private fun Project.createSettingsObject() = Settings {
    python {
        pythonPath = sdkPath?.toString()
    }
}


internal class ConnectionProvider(
    private val project: Project,
    private val executable: Path?
) : ProcessStreamConnectionProvider() {
    
    private val configurations = project.pyrightConfigurations
    
    init {
        commands = listOf(executable?.toString() ?: "", "--stdio")
        workingDirectory = project.path?.toString()
    }
    
    override fun start() {
        // FIXME: This throws NullPointerException at runtime.
        if (configurations.runningMode == RunningMode.LSP4IJ && executable != null) {
            super.start()
        }
    }
    
    // FIXME: This must be sent via didChangeConfiguration.
    override fun getInitializationOptions(rootUri: URI?) =
        project.createSettingsObject()
    
    companion object {
        fun create(project: Project): ConnectionProvider {
            val configurations = project.pyrightConfigurations
            val executable = configurations.langserverExecutable?.toPathIfItExists(base = project.path)
            
            return ConnectionProvider(project, executable)
        }
    }
    
}
