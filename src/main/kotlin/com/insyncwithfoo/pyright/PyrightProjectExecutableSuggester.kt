package com.insyncwithfoo.pyright

import com.insyncwithfoo.pyright.configuration.ConfigurationService
import com.insyncwithfoo.pyright.configuration.project.Configurations
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import java.nio.file.Path


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


private fun Project.changePyrightConfigurations(action: Configurations.() -> Unit) {
    val configurationService = ConfigurationService.getInstance(this)
    val projectConfigurations = configurationService.projectService.state
    
    projectConfigurations.action()
}


private fun Project.setAsExecutable(executable: Path) {
    changePyrightConfigurations { projectExecutable = executable.toString() }
}


private fun Project.disableSuggester() {
    changePyrightConfigurations { autoSuggestExecutable = false }
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
