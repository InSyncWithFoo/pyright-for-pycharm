package com.insyncwithfoo.pyright.commandline

import com.insyncwithfoo.pyright.Labeled
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
internal data class Result(
    val version: String,
    val time: String,
    val generalDiagnostics: List<Diagnostic>,
    val summary: OutputSummary
)


@Serializable
internal data class Diagnostic(
    val file: String,
    val severity: DiagnosticSeverity,
    val message: String,
    val rule: String? = null,
    val range: Range
)


internal enum class DiagnosticSeverity(override val label: String) : Labeled {
    @SerialName("error")
    ERROR("Error"),
    @SerialName("warning")
    WARNING("Warning"),
    @SerialName("information")
    INFORMATION("Information");
}


@Serializable
internal data class Range(
    val start: Endpoint,
    val end: Endpoint
)


@Serializable
internal data class Endpoint(
    val line: Int,
    val character: Int
)


@Serializable
internal data class OutputSummary(
    val filesAnalyzed: Int,
    val errorCount: Int,
    val warningCount: Int,
    val informationCount: Int,
    val timeInSec: Float
)
