package com.insyncwithfoo.pyright

import java.nio.file.InvalidPathException
import java.nio.file.Path
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name


private val RECOGNIZED_CONFIGURATION_FILE_NAMES = listOf("pyrightconfig.json", "pyproject.toml")


internal fun String.toPathOrNull() =
    try {
        Path.of(this)
    } catch (_: InvalidPathException) {
        null
    }


internal val Path.isEmpty: Boolean
    get() = this.toString() == ""


internal fun Path.resolvedAgainst(base: Path?) =
    base?.resolve(this) ?: this


internal val Path.isPyrightConfigurationFile: Boolean
    get() = name in RECOGNIZED_CONFIGURATION_FILE_NAMES


internal fun Path.containsConfigurationFile() =
    listDirectoryEntries().any { it.isPyrightConfigurationFile }
