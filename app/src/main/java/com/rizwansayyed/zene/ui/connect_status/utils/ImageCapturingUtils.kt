package com.rizwansayyed.zene.ui.connect_status.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Build
import android.view.MotionEvent
import android.view.Surface
import androidx.camera.core.Camera
import androidx.camera.core.DisplayOrientedMeteringPointFactory
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceOrientedMeteringPointFactory
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.LifecycleOwner
import com.rizwansayyed.zene.ui.connect_status.utils.CameraUtils.Companion.afterMeasured
import com.rizwansayyed.zene.ui.connect_status.utils.CameraUtils.Companion.imageCapture
import com.rizwansayyed.zene.ui.connect_status.utils.CameraUtils.Companion.selectorHD
import com.rizwansayyed.zene.ui.connect_status.utils.CameraUtils.Companion.vibeCompressedImageFile
import com.rizwansayyed.zene.ui.connect_status.utils.CameraUtils.Companion.vibeImageFile
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class ImageCapturingUtils(
    private val previewMain: PreviewView,
    private val ctx: Context,
    private val lifecycleOwner: LifecycleOwner
) {
    private val cameraUtils = CameraUtils(ctx, previewMain)
    private val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
    private val cameraProvider = cameraProviderFuture.get()
    private var camera: Camera? = null
    private var isBack: Boolean = true
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

    @SuppressLint("ClickableViewAccessibility")
    fun generateCameraPreview() {
        clearCamera()
        cameraProviderFuture.addListener({
            val preview = Preview.Builder().setTargetRotation(Surface.ROTATION_0)
                .setResolutionSelector(selectorHD).build().also {
                    it.surfaceProvider = previewMain.surfaceProvider
                }

            previewMain.implementationMode = PreviewView.ImplementationMode.PERFORMANCE


            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraUtils.cameraSelector(isBack), preview, imageCapture
                )
                previewMain.afterMeasured {
                    previewMain.setOnTouchListener { _, event ->
                        return@setOnTouchListener startFocus(event)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(ctx))
    }

    fun captureImage() {
        vibeCompressedImageFile.delete()
        vibeImageFile.delete()
        val outputOptions = ImageCapture.OutputFileOptions.Builder(vibeImageFile).build()

        val callback = object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(file: ImageCapture.OutputFileResults) {
                file.savedUri?.toFile()?.let {
                    val compressed = compressImageHighQuality(it, vibeCompressedImageFile)
                    if (compressed) vibeFiles = vibeCompressedImageFile
                    else vibeFiles = it
                }
            }

            override fun onError(p0: ImageCaptureException) {
                vibeFiles = null
            }
        }

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(ctx), callback)
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
}