package com.toothless.head2head.data

import com.toothless.head2head.events.SaveScore

object CurrentGame {
    lateinit var round : Round
    private set

    fun startGame(player1 : String, player2: String)
    {
        round = Round(player1, player2)
    }

    fun addEnd(score: List<Int>, player : Int, end : Int)
    {
        round.updatePair(score, player, end)
    }

    fun getTotalScores() = round.getMatchScores()
    fun getTotalScoresWithNames() = round.getPlayerNamesWithScores()
    fun playersAtSameStage() = round.playersAtSameState()

    fun completedEnds() : Int
    {
        if(round.playersAtSameState())
            return round.scores.size

        var compEnds = 0

        for (scores in round.scores)
            if(scores.completePair)
                compEnds++

        return compEnds
    }
}