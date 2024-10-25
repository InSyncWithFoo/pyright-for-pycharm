package com.insyncwithfoo.pyright.configurations

import com.insyncwithfoo.pyright.Labeled
import com.insyncwithfoo.pyright.configurations.models.Copyable
import com.insyncwithfoo.pyright.configurations.models.DisplayableState
import com.insyncwithfoo.pyright.configurations.models.ProjectOverrideState
import com.insyncwithfoo.pyright.configurations.models.SettingName
import com.insyncwithfoo.pyright.message


private const val FILE_EXTENSIONS_DELIMITER = "|"


internal typealias FileExtension = String
internal typealias DelimitedFileExtensionList = String


private fun FileExtension.normalize() = this.trim().lowercase()


private fun List<FileExtension>.toSetOfNormalized(): Set<FileExtension> =
    this.mapNotNullTo(mutableSetOf()) { extension ->
        extension.normalize().takeIf { it.isNotEmpty() }
    }


internal fun DelimitedFileExtensionList.split(): MutableList<FileExtension> =
    this.split(FILE_EXTENSIONS_DELIMITER).toSetOfNormalized().toMutableList()


internal fun List<FileExtension>.join() =
    this.toSetOfNormalized().joinToString(FILE_EXTENSIONS_DELIMITER)


internal fun DelimitedFileExtensionList.deduplicate() =
    this.split().join()


internal enum class RunningMode(override val label: String) : Labeled {
    COMMAND_LINE(message("configurations.runningMode.commandLine")),
    LSP4IJ(message("configurations.runningMode.lsp4ij")),
    LSP(message("configurations.runningMode.lsp"));
}


internal enum class LogLevel(override val label: String) : Labeled {
    ERROR(message("configurations.logLevel.error")),
    WARNING(message("configurations.logLevel.warning")),
    INFORMATION(message("configurations.logLevel.information")),
    TRACE(message("configurations.logLevel.trace"));
}


// https://github.com/microsoft/pyright/blob/acc52c7420/packages/pyright-internal/src/localization/localize.ts#L12-L26
internal enum class Locale(override val label: String) : Labeled {
    DEFAULT(message("configurations.locale.default")),
    CS("cs"),
    DE("de"),
    EN_US("en-us"),
    ES("es"),
    FR("fr"),
    IT("it"),
    JA("ja"),
    KO("ko"),
    PL("pl"),
    PT_BR("pt-br"),
    RU("ru"),
    TR("tr"),
    ZH_CN("zh-cn"),
    ZH_TW("zh-tw");
    
    override fun toString() = label
}


internal enum class WorkspaceFolders(override val label: String) : Labeled {
    PROJECT_BASE(message("configurations.workspaceFolders.projectBase")),
    SOURCE_ROOTS(message("configurations.workspaceFolders.sourceRoots"));
}


internal enum class DiagnosticMode(val value: String, override val label: String) : Labeled {
    OPEN_FILES_ONLY("openFilesOnly", message("configurations.diagnosticMode.openFilesOnly")),
    WORKSPACE("workspace", message("configurations.diagnosticMode.workspace"));
}


internal class PyrightConfigurations : DisplayableState(), Copyable {
    var executable by string(null)
    var smartExecutableResolution by property(false)
    var languageServerExecutable by string(null)
    var smartLanguageServerExecutableResolution by property(false)
    var configurationFile by string(null)
    var runningMode by enum(RunningMode.COMMAND_LINE)
    
    var autoRestartServers by property(true)
    
    var diagnostics by property(true)
    var useEditorFontForTooltips by property(false)
    var prefixTooltipMessages by property(false)
    var linkErrorCodesInTooltips by property(false)
    var taggedHints by property(true)
    
    var hover by property(true)
    
    var completion by property(false)
    var autoImportCompletions by property(true)
    var monkeypatchAutoImportDetails by property(true)
    var autocompleteParentheses by property(false)
    var monkeypatchTrailingQuoteBug by property(true)
    
    var gotoDefinition by property(false)
    
    var autoSearchPaths by property(true)
    var targetedFileExtensions by string(listOf("py", "pyi").join())
    var workspaceFolders by enum(WorkspaceFolders.PROJECT_BASE)
    var diagnosticMode by enum(DiagnosticMode.OPEN_FILES_ONLY)
    
    var logLevel by enum(LogLevel.INFORMATION)
    var locale by enum(Locale.DEFAULT)
}


internal class PyrightOverrides : DisplayableState(), ProjectOverrideState {
    override var names by map<SettingName, Boolean>()
}
