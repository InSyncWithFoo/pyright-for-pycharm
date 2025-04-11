package com.insyncwithfoo.pyright.configurations

import com.insyncwithfoo.pyright.configurations.models.Overrides
import com.insyncwithfoo.pyright.findExecutableInPath
import com.insyncwithfoo.pyright.findExecutableInVenv
import com.insyncwithfoo.pyright.interpreterDirectory
import com.insyncwithfoo.pyright.path
import com.insyncwithfoo.pyright.toNullIfNotExists
import com.insyncwithfoo.pyright.toPathOrNull
import com.intellij.openapi.project.Project
import java.nio.file.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.nameWithoutExtension


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
            ?: findExecutableInVenv("pyright")
            ?: findExecutableInPath("pyright")
    }


internal val Project.pyrightLangserverExecutable: Path?
    get() {
        val configurations = pyrightConfigurations
        val executable = configurations.languageServerExecutable
        
        return resolveExecutable(executable, configurations.smartLanguageServerExecutableResolution)
            ?: findExecutableInVenv("pyright-langserver")
            ?: findExecutableInPath("pyright-langserver")
    }


internal fun Project.changePyrightConfigurations(action: PyrightConfigurations.() -> Unit) {
    PyrightLocalService.getInstance(this).state.apply(action)
}


internal fun Project.changePyrightOverrides(action: Overrides.() -> Unit) {
    PyrightOverrideService.getInstance(this).state.names.apply(action)
}
