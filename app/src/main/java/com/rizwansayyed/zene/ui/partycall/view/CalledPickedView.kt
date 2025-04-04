package com.rizwansayyed.zene.ui.partycall.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.partycall.PartyViewModel
import com.rizwansayyed.zene.ui.view.ImageIcon
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun CalledPickedView(viewModel: PartyViewModel) {
    var showShortcut by remember { mutableStateOf(true) }
    var job by remember { mutableStateOf<Job?>(null) }
    val coroutine = rememberCoroutineScope()

    LaunchedEffect(showShortcut) {
        job?.cancel()
        job = coroutine.launch {
            delay(4.seconds)
            showShortcut = false
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { showShortcut = !showShortcut }
            .pointerInput(Unit) {
                detectDragGestures { _, _ ->
                    showShortcut = !showShortcut
                }
            }
            .fillMaxSize()) {

        AnimatedVisibility(showShortcut, Modifier.align(Alignment.BottomCenter)) {
            Row(
                Modifier
                    .padding(horizontal = 10.dp, vertical = 70.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .padding(horizontal = 5.dp, vertical = 20.dp),
                Arrangement.SpaceEvenly,
                Alignment.CenterVertically
            ) {
                Button({ viewModel.callWebViewMain?.evaluateJavascript("toggleVideo();") {} }) {
                    if (viewModel.isVideoOn)
                        ImageIcon(R.drawable.ic_video_camera, 25, Color.Black)
                    else
                        ImageIcon(R.drawable.ic_video_camer_off, 25, Color.Black)
                }

                ImageIcon(R.drawable.ic_camera_video, 25, Color.Black)
                ImageIcon(R.drawable.ic_camera_video, 25, Color.Black)
                ImageIcon(R.drawable.ic_camera_video, 25, Color.Black)
                ImageIcon(R.drawable.ic_camera_video, 25, Color.Black)
            }
        }
    }
}