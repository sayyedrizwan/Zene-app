package com.rizwansayyed.zene.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraMetadata
import android.os.Build
import android.view.MotionEvent
import android.view.Surface
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.camera.camera2.interop.Camera2CameraInfo
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.DisplayOrientedMeteringPointFactory
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceOrientedMeteringPointFactory
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toFile
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.LifecycleOwner
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.utils.MainUtils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.time.Duration.Companion.seconds

class ImageCapturingView(
    private val previewMain: PreviewView,
    private val ctx: Context,
    private val lifecycleOwner: LifecycleOwner
) {
    private val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

    private val selectorHD = ResolutionSelector.Builder()
        .setAspectRatioStrategy(AspectRatioStrategy.RATIO_16_9_FALLBACK_AUTO_STRATEGY)
        .setAllowedResolutionMode(ResolutionSelector.PREFER_CAPTURE_RATE_OVER_HIGHER_RESOLUTION)
        .build()

    private var cameraSelector =
        CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

    private var videoCameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture =
        ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .setResolutionSelector(selectorHD).setTargetRotation(Surface.ROTATION_0)
            .setJpegQuality(100).build()

    private var camera: Camera? = null

    private var videoCaptureCamera: VideoCapture<Recorder>? = null

    fun changeCamera(isBack: Boolean) = apply {
        cameraSelector = CameraSelector.Builder()
            .requireLensFacing(if (isBack) CameraSelector.LENS_FACING_BACK else CameraSelector.LENS_FACING_FRONT)
            .build()

        videoCameraSelector =
            if (isBack) CameraSelector.DEFAULT_BACK_CAMERA else CameraSelector.DEFAULT_FRONT_CAMERA
    }

    fun cameraFlash(on: Boolean) {
        if (camera?.cameraInfo?.hasFlashUnit() == true) {
            camera!!.cameraControl.enableTorch(on)
        } else {
            "no flashlight support".toast()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun startFocus() {
        previewMain.afterMeasured {
            previewMain.setOnTouchListener { _, event ->
                return@setOnTouchListener when (event.action) {
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
                            showFocusCircle(event.x, event.y)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private val circle = View(ctx)
    private fun showFocusCircle(x: Float, y: Float) {
        previewMain.removeView(circle)
        val params = FrameLayout.LayoutParams(100, 100)
        params.leftMargin = (x - 75).toInt()
        params.topMargin = (y - 75).toInt()

        circle.layoutParams = params
        circle.background =
            ResourcesCompat.getDrawable(ctx.resources, R.drawable.rounded_circle_border, null)
        circle.visibility = View.VISIBLE
        previewMain.addView(circle)

        CoroutineScope(Dispatchers.Main).launch {
            delay(1.seconds)
            previewMain.removeView(circle)
        }
    }

    fun clearCamera() {
        try {
            val cameraProvider = cameraProviderFuture.get()
            cameraProvider.unbindAll()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun generateCameraPreview() {
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().setTargetRotation(Surface.ROTATION_0)
                .setResolutionSelector(selectorHD).build().also {
                    it.surfaceProvider = previewMain.surfaceProvider
                }

            previewMain.implementationMode = PreviewView.ImplementationMode.PERFORMANCE


            val imageAnalysis = ImageAnalysis.Builder().setTargetRotation(Surface.ROTATION_0)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build()


            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, preview, imageCapture, imageAnalysis
                )
                startFocus()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(ctx))
    }

    fun captureImage(done: (File) -> Unit, error: () -> Unit) {
        val folder = File(ctx.filesDir, "temp_img").apply {
            mkdirs()
        }
        val file = File(folder, "temp_img.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

        val callback = object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(file: ImageCapture.OutputFileResults?) {
                val fileSave = File(folder, "temp_img_com.jpg")
                file?.savedUri?.toFile()?.let {
                    val compressed = compressImageHighQuality(it, fileSave)
                    if (compressed) done(fileSave)
                    else done(it)
                }
            }

            override fun onError(p0: ImageCaptureException) {
                error()
            }

        }

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(ctx), callback)
    }

    @OptIn(ExperimentalCamera2Interop::class)
    fun generateVideoPreview() {
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val cameraInfo = cameraProvider.availableCameraInfos.filter {
                Camera2CameraInfo.from(it)
                    .getCameraCharacteristic(CameraCharacteristics.LENS_FACING) == CameraMetadata.LENS_FACING_BACK
            }

            val supportedQualities = QualitySelector.getSupportedQualities(cameraInfo[0])
            val filteredQualities =
                arrayListOf(Quality.FHD, Quality.HD).filter { supportedQualities.contains(it) }

            try {
                val qualitySelector = QualitySelector.from(filteredQualities[0])

                val recorder = Recorder.Builder().setExecutor(ContextCompat.getMainExecutor(ctx))
                    .setQualitySelector(qualitySelector).build()

                videoCaptureCamera = VideoCapture.withOutput(recorder)

                val preview = Preview.Builder().setTargetRotation(Surface.ROTATION_0)
                    .setResolutionSelector(selectorHD).build().also {
                        it.surfaceProvider = previewMain.surfaceProvider
                    }

                previewMain.implementationMode = PreviewView.ImplementationMode.PERFORMANCE

                camera = cameraProvider.bindToLifecycle(
                    lifecycleOwner, videoCameraSelector, preview, videoCaptureCamera
                )
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(ctx))
    }

    fun captureVideo(done: (VideoRecordEvent) -> Unit) {
        val folder = File(ctx.filesDir, "temp_img").apply {
            mkdirs()
        }
        val file = File(folder, "temp_vid.mp4")
        val outputOptions = FileOutputOptions.Builder(file).build()

        val vid = videoCaptureCamera?.output?.prepareRecording(ctx, outputOptions)?.apply {
            withAudioEnabled()
        }?.start(ContextCompat.getMainExecutor(ctx)) {
            done(it)
        }

        CoroutineScope(Dispatchers.Main).launch {
            delay(4.seconds)
            vid?.close()
        }
    }

    fun compressImageHighQuality(
        imageFile: File,
        targetFile: File,
        maxWidth: Int = 3048,
        maxHeight: Int = 3048,
        quality: Int = 50
    ): Boolean {
        try {
            val exifInterface = ExifInterface(imageFile)
            val orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL
            )
            var originalBitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
            val matrix = Matrix()
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.postScale(-1f, 1f)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.postScale(1f, -1f)
                else -> {}
            }
            originalBitmap = Bitmap.createBitmap(
                originalBitmap, 0, 0, originalBitmap.width, originalBitmap.height, matrix, true
            )

            val aspectRatio = originalBitmap.width.toFloat() / originalBitmap.height.toFloat()

            var newWidth = originalBitmap.width
            var newHeight = originalBitmap.height

            if (originalBitmap.width > maxWidth || originalBitmap.height > maxHeight) {
                if (originalBitmap.width > originalBitmap.height) {
                    newWidth = maxWidth
                    newHeight = (newWidth / aspectRatio).toInt()
                } else {
                    newHeight = maxHeight
                    newWidth = (newHeight * aspectRatio).toInt()
                }
            }

            val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true)
            val byteArrayOutputStream = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
            val fileOutputStream = FileOutputStream(targetFile)
            fileOutputStream.write(byteArrayOutputStream.toByteArray())
            fileOutputStream.flush()
            fileOutputStream.close()
            originalBitmap.recycle()
            resizedBitmap.recycle()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }


    private inline fun View.afterMeasured(crossinline block: () -> Unit) {
        if (measuredWidth > 0 && measuredHeight > 0) {
            block()
        } else {
            viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (measuredWidth > 0 && measuredHeight > 0) {
                        viewTreeObserver.removeOnGlobalLayoutListener(this)
                        block()
                    }
                }
            })
        }
    }
}