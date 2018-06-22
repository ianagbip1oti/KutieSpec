package com.github.princesslana.kutiespec

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner
import mu.KLogging
import org.junit.platform.engine.EngineDiscoveryRequest
import org.junit.platform.engine.ExecutionRequest
import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.TestEngine
import org.junit.platform.engine.TestExecutionResult
import org.junit.platform.engine.UniqueId

class KutieSpecEngine : TestEngine {

    companion object : KLogging()

    override fun getId(): String {
        return "com.github.princesslana.kutiespec"
    }

    override fun discover(discoveryRequest: EngineDiscoveryRequest, id: UniqueId): TestDescriptor {
        val specs = HashSet<Class<out KutieSpec>>()

        FastClasspathScanner()
            .matchSubclassesOf(KutieSpec::class.java) { c -> specs.add(c) }
            .scan()

        logger.info("Discovered ${specs.size} spec(s)")

        return toDescriptor(id, specs)
    }

    override fun execute(req: ExecutionRequest) {
        req.getEngineExecutionListener().executionStarted(req.getRootTestDescriptor())

        req.getRootTestDescriptor()
            .getChildren()
            .map { c -> c as KutieSpecDescriptor }
            .map(KutieSpecDescriptor::spec)
            .forEach { s -> s.execute(req.getRootTestDescriptor().getUniqueId(), req.getEngineExecutionListener()) }

        req.getEngineExecutionListener()
            .executionFinished(req.getRootTestDescriptor(), TestExecutionResult.successful())
    }
}
