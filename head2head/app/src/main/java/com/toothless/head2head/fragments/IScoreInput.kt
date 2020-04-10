package com.toothless.head2head.fragments

import com.toothless.head2head.GameManager

interface IScoreInput {
    fun scoreInput(scores: List<Int>, player : Int, end : Int) {
        updateScoreDisplay(scores, player, end)

        GameManager.addEnd(scores, player, end)

        if (GameManager.isAiGame && !GameManager.playersAtSameStage()) {
            GameManager.addEnd(GameManager.getAiEndScore(end), 2, end)
            updateScoreDisplay(GameManager.getEnd(end).p2End, 2, end)
        }

        updateText()
    }

    fun updateText()

    fun updateScoreDisplay(scores: List<Int>, player : Int, end : Int)
}