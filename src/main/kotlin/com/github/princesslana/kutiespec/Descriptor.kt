package com.github.princesslana.kutiespec

import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor
import org.junit.platform.engine.support.descriptor.EngineDescriptor

fun toDescriptor(id: UniqueId, specs: Iterable<Class<out KutieSpec>>): TestDescriptor {
    val engine = EngineDescriptor(id, "KutieSpec")

    specs.map { c -> c.newInstance() }
        .map { s -> KutieSpecDescriptor(id, s) }
        .forEach(engine::addChild)

    return engine
}

data class KutieSpecDescriptor(val parentId: UniqueId, val spec: KutieSpec)
    : AbstractTestDescriptor(parentId.append("kutiespec", spec::class.qualifiedName), spec.name) {

    override fun getType(): TestDescriptor.Type {
        return TestDescriptor.Type.CONTAINER
    }

    override fun mayRegisterTests(): Boolean {
        return true
    }
}
