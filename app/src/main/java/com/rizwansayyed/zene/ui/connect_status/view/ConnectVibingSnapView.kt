package com.rizwansayyed.zene.ui.connect_status.view

import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.main.connect.profile.SettingsViewSimpleItems
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
        ConnectVibingSnapAlert()
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
                cameraUtils?.generateCameraPreview()
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
                isFlashLight = !isFlashLight
                cameraUtils?.cameraFlash(isFlashLight)
            }) {
                if (isVideo) ImageIcon(R.drawable.ic_camera, size = 20)
                else ImageIcon(R.drawable.ic_camera_video, size = 20)
            }
        }


        Button(
            onClick = {
                cameraUtils?.captureImage({ it.absoluteFile.toast() }, {})
            }, modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text("Capture")
        }
    }
}
