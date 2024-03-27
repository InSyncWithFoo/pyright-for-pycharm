package com.insyncwithfoo.pyright.configuration

import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import com.insyncwithfoo.pyright.configuration.application.Configurations as ApplicationConfigurations
import com.insyncwithfoo.pyright.configuration.project.Configurations as ProjectConfigurations


private fun <T> Collection<KProperty1<T, *>>.makeAccessible() =
    this.map { it.apply { isAccessible = true } }


internal fun applicationFields() =
    ApplicationConfigurations::class.declaredMemberProperties.makeAccessible().associateBy { it.name }


internal fun projectFields() =
    ProjectConfigurations::class.declaredMemberProperties.makeAccessible().associateBy { it.name }


internal fun allFields() =
    AllConfigurations::class.primaryConstructor!!.parameters.associateBy { it.name }
