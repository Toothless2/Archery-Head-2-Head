package com.toothless.head2head.data

object CurrentGame {
    lateinit var round : Round
    private set

    var aiGame = false
    var selectedAi : Int = -1

    fun startGame(player1 : String, player2: String)
    {
        round = Round(player1, player2)
    }

    fun addEnd(score: List<Int>, player : Int, end : Int)
    {
        round.updatePair(score, player, end)
    }

    fun addPair(pair: Pair<Int, Int>)
    {
        val end = EndPair()
        end.addP1End(listOf(pair.first))
        end.addP2End(listOf(pair.second))
        round.addPair(end)
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

    fun getAiScore(end : Int) : List<Int>
    {
        if(end -1 <= round.scores.lastIndex && round.scores[end-1].completePair)
            return round.scores[end-1].p2End

        return listOf(0, 1, 5)
    }
}