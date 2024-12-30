package com.rizwansayyed.zene.ui.connect.view

import android.app.Activity
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.emoji2.emojipicker.EmojiPickerView
import coil.compose.AsyncImage
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.model.MusicType.ARTISTS
import com.rizwansayyed.zene.data.api.model.MusicType.SONGS
import com.rizwansayyed.zene.data.api.model.MusicType.VIDEO
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.imgBuilder

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ZeneImageEditorView(image: Uri) {
    val context = LocalContext.current as Activity

    var bgColor by remember { mutableStateOf(Color.Black) }
    var songView by remember { mutableStateOf<ZeneMusicDataItems?>(null) }

    var bgColorAlert by remember { mutableStateOf(false) }
    var songAlert by remember { mutableStateOf(false) }
    var emojiAlert by remember { mutableStateOf(false) }
    var emojis by remember { mutableStateOf("\uD83E\uDD41") }
    val controller = rememberColorPickerController()


    var scale by remember { mutableFloatStateOf(1f) }
    var rotation by remember { mutableFloatStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        rotation += rotationChange
        offset += offsetChange
    }


    var scaleMusic by remember { mutableFloatStateOf(1f) }
    var rotationMusic by remember { mutableFloatStateOf(0f) }
    var offsetMusic by remember { mutableStateOf(Offset.Zero) }
    val stateMusic = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scaleMusic *= zoomChange
        rotationMusic += rotationChange
        offsetMusic += offsetChange
    }

    val graphicsLayer = rememberGraphicsLayer()

    Box(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxWidth()
                .drawWithContent {
                    graphicsLayer.record {
                        this@drawWithContent.drawContent()
                    }
                    drawLayer(graphicsLayer)
                }
                .graphicsLayer(scaleX = 1.2f, scaleY = 1.1f)
                .background(bgColor)
        ) {
            FlowColumn(Modifier.fillMaxWidth()) {
                repeat(800) {
                    Text(
                        emojis, fontSize = 27.sp, modifier = Modifier.padding(8.dp)
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

            if (songView != null) Row(
                Modifier
                    .graphicsLayer(
                        scaleX = scaleMusic,
                        scaleY = scaleMusic,
                        rotationZ = rotationMusic,
                        translationX = offsetMusic.x,
                        translationY = offsetMusic.y
                    )
                    .transformable(state = stateMusic)
                    .padding(bottom = 30.dp)
                    .widthIn(max = 300.dp)
                    .align(Alignment.BottomEnd)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black)
                    .padding(5.dp)
            ) {
                AsyncImage(
                    imgBuilder(songView!!.thumbnail),
                    songView!!.name,
                    Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.DarkGray),
                    contentScale = ContentScale.Crop
                )
                Column(
                    Modifier
                        .weight(1f)
                        .padding(horizontal = 9.dp),
                    Arrangement.Center,
                    Alignment.Start
                ) {
                    TextPoppins(v = songView!!.name ?: "", false, size = 16, limit = 1)
                    Spacer(Modifier.height(4.dp))
                    TextPoppins(v = songView!!.artists ?: "", false, size = 13, limit = 1)
                    when (songView!!.type()) {
                        SONGS -> TextPoppins(
                            stringResource(R.string.listen_now), false, size = 13, limit = 1
                        )

                        ARTISTS -> TextPoppins(
                            stringResource(R.string.view_artists), false, size = 13, limit = 1
                        )

                        VIDEO -> TextPoppins(
                            stringResource(R.string.play_video), false, size = 13, limit = 1
                        )

                        else -> {}
                    }

                }
            }

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
                    .clickable {
                        context.finish()
                    }
                    .size(35.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color.Black.copy(0.3f))
                    .padding(4.dp), Arrangement.Center, Alignment.CenterHorizontally) {
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
                    .clickable {
                        songAlert = true
                    }
                    .size(35.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color.Black.copy(0.3f))
                    .padding(4.dp), Arrangement.Center, Alignment.CenterHorizontally) {
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

        SendButtonView(Modifier.align(Alignment.BottomEnd), graphicsLayer, songView)

        if (bgColorAlert) HsvColorPicker(modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth()
            .height(450.dp)
            .padding(10.dp),
            controller,
            onColorChanged = { colorEnvelope: ColorEnvelope ->
                bgColor = colorEnvelope.color
            })

        if (emojiAlert) EmojisStripe(Modifier.align(Alignment.BottomCenter)) {
            emojis = it
        }
    }


    if (songAlert) ZeneConnectSongSearchView {
        songAlert = false
        songView = it
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
            .height(170.dp)
            .background(Color.White)
    )
}
