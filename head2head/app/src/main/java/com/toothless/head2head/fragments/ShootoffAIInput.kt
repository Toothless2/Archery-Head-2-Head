package com.toothless.head2head.fragments

import android.app.AlertDialog
import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toothless.head2head.MainActivity
import com.toothless.head2head.R
import com.toothless.head2head.GameManager
import com.toothless.head2head.events.EventBus
import com.toothless.head2head.events.data.KeyboardEvent
import com.toothless.head2head.events.data.ScoreInputEvent
import com.toothless.head2head.scoreInput.ScoreInputKeyboard
import kotlinx.android.synthetic.main.layout_score_input_keyboard.view.*
import kotlinx.android.synthetic.main.second_round_score_input_fragment.p1end1
import kotlinx.android.synthetic.main.second_round_score_input_fragment.p1end1score1
import kotlinx.android.synthetic.main.second_round_score_input_fragment.p2end1score1
import kotlinx.android.synthetic.main.second_round_score_input_fragment.player1Name
import kotlinx.android.synthetic.main.second_round_score_input_fragment.player2Name


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
//            displayKeyboard()
            EventBus.keyboardEvent(KeyboardEvent(this, 1, GameManager.round.scores.size+1, 1))
        }
    }

//    private var keyboard : AlertDialog? = null
//    private fun displayKeyboard() {
//        val builder = AlertDialog.Builder(context)
//        val inf = parent.layoutInflater.inflate(R.layout.layout_score_input_keyboard, view as ViewGroup, false)
//        inf.out2.visibility = View.GONE
//        inf.out3.visibility = View.GONE
//        val kb = ScoreInputKeyboard(inf, 1, GameManager.round.scores.size + 1, 1)
//        kb.setupKeyboard()
//        builder.setView(inf)
//
//        keyboard = builder.create()
//        keyboard?.show()
//        keyboard?.window?.setBackgroundDrawable(null)
//    }

    override fun scoreInput(scores: List<Int>, player : Int, end : Int) {
//        keyboard?.dismiss()
        super.scoreInput(scores, player, end)

        if(GameManager.gameOver())
            parent.continueGame(this)
        else
        {
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
                p1end1score1.text = scores[0].toString()
            2 ->
                p2end1score1.text = scores[0].toString()
        }
    }

    override fun onDestroyView() {
        EventBus.scoreInputEvent -= scoreInputEventHandler
        super.onDestroyView()
    }
}