package com.lakshyagupta7089.androidsnakegame

interface SnakeStatusUpdate {
    fun onFoodEaten(total: Int)
    fun onGameOver(total: Int)
}