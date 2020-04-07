package com.toothless.head2head.events

import android.hardware.SensorEventListener

object EventBus {
    private var saveScoreEventListeners = mutableListOf<SaveScore>()

    fun SubscribeSaveScoreEvent(listener: SaveScore)
    {
        if(saveScoreEventListeners.contains(listener))
            return

        saveScoreEventListeners.add(listener)
    }

    fun UnSubscribeSaveScoreEvent(listener: SaveScore)
    {
        if(saveScoreEventListeners.contains(listener))
            saveScoreEventListeners.remove(listener)
    }

    fun CallSaveScoreEvent(scores : List<Int>, player : Int, end : Int)
    {
        saveScoreEventListeners.forEach { it.saveScore(scores, player, end) }
    }
}