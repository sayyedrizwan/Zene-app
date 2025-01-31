package com.rizwansayyed.zene.ui.connect_status.view

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.connect_status.utils.ImageCapturingUtils
import com.rizwansayyed.zene.ui.connect_status.utils.VideoCapturingUtils
import com.rizwansayyed.zene.ui.main.connect.profile.SettingsViewSimpleItems
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.utils.ImageCapturingView
import com.rizwansayyed.zene.utils.MainUtils.toast

@Composable
fun ConnectVibingSnapView() {
    var showAlert by remember { mutableStateOf(false) }
    Spacer(Modifier.height(45.dp))
    SettingsViewSimpleItems(R.drawable.ic_camera, R.string.add_vibing_snap) {
        showAlert = true
    }

    if (showAlert) Dialog(
        { showAlert = false }, DialogProperties(usePlatformDefaultWidth = false)
    ) {
        ConnectVibingSnapAlertNew {
            showAlert = false
        }
    }
}

@Composable
fun ConnectVibingSnapAlertNew(close: () -> Unit) {
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
                var currentProgress by remember { mutableStateOf(0f) }
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
                        ?: 0).toFloat() / 15 * 100) / 100
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
                onDispose { videoCameraUtils?.clearCamera() }
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
                .clickable { imageCameraUtils?.captureImage({}, {}) }
                .background(Color.White))

            DisposableEffect(Unit) {
                onDispose { imageCameraUtils!!.clearCamera() }
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
                    videoCameraUtils?.changeCameraLens()
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

    LifecycleResumeEffect(Unit) {
        onPauseOrDispose { close() }
    }
}

@Composable
fun ConnectVibingSnapAlert() {
    var cameraUtils by remember { mutableStateOf<ImageCapturingView?>(null) }
    var isCameraBack by remember { mutableStateOf(true) }
    var isFlashLight by remember { mutableStateOf(false) }
    var isVideo by remember { mutableStateOf(false) }


    val lifecycleOwner = LocalLifecycleOwner.current

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                cameraUtils = ImageCapturingView(previewView, ctx, lifecycleOwner)
                cameraUtils?.generateCameraPreview()
                previewView
            }, modifier = Modifier.fillMaxSize()
        )


        Column(
            Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Gray)
                .padding(vertical = 10.dp, horizontal = 5.dp)
        ) {
            Box(Modifier.clickable {
                isCameraBack = !isCameraBack
                cameraUtils?.changeCamera(isCameraBack)
                cameraUtils?.clearCamera()
                if (isVideo) cameraUtils?.generateVideoPreview()
                else cameraUtils?.generateCameraPreview()
            }) {
                ImageIcon(R.drawable.ic_camera_rotated, size = 20)
            }

            Spacer(Modifier.height(10.dp))
            Box(Modifier.clickable {
                isFlashLight = !isFlashLight
                cameraUtils?.cameraFlash(isFlashLight)
            }) {
                if (isFlashLight) ImageIcon(R.drawable.ic_flash, size = 20)
                else ImageIcon(R.drawable.ic_flash_off, size = 20)
            }

            Spacer(Modifier.height(10.dp))
            Box(Modifier.clickable {
                isVideo = !isVideo
                cameraUtils?.clearCamera()
                if (isVideo) cameraUtils?.generateVideoPreview()
                else cameraUtils?.generateCameraPreview()
            }) {
                if (isVideo) ImageIcon(R.drawable.ic_camera, size = 20)
                else ImageIcon(R.drawable.ic_camera_video, size = 20)
            }
        }


        if (isVideo) {
            var recordingEvents by remember { mutableStateOf<VideoRecordEvent?>(null) }

            when (val v = recordingEvents) {
                is VideoRecordEvent.Start -> {

                }

                is VideoRecordEvent.Finalize -> {
                    recordingEvents = null
                    if (!v.hasError()) {
                        val msg = "Video capture succeeded: " + "${v.outputResults.outputUri}"

                        msg.toast()
                    }
                }
            }

            fun clickVideo() {
                cameraUtils?.captureVideo { recordingEvents = it }
            }

            if (recordingEvents == null) {
                Spacer(
                    Modifier
                        .clickable { clickVideo() }
                        .size(100.dp)
                        .clip(RoundedCornerShape(100))
                        .background(Color.White))
            }
        } else {
            Spacer(
                Modifier
                    .clickable { cameraUtils?.captureImage({ it.absoluteFile.toast() }, {}) }
                    .size(100.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color.White))
        }
    }
}
