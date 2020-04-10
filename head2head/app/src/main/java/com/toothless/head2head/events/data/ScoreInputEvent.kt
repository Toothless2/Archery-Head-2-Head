package com.toothless.head2head.events.data

data class ScoreInputEvent(val scores: List<Int>, val player : Int, val end : Int) {
}