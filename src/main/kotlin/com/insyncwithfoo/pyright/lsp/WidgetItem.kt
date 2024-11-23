package com.insyncwithfoo.pyright.lsp

import com.insyncwithfoo.pyright.PyrightIcons
import com.insyncwithfoo.pyright.configurations.PyrightConfigurable
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspServer
import com.intellij.platform.lsp.api.lsWidget.LspServerWidgetItem


@Suppress("UnstableApiUsage")
internal class WidgetItem(lspServer: LspServer, currentFile: VirtualFile?) :
    LspServerWidgetItem(lspServer, currentFile, PyrightIcons.TINY_16_WHITE, PyrightConfigurable::class.java)
