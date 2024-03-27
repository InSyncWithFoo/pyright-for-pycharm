package com.insyncwithfoo.pyright.runner

import com.insyncwithfoo.pyright.PyrightOutput
import com.intellij.execution.RunCanceledByUserException
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@Serializable
private data class ExceptionInfo(
    val type: String,
    val stdout: String,
    val stderr: String,
    val message: String
)


private fun ExceptionInfo(exception: PyrightException): ExceptionInfo {
    return ExceptionInfo(
        exception::class.simpleName!!,
        exception.stdout,
        exception.stderr,
        exception.message!!
    )
}


internal class PyrightRunner(project: Project) {
    
    private val notifier = Notifier(project)
    
    fun run(command: PyrightCommand): PyrightOutput? {
        val serializedCommand = Json.encodeToString(command)
        LOGGER.info("Running: $serializedCommand")
        
        return try {
            runAndLogOutput(command)
        } catch (_: RunCanceledByUserException) {
            null
        } catch (exception: PyrightException) {
            report(exception)
            
            when (exception) {
                is InvalidConfigurationsException -> parseOutput(exception.stdout)
                is FatalException, is InvalidParametersException -> null
            }
        }
    }
    
    private fun runAndLogOutput(command: PyrightCommand): PyrightOutput {
        val output = command.run()
        val parsed = parseOutput(output).also { logMinified(it) }
        
        return parsed
    }
    
    private fun logMinified(parsedOutput: PyrightOutput) {
        val minified = Json.encodeToString(parsedOutput)
        LOGGER.info("Output: $minified")
    }
    
    private fun parseOutput(response: String) =
        Json.decodeFromString<PyrightOutput>(response)
    
    private fun report(exception: PyrightException) {
        val exceptionInfo = Json.encodeToString(ExceptionInfo(exception))
        
        LOGGER.warn(exception)
        LOGGER.info("Exception properties: $exceptionInfo")
        
        notifier.notify(exception)
    }
    
    companion object {
        private val LOGGER = Logger.getInstance(PyrightRunner::class.java)
    }
    
}
