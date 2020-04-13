package com.toothless.head2head

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import com.toothless.head2head.ai.AIManager
import com.toothless.head2head.events.EventBus
import com.toothless.head2head.events.data.ContinueGameEvent
import com.toothless.head2head.events.data.GameOverEvent
import com.toothless.head2head.events.data.StartGameEvent
import com.toothless.head2head.fragments.*
import com.toothless.head2head.fragments.name.NameInputFragment
import com.toothless.head2head.fragments.saved.ViewSavedRounds
import com.toothless.head2head.fragments.scoreInput.FirstRoundScoreInput
import com.toothless.head2head.fragments.scoreInput.SecondRoundScoreInput
import com.toothless.head2head.fragments.scoreInput.ShootoffAIInput
import com.toothless.head2head.fragments.scoreInput.ShootoffPlayersInput
import com.toothless.head2head.save.SaveRound
import com.toothless.head2head.scoreInput.ScoreInputKeyboard
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val startGameEventHandler : (StartGameEvent) -> Unit = { (fragment, name1, name2) -> startGame(fragment, name1, name2)}
    private val continueGameEventHandler : (ContinueGameEvent) -> Unit = {continueGame(it.fragment)}
    private val gameOverEventHandler : (GameOverEvent) -> Unit = { if(GameManager.gameOver()) gameOver(it.fragment) }

    private lateinit var homeFragment : HomeScreenFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        EventBus.continueGameEvent += continueGameEventHandler
        EventBus.startGameEvent += startGameEventHandler
        EventBus.gameOverEvent += gameOverEventHandler

        GameManager.reset()
        AIManager.setupAI(this)
        SaveRound.setupSave(this)

        homeFragment = HomeScreenFragment(this)

        supportFragmentManager.beginTransaction().add(mainActivityLayout.id,homeFragment).commit()
    }

    private fun startGame(lastFragment : Fragment, name1: String, name2: String)
    {
        ScoreInputKeyboard.assignEvents()
        GameManager.setupRound(name1, name2)
        val firstRoundFragment = FirstRoundScoreInput(this)
        supportFragmentManager.beginTransaction().replace(mainActivityLayout.id, firstRoundFragment).commit()
    }

    private fun gameOver(fragment: Fragment)
    {
        Thread {
            SystemClock.sleep(500)
            supportFragmentManager.beginTransaction().remove(fragment).commit()
            supportFragmentManager.beginTransaction().replace(mainActivityLayout.id, GameWinFragment()).commit()
        }.start()
    }

    private fun continueGame(fragment: Fragment) {

        var nextScreen: Fragment? = null

        if (GameManager.completedEnds() == 3 && GameManager.playersAtSameStage()) // continue as normal
            nextScreen = SecondRoundScoreInput(this)
        else if (GameManager.completedEnds() >= 5)
            nextScreen = shootoffScreenLoad()

        if (nextScreen == null)
            return

        Thread {
            SystemClock.sleep(500)
            supportFragmentManager.beginTransaction().replace(mainActivityLayout.id, nextScreen).commit()
        }.start()
    }

    private fun shootoffScreenLoad() : Fragment
    {
        return  if(GameManager.isAiGame) ShootoffAIInput(this) else ShootoffPlayersInput(this)
    }

    override fun onBackPressed() {
        if(supportFragmentManager.fragments.size < 2) // stops fragments being stacked ontop of each other when the back button is pressed (because android is shit)
            for (i in supportFragmentManager.fragments)
                supportFragmentManager.beginTransaction().remove(i).commit()
        super.onBackPressed()
    }
}
