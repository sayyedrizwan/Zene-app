package com.rizwansayyed.zene.presenter.ui.home.views

import android.app.Activity
import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toAndroidRect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.doOnLayout
import androidx.core.view.drawToBitmap
import coil.compose.AsyncImage
import com.rizwansayyed.zene.di.ApplicationModule
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.screenshot.ImageResult
import com.rizwansayyed.zene.screenshot.ScreenshotBox
import com.rizwansayyed.zene.screenshot.rememberScreenshotState
import com.rizwansayyed.zene.screenshot.screenshot
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.math.roundToInt


@Composable
fun WallpaperSetView(s: String) {
    val activity = LocalContext.current as Activity
    var scale by remember { mutableFloatStateOf(1f) }
    var rotation by remember { mutableFloatStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        rotation += rotationChange
        offset += offsetChange
    }
    var composableBounds by remember { mutableStateOf<Rect?>(null) }

    val view: View = LocalView.current

    Box(modifier = Modifier
        .fillMaxSize()
        .clickable {
            composableBounds ?: return@clickable
            try {
                val bitmap = Bitmap.createBitmap(
                    composableBounds!!.width.toInt(),
                    composableBounds!!.height.toInt(),
                    Bitmap.Config.ARGB_8888,
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    // Above Android O not using PixelCopy throws exception
                    // https://stackoverflow.com/questions/58314397/java-lang-illegalstateexception-software-rendering-doesnt-support-hardware-bit
                    PixelCopy.request(
                        activity.window,
                        composableBounds!!.toAndroidRect(),
                        bitmap,
                        {},
                        Handler(Looper.getMainLooper())
                    )
                } else {
                    val canvas = Canvas(bitmap)
                        .apply {
                            translate(-composableBounds!!.left, -composableBounds!!.top)
                        }
                    view.draw(canvas)
                    canvas.setBitmap(null)
                }
                try {
                    val wallpaperManager = WallpaperManager.getInstance(activity)

                    wallpaperManager.setBitmap(
                        bitmap, null, true, WallpaperManager.FLAG_SYSTEM
                    )
                } catch (e: Exception) {
                    e.message?.toast()
                }
            } catch (e: Exception) {
                e.message?.toast()
            }
        }
        .background(BlackColor)
        .onGloballyPositioned {
            composableBounds = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                it.boundsInWindow()
            } else {
                it.boundsInRoot()
            }
        }
    ) {
        AsyncImage(
            s, "",
            Modifier
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    rotationZ = rotation,
                    translationX = offset.x,
                    translationY = offset.y
                )
                .transformable(state = state)
                .fillMaxSize()
        )
    }

    DisposableEffect(Unit) {
        composableBounds?.let {
            "chnaged".toast()
        }

        onDispose { }
    }

//    val screenshotState = rememberScreenshotState()
//
//    ScreenshotBox(screenshotState = screenshotState) {
//        Box(
//            Modifier
//                .clickable {
//                    screenshotState.capture()
//                }
//                .fillMaxSize()
//                .background(DarkGreyColor)
//        ) {
//            AsyncImage(
//                s, "",
//                Modifier
//                    .graphicsLayer(
//                        scaleX = scale,
//                        scaleY = scale,
//                        rotationZ = rotation,
//                        translationX = offset.x,
//                        translationY = offset.y
//                    )
//                    .transformable(state = state)
//                    .fillMaxSize()
//            )
//        }
//    }
//
//    val width = LocalConfiguration.current.screenWidthDp
//    val height = LocalConfiguration.current.screenHeightDp
//
//    screenshotState.imageBitmap?.let {
//        try {
//            val wallpaperManager = WallpaperManager.getInstance(context)
//
//            wallpaperManager.setBitmap(
//                it.asAndroidBitmap(), null, true, WallpaperManager.FLAG_SYSTEM
//            )
//        } catch (e: Exception) {
//            e.message
//        }
//    }
//    LaunchedEffect(Unit) {
//        screenshotState.liveScreenshotFlow
//            .onEach {
//                try {
//                    val wallpaperManager = WallpaperManager.getInstance(context)
//
//                    wallpaperManager.setBitmap(
//                        it.asAndroidBitmap(), null, true, WallpaperManager.FLAG_SYSTEM
//                    )
//                } catch (e: Exception) {
//                    e.message
//                }
//            }
//            .launchIn(this)
//    }

//    Box(
//        Modifier
//            .clickable {
//                screenshotState.capture()
//            }
//            .fillMaxSize()
//            .background(DarkGreyColor)
//    ) {
//        AsyncImage(
//            s, "",
//            Modifier
//                .graphicsLayer(
//                    scaleX = scale,
//                    scaleY = scale,
//                    rotationZ = rotation,
//                    translationX = offset.x,
//                    translationY = offset.y
//                )
//                .transformable(state = state)
//                .fillMaxSize()
//        )
//    }
}