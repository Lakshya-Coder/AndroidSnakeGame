package com.lakshyagupta7089.androidsnakegame

import android.media.SoundPool
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lakshyagupta7089.androidsnakegame.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity(), SnakeStatusUpdate {
    companion object {
        const val FPS = 7
    }

    private lateinit var binding: ActivityMainBinding
    private var timer = Timer()
    private var timerTask = object : TimerTask() {
        override fun run() {
            runOnUiThread { binding.game.updateScreen() }
        }
    }
    private lateinit var soundPool: SoundPool
    private var bouns = 0
    private var gameOver = 0
    private var isVolumeOn = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.toolbar.elevation = 1f

        binding.resetImageButton.setOnClickListener {
            resetGame()
        }
        binding.game.snakeStatusUpdate = this

        timer.scheduleAtFixedRate(timerTask, 10, (1000 / FPS).toLong())

        soundPool = SoundPool.Builder()
            .setMaxStreams(10)
            .build()

        bouns = soundPool.load(this, R.raw.bonus, 1)
        gameOver = soundPool.load(this, R.raw.game_over, 1)

        binding.volumeImageButton.setOnClickListener {
            if (isVolumeOn) {
                isVolumeOn = false
                binding.volumeImageButton.setBackgroundResource(R.drawable.ic_volume_off)
            } else {
                isVolumeOn = true
                binding.volumeImageButton.setBackgroundResource(R.drawable.ic_volume_up)
            }
        }
    }

    private fun resetGame() {
        binding.game.restart()
        binding.scoreNumTextView.text = "0"
    }

    override fun onFoodEaten(total: Int) {
        binding.scoreNumTextView.text = (total - 3).toString()
        if (!isVolumeOn) return
        playSound(bouns)
    }

    override fun onGameOver(total: Int) {
        if (!isVolumeOn) return
        playSound(gameOver)
    }

    private fun playSound(id: Int) {
        soundPool.play(
            id,
            1f,
            1f,
            0,
            0,
            1f
        )
    }

    override fun onStop() {
        super.onStop()

        soundPool.release()
    }


}