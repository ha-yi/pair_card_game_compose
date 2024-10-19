package com.hayi.vampair.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ThumbUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable


@Composable
fun WinDialog(
    openCount: Int,
    startTime: Long,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(Icons.TwoTone.ThumbUp, contentDescription = "Winner")
        },
        title = {
            Text(text = "Congratulations!!!")
        },
        text = {
            Text(
                text = "You have completed the puzzle in $openCount moves and ${(System.currentTimeMillis() - startTime) / 1000} seconds."
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text("Confirm")
            }
        },
        onDismissRequest = onConfirm,
    )
}