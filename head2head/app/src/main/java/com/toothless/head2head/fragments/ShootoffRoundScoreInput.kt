package com.toothless.head2head.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toothless.head2head.MainActivity
import com.toothless.head2head.R
import com.toothless.head2head.data.CurrentGame
import com.toothless.head2head.events.EventBus
import com.toothless.head2head.events.SaveScore
import com.toothless.head2head.scoreInput.ScoreInputKeyboard
import kotlinx.android.synthetic.main.second_round_score_input_fragment.*


class ShootoffRoundScoreInput(val parent : MainActivity) : Fragment(), SaveScore {

    var keyboard : AlertDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shootoff_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        EventBus.SubscribeSaveScoreEvent(this)
        assignFunctionsToButtons()
        updateText()
    }

    fun assignFunctionsToButtons()
    {
        p1end1.setOnClickListener {
            openKeyboard(1, 6)
        }

//        p2end1.setOnClickListener {
//            openKeyboard(2, 6)
//        }
    }

    fun openKeyboard(player : Int, end : Int)
    {
        val builder = AlertDialog.Builder(context)
        val inf = layoutInflater.inflate(R.layout.layout_score_input_keyboard, null)
        val kb = ScoreInputKeyboard(inf, player, end)
        kb.setupKeyboard()
        builder.setView(inf)

        keyboard = builder.create()

        keyboard!!.show()
    }

    override fun saveScore(scores: List<Int>, player : Int, end : Int) {
        keyboard?.dismiss()
        updateButtons(scores, player)

        CurrentGame.addEnd(scores, player, end)

        if (CurrentGame.aiGame) {
            CurrentGame.addEnd(CurrentGame.getAiScore(end), 2, end)
            updateButtons(CurrentGame.round.scores[end - 1].p2End, 2)
        }

        updateText()

        parent.continueGame(this)
    }

    fun updateText()
    {
        if(!CurrentGame.playersAtSameStage())
            return

        val scores = CurrentGame.getTotalScoresWithNames()
        player1Name.text = scores.first
        player2Name.text = scores.second
    }

    fun updateButtons(scores: List<Int>, player : Int) {
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
