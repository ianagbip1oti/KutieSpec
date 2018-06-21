package com.github.princesslana.kutiespec

open class KutieSpec(val block: DescribeBlock.() -> Unit) {
    internal fun toExample(): Example {
        return DescribeBlock(this::class.simpleName ?: "").also(block).toExample()
    }
}

class DescribeBlock(val name: String) {
    val examples: MutableList<Example> = mutableListOf()

    fun describe(name: String, block: DescribeBlock.() -> Unit) {
        examples.add(DescribeBlock(name).also(block).toExample())
    }

    fun it(block: ItBlock.() -> Unit) {
        examples.add(Single(null, { -> block(ItBlock()) }))
    }

    internal fun toExample(): Example {
        return Group(name, examples)
    }
}

class ItBlock {
    fun expect(s: Any): To {
        return To()
    }

    val beTrue: Expectation = { a -> null }
}

typealias Expectation = (Any) -> Unit

class To {
    infix fun to(e: Expectation) {
    }
}
