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

    fun removeRound(roundId : Int)
    {
        json.getJSONArray("rounds").remove(roundId)
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