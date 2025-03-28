package com.insyncwithfoo.pyright.misc

import com.insyncwithfoo.pyright.configurations.pyrightConfigurations
import com.insyncwithfoo.pyright.isPyrightConfig
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.jetbrains.jsonSchema.extension.JsonSchemaFileProvider
import com.jetbrains.jsonSchema.extension.JsonSchemaProviderFactory
import com.jetbrains.jsonSchema.extension.SchemaType


private val virtualFileManager: VirtualFileManager
    get() = VirtualFileManager.getInstance()


private class PyrightConfigSchemaProvider : JsonSchemaFileProvider {
    
    override fun getName() = "partial-pyright.json"
    
    override fun getSchemaType() = SchemaType.remoteSchema
    
    override fun isAvailable(file: VirtualFile) =
        file.isPyrightConfig
    
    override fun getSchemaFile() =
        virtualFileManager.findFileByUrl(URL)
    
    companion object {
        const val URL = "https://json.schemastore.org/partial-pyright.json"
    }
    
}


internal class PyrightConfigSchemaProviderFactory : JsonSchemaProviderFactory, DumbAware {
    
    override fun getProviders(project: Project): List<JsonSchemaFileProvider> =
        when (project.pyrightConfigurations.useSchemaFromStore) {
            true -> listOf(PyrightConfigSchemaProvider())
            else -> emptyList()
        }
    
}
