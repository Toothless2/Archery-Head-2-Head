package com.toothless.head2head.ai

import android.content.Context
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset
import kotlin.math.roundToInt
import kotlin.random.Random

object GetAIData {

    lateinit var aiData : JSONObject

    fun setupAI(ctx : Context)
    {
        aiData = getJsonData(ctx)
    }

    fun getAINames() : List<String>
    {
        val names = mutableListOf<String>()

        val ais = aiData.getJSONArray("ais")

        for (i in 0 until ais.length())
            names.add((ais[i] as JSONObject).getString("name"))

        return names
    }

    fun getAIScore(aiId : Int, currentScores : Pair<Int, Int>, nScoresToGenerate : Int) : List<Int> {
        val ai = aiData.getJSONArray("ais").getJSONObject(aiId)
        val scores = mutableListOf<Int>()

        val score = ai.getDouble("score")
        val endAverage = score / 20.0
        val arrowAverage = endAverage / 3
        val variance = ai.getDouble("variance")
        val arrowVariance = variance / 3.0
        val throwMultiplyer = (if (currentScores.first >= currentScores.second) 0.01 else (currentScores.second - (currentScores.first + 1))/10.0)
        val throwChance = ai.getDouble("throwChance") * throwMultiplyer

        for (i in 0 until nScoresToGenerate) {
            var score = Random.nextDouble((arrowAverage - arrowVariance) * (1-throwChance), arrowAverage + 1)
//            score *= 1 - throwChance
//            score += arrowAverage
            scores.add(if (score.roundToInt() > 10) 10 else if (score.roundToInt() < 0) 0 else score.roundToInt())
        }

        return scores.sorted().reversed()
    }

    fun getAIID(aiName : String) : Int
    {
        val ais = aiData.getJSONArray("ais")

        for (i in 0 until ais.length())
            if((ais[i] as JSONObject).getString("name").equals(aiName))
                return i

        return -1
    }

    private fun getJsonData(ctx : Context) : JSONObject
    {
        lateinit var json : String
        try {
            val aiFile = ctx.assets.open("ai.json")
            val size = aiFile.available()
            val buffer = ByteArray(size)

            aiFile.read(buffer)
            aiFile.close()

            json = String(buffer, Charset.defaultCharset())
        }
        catch (e : IOException)
        {
            e.printStackTrace()
        }
        return JSONObject(json)
    }
}