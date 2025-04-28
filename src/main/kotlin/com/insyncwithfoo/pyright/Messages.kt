package com.insyncwithfoo.pyright

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages


internal fun Project?.showMessage(title: String, body: String) {
    Messages.showMessageDialog(this, body, title, PyrightIcons.SMALL_32)
}


internal fun Project?.showMessage(body: String) {
    showMessage(message("messages.processResult.title"), body)
}


internal fun somethingIsWrong(title: String, body: String, project: Project? = null) {
    Messages.showErrorDialog(project, body, title)
}

internal fun somethingIsWrong(body: String, project: Project? = null) {
    somethingIsWrong(title = message("messages.somethingIsWrong.title"), body, project)
}

internal fun Project?.somethingIsWrong(title: String, body: String) {
    somethingIsWrong(title, body, project = this)
}

internal fun Project?.somethingIsWrong(body: String) {
    somethingIsWrong(body, project = this)
}
