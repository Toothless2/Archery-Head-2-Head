package com.toothless.head2head.events

interface Event<T> : MutableCollection<(T) -> Unit> {
    operator fun plusAssign(handler: (T) -> Unit)

    operator fun minusAssign(handler: (T) -> Unit)

    operator fun invoke(data: T)
}