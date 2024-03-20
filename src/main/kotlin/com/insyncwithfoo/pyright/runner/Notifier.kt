package com.insyncwithfoo.pyright.runner

import com.insyncwithfoo.pyright.PyrightInspection
import com.insyncwithfoo.pyright.addSimpleExpiringAction
import com.insyncwithfoo.pyright.openingPyrightNotifications
import com.insyncwithfoo.pyright.prettify
import com.insyncwithfoo.pyright.pyrightNotificationGroup
import com.insyncwithfoo.pyright.runThenNotify
import com.intellij.notification.Notification
import com.intellij.openapi.project.Project
import com.intellij.profile.codeInspection.InspectionProjectProfileManager


private infix fun Notification.sameAs(other: Notification) =
    title == other.title && content == other.content


private fun Project.disablePyrightInspection() {
    val inspectionManager = InspectionProjectProfileManager.getInstance(this)
    val profile = inspectionManager.currentProfile
    val pyrightInspection = profile.allTools.find { it.tool.shortName == PyrightInspection.SHORT_NAME }
    
    pyrightInspection?.isEnabled = false
    profile.profileChanged()
}


internal class Notifier(private val project: Project) {
    
    private val group = pyrightNotificationGroup()
    
    fun notify(exception: PyrightException) {
        val newNotification = exception.createNotification(group)
        val notified = project.openingPyrightNotifications
        
        if (!notified.any { it sameAs newNotification }) {
            notify(newNotification)
        }
    }
    
    private fun notify(notification: Notification) {
        notification.runThenNotify(project) {
            prettify()
            addSimpleExpiringAction("Disable plugin (project)") {
                project.disablePyrightInspection()
            }
        }
    }
    
}
