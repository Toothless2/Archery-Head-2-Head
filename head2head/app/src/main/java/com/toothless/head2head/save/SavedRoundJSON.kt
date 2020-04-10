package com.toothless.head2head.save

data class SavedRoundJSON(val id:Int,
                          val player1:String, val player2: String,
                          val p1SetPoints : Int, val p2SetPoints : Int,
                          val isAiGame : Boolean,
                          val p1totalScore : Int, val p2totalScore : Int,
                          val p110 : Int, val p210 : Int,
                          val p19 : Int, val p29 : Int,
                          val p1Hits : Int, val p2Hits : Int) {}