package com.toothless.head2head.save

import android.content.Context
import com.toothless.head2head.round.Round
import org.json.JSONObject
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.nio.charset.Charset

object SaveRound {
    lateinit var json : JSONObject
    private set

    fun setupSave(ctx : Context)
    {
        json = getJsonData(ctx)
    }

    fun addRound(round : Round)
    {
        val scores = round.getMatchScores()
        val jsonString = "{ \"id\":${json.getJSONArray("rounds").length()}," +
                "\"player1\":\"${round.player1}\"," +
                "\"player2\": \"${round.player2}\"," +
                "\"scores\": [${scores.first}, ${scores.second}]," +
                "\"aiGame\": ${round.aiGame}," +
                "\"totalScore\" : [${round.scores.sumBy { it.p1End.sum() }}, ${round.scores.sumBy { it.p2End.sum() }}]," +
                "\"tens\" : [${round.scores.sumBy { it.p1End.count { it == 10 } }}, ${round.scores.sumBy { it.p2End.count { it == 10 } }}]," +
                "\"nines\" : [${round.scores.sumBy { it.p1End.count { it == 9 } }}, ${round.scores.sumBy { it.p2End.count { it == 9 } }}]," +
                "\"hits\" : [${round.scores.sumBy { it.p1End.count { it != 0 } }}, ${round.scores.sumBy { it.p2End.count { it != 0 } }}]}"

        val jsonObj = JSONObject(jsonString)

        json.getJSONArray("rounds").put(jsonObj)
    }

    fun getSavedRoundFormatted() : List<SavedRoundJSON>
    {
        val rounds = json.getJSONArray("rounds")
        val returnList  = mutableListOf<SavedRoundJSON>()

        for(i in 0 until rounds.length()) {
            val temp = rounds[i] as JSONObject
            val tempEndScores = temp.getJSONArray("scores")
            val tempTotalScore = temp.getJSONArray("totalScore")
            val tempTens = temp.getJSONArray("tens")
            val tempNines = temp.getJSONArray("nines")
            val tempHits = temp.getJSONArray("hits")
            returnList.add(SavedRoundJSON(temp.getInt("id"),
                    temp.getString("player1"), temp.getString("player2"),
                    tempEndScores[0] as Int, tempEndScores[1] as Int,
                    temp.getBoolean("aiGame"),
                    tempTotalScore[0] as Int, tempTotalScore[1] as Int,
                    tempTens[0] as Int, tempTens[1] as Int,
                    tempNines[0] as Int, tempNines[1] as Int,
                    tempHits[0] as Int, tempHits[1] as Int))
        }

        return returnList
    }

    fun removeRound(roundId : Int)
    {
        var roundPos = -1

        for (i in 0 until json.getJSONArray("rounds").length())
        {
            if((json.getJSONArray("rounds")[i] as JSONObject).getInt("id") == roundId)
            {
                roundPos = i
                break
            }
        }

        if(roundPos != -1)
            json.getJSONArray("rounds").remove(roundPos)
    }

    fun clearAllData(ctx:Context)
    {
        try {
            val check = File(ctx.applicationInfo.dataDir + "/rounds/savedrounds.json")
            if (!check.exists())
                return

            check.delete()
        }catch (e : IOException)
        {
            e.printStackTrace()
        }
    }

    fun writeJsonData(ctx : Context)
    {
        try {
            val check = File(ctx.applicationInfo.dataDir + "/rounds/")
            if (!check.exists())
                check.mkdir()

            val file = FileWriter(check.absolutePath + "/savedrounds.json")
            file.write(json.toString())
            file.flush()
            file.close()
        }catch (e : IOException)
        {
            e.printStackTrace()
        }
    }

    private fun getJsonData(ctx : Context) : JSONObject
    {
        lateinit var json : String
        try {
            val check = File(ctx.applicationInfo.dataDir + "/rounds/savedrounds.json")
            if (!check.exists()) {
                val aiFile = ctx.assets.open("savedrounds.json")
                val size = aiFile.available()
                val buffer = ByteArray(size)

                aiFile.read(buffer)
                aiFile.close()

                json = String(buffer, Charset.defaultCharset())
            }else
            {
                val file = FileReader(check)
                json = ""
                for (l in file.readLines())
                    json += l
            }
        }
        catch (e : IOException)
        {
            e.printStackTrace()
        }
        return JSONObject(json)
    }
}