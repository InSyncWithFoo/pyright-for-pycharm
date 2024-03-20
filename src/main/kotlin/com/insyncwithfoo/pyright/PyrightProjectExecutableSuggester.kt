package com.insyncwithfoo.pyright

import com.insyncwithfoo.pyright.configuration.PyrightConfigurationService
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import java.nio.file.Path
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.nameWithoutExtension


private fun Project.executableShouldBeSuggested(): Boolean {
    val configurations = pyrightConfigurations
    return configurations.projectExecutable == null && !configurations.alwaysUseGlobal
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


internal class PyrightProjectExecutableSuggester : ProjectActivity {
    
    override suspend fun execute(project: Project) {
        if (project.executableShouldBeSuggested() && project.sdkIsLocal) {
            suggest(project, project.findPyrightExecutable() ?: return)
        }
    }
    
    private fun suggest(project: Project, executable: Path) {
        val projectPath = project.path ?: return
        val relativePathToExecutable = projectPath.relativize(executable)
        
        val notification = pyrightNotificationGroup().createNotification(
            title = "",
            content = """
                Possibly valid Pyright executable is found at "$relativePathToExecutable".
                Set as project executable?
            """.trimIndent(),
            NotificationType.INFORMATION
        )
        
        notification.runThenNotify(project) {
            prettify()
            addSimpleExpiringAction("Absolute path") {
                project.setAsExecutable(executable)
            }
            addSimpleExpiringAction("Relative path") {
                project.setAsExecutable(relativePathToExecutable)
            }
        }
    }
    
}
