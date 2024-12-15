package com.rizwansayyed.zene.ui.connect.view

import android.net.Uri
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.emoji2.emojipicker.EmojiPickerView
import coil.compose.AsyncImage
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.view.ImageIcon
import kotlin.random.Random

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ZeneImageEditorView(image: Uri) {
    var offsets: List<Pair<Float, Float>> by remember {
        mutableStateOf(List(800) {
            Pair(Random.nextFloat() * 300 - 150, Random.nextFloat() * 100 - 50)
        }.shuffled())
    }

    var bgColor by remember { mutableStateOf(Color.Black) }
    var bgColorAlert by remember { mutableStateOf(false) }
    var emojiAlert by remember { mutableStateOf(false) }
    var emojis by remember { mutableStateOf("\uD83E\uDD41") }

    val controller = rememberColorPickerController()

    var scale by remember { mutableStateOf(1f) }
    var rotation by remember { mutableStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        rotation += rotationChange
        offset += offsetChange
    }

    Box(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(bgColor)
        ) {
            FlowColumn(Modifier.fillMaxWidth()) {
                repeat(800) { index ->
                    Text(
                        emojis,
                        fontSize = 40.sp,
                        modifier = Modifier
                            .padding(4.dp)
                            .offset(offsets[index].first.dp, offsets[index].second.dp)
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

        Row(
            Modifier
                .padding(top = 40.dp)
                .padding(horizontal = 15.dp)
                .align(Alignment.TopCenter)
                .fillMaxWidth()
        ) {
            Column(
                Modifier
                    .padding(horizontal = 5.dp)
                    .size(35.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color.Black.copy(0.3f))
                    .padding(4.dp),
                Arrangement.Center,
                Alignment.CenterHorizontally
            ) {
                ImageIcon(R.drawable.ic_arrow_left, 25)
            }

            Spacer(Modifier.weight(1f))

            Column(
                Modifier
                    .padding(horizontal = 5.dp)
                    .clickable {
                        emojiAlert = !emojiAlert
                    }
                    .size(35.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color.Black.copy(0.3f))
                    .padding(4.dp), Arrangement.Center, Alignment.CenterHorizontally) {
                ImageIcon(R.drawable.ic_relieved, 25)
            }


            Column(
                Modifier
                    .padding(horizontal = 5.dp)
                    .size(35.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color.Black.copy(0.3f))
                    .padding(4.dp),
                Arrangement.Center,
                Alignment.CenterHorizontally
            ) {
                ImageIcon(R.drawable.ic_music, 25)
            }


            Column(
                Modifier
                    .padding(horizontal = 5.dp)
                    .clickable {
                        bgColorAlert = !bgColorAlert
                    }
                    .size(35.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color.Black.copy(0.3f))
                    .padding(4.dp), Arrangement.Center, Alignment.CenterHorizontally) {
                ImageIcon(R.drawable.ic_colors, 25)
            }
        }


        if (bgColorAlert) HsvColorPicker(modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth()
            .height(450.dp)
            .padding(10.dp),
            controller = controller,
            onColorChanged = { colorEnvelope: ColorEnvelope ->
                bgColor = colorEnvelope.color
            })

        if (emojiAlert) EmojisStripe(Modifier.align(Alignment.BottomCenter)) {
            emojis = it
            offsets = List(800) {
                Pair(Random.nextFloat() * 300 - 150, Random.nextFloat() * 100 - 50)
            }.shuffled()
        }
    }
}

@Composable
fun EmojisStripe(modifier: Modifier = Modifier, emoji: (String) -> Unit) {
    AndroidView(
        factory = {
            EmojiPickerView(it).apply {
                emojiGridRows = 1.5f
                emojiGridColumns = 7
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
                setOnEmojiPickedListener { e ->
                    emoji(e.emoji)
                }
            }

        },
        modifier
            .padding(bottom = 40.dp)
            .fillMaxWidth()
            .height(150.dp)
            .background(Color.White)
    )
}