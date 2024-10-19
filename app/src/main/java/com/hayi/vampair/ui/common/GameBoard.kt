package com.hayi.vampair.ui.common

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ThumbUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.hayi.vampair.ui.theme.PairCardGameTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GameBoard(
    start: Int,
    length: Int,
    onComplete: () -> Unit
) {
    val row = (start..<(length/2)).toList()
    val total = (row.shuffled() + row.shuffled()).shuffled().mapIndexed { index, i -> Pair(index, i) }.chunked(5)
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val startTime = remember {
        mutableLongStateOf(System.currentTimeMillis())
    }

    Log.e("TAG", "onCreate: $screenWidth")
    var openCount by remember {
        mutableIntStateOf(0)
    }

    var correctCount by remember {
        mutableIntStateOf(0)
    }

    val showWinnerDialog = remember { mutableStateOf(false) }

    if (showWinnerDialog.value) {
        AlertDialog(
            icon = {
                Icon(Icons.TwoTone.ThumbUp, contentDescription = "Winner")
            },
            title = {
                Text(text = "Congratulations!!!")
            },
            text = {
                Text(
                    text = "You have completed the puzzle in $openCount moves and ${(System.currentTimeMillis() - startTime.longValue)/1000} seconds."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onComplete()
                        openCount = 0
                        correctCount = 0
                        showWinnerDialog.value = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            onDismissRequest = { /*TODO*/ },
            )
    }

    PairCardGameTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            var opened = remember<GameCardItem?> {
                null
            }
            LazyColumn {
                items(total.size) {col ->
                    LazyRow(
                        modifier = Modifier.height(200.dp)
                    ) {
                        items(total[col].size) {row ->
                            Log.e("CARD", "recreateCard: ${total[col][row]}")
                            GameCard(
                                cardNumber = total[col][row].first,
                                cardValue = total[col][row].second,
                                onCardClicked = { status, item ->
                                    if (status) {
                                        openCount += 1
                                        if (opened == null) {
                                            opened = item
                                        } else {
                                            val correct = opened!!.value == item.value && item.index != opened!!.index
                                            opened?.revertCard?.invoke(correct)
                                            item.revertCard(correct)
                                            opened = null
                                            if (correct) {
                                                correctCount += 1
                                            }
                                        }
                                    } else {
                                        item.revertCard(false)
                                    }
                                }
                            )
                        }
                    }
                }
            }

            LaunchedEffect(key1 = correctCount) {

                if (correctCount == row.size) {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1000)
                        showWinnerDialog.value = true
                    }
                }
            }
        }
    }
}

