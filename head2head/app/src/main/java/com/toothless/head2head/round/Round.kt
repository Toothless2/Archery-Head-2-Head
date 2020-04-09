package com.toothless.head2head.round

class Round {
    val scores = mutableListOf<End>()

    lateinit var player1 : String
    lateinit var player2 : String

    var aiGame : Boolean = false
    private set

    fun setAiGame(ai : Boolean){
        aiGame = ai
    }

    fun addEnd(pair: End) = scores.add(pair)

    fun updateEnd(score : List<Int>, player : Int, end : Int)
    {
        if(end -1 <= scores.lastIndex)
            scores[end - 1].addEnd(score, player)
        else
        {
            val sp = End()
            sp.addEnd(score, player)
            scores.add(sp)
        }
    }

    fun getMatchScores() : Pair<Int, Int>
    {
        var p1 = 0
        var p2 = 0

        for (pair in scores)
        {
            val scores = pair.getEndScores()

            if(scores.first > scores.second)
                p1 += 2
            else if(scores.second > scores.first)
                p2 += 2
            else
            {
                p1++
                p2++
            }
        }

        return Pair(p1, p2)
    }

    fun getPlayerNamesWithScores() : Pair<String, String>
    {
        val scores = getMatchScores()
        return Pair("$player1 - ${scores.first}", "${scores.second} - $player2")
    }

    fun playersAtSameState() : Boolean
    {
        for (i in scores)
            if(!i.completeEnd)
                return false

        return true
    }

    fun gameOver() : Boolean
    {
        val matchScores = getMatchScores()

        //a score is above 6 and the match is not tied
        if((matchScores.first >= 6 || matchScores.second >= 6) && matchScores.first != matchScores.second)
            return true

        return false
    }

    fun getWinner() : String
    {
        val matchScores = getMatchScores()
        return if(matchScores.first > matchScores.second) player1 else player2
    }
}

