package com.insyncwithfoo.pyright.commandline

import com.insyncwithfoo.pyright.configurations.Locale
import com.insyncwithfoo.pyright.configurations.PyrightConfigurations
import com.insyncwithfoo.pyright.configurations.pyrightConfigurations
import com.insyncwithfoo.pyright.configurations.pyrightExecutable
import com.insyncwithfoo.pyright.interpreterPath
import com.insyncwithfoo.pyright.path
import com.insyncwithfoo.pyright.toNullIfNotExists
import com.insyncwithfoo.pyright.toPathIfItExists
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessHandler
import com.intellij.openapi.module.Module
import java.nio.file.Path


internal data class FileCommand(
    val executable: Path,
    val target: Path,
    val projectPath: String,
    val extraArguments: List<String>,
    val environmentVariables: Map<String, String>
) {
    private val fragments: List<String>
        get() = listOf(
            executable.toString(),
            *extraArguments.toTypedArray(),
            target.toString()
        )
    
    private val commandLine: GeneralCommandLine
        get() = GeneralCommandLine(fragments).apply {
            withCharset(Charsets.UTF_8)
            withWorkingDirectory(projectPath.toPathIfItExists())
        }
    
    private val processHandler: CapturingProcessHandler
        get() = CapturingProcessHandler(commandLine)
    
    fun run() =
        processHandler.runProcess()
    
    companion object {
        
        private fun create(
            configurations: PyrightConfigurations,
            executable: Path,
            target: Path,
            projectPath: Path,
            interpreterPath: Path
        ): FileCommand {
            val configurationFile = configurations.configurationFile
            
            val argumentForProject = configurationFile ?: projectPath
            val extraArguments: MutableList<String> = mutableListOf(
                "--outputjson",
                "--project", argumentForProject.toString(),
                "--pythonpath", interpreterPath.toString()
            )
            val environmentVariables = mutableMapOf<String, String>()
            
            if (configurations.minimumSeverityLevel != DiagnosticSeverity.INFORMATION) {
                extraArguments.add("--level")
                extraArguments.add(configurations.minimumSeverityLevel.name)
            }
            
            if (configurations.numberOfThreads != 0) {
                extraArguments.add("--threads")
                extraArguments.add(configurations.numberOfThreads.toString())
            }
            
            if (configurations.locale != Locale.DEFAULT) {
                environmentVariables["LC_ALL"] = configurations.locale.toString()
            }
            
            return FileCommand(executable, target, projectPath.toString(), extraArguments, environmentVariables)
        }
        
        internal fun create(module: Module, path: Path): FileCommand? {
            val project = module.project
            val configurations = project.pyrightConfigurations
            
            val filePath = path.toNullIfNotExists() ?: return null
            val projectPath = project.path ?: return null
            val executable = project.pyrightExecutable ?: return null
            val interpreterPath = module.interpreterPath ?: return null
            
            return create(configurations, executable, filePath, projectPath, interpreterPath)
        }
        
    }
    
}
