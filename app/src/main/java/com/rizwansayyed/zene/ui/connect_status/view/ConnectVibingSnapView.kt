package com.rizwansayyed.zene.ui.connect_status.view

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.connect_status.utils.ImageCapturingUtils
import com.rizwansayyed.zene.ui.connect_status.utils.MAX_VIDEO_CAPTURE_LENGTH
import com.rizwansayyed.zene.ui.connect_status.utils.VideoCapturingUtils
import com.rizwansayyed.zene.ui.main.connect.profile.SettingsViewSimpleItems
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.utils.MainUtils.openAppSettings
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.viewmodel.ConnectViewModel

val permissions = listOf(
    Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
)

@Composable
fun ConnectVibingSnapView(viewModel: ConnectViewModel) {
    var showAlert by remember { mutableStateOf(false) }
    val needPermission = stringResource(R.string.need_camera_microphone_permission_to_photo)

    val request =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it.any { v -> !v.value }) {
                openAppSettings()
                needPermission.toast()
            } else {
                showAlert = true
            }
        }

    Spacer(Modifier.height(45.dp))
    SettingsViewSimpleItems(R.drawable.ic_camera, R.string.add_vibing_snap) {
        request.launch(permissions.toTypedArray())
    }

    if (showAlert) Dialog(
        { showAlert = false }, DialogProperties(usePlatformDefaultWidth = false)
    ) {
        ConnectVibingSnapAlertNew(viewModel) {
            showAlert = false
        }
    }
}

@Composable
fun ConnectVibingSnapAlertNew(viewModel: ConnectViewModel, close: () -> Unit) {
    var imageCameraUtils by remember { mutableStateOf<ImageCapturingUtils?>(null) }
    var videoCameraUtils by remember { mutableStateOf<VideoCapturingUtils?>(null) }
    var isVideoRecording by remember { mutableStateOf(false) }
    var isCameraTorch by remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (isVideoRecording) {
            var isRecordingStarted by remember { mutableStateOf(false) }
            AndroidView(
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        videoCameraUtils = VideoCapturingUtils(this, ctx, lifecycleOwner)
                        videoCameraUtils?.generateVideoPreview()
                    }
                }, modifier = Modifier.fillMaxSize()
            )

            if (isRecordingStarted) {
                var currentProgress by remember { mutableFloatStateOf(0f) }
                val progressAnimate by animateFloatAsState(
                    targetValue = currentProgress, animationSpec = tween(
                        durationMillis = 300, delayMillis = 50, easing = LinearOutSlowInEasing
                    ), label = ""
                )
                Box(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .clickable { videoCameraUtils?.stopVideo() }) {
                    CircularProgressIndicator(
                        progress = { progressAnimate },
                        strokeWidth = 5.dp,
                        modifier = Modifier
                            .padding(20.dp)
                            .padding(bottom = 25.dp)
                            .width(70.dp),
                        color = MainColor,
                        trackColor = Color.White,
                    )

                    Box(
                        Modifier
                            .align(Alignment.Center)
                            .offset(y = 2.dp)
                    ) {
                        ImageIcon(R.drawable.ic_stop, size = 25)
                    }
                }

                LaunchedEffect(videoCameraUtils?.currentRecordingDifference) {
                    currentProgress = ((videoCameraUtils?.currentRecordingDifference
                        ?: 0).toFloat() / MAX_VIDEO_CAPTURE_LENGTH * 100) / 100
                }
            } else {
                Spacer(Modifier
                    .align(Alignment.BottomCenter)
                    .padding(20.dp)
                    .size(70.dp)
                    .clip(RoundedCornerShape(100))
                    .clickable { videoCameraUtils?.captureVideo() }
                    .background(Color.White))
            }

            DisposableEffect(Unit) {
                onDispose {
                    if (videoCameraUtils != null) {
                        videoCameraUtils?.clearCamera()
                        videoCameraUtils = null
                    }
                }
            }

            LaunchedEffect(videoCameraUtils?.videoRecordEvent) {
                isRecordingStarted = when (videoCameraUtils?.videoRecordEvent) {
                    is VideoRecordEvent.Status -> true
                    is VideoRecordEvent.Finalize -> false
                    else -> false
                }
            }
        } else {
            AndroidView(
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        imageCameraUtils = ImageCapturingUtils(this, ctx, lifecycleOwner)
                        imageCameraUtils?.generateCameraPreview()
                    }
                }, modifier = Modifier.fillMaxSize()
            )

            Spacer(Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp)
                .size(70.dp)
                .clip(RoundedCornerShape(100))
                .clickable { imageCameraUtils?.captureImage() }
                .background(Color.White))

            DisposableEffect(Unit) {
                onDispose {
                    if (imageCameraUtils != null) {
                        imageCameraUtils?.clearCamera()
                        imageCameraUtils = null
                    }
                }
            }
        }

        Column(
            Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Gray)
                .padding(vertical = 10.dp, horizontal = 5.dp)
        ) {
            Box(Modifier.clickable {
                if (imageCameraUtils != null) {
                    imageCameraUtils!!.changeCameraLens()
                    imageCameraUtils!!.generateCameraPreview()
                } else if (videoCameraUtils != null) {
                    videoCameraUtils!!.changeCameraLens()
                    videoCameraUtils!!.generateVideoPreview()
                }
            }) {
                ImageIcon(R.drawable.ic_camera_rotated, size = 20)
            }

            Spacer(Modifier.height(10.dp))
            Box(Modifier.clickable {
                if (imageCameraUtils != null) {
                    imageCameraUtils?.cameraTorch()
                } else if (videoCameraUtils != null) {
                    videoCameraUtils?.cameraTorch()
                }
            }) {
                if (isCameraTorch) ImageIcon(R.drawable.ic_flash, size = 20)
                else ImageIcon(R.drawable.ic_flash_off, size = 20)
            }

            Spacer(Modifier.height(10.dp))
            Box(Modifier.clickable {
                isVideoRecording = !isVideoRecording
            }) {
                if (isVideoRecording) ImageIcon(R.drawable.ic_camera, size = 20)
                else ImageIcon(R.drawable.ic_camera_video, size = 20)
            }
        }
    }

    LaunchedEffect(imageCameraUtils?.isFlashLight, videoCameraUtils?.isFlashLight) {
        if (imageCameraUtils != null) isCameraTorch = imageCameraUtils!!.isFlashLight
        else if (videoCameraUtils != null) isCameraTorch = videoCameraUtils!!.isFlashLight
    }

    LaunchedEffect(imageCameraUtils?.vibeFiles, videoCameraUtils?.vibeFiles) {
        if (imageCameraUtils?.vibeFiles != null) {
            viewModel.updateVibeFileInfo(imageCameraUtils!!.vibeFiles, true)
            close()
        } else if (videoCameraUtils?.vibeFiles != null) {
            viewModel.updateVibeFileInfo(videoCameraUtils!!.vibeFiles, true)
            close()
        }
    }

    LifecycleResumeEffect(Unit) {
        onPauseOrDispose { close() }
    }
}
