package com.insyncwithfoo.pyright.configuration

import com.insyncwithfoo.pyright.containsConfigurationFile
import com.insyncwithfoo.pyright.isProbablyPyrightExecutable
import com.insyncwithfoo.pyright.isProbablyPyrightLSExecutable
import com.insyncwithfoo.pyright.isPyrightConfigurationFile
import com.insyncwithfoo.pyright.message
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.isDirectory


internal enum class HintIcon {
    Success, Info, Warning, Error;
    
    // TODO: Re-add icons
    override fun toString() = ""
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


internal val emptyPathHint: Hint
    get() = Hint.info(message("configurations.hint.noPathSpecified"))


internal val invalidPathHint: Hint
    get() = Hint.error(message("configurations.hint.invalidPath"))


internal fun executablePathResolvingHint(path: Path) = when {
    !path.exists() ->
        Hint.warning(message("configurations.hint.fileNotFound"))
    path.isDirectory() ->
        Hint.error(message("configurations.hint.unexpectedDirectory"))
    !path.isProbablyPyrightExecutable ->
        Hint.info(message("configurations.hint.unknownExecutable"))
    else ->
        Hint.success(message("configurations.hint.fileFound"))
}


internal fun langserverExecutablePathResolvingHint(path: Path) = when {
    !path.exists() ->
        Hint.warning(message("configurations.hint.fileNotFound"))
    path.isDirectory() ->
        Hint.error(message("configurations.hint.unexpectedDirectory"))
    !path.isProbablyPyrightLSExecutable ->
        Hint.info(message("configurations.hint.unknownExecutable"))
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
            Hint.error(message("configurations.hint.unrecognizedConfigurationFile"))
        else ->
            Hint.success(message("configurations.hint.fileFound"))
    }
}
