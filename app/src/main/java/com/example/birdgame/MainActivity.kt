package com.example.birdgame

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GameMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_menu)

        val playButton = findViewById<Button>(R.id.playButton)
        val exitButton = findViewById<Button>(R.id.exitButton)

        playButton.setOnClickListener {
            // Переход к игре
            val intent = GameView(this)
            setContentView(GameView(this))

        }

        exitButton.setOnClickListener {
            // Завершение приложения
            finish()
        }
    }
}
