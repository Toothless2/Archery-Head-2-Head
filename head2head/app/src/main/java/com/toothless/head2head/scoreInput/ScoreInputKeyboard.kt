package com.toothless.head2head.scoreInput

import android.util.Log
import android.view.View
import android.widget.Button
import com.toothless.head2head.R
import kotlinx.android.synthetic.main.layout_score_input_keyboard.view.*

class ScoreInputKeyboard(val view: View) {

    private var scores = mutableListOf<Int>()
    fun setButtonBehaviour()
    {
        view.findViewById<Button>(R.id.tenIn).setOnClickListener {
            Log.d("ten", "ten")
            addButtonScore(10) }
    }

    private fun addButtonScore(score : Int) : Boolean
    {
        if(scores.size >= 3)
            return false

        scores.add(score)
        updateDisplay()

        return true
    }

    private fun updateDisplay()
    {
        if(scores.size == 1)
            view.out1.text = scores[0].toString()

        if(scores.size == 2)
            view.findViewById<Button>(R.id.out2).text = scores[1]?.toString()

        if(scores.size == 3)
            view.findViewById<Button>(R.id.out3).text = scores[2]?.toString()
    }
}