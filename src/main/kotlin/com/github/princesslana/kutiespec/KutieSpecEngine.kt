package com.github.princesslana.kutiespec

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner
import mu.KLogging
import org.junit.platform.engine.EngineDiscoveryRequest
import org.junit.platform.engine.ExecutionRequest
import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.TestEngine
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor
import org.junit.platform.engine.support.descriptor.EngineDescriptor

class KutieSpecEngine : TestEngine {

    companion object : KLogging()

    override fun getId(): String {
        return "com.github.princesslana.kutiespec"
    }

    override fun discover(discoveryRequest: EngineDiscoveryRequest, id: UniqueId): TestDescriptor {
        val engine = EngineDescriptor(id, "KutieSpec")

        val specs = HashSet<Class<out KutieSpec>>()

        FastClasspathScanner()
            .matchSubclassesOf(KutieSpec::class.java) { c -> specs.add(c) }
            .scan()

        logger.info("Discovered ${specs.size} spec(s)")

        specs.map { s -> KutieSpecDescriptor(id, s) }.forEach(engine::addChild)

        return engine
    }

    override fun execute(executionRequest: ExecutionRequest) {
        logger.info("executionRequest: $executionRequest")
    }
}

class KutieSpecDescriptor(parentId: UniqueId, clz: Class<out KutieSpec>)
    : AbstractTestDescriptor(parentId.append("spec", clz.getName()), clz.getSimpleName()) {

    override fun getType(): TestDescriptor.Type {
        return TestDescriptor.Type.CONTAINER
    }
}