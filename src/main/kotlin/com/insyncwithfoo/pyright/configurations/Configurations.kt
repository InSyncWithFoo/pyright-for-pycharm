package com.insyncwithfoo.pyright.configurations

import com.insyncwithfoo.pyright.Labeled
import com.insyncwithfoo.pyright.configurations.models.Copyable
import com.insyncwithfoo.pyright.configurations.models.DisplayableState
import com.insyncwithfoo.pyright.configurations.models.ProjectOverrideState
import com.insyncwithfoo.pyright.configurations.models.SettingName
import com.insyncwithfoo.pyright.message


internal enum class RunningMode(override val label: String) : Labeled {
    COMMAND_LINE(message("configurations.runningMode.commandLine")),
    LSP4IJ(message("configurations.runningMode.lsp4ij")),
    LSP(message("configurations.runningMode.lsp"));
}


// TODO: Fix this
internal class PyrightConfigurations : DisplayableState(), Copyable {
    var executable by string(null)
    var smartExecutableResolution by property(false)
    var languageServerExecutable by string(null)
    var smartLanguageServerExecutableResolution by property(false)
    var configurationFile by string(null)
    var runningMode by enum(RunningMode.COMMAND_LINE)
}


internal class PyrightOverrides : DisplayableState(), ProjectOverrideState {
    override var names by map<SettingName, Boolean>()
}
