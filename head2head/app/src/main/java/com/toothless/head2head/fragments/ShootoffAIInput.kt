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
import kotlinx.android.synthetic.main.second_round_score_input_fragment.*
import kotlinx.android.synthetic.main.second_round_score_input_fragment.p1end1
import kotlinx.android.synthetic.main.second_round_score_input_fragment.p1end1score1
import kotlinx.android.synthetic.main.second_round_score_input_fragment.p2end1score1
import kotlinx.android.synthetic.main.second_round_score_input_fragment.player1Name
import kotlinx.android.synthetic.main.second_round_score_input_fragment.player2Name
import kotlinx.android.synthetic.main.shootoff_fragment.*


class ShootoffAIInput(val parent : MainActivity) : Fragment(), SaveScore {

    var keyboard : AlertDialog? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.shootoff_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        EventBus.SubscribeSaveScoreEvent(this)
        assignFunctionsToButtons()
        updateText()
    }

    private fun assignFunctionsToButtons()
    {
        p1end1.setOnClickListener {
            openKeyboard()
        }
    }

    private fun openKeyboard()
    {
        val builder = AlertDialog.Builder(context)
        val inf = layoutInflater.inflate(R.layout.layout_score_input_keyboard, view as ViewGroup, false)
        val kb = ScoreInputKeyboard(inf, 1, GameManager.round.scores.size)
        kb.setupKeyboard()
        builder.setView(inf)

        keyboard = builder.create()

        keyboard!!.show()
    }

    override fun saveScore(scores: List<Int>, player : Int, end : Int) {
        keyboard?.dismiss()
        updateButtons(scores, player)

        GameManager.addEnd(scores, player, end)

        if (GameManager.isAiGame) {
            GameManager.addEnd(GameManager.getAiEndScore(end), 2, end)
            updateButtons(GameManager.getEnd(end).p2End, 2)
        }

        updateText()

        parent.continueGame(this)
    }

    private fun updateText()
    {
        if(!GameManager.playersAtSameStage())
            return

        val scores = GameManager.getTotalScoresWithNames()
        player1Name.text = scores.first
        player2Name.text = scores.second
    }

    private fun updateButtons(scores: List<Int>, player : Int) {
        when (player) {
            1 ->
                p1end1score1.text = scores[0].toString()
            2 ->
                p2end1score1.text = scores[0].toString()
        }
    }

    override fun onDestroyView() {
        EventBus.UnSubscribeSaveScoreEvent(this)
        super.onDestroyView()
    }
}
