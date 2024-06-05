package com.insyncwithfoo.pyright.lsp4ij

import com.intellij.openapi.project.Project
import com.redhat.devtools.lsp4ij.client.LanguageClientImpl


internal class Client(project: Project) : LanguageClientImpl(project)
