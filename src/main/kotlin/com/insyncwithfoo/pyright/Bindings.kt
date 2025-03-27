package com.insyncwithfoo.pyright

import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.JBIntSpinner
import com.intellij.ui.TextAccessor
import com.intellij.ui.dsl.builder.ButtonsGroup
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.MutableProperty
import com.intellij.ui.dsl.builder.bindIntValue
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.toMutableProperty
import com.intellij.ui.dsl.builder.toNonNullableProperty
import com.intellij.ui.dsl.builder.toNullableProperty
import javax.swing.JComponent
import kotlin.reflect.KMutableProperty0


private fun <T> KMutableProperty0<T?>.toNonNullableProperty(getDefaultValue: () -> T): MutableProperty<T> {
    return MutableProperty({ get() ?: getDefaultValue() }, { set(it) })
}


internal fun <C : TextFieldWithBrowseButton> Cell<C>.bindText(
    property: KMutableProperty0<String?>,
    getDefaultValue: () -> String
): Cell<C> {
    return bindText(property.toNonNullableProperty(getDefaultValue))
}


internal fun <C> Cell<C>.bindText(property: KMutableProperty0<String?>) where C : JComponent, C : TextAccessor =
    bind(TextAccessor::getText, TextAccessor::setText, property.toNonNullableProperty(""))


internal fun <C : ComboBox<T>, T : Any> Cell<C>.bindItem(property: KMutableProperty0<T>): Cell<C> {
    return bindItem(property.toNullableProperty())
}


internal inline fun <reified T> ButtonsGroup.bindSelected(property: KMutableProperty0<T>) {
    bind(property.toMutableProperty(), T::class.java)
}


internal fun <C : JBIntSpinner, K> Cell<C>.bindIntValue(map: MutableMap<K, Int>, key: K, defaultValue: Int) {
    bindIntValue({ map[key] ?: defaultValue }, { map[key] = it })
}
