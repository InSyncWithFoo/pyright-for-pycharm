package com.insyncwithfoo.pyright.configurations

import com.insyncwithfoo.pyright.configurations.models.Copyable
import com.insyncwithfoo.pyright.configurations.models.DisplayableState
import com.insyncwithfoo.pyright.configurations.models.ProjectOverrideState
import com.insyncwithfoo.pyright.configurations.models.SettingName


// TODO: Fix this
internal class PyrightConfigurations : DisplayableState(), Copyable {
    var executable by string(null)
    var languageServerExecutable by string(null)
    var configurationFile by string(null)
}


internal class PyrightOverrides : DisplayableState(), ProjectOverrideState {
    override var names by map<SettingName, Boolean>()
}
