package com.insyncwithfoo.pyright

import com.intellij.DynamicBundle
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.PropertyKey


private const val BUNDLE_PATH = "messages.pyright"


private object PyrightBundle : DynamicBundle(BUNDLE_PATH)


@Nls
internal fun message(
    @PropertyKey(resourceBundle = BUNDLE_PATH) key: String,
    vararg params: Any,
) =
    PyrightBundle.getMessage(key, *params)
