package com.insyncwithfoo.pyright.configuration

import com.insyncwithfoo.pyright.containsConfigurationFile
import com.insyncwithfoo.pyright.isPyrightConfigurationFile
import com.insyncwithfoo.pyright.message
import com.intellij.icons.ExpUiIcons
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.isDirectory


internal enum class HintIcon {
    Success, Info, Warning, Error;
    
    override fun toString() = """<icon src="${ExpUiIcons.Status::class.qualifiedName}.$name">"""
}


internal data class Hint(
    val icon: HintIcon,
    val text: String
) {
    
    override fun toString() = "$icon&nbsp;$text"
    
    companion object {
        fun success(text: String) = Hint(HintIcon.Success, text)
        fun info(text: String) = Hint(HintIcon.Info, text)
        fun warning(text: String) = Hint(HintIcon.Warning, text)
        fun error(text: String) = Hint(HintIcon.Error, text)
    }
    
}


internal fun emptyPathHint() = Hint.info(message("configurations.hint.noPathSpecified"))


internal fun invalidPathHint() =
    Hint.error(message("configurations.hint.invalidPath"))


internal fun executablePathResolvingHint(path: Path) = when {
    !path.exists() ->
        Hint.warning(message("configurations.hint.fileNotFound"))
    path.isDirectory() ->
        Hint.error(message("configurations.hint.unexpectedDirectory"))
    // Uncomment the following if it is asked for.
    // !path.isExecutable() ->
    //     Hint.warning(message("configurations.hint.fileNotExecutable"))
    else ->
        Hint.success(message("configurations.hint.fileFound"))
}


internal fun configurationFilePathResolvingHint(path: Path): Hint {
    return when {
        !path.exists() ->
            Hint.warning(message("configurations.hint.fileNotFound"))
        path.isDirectory() -> when {
            !path.containsConfigurationFile() ->
                Hint.warning(message("configurations.hint.noRecognizedFileFound"))
            else ->
                Hint.success(message("configurations.hint.recognizedFileFound"))
        }
        !path.isPyrightConfigurationFile ->
            Hint.error(message("configurations.hint.unrecognizedFile"))
        else ->
            Hint.success(message("configurations.hint.fileFound"))
    }
}
