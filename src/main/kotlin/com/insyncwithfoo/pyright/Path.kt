package com.insyncwithfoo.pyright

import java.nio.file.InvalidPathException
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name
import kotlin.io.path.nameWithoutExtension


private val RECOGNIZED_CONFIGURATION_FILENAMES = listOf("pyrightconfig.json", "pyproject.toml")
private val KNOWN_EXECUTABLE_FILENAMES = listOf("pyright", "pyright-python", "basedpyright")


internal fun String.toPathOrNull() =
    try {
        Path.of(this)
    } catch (_: InvalidPathException) {
        null
    }


internal fun String.toPathIfItExists(base: Path? = Path.of("")) =
    this.toPathOrNull()
        ?.let { (base?.resolve(it) ?: it).normalize() }
        ?.takeIf { it.exists() }


internal val Path.isEmpty: Boolean
    get() = this.toString() == ""


internal fun Path.resolvedAgainst(base: Path?) =
    base?.resolve(this) ?: this


internal val Path.isPyrightConfigurationFile: Boolean
    get() = name in RECOGNIZED_CONFIGURATION_FILENAMES


internal fun Path.containsConfigurationFile() =
    listDirectoryEntries().any { it.isPyrightConfigurationFile }


internal val Path.isProbablyPyrightExecutable: Boolean
    get() = nameWithoutExtension in KNOWN_EXECUTABLE_FILENAMES
