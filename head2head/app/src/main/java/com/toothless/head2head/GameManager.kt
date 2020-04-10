package com.toothless.head2head

import com.toothless.head2head.ai.AI
import com.toothless.head2head.ai.AIManager
import com.toothless.head2head.round.End
import com.toothless.head2head.round.Round
import com.toothless.head2head.scoreInput.ScoreInputKeyboard

object GameManager {
    lateinit var round : Round
    private set

    var isAiGame
    set(value) {
        round.setAiGame(value)
    }
    get() = round.aiGame

    private lateinit var selectedAi : AI
    
    fun setupRound(player1 : String, player2: String)
    {
        val ai = isAiGame
        round = Round()
        round.setAiGame(ai)
        round.player1 = player1
        round.player2 = player2
    }

    fun setSelectedAi(aiName : String) : String
    {
        selectedAi = AIManager.getAi(AIManager.getAiId(aiName))
        return aiName
    }

    fun reset()
    {
        ScoreInputKeyboard.removeEvents()
        round = Round()
    }

    fun addEnd(score: List<Int>, player : Int, end : Int)
    {
        round.updateEnd(score, player, end)
    }

    fun addPair(pair: Pair<Int, Int>)
    {
        val end = End()
        end.addP1End(listOf(pair.first))
        end.addP2End(listOf(pair.second))
        round.addEnd(end)
    }

    fun getTotalScoresWithNames() = round.getPlayerNamesWithScores()
    fun playersAtSameStage() = round.playersAtSameState()

    fun completedEnds() : Int
    {
        if(round.playersAtSameState())
            return round.scores.size

        var compEnds = 0

        for (scores in round.scores)
            if(scores.completeEnd)
                compEnds++

        return compEnds
    }

    fun getAiEndScore(_end : Int) : List<Int>
    {
        val end = _end - 1
        if(end <= round.scores.lastIndex && round.scores[end].completeEnd)
            return round.scores[end].p2End

        return selectedAi.getAiScore(round.getMatchScores(), if(_end <=5) 3 else 1)
    }

    fun getEnd(end : Int) = round.scores[end -1]
    fun gameOver() = round.gameOver()
    fun getLeader() = round.getWinner()
}