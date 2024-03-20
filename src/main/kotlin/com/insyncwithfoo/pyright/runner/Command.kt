package com.insyncwithfoo.pyright.runner

import com.insyncwithfoo.pyright.configuration.PyrightAllConfigurations
import com.insyncwithfoo.pyright.path
import com.insyncwithfoo.pyright.sdkPath
import com.intellij.execution.RunCanceledByUserException
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessHandler
import com.intellij.execution.process.ProcessOutput
import com.intellij.openapi.progress.ProgressManager
import com.intellij.psi.PsiFile
import com.jetbrains.python.packaging.IndicatedProcessOutputListener
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.nio.file.Path
import kotlin.io.path.exists


private fun String.toPathIfItExists(base: String = "") =
    this.toPathIfItExists(Path.of(base))


private fun String.toPathIfItExists(base: Path) =
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
data class PyrightCommand(
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
    
    private val handler: CapturingProcessHandler
        get() = CapturingProcessHandler(getCommandLine())
    
    private fun getCommandLine() =
        GeneralCommandLine(fragments)
            .withWorkDirectory(projectPath)
            .withCharset(Charsets.UTF_8)
    
    private fun runWithIndicator(): ProcessOutput {
        val indicator = ProgressManager.getInstance().progressIndicator
        
        return with(handler) {
            if (indicator != null) {
                addProcessListener(IndicatedProcessOutputListener(indicator))
                runProcessWithProgressIndicator(indicator)
            } else {
                runProcess()
            }
        }
    }
    
    fun run(): String {
        val processOutput = runWithIndicator()
        
        return processOutput.run {
            if (isCancelled) {
                throw RunCanceledByUserException()
            }
            
            when (PyrightExitCode.fromInt(exitCode)) {
                PyrightExitCode.FATAL -> throw FatalException(stdout, stderr)
                PyrightExitCode.INVALID_CONFIG -> throw InvalidConfigurationsException(stdout, stderr)
                PyrightExitCode.INVALID_PARAMETERS -> throw InvalidParametersException(stdout, stderr)
                else -> stdout
            }
        }
    }
    
    companion object {
        fun create(
            configurations: PyrightAllConfigurations,
            file: PsiFile
        ): PyrightCommand? {
            val project = file.project
            val projectPath = (project.path ?: return null).toString()
            
            val executable = configurations.executable?.toPathIfItExists(base = projectPath) ?: return null
            val target = file.virtualFile.path.toPathIfItExists() ?: return null
            val configurationFile = configurations.configurationFile
            
            val pythonExecutable = project.sdkPath ?: return null
            
            val extraArguments: List<String> = listOf(
                "--outputjson",
                "--project", configurationFile ?: projectPath,
                "--pythonpath", pythonExecutable.toString()
            )
            
            return PyrightCommand(executable, target, projectPath, extraArguments)
        }
    }
    
}
