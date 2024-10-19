package com.hayi.vampair.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun LevelChooserDialog(onLevelSelected: (Int, String) -> Unit) {
    Dialog(onDismissRequest = {}) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Levels(onLevelSelected)
        }
    }
}


@Composable
fun Levels(
    onLevelSelected: (Int, String) -> Unit
) {
    val levelList = mapOf(
        "Easy" to listOf(4, 6, 8, 10),
        "Medium" to listOf(16, 20, 30),
        "Hard" to listOf(50, 100),
        "Custom" to listOf()
    )

    val selectedKey = remember {
        mutableStateOf("Easy")
    }

    val selectedLevel = remember {
        mutableStateOf(levelList["Easy"])
    }

    val selectedTileNumber = remember {
        mutableIntStateOf(4)
    }

    val listColor = listOf(
        Color(0xFF88C273),
        Color(0xFFFFA24C),
        Color(0xFFF95454)
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Select level",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
        )
        Spacer(modifier = Modifier.height(20.dp))

        if (selectedLevel.value?.isNotEmpty() == true)
            LazyRow {
                items(selectedLevel.value!!.size) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        RadioButton(selected = selectedTileNumber.intValue == (selectedLevel.value!![it]), onClick = {
                            selectedTileNumber.intValue = selectedLevel.value!![it]
                        })
                        Text(text = "${selectedLevel.value!![it]} Cards")
                    }
                }
            }
        else
            Slider(value = selectedTileNumber.intValue.toFloat(), valueRange = 4f..100f, onValueChange = {
                val tmp = it.toInt()
                selectedTileNumber.intValue = if(tmp % 2 == 0) tmp else tmp + 1
            }, colors = SliderDefaults.colors(
                inactiveTrackColor = Color(0xFFD4BDAC),
                activeTrackColor = listColor[(selectedTileNumber.intValue/35) % listColor.size],

            ))
        Text(
            text = "${selectedTileNumber.intValue} Cards",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(20.dp))
        ToggleButtons(options = listOf("Easy", "Medium", "Hard", "Custom"), selectedOption = "Easy") {
            selectedLevel.value = levelList[it]
            selectedKey.value = it
        }

        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            onLevelSelected(selectedTileNumber.intValue, selectedKey.value)
        }) {
            Text(text = "Start new game")
        }
    }
}