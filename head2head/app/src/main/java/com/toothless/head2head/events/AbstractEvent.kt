package com.toothless.head2head.events

abstract class AbstractEvent<T> : Event<T> {
    override operator fun plusAssign(handler: (T) -> Unit) {
        add(handler)
    }

    override operator fun minusAssign(handler: (T) -> Unit) {
        remove(handler)
    }

    override fun invoke(data: T) = forEach { it(data) }
}