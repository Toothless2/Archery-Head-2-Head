package com.toothless.head2head.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toothless.head2head.GameManager
import com.toothless.head2head.R
import com.toothless.head2head.events.EventBus
import com.toothless.head2head.events.data.StartGameEvent
import com.toothless.head2head.save.SaveRound
import kotlinx.android.synthetic.main.game_win_fragment.*

class GameWinFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.game_win_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWinnerDisplay()
        assignButtons()
    }

    private fun assignButtons()
    {
        noSaveButton.setOnClickListener {
            continueNoSave()
        }

        saveButton.setOnClickListener {
            home()
        }

        playAgainButton.setOnClickListener {
            restartGame()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setWinnerDisplay()
    {
        winningPlayerName.text = GameManager.getLeader()
        finalGameScore.text = "${GameManager.round.getMatchScores().first} - ${GameManager.round.getMatchScores().second}"
    }

    private fun continueNoSave()
    {
        GameManager.reset()
        activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
    }

    private fun home()
    {
        Thread()
        {
            SaveRound.addRound(GameManager.round)
            SaveRound.writeJsonData(context!!)
            GameManager.reset()
        }.start()
        activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
    }

    private fun restartGame()
    {
        val name1  =  GameManager.round.player1
        val name2 = GameManager.round.player2
        val aiGame = GameManager.isAiGame
        val roundToSave = GameManager.round

        Thread()
        {
            SaveRound.addRound(roundToSave)
            SaveRound.writeJsonData(context!!)
        }.start()

        GameManager.reset()
        GameManager.isAiGame = aiGame

        EventBus.startGameEvent(StartGameEvent(this ,name1, name2))
    }
}
