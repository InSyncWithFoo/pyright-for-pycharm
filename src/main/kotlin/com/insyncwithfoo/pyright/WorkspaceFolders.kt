package com.insyncwithfoo.pyright

import com.insyncwithfoo.pyright.configurations.WorkspaceFolders
import com.insyncwithfoo.pyright.configurations.pyrightConfigurations
import com.insyncwithfoo.pyright.configurations.resolveConfigurationFileWorkspaceRoot
import com.intellij.openapi.project.BaseProjectDirectories.Companion.getBaseDirectories
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.toNioPathOrNull


private fun Project.getModuleSourceRoots(): Collection<VirtualFile> =
    modules.flatMap { module ->
        ModuleRootManager.getInstance(module).sourceRoots.asIterable()
    }


private fun Project.getWorkspaceFolders(type: WorkspaceFolders): Collection<VirtualFile> =
    when (type) {
        WorkspaceFolders.PROJECT_BASE -> getBaseDirectories()
        WorkspaceFolders.SOURCE_ROOTS -> getModuleSourceRoots()
    }


internal fun Project.getPyrightWorkspaceFolders(): Collection<VirtualFile> {
    val configRoot = resolveConfigurationFileWorkspaceRoot()
    
    if (configRoot != null) {
        LocalFileSystem.getInstance().findFileByNioFile(configRoot)?.let { return listOf(it) }
    }
    
    return getWorkspaceFolders(pyrightConfigurations.workspaceFolders)
}


internal fun VirtualFile.toFileUriString(): String =
    toNioPathOrNull()?.toUri()?.toASCIIString() ?: url
