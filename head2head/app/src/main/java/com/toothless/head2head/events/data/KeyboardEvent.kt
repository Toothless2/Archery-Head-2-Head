package com.toothless.head2head.events.data

import androidx.fragment.app.Fragment
import com.toothless.head2head.enums.EScoreInputType

data class KeyboardEvent(val parent : Fragment, val player : Int, val end : Int, val maxScores : EScoreInputType = EScoreInputType.NORMALEND)