package com.rizwansayyed.zene.ui.connect.view

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlin.random.Random

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ZeneImageEditorView(image: Uri) {
    var offsets: List<Pair<Float, Float>> by remember {
        mutableStateOf(List(800) {
            Pair(Random.nextFloat() * 300 - 150, Random.nextFloat() * 100 - 50)
        }.shuffled())
    }

    var scale by remember { mutableStateOf(1f) }
    var rotation by remember { mutableStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        rotation += rotationChange
        offset += offsetChange
    }

    Box(
        Modifier
            .fillMaxWidth()
            .background(Color.Black)
    ) {
        FlowColumn(Modifier.fillMaxWidth()) {
            repeat(800) { index ->
                Text(
                    "🤟",
                    fontSize = 40.sp,
                    modifier = Modifier
                        .padding(4.dp)
                        .offset(x = offsets[index].first.dp, y = offsets[index].second.dp)
                )
            }
        }
        AsyncImage(
            image, "",
            Modifier
                .fillMaxSize(0.8f)
                .align(Alignment.Center)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    rotationZ = rotation,
                    translationX = offset.x,
                    translationY = offset.y
                )
                .transformable(state = state)
        )
    }

//    LaunchedEffect(Unit) {
//        while (true) {
//            delay(2.seconds)
//            offsets = List(800) {
//                Pair(Random.nextFloat() * 300 - 150, Random.nextFloat() * 100 - 50)
//            }.shuffled()
//        }
//    }
}