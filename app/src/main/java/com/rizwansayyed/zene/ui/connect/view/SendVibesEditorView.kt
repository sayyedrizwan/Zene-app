package com.rizwansayyed.zene.ui.connect.view

import android.app.Activity
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.util.Size
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.camera.core.Camera
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.utils.Utils.toast
import java.io.File
import java.util.concurrent.TimeUnit

class FilterAnalyzer(private val applyFilter: Boolean) : ImageAnalysis.Analyzer {
    override fun analyze(image: ImageProxy) {

        if (applyFilter) {
            val matrix = ColorMatrix()
            matrix.setSaturation(0f)  // Convert to grayscale as an example

            val colorFilter = ColorMatrixColorFilter(matrix)

            // Apply the filter to the image (you need to use GPUImage or similar for actual effect)
            // In this case, we're simulating a grayscale filter.
        }

        // Close the image proxy once processed
        image.close()
    }
}


@Composable
fun SendVibesEditorView() {
    val context = LocalContext.current as Activity
    val lifecycleOwner = LocalLifecycleOwner.current

    var cameraTorch by remember { mutableStateOf(false) }
    var cameraBack by remember { mutableStateOf(true) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var camera by remember { mutableStateOf<Camera?>(null) }
    var previewView by remember { mutableStateOf<PreviewView?>(null) }


    fun startCamera(view: PreviewView) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().setMaxResolution(Size(3840, 2160)).build()
                .also { it.surfaceProvider = view.surfaceProvider }

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(if (cameraBack) CameraSelector.LENS_FACING_BACK else CameraSelector.LENS_FACING_FRONT)
                .build()

            val imageAnalysis = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(
                        ContextCompat.getMainExecutor(previewView!!.context), FilterAnalyzer(true)
                    )
                }

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, preview, imageCapture
                )

                addTapToFocus(view, camera!!.cameraControl)

                camera!!.cameraControl.enableTorch(cameraTorch)
            } catch (e: Exception) {
                e.message
            }

        }, ContextCompat.getMainExecutor(context))
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(MainColor)
    ) {
        AndroidView({ ctx ->
            PreviewView(ctx).apply {
                previewView = this
                startCamera(this)
            }
        }, Modifier.fillMaxSize())

        Row(
            Modifier
                .padding(top = 50.dp)
                .padding(horizontal = 10.dp)
                .align(Alignment.TopCenter)
                .fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically
        ) {
            ImageIcon(R.drawable.ic_arrow_left, 35) {
                context.finish()
            }

            ImageIcon(if (cameraTorch) R.drawable.ic_flash else R.drawable.ic_flash_off) {
                cameraTorch = !cameraTorch
                camera?.cameraControl?.enableTorch(cameraTorch)
            }

            ImageIcon(R.drawable.ic_add) {
            }

//            Spacer(Modifier.size(30.dp))
        }

        Row(
            Modifier
                .padding(bottom = 70.dp)
                .padding(horizontal = 10.dp)
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            Arrangement.SpaceAround,
            Alignment.CenterVertically
        ) {
            ImageIcon(R.drawable.ic_image_gallery) {

            }

            Spacer(
                Modifier
                    .clickable {
                        takePhoto(imageCapture, context)
                    }
                    .size(60.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color.White)
            )

            ImageIcon(R.drawable.ic_circle_map) {
                cameraBack = !cameraBack
            }
        }
    }

    LaunchedEffect(cameraBack) {
        previewView?.let { startCamera(it) }
    }
}

fun addTapToFocus(previewView: PreviewView, cameraControl: CameraControl) {
    previewView.setOnTouchListener { _, motionEvent ->
        if (motionEvent.action == MotionEvent.ACTION_UP) {
            val factory = previewView.meteringPointFactory
            val meteringPoint = factory.createPoint(motionEvent.x, motionEvent.y)

            val action = FocusMeteringAction.Builder(meteringPoint)
                .setAutoCancelDuration(3, TimeUnit.SECONDS).build()

            cameraControl.startFocusAndMetering(action)
            showTapIndicatorWithAnimation(previewView, motionEvent.x, motionEvent.y)
        }
        true
    }
}

fun showTapIndicatorWithAnimation(parentView: FrameLayout, x: Float, y: Float) {
    val tapIndicator = View(parentView.context).apply {
        layoutParams = FrameLayout.LayoutParams(100, 100).apply {
            gravity = Gravity.TOP or Gravity.START
            leftMargin = x.toInt() - 50
            topMargin = y.toInt() - 50
        }
        background = ContextCompat.getDrawable(parentView.context, R.drawable.circle_drawable)
    }
    parentView.addView(tapIndicator)
    tapIndicator.animate().scaleX(2f).scaleY(2f).alpha(0f).setDuration(1000)
        .withEndAction { parentView.removeView(tapIndicator) }.start()
}

private fun takePhoto(camera: ImageCapture?, context: Activity) {
    val imageCapture = camera ?: return

    val outputDir = File(context.filesDir, "vibes_img.png")
    val outputOptions = ImageCapture.OutputFileOptions.Builder(outputDir).build()

    imageCapture.takePicture(
        outputOptions, ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(exc: ImageCaptureException) {
                "error".toast()
            }

            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                "done".toast()
            }
        }
    )
}