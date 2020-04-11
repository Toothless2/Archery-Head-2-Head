package com.toothless.head2head.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toothless.head2head.MainActivity
import com.toothless.head2head.R
import com.toothless.head2head.GameManager
import com.toothless.head2head.enums.EScoreInputType
import com.toothless.head2head.events.EventBus
import com.toothless.head2head.events.data.GameOverEvent
import com.toothless.head2head.events.data.KeyboardEvent
import com.toothless.head2head.events.data.ScoreInputEvent
import kotlinx.android.synthetic.main.second_round_score_input_fragment.*


class ShootoffAIInput(val parent : MainActivity) : Fragment(), IScoreInput {

    private val scoreInputEventHandler:(ScoreInputEvent) -> Unit = {(scores, player, end) -> scoreInput(scores, player, end)}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.shootoff_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        EventBus.scoreInputEvent += scoreInputEventHandler
        assignFunctionsToButtons()
        updateText()
    }

    private fun assignFunctionsToButtons()
    {
        p1end1.setOnClickListener {
            EventBus.keyboardEvent(KeyboardEvent(this, 1, GameManager.round.scores.size+1, EScoreInputType.SHOOTOFFEND))
        }
    }

    override fun scoreInput(scores: List<Int>, player : Int, end : Int) {
        super.scoreInput(scores, player, end)

        if (GameManager.gameOver())
            EventBus.gameOverEvent(GameOverEvent(this))
        else {
            p1end1score1.text = ""
            p2end1score1.text = ""
        }
    }

    override fun updateText()
    {
        if(!GameManager.playersAtSameStage())
            return

        val scores = GameManager.getTotalScoresWithNames()
        player1Name.text = scores.first
        player2Name.text = scores.second
    }

    override fun updateScoreDisplay(scores: List<Int>, player : Int, end: Int) {
        when (player) {
            1 ->
                p1end1score1.text = scoreDisplay(scores[0])
            2 ->
                p2end1score1.text = scoreDisplay(scores[0])
        }
    }

    override fun onDestroyView() {
        EventBus.scoreInputEvent -= scoreInputEventHandler
        super.onDestroyView()
    }
}