package com.insyncwithfoo.pyright.configuration

import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import com.insyncwithfoo.pyright.configuration.application.Configurations as ApplicationConfigurations
import com.insyncwithfoo.pyright.configuration.application.RunningMode as ApplicationRunningMode
import com.insyncwithfoo.pyright.configuration.project.Configurations as ProjectConfigurations
import com.insyncwithfoo.pyright.configuration.project.RunningMode as ProjectRunningMode


private fun <T> Collection<KProperty1<T, *>>.makeAccessible() =
    this.map { it.apply { isAccessible = true } }


internal fun applicationFields() =
    ApplicationConfigurations::class.declaredMemberProperties.makeAccessible().associateBy { it.name }


internal fun projectFields() =
    ProjectConfigurations::class.declaredMemberProperties.makeAccessible().associateBy { it.name }


internal fun allFields() =
    AllConfigurations::class.primaryConstructor!!.parameters.associateBy { it.name }


internal fun applicationRunningModes() =
    ApplicationRunningMode.entries.associateBy { it.name }


internal fun projectRunningModes() =
    ProjectRunningMode.entries.associateBy { it.name }
