package com.insyncwithfoo.pyright.shared

import com.insyncwithfoo.pyright.configurations.pyrightConfigurations
import com.insyncwithfoo.pyright.modules
import com.insyncwithfoo.pyright.osDependentInterpreterPath
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project


internal fun Project.createLSPSettingsObject(module: Module? = null) = Settings().apply {
    val configurations = pyrightConfigurations
    
    python {
        pythonPath = (module ?: modules.singleOrNull())?.osDependentInterpreterPath
        
        analysis {
            logLevel = configurations.logLevel.label
            autoImportCompletions = configurations.autoImportCompletions
            diagnosticMode = configurations.diagnosticMode.value
            autoSearchPaths = configurations.autoSearchPaths
            
            inlayHints {
                variableTypes = configurations.inlayHintsVariableTypes
                callArgumentNames = configurations.inlayHintsCallArgumentNames
                functionReturnTypes = configurations.inlayHintsFunctionReturnTypes
                genericTypes = configurations.inlayHintsGenericTypes
            }
        }
    }
    
    pyright {
        disableTaggedHints = !configurations.taggedHints
    }
}
