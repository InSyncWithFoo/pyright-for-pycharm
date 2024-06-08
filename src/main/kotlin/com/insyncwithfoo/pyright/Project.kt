package com.insyncwithfoo.pyright

import com.insyncwithfoo.pyright.configuration.AllConfigurations
import com.insyncwithfoo.pyright.configuration.ConfigurationService
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.ui.Messages
import com.intellij.platform.ide.progress.withBackgroundProgress
import com.intellij.profile.codeInspection.InspectionProjectProfileManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import java.nio.file.Path
import kotlin.io.path.listDirectoryEntries


internal val Project.fileEditorManager: FileEditorManager
    get() = FileEditorManager.getInstance(this)


internal val Project.inspectionProfileManager: InspectionProjectProfileManager
    get() = InspectionProjectProfileManager.getInstance(this)


internal val Project.path: Path?
    get() = basePath?.let { Path.of(it) }


private val Project.sdk: Sdk?
    get() = ProjectRootManager.getInstance(this).projectSdk


internal val Project.sdkPath: Path?
    get() = sdk?.homePath?.let { Path.of(it) }


internal val Project.pyrightConfigurations: AllConfigurations
    get() = ConfigurationService.getInstance(this).state


internal val Project.pyrightExecutable: Path?
    get() = pyrightConfigurations.executable?.toPathIfItExists(base = this.path)


internal val Project.pyrightInspectionIsEnabled: Boolean
    get() {
        val profile = inspectionProfileManager.currentProfile
        val pyrightInspection = profile.allTools.find { it.tool.shortName == PyrightInspection.SHORT_NAME }
        
        return pyrightInspection?.isEnabled ?: false
    }


internal fun Project.findPyrightExecutable(): Path? {
    val sdkDirectory = sdkPath?.parent ?: return null
    val children = sdkDirectory.listDirectoryEntries()
    
    return children.find { it.isProbablyPyrightExecutable }
}


internal fun Project?.somethingIsWrong(title: String, message: String) {
    Messages.showErrorDialog(this, message, title)
}


internal fun Project?.somethingIsWrong(message: String) {
    somethingIsWrong(title = message("messages.somethingIsWrong.title"), message)
}


internal fun <T> Project.runWithIndicator(
    title: String,
    cancellable: Boolean = false,
    action: suspend CoroutineScope.() -> T
) = runBlocking {
    withBackgroundProgress(this@runWithIndicator, title, cancellable, action)
}
