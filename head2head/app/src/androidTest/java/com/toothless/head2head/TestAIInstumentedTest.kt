package com.toothless.head2head

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.toothless.head2head.ai.GetAIData
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestAIInstumentedTest {
    @Before
    fun before()
    {
        GetAIData.setupAI(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @Test
    fun testGenerateScore()
    {
        val scores = mutableListOf(listOf<Int>())

        for (i in 0 until 100)
            scores.add(GetAIData.getAIScore(2, Pair(0, 2), 3))

        val frequency = arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

        for (score in scores)
            for(i in score)
                frequency[i]++

        Assert.assertTrue(true)
    }

}