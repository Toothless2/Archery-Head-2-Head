package com.toothless.head2head.events

class SetEvent<T> private constructor(private val backing : MutableSet<(T) -> Unit>) : AbstractEvent<T>(), MutableCollection<(T) -> Unit> by backing{
    constructor() : this(HashSet())
}