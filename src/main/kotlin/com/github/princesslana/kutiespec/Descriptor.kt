package com.github.princesslana.kutiespec

import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor
import org.junit.platform.engine.support.descriptor.EngineDescriptor

fun toDescriptor(id: UniqueId, specs: Iterable<Class<out KutieSpec>>): TestDescriptor {
    val engine = EngineDescriptor(id, "KutieSpec")

    specs.map { c -> c.newInstance() }
        .map(KutieSpec::toExample)
        .map { g -> KutieSpecDescriptor(id, g) }
        .forEach(engine::addChild)

    return engine
}

class KutieSpecDescriptor(parentId: UniqueId, val example: Group)
    : AbstractTestDescriptor(parentId.append("kutiespec", example.name), example.name) {

    override fun getType(): TestDescriptor.Type {
        return TestDescriptor.Type.CONTAINER
    }

    override fun mayRegisterTests(): Boolean {
        return true
    }
}
