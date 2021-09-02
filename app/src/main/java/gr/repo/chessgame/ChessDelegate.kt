package gr.repo.chessgame

import com.goldenthumb.android.chess.ChessPiece
import com.goldenthumb.android.chess.Square

interface ChessDelegate {
    fun pieceAt(square: Square) : ChessPiece?
}