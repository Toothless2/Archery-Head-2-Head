package com.toothless.head2head.save

import android.content.Context
import com.toothless.head2head.data.Round
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

    fun addRound(round : Round, aiGame : Boolean)
    {
        val scores = round.getMatchScores()
        val jsonString = "{ \"id\":${json.getJSONArray("rounds").length()}," +
                "\"player1\":\"${round.player1}\"," +
                "\"player2\": \"${round.player2}\"," +
                "\"scores\": [${scores.first}, ${scores.second}]," +
                "\"aiGame\": $aiGame }"

        val jsonObj = JSONObject(jsonString)

        json.getJSONArray("rounds").put(jsonObj)
    }

    fun getSavedRoundFormatted() : List<SavedRoundJSON>
    {
        val rounds = json.getJSONArray("rounds")
        val returnList  = mutableListOf<SavedRoundJSON>()

        for(i in 0 until rounds.length())
        {
            val temp = rounds[i] as JSONObject
            val tempScores = temp.getJSONArray("scores")
            returnList.add(SavedRoundJSON(temp.getInt("id"), temp.getString("player1"), temp.getString("player2"), tempScores[0] as Int, tempScores[1] as Int, temp.getBoolean("aiGame")))
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