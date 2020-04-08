package com.toothless.head2head.ai

import android.content.Context
import org.json.JSONObject
import java.io.IOException
import java.nio.Buffer
import java.nio.charset.Charset

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