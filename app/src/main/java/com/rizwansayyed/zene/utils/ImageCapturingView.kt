package com.rizwansayyed.zene.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.view.Surface
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.LifecycleOwner
import com.rizwansayyed.zene.utils.MainUtils.toast
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

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

    private var imageCapture: ImageCapture? = null
    private var camera: Camera? = null

    fun changeCamera(isBack: Boolean) = apply {
        cameraSelector = CameraSelector.Builder()
            .requireLensFacing(if (isBack) CameraSelector.LENS_FACING_BACK else CameraSelector.LENS_FACING_FRONT)
            .build()
    }

    fun cameraFlash(on: Boolean) {
        if (camera?.cameraInfo?.hasFlashUnit() == true) {
            camera!!.cameraControl.enableTorch(on)
        } else {
            "no flashlight support".toast()
        }
    }

    fun generateCameraPreview() {
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().setTargetRotation(Surface.ROTATION_0)
                .setResolutionSelector(selectorHD).build().also {
                    it.surfaceProvider = previewMain.surfaceProvider
                }

            previewMain.implementationMode = PreviewView.ImplementationMode.PERFORMANCE

            imageCapture =
                ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                    .setResolutionSelector(selectorHD).setTargetRotation(Surface.ROTATION_0)
                    .setJpegQuality(100).build()

            val imageAnalysis = ImageAnalysis.Builder().setTargetRotation(Surface.ROTATION_0)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build()
            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, preview, imageCapture, imageAnalysis
                )

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

        imageCapture?.takePicture(outputOptions, ContextCompat.getMainExecutor(ctx), callback)
    }

    fun compressImageHighQuality(
        imageFile: File, targetFile: File, maxWidth: Int = 3048, maxHeight: Int = 3048, quality: Int = 50
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