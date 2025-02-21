package com.insyncwithfoo.pyright.lsp4ij

import com.insyncwithfoo.pyright.configurations.pyrightConfigurations
import com.intellij.psi.PsiFile
import com.redhat.devtools.lsp4ij.client.features.LSPHoverFeature


@Suppress("UnstableApiUsage")
internal class HoverFeature : LSPHoverFeature() {
    
    private val configurations by lazy {
        project.pyrightConfigurations
    }
    
    override fun isEnabled(file: PsiFile) =
        configurations.hover
    
}
