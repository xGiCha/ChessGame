package com.goldenthumb.android.chess

import gr.repo.chessgame.Chessman
import gr.repo.chessgame.Player

data class ChessPiece(val col: Int, val row: Int, val player: Player, val chessman: Chessman, val resID: Int) {
}