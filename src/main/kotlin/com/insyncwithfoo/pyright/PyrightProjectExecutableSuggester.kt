package com.insyncwithfoo.pyright

import com.insyncwithfoo.pyright.configuration.ConfigurationService
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import java.nio.file.Path
import com.insyncwithfoo.pyright.configuration.project.Configurations as ProjectConfigurations


private fun NotificationGroup.makeSuggestionNotification(executableRelativized: Path) =
    createNotification(
        title = message("notifications.suggestion.title"),
        content = message("notifications.suggestion.body", executableRelativized),
        NotificationType.INFORMATION
    )


private val Project.sdkIsLocal: Boolean
    get() = when {
        path == null -> false
        sdkPath == null -> false
        else -> sdkPath!!.startsWith(path!!)
    }


private val Project.executableShouldBeSuggested: Boolean
    get() {
        val configurations = pyrightConfigurations
        
        val suggestionIsEnabled = configurations.autoSuggestExecutable
        val noProjectExecutableGiven = configurations.projectExecutable == null
        val globalExcutableIsPreferred = configurations.alwaysUseGlobal
        
        return suggestionIsEnabled && noProjectExecutableGiven && !globalExcutableIsPreferred
    }


private fun Project.changePyrightConfigurations(action: ProjectConfigurations.() -> Unit) {
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


private fun Project.suggest(executable: Path) {
    val projectPath = path ?: return
    val executableRelativized = projectPath.relativize(executable)
    
    val notification = pyrightNotificationGroup().makeSuggestionNotification(executableRelativized)
    
    notification.runThenNotify(this) {
        prettify()
        addSimpleExpiringAction(message("notifications.suggestion.action.setAbsolute")) {
            setAsExecutable(executable)
        }
        addSimpleExpiringAction(message("notifications.suggestion.action.setRelative")) {
            setAsExecutable(executableRelativized)
        }
        addSimpleExpiringAction(message("notifications.suggestion.action.disableSuggester")) {
            disableSuggester()
        }
    }
}


internal class PyrightProjectExecutableSuggester : ProjectActivity {
    
    override suspend fun execute(project: Project) {
        if (project.run { pyrightInspectionIsEnabled && executableShouldBeSuggested && sdkIsLocal }) {
            project.suggest(project.findPyrightExecutable() ?: return)
        }
    }
    
}
