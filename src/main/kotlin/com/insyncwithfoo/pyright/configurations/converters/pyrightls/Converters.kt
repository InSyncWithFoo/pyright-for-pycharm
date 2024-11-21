package com.insyncwithfoo.pyright.configurations.converters.pyrightls

import com.insyncwithfoo.pyright.configurations.PyrightGlobalService
import com.insyncwithfoo.pyright.configurations.PyrightLocalService
import com.insyncwithfoo.pyright.configurations.RunningMode
import com.insyncwithfoo.pyright.configurations.changePyrightOverrides
import com.insyncwithfoo.pyright.configurations.models.add
import com.intellij.ide.AppLifecycleListener
import com.intellij.ide.util.RunOnceUtil
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity


private fun migrateGlobal(legacy: LegacyGlobalConfigurations) {
    PyrightGlobalService.getInstance().state.apply {
        runningMode = RunningMode.LSP
        
        executable = legacy.globalExecutable
        useEditorFontForTooltips = legacy.useEditorFont
        prefixTooltipMessages = legacy.addTooltipPrefix
        linkErrorCodesInTooltips = legacy.linkErrorCodes
        hover = legacy.hoverSupport
        completion = legacy.completionSupport
        gotoDefinition = legacy.goToDefinitionSupport
        logLevel = legacy.logLevel
        taggedHints = legacy.taggedHints
        autoImportCompletions = legacy.autoImportCompletions
        autocompleteParentheses = legacy.autocompleteParentheses
        diagnostics = legacy.diagnosticsSupport
        autoRestartServers = legacy.autoRestartServer
        monkeypatchAutoImportDetails = legacy.monkeypatchAutoImportDetails
        monkeypatchTrailingQuoteBug = legacy.monkeypatchTrailingQuoteBug
        locale = legacy.locale
    }
}


private fun Project.migrateLocal(legacy: LegacyLocalConfigurations) {
    PyrightLocalService.getInstance(this).state.apply {
        executable = legacy.projectExecutable
        workspaceFolders = legacy.workspaceFolders
        targetedFileExtensions = legacy.targetedFileExtensions
        diagnosticMode = legacy.diagnosticMode
        autoSearchPaths = legacy.autoSearchPaths
        
        changePyrightOverrides {
            add(::executable.name)
            add(::workspaceFolders.name)
            add(::targetedFileExtensions.name)
            add(::diagnosticMode.name)
            add(::autoSearchPaths.name)
        }
    }
}


internal class ConfigurationsConverter : AppLifecycleListener, ProjectActivity {
    
    override fun appFrameCreated(commandLineArgs: MutableList<String>) {
        RunOnceUtil.runOnceForApp(this::class.qualifiedName!!) {
            val service = service<LegacyGlobalService>()
            val configurations = service.state
            
            thisLogger().info("Migrating legacy Pyright Language Server configurations (global): $configurations")
            migrateGlobal(configurations)
        }
    }
    
    private fun projectOpened(project: Project) {
        RunOnceUtil.runOnceForProject(project, this::class.qualifiedName!!) {
            val service = project.service<LegacyLocalService>()
            val configurations = service.state
            
            thisLogger().info("Migrating legacy Pyright Language Server configurations (local): $configurations")
            project.migrateLocal(configurations)
        }
    }
    
    override suspend fun execute(project: Project) {
        projectOpened(project)
    }
    
}
