package com.toothless.head2head.viewmodels.savedscore

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.LocusId
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.toothless.head2head.R
import com.toothless.head2head.save.SaveRound
import com.toothless.head2head.save.SavedRoundJSON
import kotlinx.android.synthetic.main.saved_round_display.view.*

class SavedScoreViewModel(private val ctx : Activity, val savedScores : MutableList<SavedRoundJSON>) : RecyclerView.Adapter<SavedScoreViewHolder>() {

    private val inflater = LayoutInflater.from(ctx)
    override fun getItemCount() = savedScores.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SavedScoreViewHolder(inflater.inflate(R.layout.saved_round_display, parent, false))


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SavedScoreViewHolder, position: Int) {
        holder.roundId = savedScores[position].id
        holder.playerNames.text = "${savedScores[position].player1} V ${savedScores[position].player2}"
        holder.scores.text = "${savedScores[position].p1Score} - ${savedScores[position].p2Score}"
        holder.type.setImageResource(if (savedScores[position].isAiGame) R.drawable.ic_aiicon2 else R.drawable.ic_personicon)

        holder.layout.setOnClickListener {
            AlertDialog.Builder(ctx).setMessage("Delete this saved round?").setPositiveButton(android.R.string.yes, DialogInterface.OnClickListener { _, _ ->
                for (i in 0 until savedScores.size)
                    if (savedScores[i].id == holder.roundId) {
                        deleteSavedRound(holder.roundId)
                        savedScores.removeAt(i)
                        break
                    }

                notifyDataSetChanged()
            }).show()
        }
    }

    fun deleteSavedRound(roundId : Int)
    {
        SaveRound.removeRound(roundId)
        SaveRound.writeJsonData(ctx)
    }
}

class SavedScoreViewHolder(view: View) : RecyclerView.ViewHolder(view)
{
    var roundId = -1
    val playerNames = view.savedGamePlayerNames
    val scores = view.savedGamePlayerScores
    val type = view.savedGameGameType
    val layout = view.savedGameLinLayout
}