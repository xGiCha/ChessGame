package gr.repo.chessgame.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import gr.repo.chessgame.KnightPath
import gr.repo.chessgame.Point
import gr.repo.chessgame.R
import gr.repo.chessgame.chess.ChessDelegate
import gr.repo.chessgame.chess.ChessGame
import gr.repo.chessgame.data.ChessPiece
import gr.repo.chessgame.data.Chessman
import gr.repo.chessgame.data.Player
import gr.repo.chessgame.data.Square
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

    private var startingPointChessPieceIsCreated = false
    private var endingPointChessPieceIsCreated = false
    private var startingPointChessPiecePositionX = 0
    private var startingPointChessPiecePositionY = 0
    private lateinit var canvas: Canvas
    private var mCol = -1
    private var mRow = -1
    var hashMap: HashMap<Int, Point> = HashMap<Int, Point>()

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

        val col = ((event.x - originX) / cellSide).toInt()
        val row = 7 - ((event.y - originY) / cellSide).toInt()

        if (endingPointChessPieceIsCreated && (startingPointChessPiecePositionX != col || startingPointChessPiecePositionY != row)) {
            ChessGame.addPiece(ChessPiece(col, row, Player.WHITE, Chessman.KNIGHT, R.drawable.knight_black))
            endingPointChessPieceIsCreated = false
            hashMap = KnightPath.pathPoints(startingPointChessPiecePositionX, startingPointChessPiecePositionY, col, row)
        }
        if (!startingPointChessPieceIsCreated) {
            ChessGame.addPiece(ChessPiece(col, row, Player.WHITE, Chessman.KNIGHT, R.drawable.knight_white))
            startingPointChessPieceIsCreated = true
            endingPointChessPieceIsCreated = true
            startingPointChessPiecePositionX = col
            startingPointChessPiecePositionY = row
        }
        invalidate()

        return true
    }

    private fun drawPieces(canvas: Canvas) {
        for (row in 0 until 8)
            for (col in 0 until 8)
                chessDelegate?.pieceAt(Square(col, row))?.let { piece ->
                    drawPieceAt(canvas, col, row, piece.resID)
                }

    }

    private fun drawPieceAt(canvas: Canvas, col: Int, row: Int, resID: Int) =
            canvas.drawBitmap(bitmaps[resID]!!, null, RectF(originX + col * cellSide, originY + (7 - row) * cellSide, originX + (col + 1) * cellSide, originY + ((7 - row) + 1) * cellSide), paint)

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
        canvas.drawRect(originX + col * cellSide, originY + row * cellSide, originX + (col + 1) * cellSide, originY + (row + 1) * cellSide, paint)
    }

    private fun drawSquare(canvas: Canvas) {

        for (key in hashMap.entries) {
            mCol = key.value.x
            mRow = key.value.y
//            paint.color = Color.parseColor(key.value.color)
            paint.color = orange
            canvas.drawRect(mCol * cellSide + originX, 7 * cellSide - mRow * cellSide - originY, (mCol + 1) * cellSide + originX, 7 * cellSide - (mRow - 1) * cellSide - originY, paint)

        }
    }

    fun setChessPieceIsCreated(chessPieceIsCreated: Boolean) {
        this.startingPointChessPieceIsCreated = chessPieceIsCreated
        this.endingPointChessPieceIsCreated = chessPieceIsCreated
    }

    fun resetPoints() {
        mCol = -1
        mRow = -1
        hashMap = HashMap<Int, Point>()
    }

}
