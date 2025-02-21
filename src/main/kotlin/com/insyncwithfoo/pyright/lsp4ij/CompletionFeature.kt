package com.insyncwithfoo.pyright.lsp4ij

import com.insyncwithfoo.pyright.configurations.pyrightConfigurations
import com.insyncwithfoo.pyright.shared.completeWithParentheses
import com.insyncwithfoo.pyright.shared.isAutoImportCompletion
import com.insyncwithfoo.pyright.shared.isCallable
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.PsiFile
import com.redhat.devtools.lsp4ij.client.features.LSPCompletionFeature
import org.eclipse.lsp4j.CompletionItem


@Suppress("UnstableApiUsage")
internal class CompletionFeature : LSPCompletionFeature() {
    
    private val configurations by lazy {
        project.pyrightConfigurations
    }
    
    override fun isEnabled(file: PsiFile) =
        configurations.completion
    
    override fun getTypeText(item: CompletionItem): String? {
        if (item.isAutoImportCompletion && configurations.monkeypatchAutoImportDetails) {
            return item.labelDetails?.description
        }
        
        return super.getTailText(item)
    }
    
    override fun createLookupElement(item: CompletionItem, context: LSPCompletionContext): LookupElement? {
        // FIXME: Find a better method to put this in
        if (item.isCallable && configurations.autocompleteParentheses) {
            item.completeWithParentheses()
        }
        
        return super.createLookupElement(item, context)
    }
    
}
