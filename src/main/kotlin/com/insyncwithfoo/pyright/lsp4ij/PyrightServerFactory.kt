package com.insyncwithfoo.pyright.lsp4ij

import com.insyncwithfoo.pyright.configurations.RunningMode
import com.insyncwithfoo.pyright.configurations.changePyrightConfigurations
import com.insyncwithfoo.pyright.configurations.changePyrightOverrides
import com.insyncwithfoo.pyright.configurations.models.add
import com.insyncwithfoo.pyright.configurations.pyrightConfigurations
import com.insyncwithfoo.pyright.configurations.pyrightExecutable
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.OSAgnosticPathUtil
import com.intellij.openapi.vfs.VirtualFile
import com.redhat.devtools.lsp4ij.LanguageServerEnablementSupport
import com.redhat.devtools.lsp4ij.LanguageServerFactory
import com.redhat.devtools.lsp4ij.client.LanguageClientImpl
import com.redhat.devtools.lsp4ij.client.features.FileUriSupport
import com.redhat.devtools.lsp4ij.client.features.LSPClientFeatures
import com.redhat.devtools.lsp4ij.server.StreamConnectionProvider
import java.net.URI


internal const val SERVER_ID = "com.insyncwithfoo.pyright"


internal class PyrightServerFactory : LanguageServerFactory, LanguageServerEnablementSupport {
    
    override fun isEnabled(project: Project): Boolean {
        val configurations = project.pyrightConfigurations
        val runningModeIsLSP4IJ = configurations.runningMode == RunningMode.LSP4IJ
        val executable = project.pyrightExecutable
        
        return runningModeIsLSP4IJ && executable != null
    }
    
    override fun setEnabled(enabled: Boolean, project: Project) {
        project.changePyrightConfigurations {
            runningMode = when {
                enabled -> RunningMode.LSP4IJ
                else -> RunningMode.COMMAND_LINE
            }
            
            project.changePyrightOverrides { add(::runningMode.name) }
        }
    }
    
    override fun createConnectionProvider(project: Project): StreamConnectionProvider {
        return PyrightServerConnectionProvider.create(project)
    }
    
    override fun createLanguageClient(project: Project): LanguageClientImpl {
        return PyrightServerClient(project)
    }
    
    @Suppress("UnstableApiUsage")
    override fun createClientFeatures() = object : LSPClientFeatures() {
        init {
            hoverFeature = HoverFeature()
            diagnosticFeature = DiagnosticFeature()
            completionFeature = CompletionFeature()
        }
        
        override fun getFileUri(file: VirtualFile): URI {
            val uri = FileUriSupport.DEFAULT.getFileUri(file).toString()
            val prefix = "file:///"
            
            thisLogger().warn(uri)
            thisLogger().warn(OSAgnosticPathUtil.startsWithWindowsDrive(uri.substring(prefix.length)))
            thisLogger().warn(prefix + uri[prefix.length].lowercase() + "%3A" + uri.substring(prefix.length + 2))
            
            if (uri.startsWith(prefix) && OSAgnosticPathUtil.startsWithWindowsDrive(uri.substring(prefix.length))) {
                return URI.create(prefix + uri[prefix.length].lowercase() + "%3A" + uri.substring(prefix.length + 2))
            }
            return URI.create(uri)
        }
    }
    
}
