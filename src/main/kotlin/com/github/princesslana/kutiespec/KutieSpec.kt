package com.github.princesslana.kutiespec

open class KutieSpec(block: DescribeBlock.() -> Unit)

class DescribeBlock {
    fun describe(name: String, block: DescribeBlock.() -> Unit) {
    }

    fun it(block: ItBlock.() -> Unit) {
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
