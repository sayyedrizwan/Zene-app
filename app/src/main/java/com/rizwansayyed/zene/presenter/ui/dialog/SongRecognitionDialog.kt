package com.rizwansayyed.zene.presenter.ui.dialog

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.LoadingStateBar
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.utils.RecordMicAudio
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


@Composable
fun SongRecognitionDialog() {
    val recordMicAudio = RecordMicAudio()

    val navViewModel: HomeNavViewModel = hiltViewModel()
    val coroutines = rememberCoroutineScope()
    val width = LocalConfiguration.current.screenHeightDp.dp / 2

    var job by remember { mutableStateOf<Job?>(null) }
    var startRec by remember { mutableStateOf(false) }
    var processing by remember { mutableStateOf(false) }
    var captureDuration by remember { mutableIntStateOf(0) }

    val needAudioPermission =
        stringResource(id = R.string.need_audio_permission_to_start_recognition_the_song)

    val recordPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) coroutines.launch {
                startRec = true
                delay(1.seconds)
                recordMicAudio.startRecordingMusicSearch()
            }
            else needAudioPermission.toast()
        }

    Dialog(onDismissRequest = { navViewModel.songRecognitionDialog(false) }) {
        Card(
            Modifier
                .fillMaxWidth()
                .height(width), RoundedCornerShape(16.dp), CardDefaults.cardColors(MainColor)
        ) {
            Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
                Spacer(Modifier.height(20.dp))

                if (processing)
                    TextSemiBold(v = stringResource(R.string.processing_the_song_please_wait))
                else
                    TextSemiBold(v = stringResource(if (startRec) R.string.listening_ else R.string.tap_to_discover_the_song))

                Spacer(Modifier.height(50.dp))

                if (processing)
                    LoadingStateBar()
                else
                    SongRecButtonInfo(startRec, captureDuration) {
                        recordPermission.launch(Manifest.permission.RECORD_AUDIO)
                    }
            }
        }
    }

    LaunchedEffect(startRec) {
        if (startRec) {
            captureDuration = 0
            job = CoroutineScope(Dispatchers.IO).launch {
                while (captureDuration < 100) {
                    captureDuration += 10
                    delay(1.seconds)
                }
                recordMicAudio.stopRecording()
                processing = true
            }
        } else {
            job?.cancel()
            coroutines.launch {
                recordMicAudio.stopRecording()
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            startRec = false
            processing = false
            job?.cancel()
            coroutines.launch {
                recordMicAudio.stopRecording()
            }
        }
    }
}

@Composable
fun SongRecButtonInfo(startRec: Boolean, captureDuration: Int, p: () -> Unit) {
    Box(
        Modifier
            .clip(RoundedCornerShape(100))
            .background(Color.Black)
    ) {
        if (startRec)
            CircularProgressIndicator(
                progress = (captureDuration.toFloat() / 100),
                modifier = Modifier
                    .padding(horizontal = 40.dp)
                    .padding(top = 37.dp, bottom = 40.dp)
                    .width(44.dp),
                color = MainColor,
                strokeWidth = 4.dp,
                trackColor = Color.White,
            )
        else
            SmallIcons(icon = R.drawable.ic_mic, 40, 40, p)
    }
}