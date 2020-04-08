package com.toothless.head2head.fragments

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.toothless.head2head.MainActivity
import com.toothless.head2head.R
import com.toothless.head2head.ai.GetAIData
import com.toothless.head2head.data.CurrentGame
import kotlinx.android.synthetic.main.name_input_fragment.*

class NameInputFragment(val numberOfPlayers : Int, val mainActivity: MainActivity) : Fragment() {

    private lateinit var name1: String
    private lateinit var name2: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.name_input_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(CurrentGame.aiGame)
            {
                player2NameInput.visibility = View.GONE
                aiNameSelector.visibility = View.VISIBLE

                val spinValues = ArrayAdapter(mainActivity, android.R.layout.simple_spinner_item, GetAIData.getAINames())
                spinValues.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                aiNameSelector.adapter = spinValues
            }

        confirm.setOnClickListener {
            submitName()
        }
    }

    fun submitName() {
        if (!player1NameInput.text.isNullOrBlank())
            name1 = player1NameInput.text.toString()
        else {
            Toast.makeText(mainActivity, "Please input a name for player 1", Toast.LENGTH_LONG)
                .show()
            return
        }

        if (!CurrentGame.aiGame) {
            if (numberOfPlayers == 2 && !player2NameInput.text.isNullOrBlank())
                name2 = player2NameInput.text.toString()
            else {
                Toast.makeText(mainActivity, "Please input a name for player 2", Toast.LENGTH_LONG)
                    .show()
                return
            }
        }
        else
        {
            name2 = aiNameSelector.selectedItem.toString()
            CurrentGame.selectedAi = GetAIData.getAIID(name2)
        }

        if(!this::name2.isInitialized){
            Toast.makeText(mainActivity, "Please select and AI", Toast.LENGTH_LONG)
                .show()
            return
        }

        CurrentGame.startGame(name1, name2)

        mainActivity.continueGame(this)
    }
}
