package com.toothless.head2head.events

import com.toothless.head2head.events.data.ContinueGameEvent
import com.toothless.head2head.events.data.KeyboardEvent
import com.toothless.head2head.events.data.ScoreInputEvent

fun <T> event() = SetEvent<T>()
fun <T> namedEvent() = MapEvent<T>()

object EventBus {

    val scoreInputEvent = event<ScoreInputEvent>()
    val keyboardEvent = event<KeyboardEvent>()
    val continueGameEvent = event<ContinueGameEvent>()
}