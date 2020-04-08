package com.toothless.head2head.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.toothless.head2head.MainActivity
import com.toothless.head2head.R
import com.toothless.head2head.data.CurrentGame
import com.toothless.head2head.events.EventBus
import com.toothless.head2head.events.SaveScore
import kotlinx.android.synthetic.main.shootoff_fragment_players.*

class ShootoffPlayersInput(val parent: MainActivity) :  Fragment(), SaveScore {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shootoff_fragment_players, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        EventBus.SubscribeSaveScoreEvent(this)
        assignFunctionsToButtons()
        updateText()
    }

    fun assignFunctionsToButtons()
    {
        player1ShootoffWon.setOnClickListener {
            saveScore(listOf(1, 0), -1, -1)
        }

        player2ShootoffWin.setOnClickListener {
            saveScore(listOf(0, 1), -1, -1)
        }

        shootoffTieButton.setOnClickListener {
            saveScore(listOf(1, 1), -1, -1)
        }
    }

    fun updateText()
    {
        player1ShootoffWon.text = context!!.resources.getString(R.string.shootoff_player1).replace("p1", CurrentGame.round.player1)
        player2ShootoffWin.text = context!!.resources.getString(R.string.shootoff_player2).replace("p2", CurrentGame.round.player2)
    }

    override fun saveScore(scores: List<Int>, player : Int, end : Int) {

        CurrentGame.addPair(Pair(scores[0], scores[1]))

        parent.continueGame(this)
    }

    override fun onDestroyView() {
        EventBus.UnSubscribeSaveScoreEvent(this)
        super.onDestroyView()
    }
}