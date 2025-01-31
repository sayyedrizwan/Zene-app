package com.rizwansayyed.zene.ui.connect_status.utils

import android.content.Context
import androidx.camera.core.Camera
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LifecycleOwner

class VideoCapturingUtils(
    private val previewMain: PreviewView,
    private val ctx: Context,
    private val lifecycleOwner: LifecycleOwner
) {
    private val cameraUtils = CameraUtils(ctx, previewMain)
    private val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
    private var camera: Camera? = null
    private var isBack: Boolean = true
    var isFlashLight by mutableStateOf(false)

    fun changeCameraLens() {
        isBack = !isBack
    }

    fun cameraTorch() {
        isFlashLight = !isFlashLight
        camera?.let { cameraUtils.cameraFlash(isFlashLight, it) }
    }


    fun clearCamera() {
        try {
            val cameraProvider = cameraProviderFuture.get()
            cameraProvider.unbindAll()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun generateVideoPreview() {

    }
}