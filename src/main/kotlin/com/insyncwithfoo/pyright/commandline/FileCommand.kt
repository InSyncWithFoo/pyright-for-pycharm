package com.insyncwithfoo.pyright.commandline

import com.insyncwithfoo.pyright.ProcessOutputSurrogate
import com.insyncwithfoo.pyright.configurations.Locale
import com.insyncwithfoo.pyright.configurations.PyrightConfigurations
import com.insyncwithfoo.pyright.configurations.pyrightConfigurations
import com.insyncwithfoo.pyright.configurations.pyrightExecutable
import com.insyncwithfoo.pyright.osDependentInterpreterPath
import com.insyncwithfoo.pyright.path
import com.insyncwithfoo.pyright.toNullIfNotExists
import com.insyncwithfoo.pyright.wslDistribution
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessHandler
import com.intellij.execution.process.ProcessOutput
import com.intellij.execution.wsl.WSLCommandLineOptions
import com.intellij.execution.wsl.WSLDistribution
import com.intellij.execution.wsl.WslPath
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.module.Module
import java.nio.file.Path


private fun WSLDistribution?.resolve(path: String?) = when {
    path == null -> null
    this == null -> path
    else -> WslPath.parseWindowsUncPath(path)?.linuxPath?.replace("\\", "/")
}


private fun WSLDistribution?.resolve(path: Path?) = path?.let { this.resolve(it.toString()) }


internal data class FileCommand(
    val executable: String,
    val target: String,
    val projectPath: Path,
    val extraArguments: List<String>,
    val environmentVariables: Map<String, String>,
    val module: Module
) {
    
    private val wslDistribution by lazy { module.wslDistribution }
    
    private val fragments: List<String>
        get() = listOf(
            executable,
            *extraArguments.toTypedArray(),
            target
        )
    
    private val commandLine: GeneralCommandLine
        get() = GeneralCommandLine(fragments).apply {
            withCharset(Charsets.UTF_8)
            withWorkingDirectory(projectPath)
            withEnvironment(environmentVariables)
            
            wslDistribution?.patchCommandLine(this, module.project, WSLCommandLineOptions())
        }
    
    private val processHandler: CapturingProcessHandler
        get() = CapturingProcessHandler(commandLine)
    
    override fun toString() = commandLine.commandLineString
    
    fun run(): ProcessOutput {
        thisLogger().info("Running: ($projectPath) $this")
        
        return processHandler.runProcess().also {
            thisLogger().info("Output: ${ProcessOutputSurrogate(it)}")
        }
    }
    
    companion object {
        
        private fun create(
            configurations: PyrightConfigurations,
            executable: String,
            target: String,
            argumentForProject: String,
            projectPath: Path,
            interpreterPath: String,
            module: Module
        ): FileCommand {
            val extraArguments: MutableList<String> = mutableListOf(
                "--outputjson",
                "--project", argumentForProject,
                "--pythonpath", interpreterPath
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
            
            return FileCommand(executable, target, projectPath, extraArguments, environmentVariables, module)
        }
        
        internal fun create(module: Module, path: Path): FileCommand? {
            val wslDistribution = module.wslDistribution
            val project = module.project
            val projectPath = project.path ?: return null
            val configurations = project.pyrightConfigurations
            
            val target = wslDistribution.resolve(path.toNullIfNotExists()) ?: return null
            val executable = wslDistribution.resolve(project.pyrightExecutable) ?: return null
            val interpreterPath = module.osDependentInterpreterPath ?: return null
            
            val configurationFile = wslDistribution.resolve(configurations.configurationFile)
            val argumentForProject = configurationFile ?: wslDistribution.resolve(projectPath) ?: return null
            
            return create(configurations, executable, target, argumentForProject, projectPath, interpreterPath, module)
        }
        
    }
    
}
