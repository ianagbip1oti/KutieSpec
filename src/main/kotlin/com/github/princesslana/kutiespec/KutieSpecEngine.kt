package com.github.princesslana.kutiespec

import mu.KLogging
import org.junit.platform.engine.EngineDiscoveryRequest
import org.junit.platform.engine.ExecutionRequest
import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.TestEngine
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.support.descriptor.EngineDescriptor

class KutieSpecEngine : TestEngine {

    companion object : KLogging()

    override fun getId(): String {
        return "com.github.princesslana.kutiespec"
    }

    override fun discover(discoveryRequest: EngineDiscoveryRequest, id: UniqueId): TestDescriptor {
        return EngineDescriptor(id, "KutieSpec")
    }

    override fun execute(executionRequest: ExecutionRequest) {
        logger.info("executionRequest: $executionRequest")
    }
}