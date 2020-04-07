package com.toothless.head2head.events

interface SaveScore {
    fun saveScore(scores : List<Int>, player : Int, end : Int)
}