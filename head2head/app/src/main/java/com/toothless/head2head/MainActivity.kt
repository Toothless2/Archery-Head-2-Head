package com.toothless.head2head

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.toothless.head2head.ai.AIManager
import com.toothless.head2head.events.EventBus
import com.toothless.head2head.events.data.ContinueGameEvent
import com.toothless.head2head.events.data.GameOverEvent
import com.toothless.head2head.events.data.StartGameEvent
import com.toothless.head2head.fragments.*
import com.toothless.head2head.save.SaveRound
import com.toothless.head2head.scoreInput.ScoreInputKeyboard
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val startGameEventHandler : (StartGameEvent) -> Unit = { (fragment, name1, name2) -> startGame(fragment, name1, name2)}
    private val continueGameEventHandler : (ContinueGameEvent) -> Unit = {continueGame(it.fragment)}
    private val gameOverEventHandler : (GameOverEvent) -> Unit = { if(GameManager.gameOver()) gameOver(it.fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        EventBus.continueGameEvent += continueGameEventHandler
        EventBus.startGameEvent += startGameEventHandler
        EventBus.gameOverEvent += gameOverEventHandler

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
        ScoreInputKeyboard.assignEvents()
        GameManager.isAiGame = players != 2 // if there isn't 2 players then its an ai game
        val nameInput = NameInputFragment(players, this)
        supportFragmentManager.beginTransaction().add(mainActivityLayout.id, nameInput).addToBackStack(null).commit()
    }

    private fun startGame(lastFragment : Fragment, name1: String, name2: String)
    {
        ScoreInputKeyboard.assignEvents()
        GameManager.setupRound(name1, name2)
        supportFragmentManager.beginTransaction().remove(lastFragment).commit()
        val firstRoundFragment = FirstRoundScoreInput(this)
        supportFragmentManager.beginTransaction().add(mainActivityLayout.id, firstRoundFragment).addToBackStack(null).commit()
    }

    private fun gameOver(fragment: Fragment)
    {
        supportFragmentManager.beginTransaction().remove(fragment).commit()
        supportFragmentManager.beginTransaction().add(mainActivityLayout.id, GameWinFragment()).addToBackStack(null).commit()
    }

    private fun continueGame(fragment: Fragment) {

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
        return  if(GameManager.isAiGame) ShootoffAIInput(this) else ShootoffPlayersInput(this)
    }
}