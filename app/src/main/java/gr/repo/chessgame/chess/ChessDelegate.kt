package gr.repo.chessgame.chess

import gr.repo.chessgame.data.ChessPiece
import gr.repo.chessgame.data.Square

interface ChessDelegate {
    fun pieceAt(square: Square) : ChessPiece?
}