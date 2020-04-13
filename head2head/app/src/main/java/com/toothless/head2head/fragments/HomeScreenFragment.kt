package com.toothless.head2head.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.toothless.head2head.GameManager
import com.toothless.head2head.MainActivity
import com.toothless.head2head.R
import com.toothless.head2head.fragments.name.NameInputFragment
import com.toothless.head2head.fragments.saved.ViewSavedRounds
import com.toothless.head2head.scoreInput.ScoreInputKeyboard
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.home_page.*

class HomeScreenFragment(private val mainActivity: MainActivity) : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startNormalGame.setOnClickListener {
            startGame(2)
        }

        startAIGame.setOnClickListener {
            startGame(1)
        }

        viewGames.setOnClickListener {
            activity?.supportFragmentManager!!.beginTransaction().replace(mainActivity.mainActivityLayout.id, ViewSavedRounds(mainActivity)).addToBackStack(null).commit()
        }
    }

    private fun startGame(players : Int)
    {
        ScoreInputKeyboard.assignEvents()
        GameManager.isAiGame = players != 2 // if there isn't 2 players then its an ai game
        val nameInput = NameInputFragment(players, mainActivity)
        activity?.supportFragmentManager!!.beginTransaction().replace(this.id, nameInput).addToBackStack(null).commit()
    }
}