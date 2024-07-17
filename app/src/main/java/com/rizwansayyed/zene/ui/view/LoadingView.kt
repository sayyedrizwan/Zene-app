package com.rizwansayyed.zene.ui.view

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.utils.Utils.toast

@Composable
fun LoadingView(modifier: Modifier = Modifier) {
    Column(modifier, Arrangement.Center, Alignment.CenterHorizontally) {
        CircularProgressIndicator(
            Modifier.size(32.dp), MainColor, trackColor = Color.White,
        )
    }
}

@Composable
fun LoadingCardView() {
    Column(Modifier.padding(7.dp), Arrangement.Center, Alignment.CenterHorizontally) {
        Spacer(
            Modifier
                .size(220.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(shimmerEffectBrush())
        )

        Spacer(
            Modifier
                .padding(top = 9.dp)
                .padding(horizontal = 5.dp)
                .size(100.dp, 10.dp)
                .clip(RoundedCornerShape(40))
                .background(shimmerEffectBrush())
        )

        Spacer(
            Modifier
                .padding(top = 7.dp)
                .padding(horizontal = 5.dp)
                .size(155.dp, 10.dp)
                .clip(RoundedCornerShape(40))
                .background(shimmerEffectBrush())
        )
    }
}

@Composable
fun LoadingText() {
    Spacer(
        Modifier
            .padding(top = 17.dp)
            .padding(horizontal = 7.dp)
            .size(245.dp, 20.dp)
            .clip(RoundedCornerShape(40))
            .background(shimmerEffectBrush())
    )
}


@Composable
fun CardRoundLoading() {
    Spacer(
        Modifier
            .padding(6.dp)
            .size(350.dp, 100.dp)
            .clip(RoundedCornerShape(10))
            .background(shimmerEffectBrush())
    )
}


@Composable
fun LoadingArtistsCardView() {
    Column(Modifier.padding(7.dp), Arrangement.Center, Alignment.CenterHorizontally) {
        Spacer(
            Modifier
                .size(180.dp)
                .clip(RoundedCornerShape(100))
                .background(shimmerEffectBrush())
        )

        Spacer(
            Modifier
                .padding(top = 8.dp)
                .padding(horizontal = 5.dp)
                .size(100.dp, 10.dp)
                .clip(RoundedCornerShape(40))
                .background(shimmerEffectBrush())
        )
    }
}

@Composable
fun shimmerEffectBrush(): ShaderBrush {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")

    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1_000),
        ),
        label = "shimmer offset"
    )
    return remember(offset) {
        object : ShaderBrush() {
            override fun createShader(size: Size): Shader {
                val widthOffset = size.width * offset
                val heightOffset = size.height * offset
                return LinearGradientShader(
                    colors = listOf(
                        Color.DarkGray.copy(alpha = 0.4f),
                        Color.DarkGray.copy(alpha = 0.6f),
                        Color.DarkGray.copy(alpha = 0.4f),
                    ),
                    from = Offset(widthOffset, heightOffset),
                    to = Offset(widthOffset + size.width, heightOffset + size.height),
                    tileMode = TileMode.Mirror
                )
            }
        }
    }
}