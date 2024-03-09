package com.insyncwithfoo.pyright.runner

import com.insyncwithfoo.pyright.PyrightIcon
import com.insyncwithfoo.pyright.PyrightInspection
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationAction.createSimpleExpiring
import com.intellij.notification.NotificationGroupManager
import com.intellij.openapi.project.Project
import com.intellij.profile.codeInspection.InspectionProjectProfileManager
import java.lang.ref.WeakReference


private infix fun Notification.sameAs(other: Notification?) =
    title == other?.title && content == other.content


internal class Notifier(private val project: Project) {
    
    private val groupManager = NotificationGroupManager.getInstance()
    private val group = groupManager.getNotificationGroup(ID)
    
    fun notify(exception: PyrightException) {
        val notification = exception.createNotification(group)
        
        if (notified.any { notification sameAs it.get() }) {
            return
        }
        
        notified.add(WeakReference(notification))
        
        return notify(notification)
    }
    
    private fun notify(notification: Notification) {
        notification.run {
            isImportant = false
            icon = PyrightIcon.COLORED_SMALL
            addAction(disablePlugin(project))
            
            notify(project)
        }
    }
    
    private fun disablePlugin(project: Project): NotificationAction {
        return createSimpleExpiring(DISABLE_PLUGIN_TEXT) {
            val inspectionManager = InspectionProjectProfileManager.getInstance(project)
            val profile = inspectionManager.currentProfile
            val pyrightInspection = profile.allTools.find { it.tool.shortName == PyrightInspection.SHORT_NAME }
            
            pyrightInspection?.isEnabled = false
            profile.profileChanged()
            notified.clear()
        }
    }
    
    companion object {
        private const val ID = "Pyright"
        private const val DISABLE_PLUGIN_TEXT = "Disable plugin (project)"
        private val notified = mutableListOf<WeakReference<Notification>>()
    }
    
}
