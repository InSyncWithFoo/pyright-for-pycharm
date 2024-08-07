package com.insyncwithfoo.pyright.configuration

import com.insyncwithfoo.pyright.isEmpty
import com.insyncwithfoo.pyright.toPathOrNull
import com.intellij.openapi.observable.properties.ObservableMutableProperty
import com.intellij.openapi.observable.properties.PropertyGraph
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.RightGap
import com.intellij.ui.dsl.builder.Row
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.text
import java.nio.file.Path
import javax.swing.JComponent


private fun randomPlaceholder(): String {
    val hexDigits = ('0'..'9') + ('A'..'F')
    return (1..40).map { hexDigits.random() }.joinToString("")
}


private fun makePathHint(input: String, makeValidPathHint: (Path) -> Hint): Hint {
    val path = input.toPathOrNull()
    
    return when {
        path == null -> invalidPathHint
        path.isEmpty -> emptyPathHint
        else -> makeValidPathHint(path)
    }
}


internal fun <T : JComponent> Cell<T>.makeFlexible() = this.apply {
    gap(RightGap.SMALL)
    align(AlignX.FILL)
    resizableColumn()
}


internal fun <T : TextFieldWithBrowseButton> Cell<T>.triggerChange() {
    val originalContent = component.text
    
    text(randomPlaceholder())
    text(originalContent)
}


internal fun Row.reactiveLabel(property: ObservableMutableProperty<String>) =
    comment("").bindText(property)


internal class PathHintState(private val makeValidPathHint: (Path) -> Hint) {
    
    private val propertyGraph = PropertyGraph()
    
    val path = propertyGraph.property("")
    val hint = propertyGraph.property("").apply {
        dependsOn(path) { makePathHint(path.get(), makeValidPathHint).toString() }
    }
    
}
