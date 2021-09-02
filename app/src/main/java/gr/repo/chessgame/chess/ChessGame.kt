package gr.repo.chessgame.chess

import gr.repo.chessgame.data.ChessPiece
import gr.repo.chessgame.data.Square

object ChessGame {
    private var piecesBox = mutableSetOf<ChessPiece>()

    init {
        reset()
    }

    fun clear() {
        piecesBox.clear()
    }

    fun addPiece(piece: ChessPiece) {
        piecesBox.add(piece)
    }


    fun reset() {
        clear()
    }

    fun pieceAt(square: Square): ChessPiece? {
        return pieceAt(square.col, square.row)
    }

    private fun pieceAt(col: Int, row: Int): ChessPiece? {
        for (piece in piecesBox) {
            if (col == piece.col && row == piece.row) {
                return  piece
            }
        }
        return null
    }
}