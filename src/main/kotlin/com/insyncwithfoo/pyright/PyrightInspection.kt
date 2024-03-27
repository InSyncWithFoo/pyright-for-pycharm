package com.insyncwithfoo.pyright

import com.intellij.codeInspection.ex.ExternalAnnotatorBatchInspection
import com.jetbrains.python.inspections.PyInspection


internal class PyrightInspection : PyInspection(), ExternalAnnotatorBatchInspection {
    override fun getShortName() = SHORT_NAME
    
    companion object {
        const val SHORT_NAME = "PyrightInspection"
    }
}
