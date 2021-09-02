package gr.repo.chessgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.goldenthumb.android.chess.ChessPiece
import com.goldenthumb.android.chess.Square
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity(), ChessDelegate {

    private lateinit var chessView: ChessView
    private lateinit var resetButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resetButton = findViewById<Button>(R.id.reset_button)
        chessView = findViewById<ChessView>(R.id.chess_view)
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