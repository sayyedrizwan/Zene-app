package com.rizwansayyed.zene.presenter.ui.home.views


import android.os.Build
import android.widget.ImageView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import jp.wasabeef.glide.transformations.BlurTransformation


@Composable
fun ManipulateImage(composableBounds: MutableState<Rect?>, s: String, doTouch: (Boolean) -> Unit) {
    var scale by remember { mutableFloatStateOf(1f) }
    var rotation by remember { mutableFloatStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        rotation += rotationChange
        offset += offsetChange
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(BlackColor)
            .onGloballyPositioned {
                composableBounds.value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    it.boundsInWindow()
                } else {
                    it.boundsInRoot()
                }
            }
    ) {

        AndroidView({ ctx ->
            ImageView(ctx).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
                Glide.with(ctx)
                    .load(s)
                    .apply(bitmapTransform(BlurTransformation(85, 3)))
                    .into(this)

            }
        }, Modifier.fillMaxSize())

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

    LaunchedEffect(Unit) {
        scale = 2.5f
    }

    LaunchedEffect(state.isTransformInProgress) {
        doTouch(state.isTransformInProgress)
    }
}

@Composable
fun WallpaperSetView(s: String) {
    val composableBounds = remember { mutableStateOf<Rect?>(null) }
    var settingsVisible by remember { mutableStateOf(false) }
    Box(
        Modifier
            .fillMaxSize()
            .background(BlackColor)
    ) {
        ManipulateImage(composableBounds, s) {
            settingsVisible = it
        }


        AnimatedVisibility(!settingsVisible, Modifier.align(Alignment.BottomCenter)) {
            ImageViewSettingsBar()
        }
    }


//    val activity = LocalContext.current as Activity
//    var scale by remember { mutableFloatStateOf(1f) }
//    var rotation by remember { mutableFloatStateOf(0f) }
//    var offset by remember { mutableStateOf(Offset.Zero) }
//    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
//        scale *= zoomChange
//        rotation += rotationChange
//        offset += offsetChange
//    }
//
//    val view: View = LocalView.current
//
//    Box(modifier = Modifier
//        .fillMaxSize()
//        .clickable {
//            composableBounds ?: return@clickable
//            try {
//                val bitmap = Bitmap.createBitmap(
//                    composableBounds!!.width.toInt(),
//                    composableBounds!!.height.toInt(),
//                    Bitmap.Config.ARGB_8888,
//                )
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//                    // Above Android O not using PixelCopy throws exception
//                    // https://stackoverflow.com/questions/58314397/java-lang-illegalstateexception-software-rendering-doesnt-support-hardware-bit
//                    PixelCopy.request(
//                        activity.window,
//                        android.graphics.Rect(
//                            composableBounds!!.left.toInt(),
//                            composableBounds!!.top.toInt(),
//                            composableBounds!!.right.toInt(),
//                            composableBounds!!.bottom.toInt()
//                        ),
//                        bitmap,
//                        {},
//                        Handler(Looper.getMainLooper())
//                    )
//                } else {
//                    val canvas = Canvas(bitmap)
//                        .apply {
//                            translate(-composableBounds!!.left, -composableBounds!!.top)
//                        }
//                    view.draw(canvas)
//                    canvas.setBitmap(null)
//                }
//                try {
//                    val wallpaperManager = WallpaperManager.getInstance(activity)
//
//                    wallpaperManager.setBitmap(
//                        bitmap, null, true, WallpaperManager.FLAG_SYSTEM
//                    )
//                } catch (e: Exception) {
//                    e.message?.toast()
//                }
//            } catch (e: Exception) {
//                e.message?.toast()
//            }
//        }
//        .background(BlackColor)
//        .onGloballyPositioned {
//            composableBounds = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                it.boundsInWindow()
//            } else {
//                it.boundsInRoot()
//            }
//        }
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

@Composable
fun ImageViewSettingsBar() {
    Column(
        Modifier
            .padding(8.dp)
            .padding(bottom = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MainColor)
    ) {
        Spacer(Modifier.height(10.dp))
        TextSemiBold(stringResource(R.string.pin_with_finger_for_wallpaper))
        Spacer(Modifier.height(10.dp))
    }
}