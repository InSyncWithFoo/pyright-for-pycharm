package com.insyncwithfoo.pyright.commandline

import com.insyncwithfoo.pyright.inspectionProfileManager
import com.insyncwithfoo.pyright.message
import com.intellij.codeInspection.ex.ExternalAnnotatorBatchInspection
import com.intellij.codeInspection.options.OptPane.dropdown
import com.intellij.codeInspection.options.OptPane.group
import com.intellij.codeInspection.options.OptPane.pane
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.project.Project
import com.intellij.profile.codeInspection.InspectionProjectProfileManager
import com.intellij.psi.PsiFile
import com.jetbrains.python.inspections.PyInspection
import kotlin.reflect.KProperty0


private fun highlightSeverityOptions() = listOf(
    HighlightSeverity.ERROR,
    HighlightSeverity.WARNING,
    HighlightSeverity.WEAK_WARNING,
    HighlightSeverity.INFORMATION
)


private fun highlightSeverityDropdown(label: String, property: KProperty0<String>) = dropdown(
    property.name,
    label,
    highlightSeverityOptions(),
    { it.name },
    { it.displayCapitalizedName }
)


internal fun HighlightSeverity(name: String): HighlightSeverity {
    return highlightSeverityOptions().find { it.name == name }!!
}


internal fun PyrightInspection.highlightSeverityFor(severity: DiagnosticSeverity) =
    when (severity) {
        DiagnosticSeverity.ERROR -> HighlightSeverity(highlightSeverityForErrors)
        DiagnosticSeverity.WARNING -> HighlightSeverity(highlightSeverityForWarnings)
        DiagnosticSeverity.INFORMATION -> HighlightSeverity(highlightSeverityForInformation)
    }


internal class PyrightInspection : PyInspection(), ExternalAnnotatorBatchInspection {
    
    var highlightSeverityForErrors = HighlightSeverity.ERROR.name
    var highlightSeverityForWarnings = HighlightSeverity.WARNING.name
    var highlightSeverityForInformation = HighlightSeverity.WEAK_WARNING.name
    
    override fun getShortName() = SHORT_NAME
    
    override fun getOptionsPane() = pane(
        highlightSeveritiesGroup()
    )
    
    private fun highlightSeveritiesGroup() = group(
        message("inspection.severity.label"),
        highlightSeverityDropdown(message("inspection.severity.error"), ::highlightSeverityForErrors),
        highlightSeverityDropdown(message("inspection.severity.warning"), ::highlightSeverityForWarnings),
        highlightSeverityDropdown(message("inspection.severity.information"), ::highlightSeverityForInformation)
    )
    
    companion object {
        const val SHORT_NAME = "com.insyncwithfoo.pyright.commandline.PyrightInspection"
    }
    
}


internal val PsiFile.pyrightInspection: PyrightInspection
    get() {
        val profile = project.inspectionProfileManager.currentProfile
        val entry = profile.getUnwrappedTool(PyrightInspection.SHORT_NAME, this)
        
        return entry as PyrightInspection
    }


internal val Project.pyrightInspection: PyrightInspection
    get() {
        val inspectionManager = InspectionProjectProfileManager.getInstance(this)
        val profile = inspectionManager.currentProfile
        
        return profile.getInspectionTool(PyrightInspection.SHORT_NAME, this)!!.tool as PyrightInspection
    }


internal var Project.pyrightInspectionisEnabled: Boolean
    @Deprecated("The getter must not be used.", level = DeprecationLevel.ERROR)
    get() = throw RuntimeException()
    set(enabled) {
        val profile = inspectionProfileManager.currentProfile
        val toolState = profile.allTools.find { it.tool.shortName == PyrightInspection.SHORT_NAME }
        
        toolState?.isEnabled = enabled
        profile.profileChanged()
    }
