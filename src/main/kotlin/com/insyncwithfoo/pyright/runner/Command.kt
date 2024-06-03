package com.insyncwithfoo.pyright.runner

import com.insyncwithfoo.pyright.PyrightDiagnosticSeverity
import com.insyncwithfoo.pyright.configuration.AllConfigurations
import com.insyncwithfoo.pyright.path
import com.insyncwithfoo.pyright.pyrightConfigurations
import com.insyncwithfoo.pyright.sdkPath
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessHandler
import com.intellij.execution.process.ProcessOutput
import com.intellij.psi.PsiFile
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.nio.file.Path
import kotlin.io.path.exists


private val GeneralCommandLine.handler: CapturingProcessHandler
    get() = CapturingProcessHandler(this)


private fun String.toPathIfItExists(base: Path = Path.of("")) =
    Path.of(this)
        .let { base.resolve(it).normalize() }
        .takeIf { it.exists() }


private object PathSerializer : KSerializer<Path> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Path", PrimitiveKind.STRING)
    
    override fun serialize(encoder: Encoder, value: Path) {
        encoder.encodeString(value.toString())
    }
    
    override fun deserialize(decoder: Decoder): Path {
        throw NotImplementedError("The deserializer should not be used")
    }
}


@Serializable
internal data class PyrightCommand(
    @Serializable(with = PathSerializer::class)
    val executable: Path,
    @Serializable(with = PathSerializer::class)
    val target: Path,
    val projectPath: String,
    val extraArguments: List<String>
) {
    
    private val fragments: List<String>
        get() = listOf(
            executable.toString(),
            *extraArguments.toTypedArray(),
            target.toString()
        )
    
    private val commandLine: GeneralCommandLine
        get() = GeneralCommandLine(fragments).apply {
            withWorkDirectory(projectPath)
            withCharset(Charsets.UTF_8)
        }
    
    fun run(timeoutInMilliseconds: Int): ProcessOutput =
        commandLine.handler.runProcess(timeoutInMilliseconds)
    
    companion object {
        
        internal fun create(
            configurations: AllConfigurations,
            executable: Path,
            target: Path,
            projectPath: Path,
            interpreterPath: Path
        ): PyrightCommand {
            val configurationFile = configurations.configurationFile
            
            val argumentForProject = configurationFile ?: projectPath
            val extraArguments: MutableList<String> = mutableListOf(
                "--outputjson",
                "--project", argumentForProject.toString(),
                "--pythonpath", interpreterPath.toString()
            )
            
            if (configurations.minimumSeverityLevel != PyrightDiagnosticSeverity.INFORMATION) {
                extraArguments.add("--level")
                extraArguments.add(configurations.minimumSeverityLevel.name)
            }
            
            return PyrightCommand(executable, target, projectPath.toString(), extraArguments)
        }
        
        fun create(file: PsiFile): PyrightCommand? {
            val project = file.project
            
            val configurations = project.pyrightConfigurations
            val filePath = file.virtualFile.path.toPathIfItExists() ?: return null
            val projectPath = project.path ?: return null
            val executable = configurations.executable?.toPathIfItExists(base = projectPath) ?: return null
            val interpreterPath = project.sdkPath ?: return null
            
            return create(configurations, executable, filePath, projectPath, interpreterPath)
        }
        
    }
    
}
