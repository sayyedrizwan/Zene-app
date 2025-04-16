package com.rizwansayyed.zene.ui.view

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.ui.theme.MainColor

@Composable
fun CircularLoadingView() {
    Row(
        Modifier
            .padding(20.dp)
            .fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically
    ) {
        CircularProgressIndicator(Modifier.width(35.dp), Color.White, trackColor = MainColor)
    }
}


@Composable
fun CircularLoadingViewSmall() {
    Box(Modifier.size(20.dp)) {
        CircularProgressIndicator(Modifier, Color.White, trackColor = Color.Gray)
    }
}


@Composable
fun HorizontalShimmerLoadingCard() {
    LazyRow(Modifier.fillMaxSize()) {
        items(9) {
            Column(
                Modifier
                    .padding(horizontal = 9.dp)
                    .width(175.dp)
            ) {
                Box(Modifier.fillMaxWidth()) {
                    ShimmerEffect(Modifier.size(170.dp), durationMillis = 1000)
                }
                Spacer(Modifier.height(12.dp))
                ShimmerEffect(
                    Modifier
                        .padding(horizontal = 3.dp)
                        .size(120.dp, 5.dp), durationMillis = 1000
                )
                Spacer(Modifier.height(6.dp))
                ShimmerEffect(
                    Modifier
                        .padding(horizontal = 3.dp)
                        .size(80.dp, 5.dp), durationMillis = 1000
                )
            }
        }
    }
}

@Composable
fun HorizontalShimmerVideoLoadingCard() {
    LazyRow(Modifier.fillMaxSize()) {
        items(9) {
            Column(
                Modifier
                    .padding(horizontal = 9.dp)
                    .width(245.dp)
            ) {
                Box(Modifier.fillMaxWidth()) {
                    ShimmerEffect(Modifier.size(240.dp, 150.dp), durationMillis = 1000)
                }
                Spacer(Modifier.height(12.dp))
                ShimmerEffect(
                    Modifier
                        .padding(horizontal = 3.dp)
                        .size(200.dp, 5.dp), durationMillis = 1000
                )
                Spacer(Modifier.height(6.dp))
                ShimmerEffect(
                    Modifier
                        .padding(horizontal = 3.dp)
                        .size(100.dp, 5.dp), durationMillis = 1000
                )
            }
        }
    }
}

@Composable
fun HorizontalCircleShimmerLoadingCard() {
    LazyRow(Modifier.fillMaxSize()) {
        items(9) {
            ShimmerEffect(
                modifier = Modifier
                    .padding(horizontal = 9.dp)
                    .size(170.dp)
                    .clip(RoundedCornerShape(100)), durationMillis = 1000
            )
        }
    }
}

@Composable
fun FullUsersShimmerLoadingCard() {
    Row(
        Modifier
            .padding(horizontal = 13.dp, vertical = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ShimmerEffect(
            Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(20))
        )

        Column(
            Modifier
                .padding(horizontal = 7.dp)
                .weight(1f), Arrangement.Center, Alignment.Start
        ) {
            ShimmerEffect(modifier = Modifier.size(115.dp, 10.dp), durationMillis = 1000)
            Spacer(Modifier.height(5.dp))
            ShimmerEffect(modifier = Modifier.size(95.dp, 10.dp), durationMillis = 1000)
        }
    }
}

@Composable
fun ShimmerEffect(
    modifier: Modifier,
    widthOfShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000,
) {


    val shimmerColors = listOf(
        Color.White.copy(alpha = 0.3f),
        Color.White.copy(alpha = 0.5f),
        Color.White.copy(alpha = 1.0f),
        Color.White.copy(alpha = 0.5f),
        Color.White.copy(alpha = 0.3f),
    )

    val transition = rememberInfiniteTransition(label = "")

    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "Shimmer loading animation",
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = translateAnimation.value - widthOfShadowBrush, y = 0.0f),
        end = Offset(x = translateAnimation.value, y = angleOfAxisY),
    )

    Box(
        modifier = modifier
    ) {
        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(brush)
        )
    }


}