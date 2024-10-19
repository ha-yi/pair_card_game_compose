@file:OptIn(ExperimentalGlideComposeApi::class)

package com.hayi.vampair.ui.common

import android.R
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.twotone.Check
import androidx.compose.material.icons.twotone.PlayArrow
import androidx.compose.material.icons.twotone.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GameCard(
    cardNumber: Int,
    cardValue: Int,
    onCardClicked: (rotated: Boolean, item: GameCardItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val visible = remember { mutableStateOf(true) }

    if (!visible.value) {
        Box(
            modifier = Modifier
                .aspectRatio(0.5f)
                .fillMaxWidth()
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(modifier = Modifier.align(Alignment.Center)) {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = "", modifier = Modifier.size(50.dp).padding(10.dp),
                    tint = Color.Green.copy(alpha = 0.5f)
                )
            }
        }
    } else {
        var rotated by remember { mutableStateOf(false) }

        val rotation by animateFloatAsState(
            targetValue = if (rotated) 180f else 0f,
            animationSpec = tween(500)
        )

        val animateFront by animateFloatAsState(
            targetValue = if (!rotated) 1f else 0f,
            animationSpec = tween(500)
        )

        val animateBack by animateFloatAsState(
            targetValue = if (rotated) 1f else 0f,
            animationSpec = tween(500)
        )

        val revertCard:(status: Boolean) -> Unit = {
            CoroutineScope(Dispatchers.Main).launch {
                delay(1000)
                rotated = false
                visible.value = !it
            }
        }

        Box(
            modifier = Modifier
                .aspectRatio(0.5f)
                .fillMaxWidth()
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        rotationY = rotation
                        cameraDistance = 8 * density
                    }
                    .clickable {
                        rotated = !rotated
                        onCardClicked(rotated, GameCardItem(cardNumber, cardValue, revertCard))
                    },
            )
            {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            alpha = if (rotated) animateBack else animateFront
                        },
                ) {
                    if (rotated) {
                        CardFace("https://i.pravatar.cc/300?img=$cardValue")
                    }
                    else
                        Box(modifier = Modifier.align(Alignment.Center)) {
                            Icon(
                                Icons.TwoTone.PlayArrow,
                                contentDescription = "", modifier = Modifier.size(50.dp)
                            )
                        }


                }

            }
        }
    }
}

data class GameCardItem (
    val index: Int,
    val value: Int,
    val revertCard: (Boolean) -> Unit
)

@Composable
fun CardFace(image:String) {
    if (image.isEmpty())
        Box(Modifier.fillMaxSize())
    GlideImage(
        model = image,
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
}
//
//@Preview(showBackground = true)
//@Composable
//fun GameCardPreview() {
//    GameCard(1,1, true, { index, value, rotated, revertCard -> })
//}