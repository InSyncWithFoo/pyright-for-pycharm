package com.insyncwithfoo.pyright

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
internal data class PyrightOutput(
    val version: String,
    val time: String,
    val generalDiagnostics: List<PyrightDiagnostic>,
    val summary: PyrightOutputSummary
)


@Serializable
internal data class PyrightDiagnostic(
    val file: String,
    val severity: PyrightDiagnosticSeverity,
    val message: String,
    val rule: String? = null,
    val range: PyrightDiagnosticTextRange
)


internal enum class PyrightDiagnosticSeverity {
    @SerialName("error") ERROR,
    @SerialName("warning") WARNING,
    @SerialName("information") INFORMATION
}


@Serializable
internal data class PyrightDiagnosticTextRange(
    val start: PyrightDiagnosticTextRangeEndpoint,
    val end: PyrightDiagnosticTextRangeEndpoint
)


@Serializable
internal data class PyrightDiagnosticTextRangeEndpoint(
    val line: Int,
    val character: Int
)


@Serializable
internal data class PyrightOutputSummary(
    val filesAnalyzed: Int,
    val errorCount: Int,
    val warningCount: Int,
    val informationCount: Int,
    val timeInSec: Float
)
