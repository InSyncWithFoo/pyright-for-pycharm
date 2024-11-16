package com.insyncwithfoo.pyright

import com.intellij.execution.process.ProcessOutput
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@Serializable
internal class ProcessOutputSurrogate(
    val stdout: String,
    val stderr: String,
    val exitCode: Int,
    val isTimeout: Boolean,
    val isCancelled: Boolean
) {
    override fun toString() = Json.encodeToString(this)
}


internal fun ProcessOutputSurrogate(processOutput: ProcessOutput) = with(processOutput) {
    ProcessOutputSurrogate(stdout, stderr, exitCode, isTimeout, isCancelled)
}
