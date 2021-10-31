package com.lakshyagupta7089.androidsnakegame.objects

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.ContextCompat
import com.lakshyagupta7089.androidsnakegame.Callback
import com.lakshyagupta7089.androidsnakegame.R
import com.lakshyagupta7089.androidsnakegame.SnakeStatusUpdate
import java.util.*
import kotlin.math.ceil
import kotlin.math.sqrt

class Snake(
    private val screenWidth: Int,
    private val screenHeight: Int,
    private val context: Context
) : Callback {
    private val snakeScale = 100f
    private var snakeX = 0f
    private var snakeY = 0f

    private var xSpeed = 1
    private var ySpeed = 0

    private var food: Food? = null

    private val snakeHeadPaint = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.snake_head_color)
        strokeWidth = 2f
    }
    private val snakeBodyPaint = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.snake_body_color)
        strokeWidth = 2f
    }

    private var total = 3
    private var tail = ArrayList<SnakeBlock>()
    private var snakeStatusUpdate: SnakeStatusUpdate? = null
    var isGameOver = false

    init {
        getFood()
        tail.add(SnakeBlock(0f, 0f))
        tail.add(SnakeBlock(0f, 0f))
    }

    private fun getFood() {
        val columns = (screenWidth - (2 * snakeScale)) / snakeScale
        val row = (screenHeight - (2 * snakeScale)) / snakeScale

        val foodX = ceil((snakeScale * (0..columns.toInt()).random()).toDouble())
        val foodY = ceil((snakeScale * (0..row.toInt()).random()).toDouble())
        food = Food(foodX.toFloat(), foodY.toFloat(), snakeScale, context)
    }

    private fun setDirection(xDir: Int, yDir: Int) {
        xSpeed = xDir
        ySpeed = yDir
    }

    private fun eatFood() {
        val distance =
            sqrt((((food!!.x - snakeX) * (food!!.x - snakeX)) + ((food!!.y - snakeY) * (food!!.y - snakeY))).toDouble())
        if (distance < snakeScale) {
            getFood()
            total++
            snakeStatusUpdate?.onFoodEaten(total)
        }
    }

    fun update() {
        if (isGameOver) return

        snakeX += xSpeed * snakeScale
        snakeY += ySpeed * snakeScale

        if (snakeY < -80) {
            snakeY = screenHeight - snakeScale
        }

        if (snakeX < -80) {
            snakeX = screenWidth - snakeScale
        }

        if (snakeX > screenWidth) {
            snakeX = 0f
        }

        if (snakeY > screenHeight) {
            snakeY = 0f
        }

        if (snakeDeath()) {
            snakeStatusUpdate?.onGameOver(total)
            return
        }
        eatFood()

        if (total > 0 && total == tail.size) {
            for (i in 0..tail.size - 2) {
                tail[i] = tail[i + 1]
            }
        }

        if (total > 0 && tail.size < total) {
            tail.add(SnakeBlock(snakeX, snakeY))
        } else if (total > 0) {
            tail[tail.size - 1] = SnakeBlock(snakeX, snakeY)
        }
    }

    private fun snakeDeath(): Boolean {
        for (snakeBlock in tail) {
            val distance = sqrt(
                (((snakeBlock.x - snakeX) * (snakeBlock.x - snakeX))
                        + ((snakeBlock.y - snakeY) * (snakeBlock.y - snakeY))).toDouble()
            )
            if (distance < snakeScale) {
                isGameOver = true
                return true
            }
        }
        return false
    }

    fun show(canvas: Canvas) {
        for (snakeBlock in tail) {
            canvas.drawRect(
                snakeBlock.x,
                snakeBlock.y,
                snakeBlock.x + snakeScale,
                snakeBlock.y + snakeScale,
                snakeBodyPaint
            )
        }

        canvas.drawRect(
            snakeX,
            snakeY,
            snakeX + snakeScale,
            snakeY + snakeScale,
            snakeHeadPaint
        )

        food?.show(canvas)
    }

    override fun onSwipeRight() {
        if (xSpeed == -1) return
        setDirection(1, 0)
    }

    override fun onSwipeLeft() {
        if (xSpeed == 1) return
        setDirection(-1, 0)
    }

    override fun onSwipeTop() {
        if (ySpeed == 1) return
        setDirection(0, -1)
    }

    override fun onSwipeBottom() {
        if (ySpeed == -1) return
        setDirection(0, 1)
    }

    fun setSnakeStatusUpdateListener(snakeStatusUpdate: SnakeStatusUpdate) {
        this.snakeStatusUpdate = snakeStatusUpdate
    }

    data class SnakeBlock(
        val x: Float = 0f,
        val y: Float = 0f
    )

}