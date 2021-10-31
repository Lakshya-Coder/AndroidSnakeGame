package com.lakshyagupta7089.androidsnakegame.objects

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.ContextCompat
import com.lakshyagupta7089.androidsnakegame.R

class Food(
    var x: Float,
    var y: Float,
    private val scale: Float,
    private val context: Context,
) {
    private val foodPaint = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.snake_food_color)
        strokeWidth = 2f
    }

    fun show(canvas: Canvas) {
        canvas.drawRect(x, y, x + scale, y + scale, foodPaint)
    }
}