package com.insyncwithfoo.pyright.runner


internal enum class PyrightExitCode(val value: Int) {
    SUCCESS(0),
    ERRORS(1),
    FATAL(2),
    INVALID_CONFIG(3),
    INVALID_PARAMETERS(4);
    
    companion object {
        fun fromInt(value: Int) = entries.firstOrNull { it.value == value }
    }
}
