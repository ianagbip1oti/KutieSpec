package com.github.princesslana.kutiespec

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner
import mu.KLogging
import org.junit.platform.engine.EngineDiscoveryRequest
import org.junit.platform.engine.ExecutionRequest
import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor
import org.junit.platform.engine.support.descriptor.EngineDescriptor
import org.junit.platform.engine.support.hierarchical.EngineExecutionContext
import org.junit.platform.engine.support.hierarchical.HierarchicalTestEngine
import org.junit.platform.engine.support.hierarchical.Node

class KutieSpecEngine : HierarchicalTestEngine<KutieContext>() {

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

    override fun createExecutionContext(req: ExecutionRequest): KutieContext {
        return KutieContext()
    }
}

class KutieSpecDescriptor(parentId: UniqueId, val clz: Class<out KutieSpec>)
    : AbstractTestDescriptor(parentId.append("kutiespec", clz.getName()), clz.getSimpleName()), Node<KutieContext> {

    companion object : KLogging()

    override fun getType(): TestDescriptor.Type {
        return TestDescriptor.Type.CONTAINER
    }

    override fun mayRegisterTests(): Boolean {
        return true
    }

    fun getSpec(): KutieSpec {
        return clz.newInstance()
    }

    override fun execute(ctx: KutieContext, exec: Node.DynamicTestExecutor): KutieContext {
        logger.info("Executing $clz...")
        return ctx
    }
}

class KutieContext : EngineExecutionContext