package com.toothless.head2head.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toothless.head2head.MainActivity
import com.toothless.head2head.R
import com.toothless.head2head.GameManager
import com.toothless.head2head.events.EventBus
import com.toothless.head2head.events.SaveScore
import com.toothless.head2head.scoreInput.ScoreInputKeyboard
import kotlinx.android.synthetic.main.first_round_score_input_fragment.*


class FirstRoundScoreInput(val parent : MainActivity) : Fragment(), SaveScore {

    var keyboard : AlertDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.first_round_score_input_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        EventBus.SubscribeSaveScoreEvent(this)
        assignFunctionsToButtons()
        updateText()
    }

    fun assignFunctionsToButtons() {
        p1end1.setOnClickListener {
            openKeyboard(1, 1)
        }

        p1end2.setOnClickListener {
            if (GameManager.completedEnds() >= 1)
                openKeyboard(1, 2)
        }

        p1end3.setOnClickListener {
            if (GameManager.completedEnds() >= 2)
                openKeyboard(1, 3)
        }

        if (!GameManager.isAiGame) {
            p2end1.setOnClickListener {
                openKeyboard(2, 1)
            }

            p2end2.setOnClickListener {
                if (GameManager.completedEnds() >= 1)
                    openKeyboard(2, 2)
            }

            p2end3.setOnClickListener {
                if (GameManager.completedEnds() >= 2)
                    openKeyboard(2, 3)
            }
        }
    }

    fun openKeyboard(player : Int, end : Int)
    {
        val builder = AlertDialog.Builder(context)
        val inf = layoutInflater.inflate(R.layout.layout_score_input_keyboard, view as ViewGroup, false)
        val kb = ScoreInputKeyboard(inf, player, end)
        kb.setupKeyboard()
        builder.setView(inf)

        keyboard = builder.create()

        keyboard?.show()
        keyboard?.window?.setBackgroundDrawable(null)
    }

    override fun saveScore(scores: List<Int>, player : Int, end : Int) {
        keyboard?.dismiss()
        updateButtons(scores, player, end)

        GameManager.addEnd(scores, player, end)

        if (GameManager.isAiGame) {
            GameManager.addEnd(GameManager.getAiEndScore(end), 2, end)
            updateButtons(GameManager.getEnd(end).p2End, 2, end)
        }

        updateText()

        parent.continueGame(this)

    }

    fun updateText()
    {
        if(!GameManager.playersAtSameStage())
            return

        val scores = GameManager.getTotalScoresWithNames()
        player1Name.text = scores.first
        player2Name.text = scores.second
    }

    fun updateButtons(scores: List<Int>, player : Int, end : Int)
    {
        when(player)
        {
            1 ->
                when(end) {
                    1 -> {
                        p1end1score1.text = scores[0].toString()
                        p1end1score2.text = scores[1].toString()
                        p1end1score3.text = scores[2].toString()
                        p1end1score.text = scores.sum().toString()
                    }
                    2 -> {
                        p1end2score1.text = scores[0].toString()
                        p1end2score2.text = scores[1].toString()
                        p1end2score3.text = scores[2].toString()
                        p1end2score.text = scores.sum().toString()
                    }
                    3 -> {
                        p1end3score1.text = scores[0].toString()
                        p1end3score2.text = scores[1].toString()
                        p1end3score3.text = scores[2].toString()
                        p1end3score.text = scores.sum().toString()
                    }
                }
            2 ->
                when(end) {
                    1 -> {
                        p2end1score1.text = scores[0].toString()
                        p2end1score2.text = scores[1].toString()
                        p2end1score3.text = scores[2].toString()
                        p2end1score.text = scores.sum().toString()
                    }
                    2 -> {
                        p2end2score1.text = scores[0].toString()
                        p2end2score2.text = scores[1].toString()
                        p2end2score3.text = scores[2].toString()
                        p2end2score.text = scores.sum().toString()
                    }
                    3 -> {
                        p2end3score1.text = scores[0].toString()
                        p2end3score2.text = scores[1].toString()
                        p2end3score3.text = scores[2].toString()
                        p2end3score.text = scores.sum().toString()
                    }
                }
        }
    }

    override fun onDestroyView() {
        EventBus.UnSubscribeSaveScoreEvent(this)
        super.onDestroyView()
    }
}
