package com.toothless.head2head.fragments.saved

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toothless.head2head.R
import com.toothless.head2head.save.SavedRoundJSON
import kotlinx.android.synthetic.main.save_round_match_breakdown_fragment.*

class SaveRoundMatchBreakdown(private var rounds : SavedRoundJSON) : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.save_round_match_breakdown_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedScorePlayer1Name.text = rounds.player1
        savedScorePlayer2Name.text = rounds.player2

        savedScorePlayer1SetPoints.text = rounds.p1SetPoints.toString()
        savedScorePlayer2SetPoints.text = rounds.p2SetPoints.toString()

        savedScorePlayer1Score.text = rounds.p1totalScore.toString()
        savedScorePlayer2Score.text = rounds.p2totalScore.toString()

        savedScorePlayer1Tens.text = rounds.p110.toString()
        savedScorePlayer2Tens.text = rounds.p210.toString()

        savedScorePlayer1Nines.text = rounds.p19.toString()
        savedScorePlayer2Nines.text = rounds.p29.toString()

        savedScorePlayer1Hits.text = rounds.p1Hits.toString()
        savedScorePlayer2Hits.text = rounds.p2Hits.toString()
    }
}