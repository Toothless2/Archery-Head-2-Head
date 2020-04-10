package com.toothless.head2head.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toothless.head2head.MainActivity
import com.toothless.head2head.R
import com.toothless.head2head.GameManager
import com.toothless.head2head.events.*
import com.toothless.head2head.events.data.KeyboardEvent
import com.toothless.head2head.events.data.ScoreInputEvent
import kotlinx.android.synthetic.main.first_round_score_input_fragment.*


class FirstRoundScoreInput(val parent : MainActivity) : Fragment(), IScoreInput {

    private val scoreInputEventHandler : (ScoreInputEvent) -> Unit = { (scores, player, end) -> scoreInput(scores, player, end)}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.first_round_score_input_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        EventBus.scoreInputEvent += scoreInputEventHandler
        assignFunctionsToButtons()
        updateText()
    }

    private fun assignFunctionsToButtons() {
        p1end1.setOnClickListener {
            EventBus.keyboardEvent(KeyboardEvent(this, 1, 1))
        }

        p1end2.setOnClickListener {
            if (GameManager.completedEnds() >= 1)
                EventBus.keyboardEvent(KeyboardEvent(this, 1, 2))
        }

        p1end3.setOnClickListener {
            if (GameManager.completedEnds() >= 2)
                EventBus.keyboardEvent(KeyboardEvent(this, 1, 3))
        }

        if (!GameManager.isAiGame) {
            p2end1.setOnClickListener {
                EventBus.keyboardEvent(KeyboardEvent(this, 2, 1))
            }

            p2end2.setOnClickListener {
                if (GameManager.completedEnds() >= 1)
                    EventBus.keyboardEvent(KeyboardEvent(this, 2, 2))
            }

            p2end3.setOnClickListener {
                if (GameManager.completedEnds() >= 2)
                    EventBus.keyboardEvent(KeyboardEvent(this, 2, 3))
            }
        }
    }

    override fun scoreInput(scores: List<Int>, player : Int, end : Int) {
        super.scoreInput(scores, player, end)
        parent.continueGame(this)
    }

    override fun updateText()
    {
        super.updateText()

        val scores = GameManager.getTotalScoresWithNames()
        player1Name.text = scores.first
        player2Name.text = scores.second
    }

    override fun updateScoreDisplay(scores: List<Int>, player : Int, end : Int)
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
        EventBus.scoreInputEvent -= scoreInputEventHandler
        super.onDestroyView()
    }
}
