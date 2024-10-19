package com.hayi.vampair

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.hayi.vampair.ui.game.GameBoard

class GameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val start by remember {
                mutableStateOf(intent.getIntExtra("start", 0))
            }

            GameBoard(
                start = start,
                length = intent.getIntExtra("end", 4),
                title = intent.getStringExtra("level") ?: "Custom Game"
            ) {
                finish()
            }
        }
    }
}
