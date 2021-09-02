package gr.repo.chessgame

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.goldenthumb.android.chess.ChessPiece
import com.goldenthumb.android.chess.Square
import java.util.HashMap
import kotlin.math.min

class ChessView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val scaleFactor = 1.0f
    private var originX = 20f
    private var originY = 200f
    private var cellSide = 130f
    private val lightColor = Color.parseColor("#EEEEEE")
    private val darkColor = Color.parseColor("#BBBBBB")
    private val orange = Color.parseColor("#ffa500")
    private val imgResIDs = setOf(
            R.drawable.knight_black,
            R.drawable.knight_white,
    )
    private val bitmaps = mutableMapOf<Int, Bitmap>()
    private val paint = Paint()

    private var movingPieceBitmap: Bitmap? = null
    private var movingPiece: ChessPiece? = null
    private var fromCol: Int = -1
    private var fromRow: Int = -1
    private var movingPieceX = -1f
    private var movingPieceY = -1f
    private var startingPointChessPieceIsCreated = false
    private var endingPointChessPieceIsCreated = false
    private var startingPointChessPiecePositionX = 0
    private var startingPointChessPiecePositionY = 0
    private var endingPointChessPiecePositionX = 0
    private var endingPointChessPiecePositionY = 0
    private lateinit var canvas: Canvas
    private var mCol = -1
    private var mRow = -1
    var hashMap : HashMap<Int, Point> = HashMap<Int, Point> ()

    var chessDelegate: ChessDelegate? = null

    init {
        loadBitmaps()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val smaller = min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(smaller, smaller)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        val chessBoardSide = min(width, height) * scaleFactor
        cellSide = chessBoardSide / 8f
        originX = (width - chessBoardSide) / 2f
        originY = (height - chessBoardSide) / 2f
        this.canvas = canvas
        drawChessboard(canvas)
        drawPieces(canvas)
        drawSquare(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                fromCol = ((event.x - originX) / cellSide).toInt()
                fromRow = 7 - ((event.y - originY) / cellSide).toInt()

//                chessDelegate?.pieceAt(Square(fromCol, fromRow))?.let {
//                    movingPiece = it
//                    movingPieceBitmap = bitmaps[it.resID]
//                }
            }
            MotionEvent.ACTION_MOVE -> {
                movingPieceX = event.x
                movingPieceY = event.y
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                val col = ((event.x - originX) / cellSide).toInt()
                val row = 7 - ((event.y - originY) / cellSide).toInt()
                val event = 7*cellSide - row * cellSide - originY
                val ee = event
                if (endingPointChessPieceIsCreated && (startingPointChessPiecePositionX != col || startingPointChessPiecePositionY != row)) {
                    ChessGame.addPiece(ChessPiece(col, row, Player.WHITE, Chessman.KNIGHT, R.drawable.knight_black))
                    endingPointChessPieceIsCreated = false
                    endingPointChessPiecePositionX = col
                    endingPointChessPiecePositionY = row
                    hashMap = KnightPath.pathPoints(startingPointChessPiecePositionX, startingPointChessPiecePositionY, col, row)
                    println("aaaaaaa1 ${col}, ${row}")
                }
                if (!startingPointChessPieceIsCreated) {
                    ChessGame.addPiece(ChessPiece(col, row, Player.WHITE, Chessman.KNIGHT, R.drawable.knight_white))
                    startingPointChessPieceIsCreated = true
                    endingPointChessPieceIsCreated = true
                    startingPointChessPiecePositionX = col
                    startingPointChessPiecePositionY = row
                    println("aaaaaaa ${col}, ${row}")
                }
//                if (fromCol != col || fromRow != row) {
//                    chessDelegate?.movePiece(Square(fromCol, fromRow), Square(col, row))
//                }
                movingPiece = null
                movingPieceBitmap = null
                invalidate()
            }
        }
        return true
    }

    private fun drawPieces(canvas: Canvas) {
        for (row in 0 until 8)
            for (col in 0 until 8)
                chessDelegate?.pieceAt(Square(col, row))?.let { piece ->
                    if (piece != movingPiece) {
                        drawPieceAt(canvas, col, row, piece.resID)
                    }
                }

        movingPieceBitmap?.let {
            canvas.drawBitmap(it, null, RectF(movingPieceX - cellSide/2, movingPieceY - cellSide/2,movingPieceX + cellSide/2,movingPieceY + cellSide/2), paint)
        }
    }

    private fun drawPieceAt(canvas: Canvas, col: Int, row: Int, resID: Int) =
        canvas.drawBitmap(bitmaps[resID]!!, null, RectF(originX + col * cellSide,originY + (7 - row) * cellSide,originX + (col + 1) * cellSide,originY + ((7 - row) + 1) * cellSide), paint)

    private fun loadBitmaps() =
        imgResIDs.forEach { imgResID ->
            bitmaps[imgResID] = BitmapFactory.decodeResource(resources, imgResID)
        }

    private fun drawChessboard(canvas: Canvas) {
        for (row in 0 until 8)
            for (col in 0 until 8)
                drawSquareAt(canvas, col, row, (col + row) % 2 == 1)
    }

    private fun drawSquareAt(canvas: Canvas, col: Int, row: Int, isDark: Boolean) {
        paint.color = if (isDark) darkColor else lightColor
        canvas.drawRect(originX + col * cellSide, originY + row * cellSide, originX + (col + 1)* cellSide, originY + (row + 1) * cellSide, paint)
    }

    private fun drawSquare(canvas: Canvas) {

        for(key in hashMap.entries){
            println("Element at key $key : ${hashMap[key]}")
            mCol = key.value.x
            mRow = key.value.y
            paint.color = Color.parseColor(key.value.color)
            canvas.drawRect(mCol*cellSide + originX, 7*cellSide - mRow * cellSide - originY, (mCol + 1)*cellSide + originX, 7*cellSide - (mRow - 1) * cellSide - originY, paint)

        }
    }

    fun setChessPieceIsCreated(chessPieceIsCreated: Boolean){
        this.startingPointChessPieceIsCreated = chessPieceIsCreated
        this.endingPointChessPieceIsCreated = chessPieceIsCreated
    }

    fun resetPoints(){
        mCol = -1
        mRow = -1
        hashMap = HashMap<Int, Point> ()
    }

}
