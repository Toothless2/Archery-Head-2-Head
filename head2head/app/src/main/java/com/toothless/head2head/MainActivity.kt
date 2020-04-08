package com.toothless.head2head

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.Fragment
import com.toothless.head2head.ai.GetAIData
import com.toothless.head2head.data.CurrentGame
import com.toothless.head2head.fragments.*
import com.toothless.head2head.save.SaveRound
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GetAIData.setupAI(this)
        SaveRound.setupSave(this)

        startNormalGame.setOnClickListener {
            CurrentGame.aiGame = false
            startGame(2)
        }

        startAIGame.setOnClickListener {
            CurrentGame.aiGame = true
            startGame(1)
        }
    }

    fun startGame(players : Int)
    {
        val nameInput = NameInputFragment(players, this)
        supportFragmentManager.beginTransaction().add(mainActivityLayout.id, nameInput).addToBackStack(null).commit()
    }

    fun closeFragment(fragment: Fragment)=supportFragmentManager.beginTransaction().remove(fragment)

    fun continueGame(fragment: Fragment) {

        if (CurrentGame.round.scores.size == 0) { // to start the game
            supportFragmentManager.beginTransaction().remove(fragment).commit()
            val first = FirstRoundScoreInput(this)
            supportFragmentManager.beginTransaction().add(mainActivityLayout.id, first)
                .addToBackStack(null).commit()
            return
        }

        val scores = CurrentGame.getTotalScores()
        var nextScreen: Fragment? = null

        if (gameOver(scores) && CurrentGame.playersAtSameStage()) {

            AlertDialog.Builder(this).setTitle("Congratulations!")
                .setMessage("Well Done ${if (scores.first > scores.second) CurrentGame.round.player1 else CurrentGame.round.player2}")
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    Thread() {
                        SaveRound.addRound(CurrentGame.round, CurrentGame.aiGame) // move this to its own thread
                        SaveRound.writeJsonData(this)
                        CurrentGame.reset()
                    }.start()
                    supportFragmentManager.beginTransaction().remove(fragment).commit()
                } // notify of who won and close the fragment when ok is pressed
                .show()

            return
        } else if (CurrentGame.completedEnds() == 3 && CurrentGame.playersAtSameStage()) // continue as normal
            nextScreen = SecondRoundScoreInput(this)
        else if (CurrentGame.completedEnds() >= 5)
            nextScreen = shootoffScreenLoad()

        if (nextScreen != null) {
            supportFragmentManager.beginTransaction().remove(fragment).commit()
            supportFragmentManager.beginTransaction().replace(mainActivityLayout.id, nextScreen)
                .addToBackStack(null).commit()
        }
    }

    private fun shootoffScreenLoad() : Fragment
    {
        lateinit var shootoff : Fragment
        if(CurrentGame.aiGame)
            shootoff = ShootoffRoundScoreInput(this)
        else
            shootoff = ShootoffPlayersInput(this)

        return shootoff
    }

    fun gameOver(scores : Pair<Int, Int>) = (scores.first != scores.second && (scores.second >= 6 || scores.first >= 6))
}
