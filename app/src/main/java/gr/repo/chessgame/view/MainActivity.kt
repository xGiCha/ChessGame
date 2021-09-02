package gr.repo.chessgame.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import gr.repo.chessgame.chess.ChessDelegate
import gr.repo.chessgame.chess.ChessGame
import gr.repo.chessgame.R
import gr.repo.chessgame.data.ChessPiece
import gr.repo.chessgame.data.Square

class MainActivity : AppCompatActivity(), ChessDelegate {

    private lateinit var chessView: ChessView
    private lateinit var resetButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resetButton = findViewById(R.id.reset_button)
        chessView = findViewById(R.id.chess_view)
        chessView.chessDelegate = this

        resetButton.setOnClickListener {
            chessView.resetPoints()
            ChessGame.reset()
            chessView.invalidate()
            chessView.setChessPieceIsCreated(false)
        }
    }

    override fun pieceAt(square: Square): ChessPiece? = ChessGame.pieceAt(square)
}