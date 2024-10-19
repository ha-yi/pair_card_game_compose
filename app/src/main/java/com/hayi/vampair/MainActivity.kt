package com.hayi.vampair

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.hayi.vampair.ui.common.LevelChooserDialog


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalGlideComposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val showLevelDialog = remember { mutableStateOf(false) }

            Box {
                Box(modifier = Modifier.fillMaxSize()) {
                    GlideImage(
                        model = R.drawable.background,
                        contentDescription = "",
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }
                Box {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                    ) {

                    }
                }
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(text = "Pair Card Game", style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                    ))

                    Spacer(modifier = Modifier.height(50.dp))
                    Text(text = "Sharpen Your Mind: Find the Matching Cards!", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(onClick = {
                        showLevelDialog.value = true
                    }) {
                        Text(text = "Start new game")
                    }

                    when {
                        showLevelDialog.value -> {
                            LevelChooserDialog { number, levelName ->
                                showLevelDialog.value = false
                                val intent = Intent(this@MainActivity, GameActivity::class.java)
                                intent.putExtra("end", (number))
                                intent.putExtra("level", levelName)
                                startActivity(intent)
                            }
                        }
                    }


                }

                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp), contentAlignment = Alignment.BottomEnd) {
                    GlideImage(
                        model = R.drawable.oooh,
                        contentDescription = "",
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.FillWidth
                    )
                }
            }
        }
    }
}
