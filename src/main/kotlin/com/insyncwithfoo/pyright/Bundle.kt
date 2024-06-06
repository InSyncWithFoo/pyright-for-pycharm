package com.insyncwithfoo.pyright

import com.intellij.DynamicBundle
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.PropertyKey


private const val BUNDLE_PATH = "messages.pyright"


private object Bundle : DynamicBundle(BUNDLE_PATH)


@Nls
internal fun message(
    @PropertyKey(resourceBundle = BUNDLE_PATH) key: String,
    vararg params: Any,
) =
    Bundle.getMessage(key, *params)
