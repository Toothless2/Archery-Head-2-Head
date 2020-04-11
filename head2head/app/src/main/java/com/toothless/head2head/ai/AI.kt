package com.toothless.head2head.ai

import kotlin.math.roundToInt
import kotlin.random.Random

data class AI(val id: Int, val name : String, val score : Double, val variance: Double, val throwChance : Double) {
    fun getAiScore(currentScores : Pair<Int, Int>, nScoresToGenerate : Int) : List<Int>
    {
        val scores = mutableListOf<Int>()

        val score = score
        val endAverage = score / 20.0
        val arrowAverage = endAverage / 3
        val variance = variance
        val arrowVariance = variance / 3.0
        val throwMultiplier = (if (currentScores.first >= currentScores.second) 0.01 else (currentScores.second - (currentScores.first + 1))/10.0)
        val throwChance = throwChance * throwMultiplier

        for (i in 0 until nScoresToGenerate) {
            val arrowScore = Random.nextDouble((arrowAverage - arrowVariance) * (1-throwChance), arrowAverage + 1)
            scores.add(if (arrowScore.roundToInt() > 10) 10 else if (arrowScore.roundToInt() < 0) 0 else arrowScore.roundToInt())
        }

        return scores.sorted().reversed()
    }
}