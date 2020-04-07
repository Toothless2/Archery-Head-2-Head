package com.toothless.head2head.data

class Round(
    val player1 : String,
    val player2 : String
) {
    val scores = mutableListOf<EndPair>()

    fun addPair(pair: EndPair) = scores.add(pair)

    fun addPair(pair: EndPair, end : Int)
    {
        if(end - 1 <= scores.lastIndex)
            scores[end - 1] = pair
        else
            scores.add(pair)
    }

    fun updatePair(score : List<Int>, player : Int, end : Int)
    {
        if(end -1 <= scores.lastIndex)
            scores[end - 1].addEnd(score, player)
        else
        {
            val sp = EndPair()
            sp.addEnd(score, player)
            scores.add(sp)
        }
    }

    fun getEndScores(end : Int) : Pair<Int, Int>
    {
        return if (playersAtSameState()) scores[end - 1].getEndScores() else Pair(-1, -1)
    }

    fun getMatchScores() : Pair<Int, Int>
    {
        var p1 = 0;
        var p2 = 0;

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
            if(!i.completePair)
                return false

        return true
    }
}

class EndPair {
    var p1End = mutableListOf<Int>()
    var p2End = mutableListOf<Int>()

    fun addEnd(end : List<Int>, player : Int)
    {
        when(player){
            1 -> addP1End(end)
            2 -> addP2End(end)
        }
    }

    fun addP1End(end: List<Int>) {
        p1End.clear()
        p1End.addAll(end)
    }

    fun addP2End(end: List<Int>) {
        p2End.clear()
        p2End.addAll(end)
    }

    fun getEndScores() : Pair<Int, Int>
    {
        return Pair(p1End.sum(), p2End.sum())
    }

    val completePair : Boolean
    get() {
        if(p1End.isNullOrEmpty() && p2End.isNullOrEmpty())
            return false
        if(p1End.size == 3 && p2End.size == 3)
            return true

        return false
    }
}