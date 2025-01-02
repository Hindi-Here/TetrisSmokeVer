package com.example.cubres

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import kotlin.random.Random

class FigureGameplay(private val context: Context, private val tableLayout: TableLayout, private val figureScore: FigureScore) {

    private val table: Array<Array<Boolean>> = Array(20) { Array(10) { false } }
    private var figure: List<Pair<Int, Int>> = listOf()
    private var figureColor: Int = 0
    private var rowIndex: Int = 0
    private var colIndex: Int = 0
    private var gameOver: Boolean = false

    private var isPaused: Boolean = false
    private val handler = Handler(Looper.getMainLooper())

    init {
        generateGameField()
        generateFigure()
        setMotionListener()
    }

    // generation methods
    private fun generateGameField() {
        val cols = 10

        val displayMetrics = context.resources.displayMetrics
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        val size = width / cols
        var rows = height / size
        rows -= 3

        (0 until rows).forEach { i ->
            val tableRow = TableRow(context)

            (0 until cols).forEach { j ->
                val textView = TextView(context)
                textView.layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT,
                    1.0f
                )
                textView.setBackgroundResource(R.drawable.cell_border)

                textView.viewTreeObserver.addOnGlobalLayoutListener{
                    val width = textView.width
                    val layoutParams = textView.layoutParams
                    layoutParams.height = width
                    textView.layoutParams = layoutParams
                }

                tableRow.addView(textView)
            }
            tableLayout.addView(tableRow)
        }
    }

    private fun generateFigure() {
        figure = FigurePropetry.figures[Random.nextInt(FigurePropetry.figures.size)]
        figureColor = FigurePropetry.colors[Random.nextInt(FigurePropetry.colors.size)]

        val maxWidth = figure.maxOf { it.second }
        val cols = table[0].size
        colIndex = Random.nextInt(2, cols - maxWidth)
        rowIndex = 0

        drawFigure()
        animationFigure()
    }

    // methods of behavior
    private fun updateFigure(update: (TextView) -> Unit) {
        for (cell in figure)
        {
            val row = cell.first + rowIndex
            val col = cell.second + colIndex
            val tableRow = tableLayout.getChildAt(row) as TableRow
            val textView = tableRow.getChildAt(col) as TextView
            update(textView)
        }
    }

    private fun drawFigure() {
        updateFigure { textView ->
            val shadowDrawable = FigurePropetry.shadows(figureColor)
            textView.background = shadowDrawable
        }
    }

    private fun clearTrail() {
        updateFigure { it.setBackgroundResource(R.drawable.cell_border) }
    }

    private fun moveFigure(direction: Int) {
        if (canMoveX(direction))
        {
            clearTrail()
            colIndex += direction
            drawFigure()
        }
    }

    private fun fixFigure() {
        for (cell in figure)
        {
            val row = cell.first + rowIndex
            val col = cell.second + colIndex
            table[row][col] = true
        }

        if (figure.any { it.first + rowIndex == 0 })
            gameOver()

        removeFigure()
    }

    private fun rotateFigure() {
        val rotatedFigure = figure.map { (x, y) -> Pair(y, -x) }
        if (canRotate(rotatedFigure))
        {
            clearTrail()
            figure = rotatedFigure
            drawFigure()
        }
    }

    // boolean methods
    private fun canMoveX(direction: Int): Boolean {
        return figure.all { (x, y) ->
            val col = y + colIndex + direction
            col in 0 until table[0].size && !table[x + rowIndex][col]
        }
    }

    private fun canMoveDown(): Boolean {
        return figure.all { (x, y) ->
            val row = x + rowIndex + 1
            row < tableLayout.childCount && !table[row][y + colIndex]
        }
    }

    private fun canRotate(rotatedFigure: List<Pair<Int, Int>>): Boolean {
        return rotatedFigure.all { (x, y) ->
            val row = x + rowIndex
            val col = y + colIndex
            row in 0 until tableLayout.childCount && col in 0 until (tableLayout.getChildAt(0) as TableRow).childCount && !table[row][col]
        }
    }

    // event methods
    private fun animationFigure() {
        if (isPaused) return

        handler.postDelayed(object : Runnable {
            override fun run()
            {
                if (isPaused)
                {
                    handler.removeCallbacks(this)
                    return
                }

                if (canMoveDown())
                {
                    clearTrail()
                    rowIndex++
                    drawFigure()
                    handler.postDelayed(this, animationDelay())
                }
                else
                {
                    fixFigure()
                    if (!gameOver)
                        generateFigure()
                }
            }
        }, animationDelay())
    }

    fun pauseGame() {
        isPaused = true
        handler.removeCallbacksAndMessages(null)
    }

    fun resumeGame() {
        if (isPaused)
        {
            isPaused = false
            animationFigure()
        }
    }

    private fun animationDelay(): Long {
        var delay = 200L
        var score = 1000
        while  (figureScore.getScore() >= score)
        {
            delay /= 2
            score += 1000
        }
        return delay
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setMotionListener() {
        var x = 0f
        var isDragging = false

        tableLayout.setOnTouchListener { _, event ->
            when (event.action)
            {
                MotionEvent.ACTION_DOWN -> {
                    x = event.x
                    isDragging = false
                    return@setOnTouchListener true
                }
                MotionEvent.ACTION_MOVE -> {
                    val lengthX = event.x - x

                    if (!isDragging && Math.abs(lengthX) > 0)
                    {
                        isDragging = true
                        if (lengthX > 0)
                            moveFigure(1)
                        else
                            moveFigure(-1)

                        return@setOnTouchListener true
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (!isDragging)
                        rotateFigure()
                }
            }
            false
        }
    }

    fun gameOver() {
        gameOver = true
        if (figureScore.getScore() > figureScore.getBestScore())
            figureScore.saveBestScore(figureScore.getScore())

        Toast.makeText(context, "Game Over! Your score: ${figureScore.getScore()}", Toast.LENGTH_LONG).show()
        clearFields()
        gameOver = false
    }

    fun clearFields() {
        if (figureScore.getScore() > figureScore.getBestScore())
            figureScore.saveBestScore(figureScore.getScore())

        for (row in table.indices)
        {
            for (col in table[row].indices)
            {
                table[row][col] = false
                if (row < tableLayout.childCount)
                {
                    val tableRow = tableLayout.getChildAt(row) as TableRow
                    if (col < tableRow.childCount)
                    {
                        val textView = tableRow.getChildAt(col) as TextView
                        textView.setBackgroundResource(R.drawable.cell_border)
                    }
                }
            }
        }
        figureScore.resetScore()
    }

    // remove rows methods
    private fun removeFigure() {
        val filledRows = table.indices.filter { isRowFilled(it) }
        filledRows.forEach { row ->
            removeRow(row)
            shiftRowsDown(row)
            figureScore.setScore(100)
            figureScore.updateScore()
        }
    }

    private fun isRowFilled(row: Int): Boolean {
        return table[row].all { it }
    }

    private fun removeRow(row: Int) {
        val tableRow = tableLayout.getChildAt(row) as TableRow
        for (col in 0 until tableRow.childCount)
        {
            val textView = tableRow.getChildAt(col) as TextView
            textView.setBackgroundResource(R.drawable.cell_border)
        }
        table[row] = Array(table[row].size) { false }
    }

    private fun shiftRowsDown(startRow: Int) {
        val cols = table[0].size

        for (row in startRow downTo 1)
        {
            for (col in 0 until cols)
            {
                table[row][col] = table[row - 1][col]
                val aboveTableRow = tableLayout.getChildAt(row - 1) as TableRow
                val aboveTextView = aboveTableRow.getChildAt(col) as TextView
                val currentTableRow = tableLayout.getChildAt(row) as TableRow
                val currentTextView = currentTableRow.getChildAt(col) as TextView

                currentTextView.background = aboveTextView.background
            }
        }
        clearTopRow()
    }

    private fun clearTopRow() {
        val topRow = tableLayout.getChildAt(0) as TableRow
        for (col in 0 until topRow.childCount)
        {
            val textView = topRow.getChildAt(col) as TextView
            textView.setBackgroundResource(R.drawable.cell_border)
        }
        table[0] = Array(table[0].size) { false }
    }

}