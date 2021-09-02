package gr.repo.chessgame.data

import gr.repo.chessgame.data.Chessman
import gr.repo.chessgame.data.Player

data class ChessPiece(val col: Int, val row: Int, val player: Player, val chessman: Chessman, val resID: Int)