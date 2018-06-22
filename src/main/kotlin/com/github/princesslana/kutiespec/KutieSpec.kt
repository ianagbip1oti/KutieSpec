package com.github.princesslana.kutiespec

import mu.KLogging
import org.junit.platform.engine.EngineExecutionListener
import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.TestExecutionResult
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor
import org.junit.platform.engine.support.descriptor.ClassSource

open class KutieSpec(val block: DescribeBlock.() -> Unit) {

    companion object : KLogging()

    val name = this::class.simpleName

    internal fun execute(id: UniqueId, l: EngineExecutionListener) {
        logger.info("Executing $name...")
        block(DescribeBlock(id, this, l))
    }
}

class DescribeBlock(val id: UniqueId, val spec: KutieSpec, val l: EngineExecutionListener) {

    companion object : KLogging()

    fun describe(name: String, block: DescribeBlock.() -> Unit) {
        logger.info("Executing $name...")

        val descriptor = ExampleGroupDescriptor(id, name, spec)

        l.dynamicTestRegistered(descriptor)
        l.executionStarted(descriptor)

        block(DescribeBlock(id, spec, l))

        l.executionFinished(descriptor, TestExecutionResult.successful())
    }

    fun it(name: String, block: ItBlock.() -> Unit) {
        val descriptor = ExampleDescriptor(id, name, spec)

        l.dynamicTestRegistered(descriptor)
        l.executionStarted(descriptor)

        try {
            block(ItBlock())

            l.executionFinished(descriptor, TestExecutionResult.successful())
        } catch (e: AssertionError) {
            l.executionFinished(descriptor, TestExecutionResult.failed(e))
        }
    }
}

class ItBlock {
    fun expect(v: Any): To {
        return To(v)
    }

    val beTrue: Expectation = { a -> null }
}

typealias Expectation = (Any) -> Unit

class To(val v: Any) {
    infix fun to(e: Expectation) {
        e(v)
    }
}

class ExampleGroupDescriptor(parentId: UniqueId, name: String, spec: KutieSpec)
    : AbstractTestDescriptor(parentId.append("exampleGroup", name), name, ClassSource.from(spec.javaClass)) {

    override fun getType(): TestDescriptor.Type {
        return TestDescriptor.Type.CONTAINER
    }

    override fun mayRegisterTests(): Boolean {
        return true
    }
}

class ExampleDescriptor(parentId: UniqueId, name: String, spec: KutieSpec)
    : AbstractTestDescriptor(parentId.append("example", name), name, ClassSource.from(spec.javaClass)) {

    override fun getType(): TestDescriptor.Type {
        return TestDescriptor.Type.TEST
    }
}
