package com.insyncwithfoo.pyright.configurations

import com.insyncwithfoo.pyright.configurations.models.Overrides
import com.insyncwithfoo.pyright.findExecutableInPath
import com.insyncwithfoo.pyright.findExecutableInVenv
import com.insyncwithfoo.pyright.interpreterDirectory
import com.insyncwithfoo.pyright.path
import com.insyncwithfoo.pyright.toNullIfNotExists
import com.insyncwithfoo.pyright.toPathOrNull
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import java.nio.file.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.nameWithoutExtension


private val LOGGER = Logger.getInstance("com.insyncwithfoo.pyright.configurations")

private val EXECUTABLE_NAMES = listOf("pyright", "basedpyright")
private val LANGSERVER_EXECUTABLE_NAMES = listOf("pyright-langserver", "basedpyright-langserver")

private val SUPPORTED_LSP_CONFIG_FILE_NAMES = setOf("pyrightconfig.json", "pyproject.toml")


internal fun findPyrightExecutableInPath() =
    EXECUTABLE_NAMES.firstNotNullOfOrNull { findExecutableInPath(it) }


internal fun findPyrightLangserverExecutableInPath() =
    LANGSERVER_EXECUTABLE_NAMES.firstNotNullOfOrNull { findExecutableInPath(it) }


internal fun Project.findPyrightExecutableInVenv() =
    EXECUTABLE_NAMES.firstNotNullOfOrNull { findExecutableInVenv(it) }


internal fun Project.findPyrightLangserverExecutableInVenv() =
    LANGSERVER_EXECUTABLE_NAMES.firstNotNullOfOrNull { findExecutableInVenv(it) }


internal fun Project.resolveExecutable(settingValue: String?, smartResolution: Boolean): Path? {
    val executable = settingValue?.toPathOrNull() ?: return null
    
    if (executable.isAbsolute) {
        return executable.toNullIfNotExists()
    }
    
    if (!smartResolution) {
        return this.path?.resolve(executable)
    }
    
    val resolutionBase = when (val parent = executable.parent) {
        null -> interpreterDirectory
        else -> interpreterDirectory?.resolve(parent)
    }
    
    return resolutionBase?.takeIf { it.isDirectory() }
        ?.listDirectoryEntries()
        ?.find { it.nameWithoutExtension == executable.nameWithoutExtension }
}


internal val Project.pyrightExecutable: Path?
    get() {
        val configurations = pyrightConfigurations
        val executable = configurations.executable
        
        return resolveExecutable(executable, configurations.smartExecutableResolution)
            ?: findPyrightExecutableInVenv()
            ?: findPyrightExecutableInPath()
    }


internal val Project.pyrightLangserverExecutable: Path?
    get() {
        val configurations = pyrightConfigurations
        val executable = configurations.languageServerExecutable
        
        return resolveExecutable(executable, configurations.smartLanguageServerExecutableResolution)
            ?: findPyrightLangserverExecutableInVenv()
            ?: findPyrightLangserverExecutableInPath()
    }


internal fun Project.resolveConfigurationFileWorkspaceRoot(): Path? {
    val configurations = pyrightConfigurations
    
    if (!configurations.useConfigurationFileInLSPModes) {
        return null
    }
    
    val configFile = configurations.configurationFile?.toPathOrNull() ?: return null
    
    val resolved = when {
        configFile.isAbsolute -> configFile
        else -> path?.resolve(configFile)
    } ?: return null
    
    if (!resolved.toFile().exists()) {
        LOGGER.warn("Configuration file does not exist: $resolved")
        return null
    }
    
    val fileName = resolved.fileName?.toString()
    
    if (fileName != null && fileName !in SUPPORTED_LSP_CONFIG_FILE_NAMES) {
        LOGGER.warn(
            "Configuration file '$fileName' is not supported in LSP mode. " +
            "Only ${SUPPORTED_LSP_CONFIG_FILE_NAMES.joinToString()} are recognized by Pyright's language server."
        )
        return null
    }
    
    return resolved.parent
}


internal fun Project.changePyrightConfigurations(action: PyrightConfigurations.() -> Unit) {
    PyrightLocalService.getInstance(this).state.apply(action)
}


internal fun Project.changePyrightOverrides(action: Overrides.() -> Unit) {
    PyrightOverrideService.getInstance(this).state.names.apply(action)
}
