package com.toothless.head2head

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.toothless.head2head.scoreInput.ScoreInputKeyboard
import kotlinx.android.synthetic.main.first_round_score_input_fragment.*
import kotlinx.android.synthetic.main.layout_score_input_keyboard.*
import java.util.zip.Inflater


class FirstRoundScoreInput : Fragment() {

    companion object {
        fun newInstance() = FirstRoundScoreInput()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.first_round_score_input_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        assignFunctionsToButtons()
    }

    fun assignFunctionsToButtons()
    {
        p1end1.setOnClickListener {
            Log.d("First End", "First End Clicked")
            fillOutScores(listOf(p1end1score1, p1end1score2, p1end1score3), p1end1score)
        }
    }

    fun fillOutScores(inputs : List<Button>, output : Button)
    {

        openKeyboard()
//        for (i in inputs)
//            i.text = resources.getString(R.string.ten)
//
//        output.text = (30).toString()
    }

    fun openKeyboard()
    {
//        val board = ScoreInputKeyboard()
//
//        board.show(activity!!.supportFragmentManager, "Keybopard input")
//        board.setButtonBehaviour()

        val dialog = AlertDialog.Builder(context)
        val inf = layoutInflater.inflate(R.layout.layout_score_input_keyboard, null)
        val kb = ScoreInputKeyboard(inf)
        kb.setButtonBehaviour()
        dialog.setView(inf)

        dialog.show()
    }

}
