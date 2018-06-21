package com.github.princesslana.kutiespec

sealed class Example
data class Group(val name: String, val examples: Iterable<Example>) : Example()
data class Single(val name: String?, val execute: () -> Unit) : Example()
