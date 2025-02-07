package com.rizwansayyed.zene.ui.connect_status.utils

import android.content.Context
import android.view.Surface
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.view.PreviewView
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import androidx.media3.transformer.Composition
import androidx.media3.transformer.EditedMediaItem
import androidx.media3.transformer.ExportException
import androidx.media3.transformer.ExportResult
import androidx.media3.transformer.Transformer
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode
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

        var imageCapture: ImageCapture =
            ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .setResolutionSelector(selectorHD).setTargetRotation(Surface.ROTATION_0)
                .setJpegQuality(100).build()


        val imageAnalysis: ImageAnalysis =
            ImageAnalysis.Builder().setTargetRotation(Surface.ROTATION_0)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build()


        private val vibeFolder = File(context.filesDir, "temp_connect_files").apply {
            mkdirs()
        }
        private val vibeTempThumbnailFolder = File(context.filesDir, "temp_thumbnail_img").apply {
            mkdirs()
        }
        val vibeImageFile = File(vibeFolder, "temp_vibe_img.jpg")
        val vibeCompressedImageFile = File(vibeFolder, "temp_vibe_img_compressed.jpg")

        val vibeVideoFile = File(vibeFolder, "temp_vibe_vid.mp4")
        private val vibeVideoCroppedFile = File(vibeFolder, "temp_vibe_cropped_vid.mp4")
        private val vibeCompressedVideoFile = File(vibeFolder, "temp_vibe_vid_compressed.mp4")

        fun isFileExtensionVideo(mimeType: String?): Boolean {
            return when (mimeType) {
                "image/jpeg" -> false
                "image/png" -> false
                "image/gif" -> true
                "video/mp4" -> true
                "video/mpeg" -> true
                else -> false
            }
        }


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

        @OptIn(UnstableApi::class)
        fun compressVideoFile(inputFile: String): String {
            vibeCompressedVideoFile.delete()



//            val clippingConfiguration = MediaItem.ClippingConfiguration.Builder()
//                .setStartPositionMs(start.toLong()).setEndPositionMs(end.toLong()).build()
//
//            val mediaItem = MediaItem.Builder()
//                .setUri(inputFile.toUri())
//                .setClippingConfiguration(clippingConfiguration)
//                .build()
//
//            val editedMediaItem = EditedMediaItem.Builder(mediaItem)
//                .build()
//            val transformer = Transformer.Builder(context)
//                .setVideoMimeType(MimeTypes.VIDEO_H265)
//                .addListener(transformerListener)
//                .build()
//
//            transformer.start(editedMediaItem, vibeVideoCroppedFile.absolutePath)

//            val cmd =
//                "-i $inputFile -vf scale=720:-2 -c:v h264_mediacodec -b:v 200K -c:a aac -b:a 128k -movflags +faststart -brand mp42 -f mp4 $vibeCompressedVideoFile"
//
//            val session = FFmpegKit.execute(cmd)
//            return if (ReturnCode.isSuccess(session.returnCode)) vibeCompressedVideoFile.absolutePath
//            else inputFile

            return  ""
        }

        @OptIn(UnstableApi::class)
        fun cropVideoFile(inputFile: File, start: Float, end: Float, d: (File) -> Unit) =
            CoroutineScope(Dispatchers.Main).launch {
                vibeVideoCroppedFile.delete()

                val transformerListener = object : Transformer.Listener {
                    override fun onCompleted(
                        composition: Composition, exportResult: ExportResult
                    ) {
                        super.onCompleted(composition, exportResult)
                        d(vibeVideoCroppedFile)
                    }

                    override fun onError(
                        composition: Composition,
                        exportResult: ExportResult,
                        exportException: ExportException
                    ) {
                        super.onError(composition, exportResult, exportException)
                        d(inputFile)
                    }
                }

                val clippingConfiguration = MediaItem.ClippingConfiguration.Builder()
                    .setStartPositionMs(start.toLong()).setEndPositionMs(end.toLong()).build()

                val mediaItem = MediaItem.Builder()
                    .setUri(inputFile.toUri())
                    .setClippingConfiguration(clippingConfiguration)
                    .build()

                val editedMediaItem = EditedMediaItem.Builder(mediaItem)
                    .build()
                val transformer = Transformer.Builder(context)
                    .setVideoMimeType(MimeTypes.VIDEO_H265)
                    .addListener(transformerListener)
                    .build()

                transformer.start(editedMediaItem, vibeVideoCroppedFile.absolutePath)

//                val cmd = "-ss ${formatMillisecondsToRead(start.toLong())} -i " +
//                        "${inputFile.absolutePath} -to ${formatMillisecondsToRead(end.toLong())} " +
//                        "-c:v copy -c:a copy $vibeVideoCroppedFile"
//
//                val session = FFmpegKit.execute(cmd)
//                if (ReturnCode.isSuccess(session.returnCode)) d(vibeVideoCroppedFile)
//                else d(inputFile)
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