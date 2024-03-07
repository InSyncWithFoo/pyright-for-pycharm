package com.insyncwithfoo.pyright.runner

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.ui.Messages


internal class PyrightErrorReporter(private val logger: Logger) {
    
    fun reportException(exception: PyrightException) {
        logger.error(exception)
        Messages.showErrorDialog(ERROR_MESSAGE, ERROR_MESSAGE_TITLE)
    }
    
    companion object {
        private const val ISSUE_TRACKER_LINK = "https://github.com/insyncwithfoo/pyright-plugin/issues"
        private const val ERROR_MESSAGE_TITLE = "Pyright"
        private val ERROR_MESSAGE = """
            An error is encountered. Please report it at ${ISSUE_TRACKER_LINK}.
            See your <code>idea.log</code> file for raw output.
        """.trimIndent()
    }
    
}
