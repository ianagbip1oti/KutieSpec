package com.github.princesslana.kutiespec

import mu.KotlinLogging
import org.junit.platform.engine.EngineExecutionListener

private val logger = KotlinLogging.logger {}

fun execute(example: Example, listener: EngineExecutionListener): Unit {
    when(example) {
        is Group -> {
            logger.info("Executing ${example.name}...")
            example.examples.forEach {e -> execute(e, listener) }   
        }
        is Single -> {
            logger.info("Executing ${example.name ?: "unnamed example"}")
            example.execute()
        }
    }
}