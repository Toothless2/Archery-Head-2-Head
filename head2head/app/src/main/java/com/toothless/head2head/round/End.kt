package com.toothless.head2head.round

class End {

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

    val completeEnd : Boolean
    get() {
        if(p1End.isNullOrEmpty() && p2End.isNullOrEmpty())
            return false
        if(p1End.size == p2End.size)
            return true

        return false
    }
}