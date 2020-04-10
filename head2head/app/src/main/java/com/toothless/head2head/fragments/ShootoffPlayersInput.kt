package com.toothless.head2head.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.toothless.head2head.MainActivity
import com.toothless.head2head.R
import com.toothless.head2head.GameManager
import com.toothless.head2head.events.EventBus
import com.toothless.head2head.events.data.ContinueGameEvent
import com.toothless.head2head.events.data.GameOverEvent
import kotlinx.android.synthetic.main.shootoff_fragment_players.*

class ShootoffPlayersInput(val parent: MainActivity) :  Fragment(), IScoreInput {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shootoff_fragment_players, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        assignFunctionsToButtons()
        updateText()
    }

    private fun assignFunctionsToButtons() {
        player1ShootoffWon.setOnClickListener {
            scoreInput(listOf(1, 0), -1, -1)
        }

        player2ShootoffWin.setOnClickListener {
            scoreInput(listOf(0, 1), -1, -1)
        }

        shootoffTieButton.setOnClickListener {
            scoreInput(listOf(1, 1), -1, -1)
        }
    }

    override fun updateText() {
        player1ShootoffWon.text = context!!.resources.getString(R.string.shootoff_player1).replace("p1", GameManager.round.player1)
        player2ShootoffWin.text = context!!.resources.getString(R.string.shootoff_player2).replace("p2", GameManager.round.player2)
    }

    override fun scoreInput(scores: List<Int>, player: Int, end: Int) {

        GameManager.addPair(Pair(scores[0], scores[1]))

        if(GameManager.gameOver())
            EventBus.gameOverEvent(GameOverEvent(this))
        else
            EventBus.continueGameEvent(ContinueGameEvent(this))
    }

    override fun updateScoreDisplay(scores: List<Int>, player: Int, end: Int) {}
}