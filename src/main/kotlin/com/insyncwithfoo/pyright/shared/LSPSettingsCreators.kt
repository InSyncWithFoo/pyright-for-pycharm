package com.insyncwithfoo.pyright.shared

import com.insyncwithfoo.pyright.configurations.pyrightConfigurations
import com.insyncwithfoo.pyright.interpreterPath
import com.insyncwithfoo.pyright.wslDistribution
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project


private val Module.osDependentInterpreterPath: String?
    get() {
        val interpreterPath = this.interpreterPath?.toString()
        
        return when (wslDistribution) {
            null -> interpreterPath
            else -> interpreterPath?.replace("\\", "/")
        }
    }


internal fun Project.createLSPSettingsObject(module: Module? = null) = Settings().apply {
    val configurations = pyrightConfigurations
    
    python {
        pythonPath = module?.osDependentInterpreterPath
        
        analysis {
            logLevel = configurations.logLevel.label
            autoImportCompletions = configurations.autoImportCompletions
            diagnosticMode = configurations.diagnosticMode.value
            autoSearchPaths = configurations.autoSearchPaths
        }
    }
    
    pyright {
        disableTaggedHints = !configurations.taggedHints
    }
}
