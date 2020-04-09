package com.toothless.head2head.scoreInput

import android.view.View
import com.toothless.head2head.R
import com.toothless.head2head.GameManager
import com.toothless.head2head.events.EventBus
import kotlinx.android.synthetic.main.layout_score_input_keyboard.view.*

class ScoreInputKeyboard(val view: View, val player : Int, val end : Int) {

    private var scores = mutableListOf<Int>()

    fun setupKeyboard()
    {
        setButtonBehaviour()

        if(GameManager.round.scores.lastIndex >= end - 1) // if the end has already been filled out add the values into the keyboard
        {
            val scores = GameManager.getEnd(end)
            this.scores.addAll(if(player==1) scores.p1End else scores.p2End)
            updateDisplay()
        }
    }

    fun setButtonBehaviour()
    {
        view.tenIn.setOnClickListener {
            addButtonScore(10) }
        view.nineIn.setOnClickListener {
            addButtonScore(9)
        }
        view.eightIn.setOnClickListener {
            addButtonScore(8)
        }
        view.sevenIn.setOnClickListener {
            addButtonScore(7)
        }
        view.sixIn.setOnClickListener {
            addButtonScore(6)
        }
        view.fiveIn.setOnClickListener {
            addButtonScore(5)
        }
        view.fourIn.setOnClickListener {
            addButtonScore(4)
        }
        view.threeIn.setOnClickListener {
            addButtonScore(3)
        }
        view.twoIn.setOnClickListener {
            addButtonScore(2)
        }
        view.oneIn.setOnClickListener {
            addButtonScore(1)
        }
        view.missIn.setOnClickListener {
            addButtonScore(0)
        }
        view.del.setOnClickListener {
            removeButtonScore()
        }
        view.enter.setOnClickListener {
            saveScore()
        }
    }

    private fun saveScore() {
        if(scores.size != 3)
            while (scores.size < 3)
                scores.add(0)

        EventBus.CallSaveScoreEvent(scores.sorted().reversed(), player, end)
    }

    private fun removeButtonScore()
    {
        if(scores.isNotEmpty())
            scores.removeAt(scores.lastIndex)

        if(scores.size == 0)
            view.out1.text = view.resources.getString(R.string.none)

        if(scores.size == 1)
            view.out2.text = view.resources.getString(R.string.none)

        if(scores.size == 2)
            view.out3.text = view.resources.getString(R.string.none)
    }

    private fun addButtonScore(score : Int)
    {
        if(scores.size >= 3) // stops more than 3 scores being input
            return

        scores.add(score)
        updateDisplay()
    }

    private fun updateDisplay()
    {
        if(scores.size >= 1)
            view.out1.text = scores[0].toString()

        if(scores.size >= 2)
            view.out2.text = scores[1].toString()

        if(scores.size >= 3)
            view.out3.text = scores[2].toString()
    }
}