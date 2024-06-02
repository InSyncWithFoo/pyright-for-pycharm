package com.insyncwithfoo.pyright.runner

import com.insyncwithfoo.pyright.PyrightInspection
import com.insyncwithfoo.pyright.addSimpleExpiringAction
import com.insyncwithfoo.pyright.inspectionProfileManager
import com.insyncwithfoo.pyright.message
import com.insyncwithfoo.pyright.openingPyrightNotifications
import com.insyncwithfoo.pyright.prettify
import com.insyncwithfoo.pyright.pyrightNotificationGroup
import com.insyncwithfoo.pyright.runThenNotify
import com.intellij.notification.Notification
import com.intellij.notification.NotificationGroup
import com.intellij.openapi.project.Project


private infix fun Notification.sameAs(other: Notification) =
    this.title == other.title


private fun Project.disablePyrightInspection() {
    val profile = inspectionProfileManager.currentProfile
    val pyrightInspection = profile.allTools.find { it.tool.shortName == PyrightInspection.SHORT_NAME }
    
    pyrightInspection?.isEnabled = false
    profile.profileChanged()
}


internal class Notifier(private val project: Project) {
    
    private val group = pyrightNotificationGroup()
    
    fun notify(createNotification: (NotificationGroup, Project) -> Notification) {
        val newNotification = createNotification(group, project)
        val notified = project.openingPyrightNotifications
        
        if (!notified.any { it sameAs newNotification }) {
            notify(newNotification)
        }
    }
    
    private fun notify(notification: Notification) {
        notification.runThenNotify(project) {
            prettify()
            addSimpleExpiringAction(message("notifications.error.action.disablePlugin")) {
                project.disablePyrightInspection()
            }
        }
    }
    
}
