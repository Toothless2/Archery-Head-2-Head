package com.toothless.head2head.scoreInput

import android.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import androidx.core.text.toSpannable
import androidx.fragment.app.Fragment
import com.toothless.head2head.R
import com.toothless.head2head.GameManager
import com.toothless.head2head.enums.EScoreDisplay
import com.toothless.head2head.enums.EScoreInputType
import com.toothless.head2head.events.EventBus
import com.toothless.head2head.events.data.KeyboardEvent
import com.toothless.head2head.events.data.ScoreInputEvent
import kotlinx.android.synthetic.main.layout_score_input_keyboard.view.*

class ScoreInputKeyboard(val view: View, val player : Int, val end : Int, private val maxScores : EScoreInputType = EScoreInputType.NORMALEND) {

    companion object
    {
        private val openKbEventHandler : (KeyboardEvent) -> Unit = {(parent, player, end, maxScores) -> openKeyboard(parent, player, end, maxScores)}
        private val closeKbEventHandler : (ScoreInputEvent) -> Unit = { closeKeyboard()}

        fun assignEvents()
        {
            EventBus.scoreInputEvent += closeKbEventHandler
            EventBus.keyboardEvent += openKbEventHandler
        }

        fun removeEvents()
        {
            EventBus.scoreInputEvent -= closeKbEventHandler
            EventBus.keyboardEvent -= openKbEventHandler
        }

        private var keyboard : AlertDialog? = null

        private fun openKeyboard(parent : Fragment, player : Int, end : Int, maxScores : EScoreInputType) {
            if (keyboard != null && keyboard?.isShowing!!)
                return

            val builder = AlertDialog.Builder(parent.context)
            val inf = parent.layoutInflater.inflate(R.layout.layout_score_input_keyboard, parent.view as ViewGroup, false)

            if (maxScores == EScoreInputType.SHOOTOFFEND) { // hides 2 of the core output boxes on the keyboard (used in shoot-off)
                inf.out2.visibility = View.GONE
                inf.out3.visibility = View.GONE
            }

            val kb = ScoreInputKeyboard(inf, player, end, maxScores)
            kb.setupKeyboard()
            builder.setView(inf)

            keyboard = builder.create()
            keyboard?.show()
            keyboard?.window?.setBackgroundDrawable(null)
        }

        private fun closeKeyboard()
        {
            keyboard?.dismiss()
        }
    }

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

    private fun setButtonBehaviour()
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
        if(scores.size != maxScores.number)
            while (scores.size < maxScores.number)
                scores.add(0)

        EventBus.scoreInputEvent(ScoreInputEvent(scores.sorted().reversed(), player, end))
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
        if(scores.size >= maxScores.number)
            return

        scores.add(score)
        updateDisplay()
    }

    private fun updateDisplay()
    {
        if(scores.size >= 1)
            view.out1.text = EScoreDisplay.values()[scores[0]].display

        if(scores.size >= 2)
            view.out2.text = EScoreDisplay.values()[scores[1]].display

        if(scores.size >= 3)
            view.out3.text = EScoreDisplay.values()[scores[2]].display
    }
}
