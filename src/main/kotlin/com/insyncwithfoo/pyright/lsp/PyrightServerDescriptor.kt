package com.insyncwithfoo.pyright.lsp

import com.insyncwithfoo.pyright.asFileURI
import com.insyncwithfoo.pyright.configurations.Locale
import com.insyncwithfoo.pyright.configurations.WorkspaceFolders
import com.insyncwithfoo.pyright.configurations.pyrightConfigurations
import com.insyncwithfoo.pyright.configurations.targetedFileExtensionList
import com.insyncwithfoo.pyright.getPureLinuxOrWindowsPath
import com.insyncwithfoo.pyright.message
import com.insyncwithfoo.pyright.modules
import com.insyncwithfoo.pyright.path
import com.insyncwithfoo.pyright.pathIsAbsoluteDos
import com.insyncwithfoo.pyright.wslDistribution
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.wsl.WSLCommandLineOptions
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.BaseProjectDirectories.Companion.getBaseDirectories
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspServerDescriptor
import java.net.URI
import java.nio.file.Path


private fun Project.getModuleSourceRoots(): Collection<VirtualFile> =
    modules.flatMap { module ->
        ModuleRootManager.getInstance(module).sourceRoots.asIterable()
    }


private fun Project.getWorkspaceFolders(type: WorkspaceFolders): Collection<VirtualFile> =
    when (type) {
        WorkspaceFolders.PROJECT_BASE -> getBaseDirectories()
        WorkspaceFolders.SOURCE_ROOTS -> getModuleSourceRoots()
    }


private fun Project.getWorkspaceFolders(): Collection<VirtualFile> =
    getWorkspaceFolders(pyrightConfigurations.workspaceFolders)


private sealed interface ServerRunner {
    data class Executable(val executable: Path) : ServerRunner
    data class Command(val command: List<String>) : ServerRunner
}


@Suppress("UnstableApiUsage")
internal class PyrightServerDescriptor private constructor(
    project: Project,
    module: Module?,
    private val runner: ServerRunner
) : LspServerDescriptor(project, getPresentableName(project, module), *project.getWorkspaceFolders().toTypedArray()) {
    
    private val configurations = project.pyrightConfigurations
    
    override val lspServerListener = Listener(project, module)
    
    override val lspHoverSupport = configurations.hover
    override val lspCompletionSupport = CompletionSupport(project).takeIf { configurations.completion }
    override val lspDiagnosticsSupport = DiagnosticsSupport(project).takeIf { configurations.diagnostics }
    
    private val wslDistribution by lazy { module?.wslDistribution }
    
    init {
        when (runner) {
            is ServerRunner.Executable -> LOGGER.info("Executable: ${runner.executable}")
            is ServerRunner.Command -> LOGGER.info("Command: ${runner.command}")
        }
        
        LOGGER.info("WSL distro: $wslDistribution")
        LOGGER.info(configurations.toString())
    }
    
    override fun isSupportedFile(file: VirtualFile) =
        file.extension in configurations.targetedFileExtensionList
    
    override fun getFileUri(file: VirtualFile): String {
        return when {
            wslDistribution == null -> super.getFileUri(file)
            else -> wslDistribution!!.getWslPath(Path.of(file.path))!!.asFileURI()
        }
    }
    
    override fun findFileByUri(fileUri: String) =
        findFileByUri(URI.create(fileUri))
    
    private fun findFileByUri(fileUri: URI): VirtualFile? {
        val virtualFileUri = when {
            wslDistribution == null || fileUri.pathIsAbsoluteDos -> fileUri
            else -> Path.of(wslDistribution!!.getWindowsPath(fileUri.path)).toUri()
        }
        
        return super.findFileByUri(virtualFileUri.toString())
    }
    
    override fun createCommandLine(): GeneralCommandLine {
        val commandLine = when (runner) {
            is ServerRunner.Executable -> createCommandLineFromExecutable(runner.executable)
            is ServerRunner.Command -> createCommandLineFromArguments(runner.command)
        }
        
        return commandLine.apply {
            val projectPath = project.path
            
            withCharset(Charsets.UTF_8)
            withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            
            if (configurations.locale != Locale.DEFAULT) {
                withEnvironment("LC_ALL", configurations.locale.toString())
            }
            
            if (projectPath != null) {
                withWorkDirectory(projectPath.toString())
            }
        }
    }
    
    private fun createCommandLineFromExecutable(executable: Path) = GeneralCommandLine().apply {
        val exePath = wslDistribution.getPureLinuxOrWindowsPath(executable)
        
        withExePath(exePath)
        addParameter("--stdio")
        
        wslDistribution?.patchCommandLine(this, project, WSLCommandLineOptions())
    }
    
    private fun createCommandLineFromArguments(arguments: List<String>) = GeneralCommandLine().apply {
        val (executable, rest) = Pair(arguments[0], arguments.drop(1))
        
        withExePath(executable)
        addParameters(rest)
    }
    
    companion object {
        
        private val LOGGER = Logger.getInstance(PyrightServerDescriptor::class.java)
        
        private fun getPresentableName(project: Project, module: Module?) = when {
            module == null || project.modules.size == 1 -> message("languageServer.presentableName.project")
            else -> message("languageServer.presentableName.module", module.name)
        }
        
        fun fromExecutable(project: Project, module: Module?, executable: Path): PyrightServerDescriptor {
            val runner = ServerRunner.Executable(executable)
            return PyrightServerDescriptor(project, module, runner)
        }
        
        fun fromCommand(project: Project, module: Module?, command: List<String>): PyrightServerDescriptor {
            val runner = ServerRunner.Command(command)
            return PyrightServerDescriptor(project, module, runner)
        }
        
    }
    
}
