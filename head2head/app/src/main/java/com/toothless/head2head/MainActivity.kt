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
import com.toothless.head2head.data.CurrentGame
import com.toothless.head2head.fragments.FirstRoundScoreInput
import com.toothless.head2head.fragments.NameInputFragment
import com.toothless.head2head.fragments.SecondRoundScoreInput
import com.toothless.head2head.fragments.ShootoffRoundScoreInput
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startNormalGame.setOnClickListener {
            startGame(2)
        }
    }

    fun startGame(players : Int)
    {
        val nameInput = NameInputFragment(players, this)
        supportFragmentManager.beginTransaction().add(mainActivityLayout.id, nameInput).addToBackStack(null).commit()
    }

    fun closeFragment(fragment: Fragment)=supportFragmentManager.beginTransaction().remove(fragment)

    fun continueGame(fragment: Fragment) {

        if(CurrentGame.round.scores.size == 0)
        {
            supportFragmentManager.beginTransaction().remove(fragment).commit()
            val first = FirstRoundScoreInput(this)
            supportFragmentManager.beginTransaction().add(mainActivityLayout.id, first).addToBackStack(null).commit()
            return
        }

        val scores = CurrentGame.getTotalScores()

        if(gameOver(scores) && CurrentGame.playersAtSameStage())
        {
            val notification = AlertDialog.Builder(this)
            notification.setTitle("Congratulations!").setMessage("Well Done ${if(scores.first > scores.second) CurrentGame.round.player1 else CurrentGame.round.player2 }").setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialogInterface, i ->
                supportFragmentManager.beginTransaction().remove(fragment).commit()
            }).show()

            return
        }

        if (CurrentGame.completedEnds() == 3 && CurrentGame.playersAtSameStage()) { // continue as normal

            supportFragmentManager.beginTransaction().remove(fragment!!).commit()
            val secondRound = SecondRoundScoreInput(this)
            supportFragmentManager.beginTransaction().replace(mainActivityLayout.id, secondRound).addToBackStack(null).commit()
            return
        }

        if (CurrentGame.completedEnds() == 5 && scores.first == scores.second) { // shootoff time :)

            supportFragmentManager.beginTransaction().remove(fragment).commit()
            val secondRound = ShootoffRoundScoreInput(this)
            supportFragmentManager.beginTransaction().replace(mainActivityLayout.id, secondRound).addToBackStack(null).commit()
            return
        }
    }

    fun gameOver(scores : Pair<Int, Int>) = (scores.first >= 6 || scores.second >= 6)
}
