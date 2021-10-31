package com.lakshyagupta7089.androidsnakegame

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.lakshyagupta7089.androidsnakegame.objects.Snake

class Game : View {
    companion object {
        val textPaint = TextPaint().apply {
            color = Color.RED
            isAntiAlias = true
            strokeWidth = 2f
            textSize = 100f
            textAlign = Paint.Align.CENTER
        }
    }

    private var textHeight = 0f
    private var textOffset = 0f

    init {
        textHeight = textPaint.descent() - textPaint.ascent()
        textOffset = textHeight / 2
    }

    private var snake: Snake? = null

    var snakeStatusUpdate: SnakeStatusUpdate? = null

    private var screenHeight = 0
    private var screenWidth = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawColor(
            Color.parseColor("#4E736E")
        )

        if (snake!!.isGameOver) {
            canvas!!.drawText("Game over", width / 2f, height / 2f, textPaint)
        } else {
            snake!!.update()
            snake!!.show(canvas!!)
        }
    }

    fun updateScreen() { invalidate() }

    override fun onSizeChanged(
        w: Int,
        h: Int,
        oldw: Int,
        oldh: Int
    ) {
        super.onSizeChanged(w, h, oldw, oldh)

        screenHeight = h
        screenWidth = w
        restart()
//        snake = Snake(w, h, context, resources)
//        snake!!.setSnakeStatusUpdateListener(snakeStatusUpdate!!)
//        this.setOnTouchListener(OnSwipeTouchListener(context, snake!!))
    }

    private fun setDirection(xDir: Int, yDir: Int) {
        snake!!.setDirection(xDir, yDir)
    }

    fun restart() {
        snake = Snake(screenWidth, screenHeight, context, resources)
        snake!!.setSnakeStatusUpdateListener(snakeStatusUpdate!!)
        this.setOnTouchListener(OnSwipeTouchListener(context, snake!!))
    }
}