package com.toothless.head2head.events.data

import androidx.fragment.app.Fragment

data class KeyboardEvent(val parent : Fragment, val player : Int, val end : Int, val maxScores : Int = 3) {
}