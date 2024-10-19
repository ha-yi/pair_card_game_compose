package com.hayi.vampair.ui.game

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Refresh
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.hayi.vampair.domain.store.StoreData
import com.hayi.vampair.ui.common.GameCard
import com.hayi.vampair.ui.common.GameCardItem
import com.hayi.vampair.ui.common.SettingsDialog
import com.hayi.vampair.ui.common.WinDialog
import com.hayi.vampair.ui.theme.PairCardGameTheme
import com.hayi.vampair.utils.getActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GameBoard(
    start: Int,
    length: Int,
    title: String,
    onComplete: () -> Unit
) {
    val row = (start..<(length / 2)).toList()
    val total = (row.shuffled() + row.shuffled()).shuffled().mapIndexed { index, i -> Pair(index, i) }.chunked(5)
    val startTime = remember {
        mutableLongStateOf(System.currentTimeMillis())
    }

    var openCount by remember {
        mutableIntStateOf(0)
    }

    var correctCount by remember {
        mutableIntStateOf(0)
    }

    val showWinnerDialog = remember { mutableStateOf(false) }

    when {
        showWinnerDialog.value -> WinDialog(
            openCount = openCount,
            startTime = startTime.longValue,
        ) {
            onComplete()
        }
    }

    var opened = remember<GameCardItem?> {
        null
    }

    val showImageUponSuccess = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    SideEffect {
        context.getActivity()?.let {
            showImageUponSuccess.value = StoreData(it).getShowImageUponSuccess()
        }
    }

    PairCardGameTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                GameBoardAppBar(
                    level = "$title - correct $correctCount of ${length/2} #$openCount moves",
                    onSetting = {
                        context.getActivity()?.let {
                            showImageUponSuccess.value = StoreData(it).getShowImageUponSuccess()
                        }
                    }
                )
            },
            containerColor = MaterialTheme.colorScheme.onSurface,
        ) { innerPadding ->
            LazyColumn(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ){
                item {
                    Spacer(modifier = Modifier.height(innerPadding.calculateTopPadding()))
                }
                items(total.size) { col ->
                    LazyRow(
                        modifier = Modifier.height(200.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        items(total[col].size) { row ->
                            Log.e("CARD", "recreateCard: ${total[col][row]}")
                            GameCard(
                                cardNumber = total[col][row].first,
                                cardValue = total[col][row].second,
                                showImageOnCorrect = showImageUponSuccess.value,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameBoardAppBar(
    level: String,
    onSetting: () -> Unit,
) {

    var openSettings by  remember {
        mutableStateOf(false)
    }

    var openRecreate by  remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    when {
        openSettings -> SettingsDialog(
            onDismiss = {
                onSetting()
                openSettings = false
            }
        )
        openRecreate -> {
            AlertDialog(
                title = { Text(text = "Are you sure you want to restart the game?") },
                text = { Text(text = "This action cannot be undone") },
                onDismissRequest = {
                    openRecreate = false
                },
                confirmButton = {
                Button(onClick = {
                    context.getActivity()?.recreate()
                }) {
                    Text(text = "Yes Restart Game")
                }
            })
        }
    }



    TopAppBar(
        title = { Text(text = level) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.onSurface,
            titleContentColor = MaterialTheme.colorScheme.surface,
            actionIconContentColor = Color(0xFF7E60BF)
        ),
        actions = {
            IconButton(
                onClick = {
                    openRecreate = true
                }
            ) {
                Icon(Icons.TwoTone.Refresh, contentDescription = "Reset")
            }

            IconButton(
                onClick = {
                    openSettings = true
                }
            ) {
                Icon(Icons.TwoTone.Settings, contentDescription = "settings")
            }
        }
    )
}
