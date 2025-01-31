package com.rizwansayyed.zene.ui.connect_status.utils

import android.content.Context
import android.view.Surface
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.view.PreviewView
import androidx.core.content.res.ResourcesCompat
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.utils.MainUtils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import kotlin.time.Duration.Companion.seconds

class CameraUtils(private val ctx: Context, private val previewMain: PreviewView) {
    companion object {
        val selectorHD: ResolutionSelector = ResolutionSelector.Builder()
            .setAspectRatioStrategy(AspectRatioStrategy.RATIO_16_9_FALLBACK_AUTO_STRATEGY)
            .setAllowedResolutionMode(ResolutionSelector.PREFER_CAPTURE_RATE_OVER_HIGHER_RESOLUTION)
            .build()

        private var videoCameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        var imageCapture: ImageCapture =
            ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .setResolutionSelector(selectorHD).setTargetRotation(Surface.ROTATION_0)
                .setJpegQuality(100).build()


        val imageAnalysis: ImageAnalysis =
            ImageAnalysis.Builder().setTargetRotation(Surface.ROTATION_0)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build()


        private val vibeFolder = File(context.filesDir, "temp_img").apply {
            mkdirs()
        }
        val vibeImageFile = File(vibeFolder, "temp_vibe_img.jpg")
        val vibeCompressedImageFile = File(vibeFolder, "temp_vibe_img_compressed.jpg")

        val vibeVideoFile = File(vibeFolder, "temp_vibe_vid.mp4")
        val vibeCompressedVideoFile = File(vibeFolder, "temp_vibe_vid_compressed.mp4")


        inline fun View.afterMeasured(crossinline block: () -> Unit) {
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


    fun cameraSelector(isBack: Boolean): CameraSelector = CameraSelector.Builder()
        .requireLensFacing(if (isBack) CameraSelector.LENS_FACING_BACK else CameraSelector.LENS_FACING_FRONT)
        .build()


    fun cameraFlash(on: Boolean, camera: Camera) {
        if (camera.cameraInfo.hasFlashUnit()) {
            camera.cameraControl.enableTorch(on)
        } else {
            "no flashlight support".toast()
        }
    }


    private val circle = View(ctx)
    fun showFocusCircle(x: Float, y: Float) {
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
}