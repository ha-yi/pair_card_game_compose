package com.hayi.vampair.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.hayi.vampair.domain.store.StoreData
import com.hayi.vampair.utils.getActivity


@Composable
fun SettingsDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var checked by remember {
        mutableStateOf(false)
    }
    SideEffect {
        context.getActivity()?.let {
            checked = StoreData(it).getShowImageUponSuccess()
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.padding(24.dp)) {
            Column {
                Row {
                    Checkbox(checked = checked, onCheckedChange = { it ->
                        checked = it
                        context.getActivity()?.let {act ->
                            StoreData(act).setShowImageUponSuccess(it)
                        }
                    })
                    Text(text = "Show image on Correct")
                }
                Button(onClick = onDismiss) {
                    Text(text = "Close")
                }
            }
        }
    }
}
