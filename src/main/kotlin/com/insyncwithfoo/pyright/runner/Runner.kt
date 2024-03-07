package com.insyncwithfoo.pyright.runner

import com.insyncwithfoo.pyright.PyrightOutput
import com.intellij.execution.RunCanceledByUserException
import com.intellij.openapi.diagnostic.Logger
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class PyrightRunner(private val command: PyrightCommand) {
    
    fun run(): PyrightOutput? {
        val serializedCommand = Json.encodeToString(command)
        LOGGER.info("Running: $serializedCommand")
        
        return try {
            runAndLogOutput()
        } catch (_: RunCanceledByUserException) {
            null
        } catch (exception: FatalException) {
            ERROR_REPORTER.reportException(exception)
            null
        } catch (exception: InvalidParametersException) {
            ERROR_REPORTER.reportException(exception)
            null
        }
    }
    
    private fun runAndLogOutput(): PyrightOutput {
        val output = command.run()
        val parsed = parseOutput(output).also { logOutput(it) }
        
        return parsed
    }
    
    private fun logOutput(parsedOutput: PyrightOutput) {
        val minified = Json.encodeToString(parsedOutput)
        LOGGER.info("Output: $minified")
    }
    
    private fun parseOutput(response: String) =
        Json.decodeFromString<PyrightOutput>(response)
    
    companion object {
        private val LOGGER = Logger.getInstance(PyrightRunner::class.java)
        private val ERROR_REPORTER = PyrightErrorReporter(LOGGER)
    }
    
}
