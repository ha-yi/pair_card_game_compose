package com.hayi.vampair.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ToggleButtons(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    val selected = remember {
        mutableStateOf(selectedOption)
    }
    val listColor = listOf(
        Color(0xFF88C273),
        Color(0xFFFFA24C),
        Color(0xFFF95454),
        Color(0xFF37AFE1),
    )

    Row (
        horizontalArrangement = Arrangement.Center
    ){
        options.forEachIndexed { index, option ->
            OutlinedButton(
                onClick = {
                    selected.value = option
                    onOptionSelected(option)
                },
                border = BorderStroke(0.dp, Color.Transparent),
                shape = RoundedCornerShape(
                    topStart = if (index == 0) 16.dp else 0.dp,
                    bottomStart = if (index == 0) 16.dp else 0.dp,
                    topEnd = if (index == options.size - 1) 16.dp else 0.dp,
                    bottomEnd = if (index == options.size - 1) 16.dp else 0.dp
                ),
                colors = if(selected.value == option) ButtonDefaults.buttonColors(
                    containerColor = listColor[index%listColor.size],
                    contentColor = Color.White,
                ) else ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                )
            ) {
                Text(text = option)
            }
        }

    }
}