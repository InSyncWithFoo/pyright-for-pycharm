package com.insyncwithfoo.pyright.configuration

import com.insyncwithfoo.pyright.isEmpty
import com.insyncwithfoo.pyright.toPathOrNull
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.RightGap
import com.intellij.ui.dsl.builder.Row
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.text
import java.nio.file.Path
import javax.swing.JComponent
import kotlin.reflect.KMutableProperty0


internal const val NO_LABEL = ""


private fun randomPlaceholder(): String {
    val hexDigits = ('0'..'9') + ('A'..'F')
    return (1..40).map { hexDigits.random() }.joinToString("")
}


internal fun <T : JComponent> Cell<T>.ensureComment() = this.apply {
    comment(NO_LABEL)
    // Whether this actually does anything noticeable is a subject of wonder.
    comment!!.maximumSize = component.size
}


internal fun Row.secondColumnPathInput() = textFieldWithBrowseButton().apply {
    ensureComment()
    
    gap(RightGap.SMALL)
    align(AlignX.FILL)
    resizableColumn()
}


internal fun <T : TextFieldWithBrowseButton> Cell<T>.bindText(property: KMutableProperty0<String?>) =
    bindText({ property.get().orEmpty() }, property::set)


internal fun <T : TextFieldWithBrowseButton> Cell<T>.onInput(block: Cell<T>.(String) -> Unit) {
    onChanged { _ -> block(component.text) }
    onApply { block(component.text) }
}


internal fun <T : TextFieldWithBrowseButton> Cell<T>.onInput(
    callbackMaker: ((Path) -> Hint) -> Cell<T>.(String) -> Unit,
    block: (Path) -> Hint
) {
    onInput(callbackMaker(block))
}


// onChanged() callbacks don't get called when the new value is the same,
// which potentially prevents the listener(s) from being called on panel opening.
internal fun <T : TextFieldWithBrowseButton> Cell<T>.prefilledWithRandomPlaceholder() =
    text(randomPlaceholder())


internal fun <T : TextFieldWithBrowseButton> displayPathHint(
    validPathHintMaker: (Path) -> Hint
): Cell<T>.(String) -> Unit {
    fun Cell<T>.callback(input: String) {
        val path = input.toPathOrNull()
        val hint = when {
            path == null -> invalidPathHint()
            path.isEmpty -> emptyPathHint()
            else -> validPathHintMaker(path)
        }
        
        comment!!.text = hint.toString()
    }
    
    return Cell<T>::callback
}
