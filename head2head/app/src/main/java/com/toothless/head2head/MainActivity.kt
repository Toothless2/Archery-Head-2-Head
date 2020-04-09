package com.toothless.head2head

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.toothless.head2head.ai.AIManager
import com.toothless.head2head.fragments.*
import com.toothless.head2head.save.SaveRound
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GameManager.reset()
        AIManager.setupAI(this)
        SaveRound.setupSave(this)

        startNormalGame.setOnClickListener {
            startGame(2)
        }

        startAIGame.setOnClickListener {
            startGame(1)
        }

        viewGames.setOnClickListener {
            supportFragmentManager.beginTransaction().add(mainActivityLayout.id, ViewSavedRounds(this)).addToBackStack(null).commit()
        }
    }

    private fun startGame(players : Int)
    {
        GameManager.isAiGame = players != 2 // if there isnt 2 players then its an ai game
        val nameInput = NameInputFragment(players, this)
        supportFragmentManager.beginTransaction().add(mainActivityLayout.id, nameInput).addToBackStack(null).commit()
    }

    fun loadFirstRound(nameInputFragment: NameInputFragment)
    {
        supportFragmentManager.beginTransaction().remove(nameInputFragment).commit()
        val firstRoundFragment = FirstRoundScoreInput(this)
        supportFragmentManager.beginTransaction().add(mainActivityLayout.id, firstRoundFragment).addToBackStack(null).commit()
    }

    fun continueGame(fragment: Fragment) {
        if (GameManager.gameOver()) {

            AlertDialog.Builder(this).setTitle("Congratulations!")
                    .setMessage("Well Done ${GameManager.getLeader()}")
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        Thread() {
                            SaveRound.addRound(GameManager.round)
                            SaveRound.writeJsonData(this)
                            GameManager.reset()
                        }.start()
                        supportFragmentManager.beginTransaction().remove(fragment).commit()
                    } // notify of who won and close the fragment when ok is pressed
                    .show()

            return
        }

        var nextScreen: Fragment? = null

        if (GameManager.completedEnds() == 3 && GameManager.playersAtSameStage()) // continue as normal
            nextScreen = SecondRoundScoreInput(this)
        else if (GameManager.completedEnds() >= 5)
            nextScreen = shootoffScreenLoad()

        if (nextScreen == null)
            return

        supportFragmentManager.beginTransaction().remove(fragment).commit()
        supportFragmentManager.beginTransaction().replace(mainActivityLayout.id, nextScreen).addToBackStack(null).commit()
    }

    private fun shootoffScreenLoad() : Fragment
    {
        lateinit var shootoff : Fragment
        if(GameManager.isAiGame)
            shootoff = ShootoffAIInput(this)
        else
            shootoff = ShootoffPlayersInput(this)

        return shootoff
    }
}
