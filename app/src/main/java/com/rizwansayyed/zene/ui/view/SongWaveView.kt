package com.rizwansayyed.zene.ui.view

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SongWaveView(modifier: Modifier = Modifier, waveColor: Color = Color.White) {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val waveHeight1 by infiniteTransition.animateFloat(
        initialValue = 10f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val waveHeight2 by infiniteTransition.animateFloat(
        initialValue = 15f,
        targetValue = 35f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val waveHeight3 by infiniteTransition.animateFloat(
        initialValue = 20f,
        targetValue = 40f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Row(
        modifier = modifier.offset(x = 10.dp, y = -20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        WaveBar(waveHeight = waveHeight1, waveColor = waveColor)
        WaveBar(waveHeight = waveHeight2, waveColor = waveColor)
        WaveBar(waveHeight = waveHeight3, waveColor = waveColor)
    }
}

@Composable
fun WaveBar(waveHeight: Float, waveColor: Color) {
    Canvas(
        modifier = Modifier
            .width(3.dp)
            .height(2.dp)
    ) {
        drawRoundRect(
            color = waveColor,
            size = androidx.compose.ui.geometry.Size(width = size.width, height = waveHeight),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                x = 4.dp.toPx(),
                y = 4.dp.toPx()
            ),
            topLeft = androidx.compose.ui.geometry.Offset(
                x = 0f,
                y = (size.height - waveHeight) / 2
            )
        )
    }
}