package com.insyncwithfoo.pyright.shared

import com.insyncwithfoo.pyright.commandline.Diagnostic
import com.insyncwithfoo.pyright.configurations.PyrightConfigurations
import org.eclipse.lsp4j.Diagnostic as LSPDiagnostic


private val unsuppressableErrorCodes = listOf("reportUnnecessaryTypeIgnoreComment")


internal val LSPDiagnostic.codeAsString: String?
    get() = code.get() as? String


internal val Diagnostic.isUnsuppressable: Boolean
    get() = rule in unsuppressableErrorCodes


internal val LSPDiagnostic.isUnsuppressable: Boolean
    get() = code.get() in unsuppressableErrorCodes


internal val Diagnostic.suffixedMessage: String
    get() {
        val suffix = when (rule) {
            null -> ""
            else -> " ($rule)"
        }
        
        return "$message$suffix"
    }


internal val LSPDiagnostic.suffixedMessage: String
    get() {
        val suffix = when (val code = codeAsString) {
            null -> ""
            else -> " ($code)"
        }
        
        return "$message$suffix"
    }


internal fun Diagnostic.getFormattedTooltip(configurations: PyrightConfigurations) =
    message.toFormattedTooltip(
        code = rule,
        useEditorFont = configurations.useEditorFontForTooltips,
        addPrefix = configurations.prefixTooltipMessages,
        linkErrorCode = configurations.linkErrorCodesInTooltips
    )


internal fun LSPDiagnostic.getFormattedTooltip(configurations: PyrightConfigurations) =
    message.toFormattedTooltip(
        code = codeAsString,
        useEditorFont = configurations.useEditorFontForTooltips,
        addPrefix = configurations.prefixTooltipMessages,
        linkErrorCode = configurations.linkErrorCodesInTooltips
    )
