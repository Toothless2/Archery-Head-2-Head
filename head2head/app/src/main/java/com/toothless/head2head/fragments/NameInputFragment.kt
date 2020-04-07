package com.toothless.head2head.fragments

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.toothless.head2head.MainActivity
import com.toothless.head2head.R
import com.toothless.head2head.data.CurrentGame
import kotlinx.android.synthetic.main.name_input_fragment.*

class NameInputFragment(val numberOfPlayers : Int, val mainActivity: MainActivity) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.name_input_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(numberOfPlayers == 1)
            player2NameInput.visibility = View.GONE

        confirm.setOnClickListener {
            submitName()
        }
    }

    fun submitName() {
        var name1 = ""

        if (!player1NameInput.text.isNullOrBlank())
            name1 = player1NameInput.text.toString()
        else {
            Toast.makeText(mainActivity, "Please input a name for player 1", Toast.LENGTH_LONG)
                .show()
            return
        }

        var name2 = ""
        if (numberOfPlayers == 2 && !player2NameInput.text.isNullOrBlank())
            name2 = player2NameInput.text.toString()
        else {
            Toast.makeText(mainActivity, "Please input a name for player 2", Toast.LENGTH_LONG)
                .show()
            return
        }

        CurrentGame.startGame(name1, name2)

        mainActivity.continueGame(this)
    }
}
