package gr.repo.chessgame.view

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gr.repo.chessgame.KnightPath
import gr.repo.chessgame.chess.ChessDelegate
import gr.repo.chessgame.chess.ChessGame
import gr.repo.chessgame.R
import gr.repo.chessgame.adapter.MovesAdapter
import gr.repo.chessgame.data.ChessPiece
import gr.repo.chessgame.data.Square

class MainActivity : AppCompatActivity(), ChessDelegate {

    private lateinit var chessView: ChessView
    private lateinit var resetButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterMove: MovesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerV)
        resetButton = findViewById(R.id.reset_button)
        chessView = findViewById(R.id.chess_view)

        adapterMove = MovesAdapter(mutableListOf())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapterMove

        chessView.chessDelegate = this

        chessView.callbackMessage = {
            buildErrorMsg()
        }

        chessView.callbackListAdapter = {
            adapterMove.updateItems(KnightPath.listForAdapter.asReversed())
        }

        resetButton.setOnClickListener {
            resetView()
        }
    }

    override fun pieceAt(square: Square): ChessPiece? = ChessGame.pieceAt(square)

    private fun buildErrorMsg() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.no_solution_message, null)
        val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)

        val mAlertDialog = mBuilder.show()

        mAlertDialog.findViewById<TextView>(R.id.errorBtn).setOnClickListener {
            resetView()
            mAlertDialog.dismiss()
        }
        mAlertDialog.window?.setBackgroundDrawableResource(R.drawable.ic_rectangle_white_radius_24)

        // height and width from screen
        val displayMetrics = resources.displayMetrics
        val height: Int = displayMetrics.heightPixels
        val width: Int = displayMetrics.widthPixels - 150 // 80px is from figma

        //set alert dialog width
        val lp: WindowManager.LayoutParams = WindowManager.LayoutParams()
        lp.copyFrom(mAlertDialog.window?.attributes)
        lp.width = width
        mAlertDialog.window?.attributes = lp
    }

    private fun resetView(){
        adapterMove.updateItems(mutableListOf())
        chessView.resetPoints()
        ChessGame.reset()
        chessView.invalidate()
        chessView.setChessPieceIsCreated(false)
    }
}