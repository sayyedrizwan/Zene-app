package com.rizwansayyed.zene.ui.connect_status.utils

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraMetadata
import android.os.Build
import android.view.MotionEvent
import android.view.Surface
import androidx.annotation.OptIn
import androidx.camera.camera2.interop.Camera2CameraInfo
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.DisplayOrientedMeteringPointFactory
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceOrientedMeteringPointFactory
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.lifecycle.LifecycleOwner
import com.rizwansayyed.zene.ui.connect_status.utils.CameraUtils.Companion.afterMeasured
import com.rizwansayyed.zene.ui.connect_status.utils.CameraUtils.Companion.selectorHD
import com.rizwansayyed.zene.ui.connect_status.utils.CameraUtils.Companion.vibeVideoFile
import com.rizwansayyed.zene.utils.MainUtils.timeDifferenceInSeconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

const val MAX_VIDEO_CAPTURE_LENGTH = 11

class VideoCapturingUtils(
    private val previewMain: PreviewView,
    private val ctx: Context,
    private val lifecycleOwner: LifecycleOwner
) {
    private val cameraUtils = CameraUtils(ctx, previewMain)
    private val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
    private val cameraProvider = cameraProviderFuture.get()
    private var camera: Camera? = null
    private var videoCaptureCamera: VideoCapture<Recorder>? = null

    private var vidRecording: Recording? = null
    private var isBack: Boolean = true
    var videoRecordEvent by mutableStateOf<VideoRecordEvent?>(null)
    private var currentRecordingDuration by mutableLongStateOf(System.currentTimeMillis())
    var currentRecordingDifference by mutableLongStateOf(0L)
    var isFlashLight by mutableStateOf(false)
    var vibeFiles by mutableStateOf<File?>(null)

    fun changeCameraLens() {
        isBack = !isBack
    }

    fun cameraTorch() {
        isFlashLight = !isFlashLight
        camera?.let { cameraUtils.cameraFlash(isFlashLight, it) }
    }

    fun clearCamera() {
        try {
            cameraProvider.unbindAll()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startFocus(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> true
            MotionEvent.ACTION_UP -> {
                val factory = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    DisplayOrientedMeteringPointFactory(
                        ctx.display,
                        camera?.cameraInfo!!,
                        previewMain.width.toFloat(),
                        previewMain.height.toFloat()
                    )
                } else {
                    SurfaceOrientedMeteringPointFactory(
                        previewMain.width.toFloat(), previewMain.height.toFloat()
                    )
                }

                val autoFocusPoint = factory.createPoint(event.x, event.y)
                try {
                    camera?.cameraControl?.startFocusAndMetering(
                        FocusMeteringAction.Builder(
                            autoFocusPoint, FocusMeteringAction.FLAG_AF
                        ).apply {
                            disableAutoCancel()
                        }.build()
                    )
                    cameraUtils.showFocusCircle(event.x, event.y)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                true
            }

            else -> false
        }
    }

    @OptIn(androidx.camera.camera2.interop.ExperimentalCamera2Interop::class)
    @SuppressLint("ClickableViewAccessibility")
    fun generateVideoPreview() {
        stopVideo()
        clearCamera()

        val cameraInfo = cameraProvider.availableCameraInfos.filter {
            if (isBack) Camera2CameraInfo.from(it)
                .getCameraCharacteristic(CameraCharacteristics.LENS_FACING) == CameraMetadata.LENS_FACING_BACK
            else Camera2CameraInfo.from(it)
                .getCameraCharacteristic(CameraCharacteristics.LENS_FACING) == CameraMetadata.LENS_FACING_FRONT
        }

        val supportedQualities = QualitySelector.getSupportedQualities(cameraInfo[0])
        val filteredQualities = arrayListOf(
            Quality.FHD, Quality.HD, Quality.SD
        ).filter { supportedQualities.contains(it) }

        cameraProviderFuture.addListener({
            val qualitySelector = QualitySelector.from(filteredQualities[0])

            val recorder = Recorder.Builder().setExecutor(ContextCompat.getMainExecutor(ctx))
                .setQualitySelector(qualitySelector).build()

            videoCaptureCamera = VideoCapture.withOutput(recorder)

            val preview = Preview.Builder().setTargetRotation(Surface.ROTATION_0)
                .setResolutionSelector(selectorHD).build().also {
                    it.surfaceProvider = previewMain.surfaceProvider
                }
            previewMain.implementationMode = PreviewView.ImplementationMode.PERFORMANCE
            val videoCameraSelector =
                if (isBack) CameraSelector.DEFAULT_BACK_CAMERA else CameraSelector.DEFAULT_FRONT_CAMERA

            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(
                lifecycleOwner, videoCameraSelector, preview, videoCaptureCamera
            )

            previewMain.afterMeasured {
                previewMain.setOnTouchListener { _, event ->
                    return@setOnTouchListener startFocus(event)
                }
            }
        }, ContextCompat.getMainExecutor(ctx))
    }


    @SuppressLint("MissingPermission")
    fun captureVideo() {
        vibeVideoFile.delete()
        val outputOptions = FileOutputOptions.Builder(vibeVideoFile).build()

        vidRecording = videoCaptureCamera?.output?.prepareRecording(ctx, outputOptions)?.apply {
            withAudioEnabled()
        }?.start(ContextCompat.getMainExecutor(ctx)) {
            videoRecordEvent = it

            if (it is VideoRecordEvent.Start) currentRecordingDuration = System.currentTimeMillis()
            else if (it is VideoRecordEvent.Status) {
                currentRecordingDifference = timeDifferenceInSeconds(currentRecordingDuration)
                if (timeDifferenceInSeconds(currentRecordingDuration) >= MAX_VIDEO_CAPTURE_LENGTH) stopVideo()
            } else if (it is VideoRecordEvent.Finalize && !it.hasError()) {
                vibeFiles = it.outputResults.outputUri.toFile()
            }
        }
    }

    fun stopVideo() = CoroutineScope(Dispatchers.Main).launch {
        vidRecording?.stop()
        vidRecording?.close()
    }
}