package com.rizwansayyed.zene.ui.videoplayer.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.datastore.DataStorageManager.videoQualityDB
import com.rizwansayyed.zene.datastore.DataStorageManager.videoSpeedDB
import com.rizwansayyed.zene.datastore.model.VideoQualityEnum
import com.rizwansayyed.zene.datastore.model.VideoSpeedEnum
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ButtonWithBorder
import com.rizwansayyed.zene.ui.view.ImageWithBorder
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextWithBgAndBorder
import com.rizwansayyed.zene.viewmodel.PlayingVideoInfoViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun VideoPlayerButtonView(viewModel: PlayingVideoInfoViewModel) {
    val quality by videoQualityDB.collectAsState(null)

    val coroutine = rememberCoroutineScope()

    ButtonWithBorder(
        R.string.fourteen_forty_eighty_p,
        if (quality == VideoQualityEnum.`1440`) Color.White else Color.Gray
    ) {
        coroutine.launch { videoQualityDB = flowOf(VideoQualityEnum.`1440`) }
        viewModel.loadWebView(false)
    }
    Spacer(Modifier.height(14.dp))
    ButtonWithBorder(
        R.string.seven_twenty_p, if (quality == VideoQualityEnum.`720`) Color.White else Color.Gray
    ) {
        coroutine.launch { videoQualityDB = flowOf(VideoQualityEnum.`720`) }
        viewModel.loadWebView(false)
    }
    Spacer(Modifier.height(14.dp))
    ButtonWithBorder(
        R.string.four_eighty_p, if (quality == VideoQualityEnum.`480`) Color.White else Color.Gray
    ) {
        coroutine.launch { videoQualityDB = flowOf(VideoQualityEnum.`480`) }
        viewModel.loadWebView(false)
    }
    Spacer(Modifier.height(14.dp))
}

@Composable
fun VideoPlayerSpeedView(viewModel: PlayingVideoInfoViewModel) {
    val videoSpeed by videoSpeedDB.collectAsState(null)
    var showAlert by remember { mutableStateOf(false) }

    Box(Modifier, Alignment.Center) {
        ImageWithBorder(R.drawable.ic_dashboard_speed) {
            showAlert = true
        }
        Box(
            Modifier
                .align(Alignment.BottomEnd)
                .offset(y = 6.dp)
        ) {
            TextViewBold(
                "x${videoSpeed?.name?.replace("_", ".")}", 12, color = Color.Gray
            )
        }
    }


    if (showAlert) Dialog(
        { showAlert = false }, DialogProperties(usePlatformDefaultWidth = false)
    ) {
        val coroutines = rememberCoroutineScope()
        var job by remember { mutableStateOf<Job?>(null) }

        VideoSpeedChangeAlert(videoSpeed) {
            val name = it.name.replace("_", ".")
            coroutines.launch {
                videoSpeedDB = flowOf(it)
            }
            viewModel.webView?.evaluateJavascript("playbackRate(${name});", null)
        }

        DisposableEffect(Unit) {
            job?.cancel()
            job = coroutines.launch {
                while (true) {
                    viewModel.showControlView(true)
                    delay(1.seconds)
                }
            }
            onDispose { job?.cancel() }
        }
    }
}

@Composable
fun VideoSpeedChangeAlert(videoSpeed: VideoSpeedEnum?, changed: (VideoSpeedEnum) -> Unit) {
    Column(
        Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MainColor)
    ) {
        Spacer(Modifier.height(25.dp))
        Box(Modifier.padding(horizontal = 6.dp)) {
            TextViewBold(stringResource(R.string.playback_speed_of_video), 17)
        }
        Spacer(Modifier.height(14.dp))
        Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
            VideoSpeedEnum.entries.forEach {
                val name = it.name.replace("_", ".")
                TextWithBgAndBorder("x${name}", if (videoSpeed == it) Color.Gray else Color.Black) {
                    changed(it)
                }
            }
        }
        Spacer(Modifier.height(35.dp))
    }
}