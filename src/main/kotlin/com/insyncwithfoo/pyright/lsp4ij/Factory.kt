package com.insyncwithfoo.pyright.lsp4ij


import com.intellij.openapi.project.Project
import com.redhat.devtools.lsp4ij.LanguageServerFactory
import com.redhat.devtools.lsp4ij.client.LanguageClientImpl
import com.redhat.devtools.lsp4ij.server.StreamConnectionProvider
import org.eclipse.lsp4j.services.LanguageServer


internal class PyrightLanguageServerFactory : LanguageServerFactory {
    
    override fun createConnectionProvider(project: Project): StreamConnectionProvider {
        return ConnectionProvider.create(project)
    }
    
    override fun createLanguageClient(project: Project): LanguageClientImpl {
        return Client(project)
    }
    
    override fun getServerInterface(): Class<out LanguageServer> {
        return super.getServerInterface()
    }
    
}


