package com.insyncwithfoo.pyright.configurations

import com.insyncwithfoo.pyright.findExecutableInPath
import com.insyncwithfoo.pyright.findExecutableInVenv
import com.insyncwithfoo.pyright.interpreterDirectory
import com.insyncwithfoo.pyright.path
import com.insyncwithfoo.pyright.removeExtension
import com.insyncwithfoo.pyright.toNullIfNotExists
import com.insyncwithfoo.pyright.toPathOrNull
import com.intellij.openapi.project.Project
import java.nio.file.Path


internal fun Project.resolveExecutable(settingValue: String?, smartResolution: Boolean): Path? {
    val executable = settingValue?.toPathOrNull()
    
    if (executable?.isAbsolute == true) {
        return executable.toNullIfNotExists()
    }
    
    val resolutionBase = when {
        smartResolution -> interpreterDirectory
        else -> this.path
    }
    val resolved = executable?.removeExtension()?.let { resolutionBase?.resolve(it) }
    
    return resolved?.toNullIfNotExists()
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
