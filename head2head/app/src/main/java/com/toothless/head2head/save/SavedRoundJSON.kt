package com.toothless.head2head.save

data class SavedRoundJSON(val id:Int, val player1:String, val player2: String, val p1Score : Int, val p2Score : Int, val isAiGame : Boolean) {}