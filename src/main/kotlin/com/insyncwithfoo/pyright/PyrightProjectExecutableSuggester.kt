package com.insyncwithfoo.pyright

import com.insyncwithfoo.pyright.configuration.PyrightConfigurationService
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import java.nio.file.Path
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.nameWithoutExtension


private val Project.sdkIsLocal: Boolean
    get() = when {
        path == null -> false
        sdkPath == null -> false
        else -> sdkPath!!.startsWith(path!!)
    }


private fun Project.executableShouldBeSuggested(): Boolean {
    val configurations = pyrightConfigurations
    
    val suggestionIsEnabled = configurations.autoSuggestExecutable
    val noProjectExecutableGiven = configurations.projectExecutable == null
    val globalExcutableIsPreferred = configurations.alwaysUseGlobal
    
    return suggestionIsEnabled && noProjectExecutableGiven && !globalExcutableIsPreferred
}


private fun Project.findPyrightExecutable(): Path? {
    val sdkDirectory = sdkPath?.parent ?: return null
    val children = sdkDirectory.listDirectoryEntries("*")
    
    return children.find { it.nameWithoutExtension == "pyright" }
}


private fun Project.setAsExecutable(executable: Path) {
    val configurationService = PyrightConfigurationService.getInstance(this)
    val projectConfigurations = configurationService.projectService.configurations
        
    projectConfigurations.projectExecutable = executable.toString()
}


private fun Project.disableSuggester() {
    val configurationService = PyrightConfigurationService.getInstance(this)
    val projectConfigurations = configurationService.projectService.configurations
    
    projectConfigurations.autoSuggestExecutable = false
}


internal class PyrightProjectExecutableSuggester : ProjectActivity {
    
    override suspend fun execute(project: Project) {
        if (project.run { isPyrightInspectionEnabled() && executableShouldBeSuggested() && sdkIsLocal }) {
            suggest(project, project.findPyrightExecutable() ?: return)
        }
    }
    
    private fun suggest(project: Project, executable: Path) {
        val projectPath = project.path ?: return
        val executableRelativized = projectPath.relativize(executable)
        
        val notification = pyrightNotificationGroup().createNotification(
            title = message("notifications.suggestion.title"),
            content = message("notifications.suggestion.body", executableRelativized),
            NotificationType.INFORMATION
        )
        
        notification.runThenNotify(project) {
            prettify()
            addSimpleExpiringAction(message("notifications.suggestion.action.setAbsolute")) {
                project.setAsExecutable(executable)
            }
            addSimpleExpiringAction(message("notifications.suggestion.action.setRelative")) {
                project.setAsExecutable(executableRelativized)
            }
            addSimpleExpiringAction(message("notifications.suggestion.action.disableSuggester")) {
                project.disableSuggester()
            }
        }
    }
    
}
