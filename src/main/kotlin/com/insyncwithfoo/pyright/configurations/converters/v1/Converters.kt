package com.insyncwithfoo.pyright.configurations.converters.v1

import com.insyncwithfoo.pyright.configurations.PyrightGlobalService
import com.insyncwithfoo.pyright.configurations.PyrightLocalService
import com.insyncwithfoo.pyright.configurations.changePyrightOverrides
import com.insyncwithfoo.pyright.configurations.models.add
import com.intellij.ide.AppLifecycleListener
import com.intellij.ide.util.RunOnceUtil
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity


internal fun migrateGlobal(legacy: LegacyGlobalConfigurations) {
    PyrightGlobalService.getInstance().state.apply {
        runningMode = legacy.globalRunningMode
        
        executable = legacy.globalExecutable
        configurationFile = legacy.globalConfigurationFile
        useEditorFontForTooltips = legacy.useEditorFont
        prefixTooltipMessages = legacy.addTooltipPrefix
        minimumSeverityLevel = legacy.minimumSeverityLevel
        languageServerExecutable = legacy.globalLangserverExecutable
        runningMode = legacy.globalRunningMode
        numberOfThreads = legacy.numberOfThreads
        locale = legacy.locale
    }
}


internal fun Project.migrateLocal(legacy: LegacyLocalConfigurations) {
    PyrightLocalService.getInstance(this).state.apply {
        val (legacyExecutable, legacyConfigurationFile, legacyLangserverExecutable) = Triple(
            legacy.projectExecutable,
            legacy.projectConfigurationFile,
            legacy.projectLangserverExecutable
        );
        
        if (legacyExecutable != null && legacyExecutable != executable) {
            executable = legacyExecutable
            
            changePyrightOverrides {
                add(::executable.name)
            }
        }
        
        if (legacyConfigurationFile != null && legacyConfigurationFile != configurationFile) {
            configurationFile = legacyConfigurationFile
            
            changePyrightOverrides {
                add(::configurationFile.name)
            }
        }
        
        if (legacyLangserverExecutable != null && legacyLangserverExecutable != languageServerExecutable) {
            languageServerExecutable = legacyLangserverExecutable
            
            changePyrightOverrides {
                add(::languageServerExecutable.name)
            }
        }
    }
}


internal class ConfigurationsConverter : AppLifecycleListener, ProjectActivity {
    
    override fun appFrameCreated(commandLineArgs: MutableList<String>) {
        RunOnceUtil.runOnceForApp(this::class.qualifiedName!!) {
            val service = service<LegacyGlobalService>()
            val configurations = service.state
            
            thisLogger().info("Migrating legacy V1 configurations (global): $configurations")
            migrateGlobal(configurations)
        }
    }
    
    private fun projectOpened(project: Project) {
        RunOnceUtil.runOnceForProject(project, this::class.qualifiedName!!) {
            val service = project.service<LegacyLocalService>()
            val configurations = service.state
            
            thisLogger().info("Migrating legacy V1 configurations (local): $configurations")
            project.migrateLocal(configurations)
        }
    }
    
    override suspend fun execute(project: Project) {
        projectOpened(project)
    }
    
}
