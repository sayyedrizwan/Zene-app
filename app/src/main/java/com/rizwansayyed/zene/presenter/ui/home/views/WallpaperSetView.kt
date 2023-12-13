package com.rizwansayyed.zene.presenter.ui.home.views


import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.widget.ImageView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.util.UiUtils
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.THREE_ITEMS_GRID
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.TOTAL_ITEMS_GRID
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.presenter.util.UtilsWallpaperImage
import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.utils.Utils.customBrowser
import com.rizwansayyed.zene.viewmodel.ArtistsViewModel
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds


@Composable
fun ManipulateImage(
    composableBounds: MutableState<Rect?>, s: String, list: List<String>, doTouch: (Boolean) -> Unit
) {
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

        list.forEach { img ->
            var scaleList by remember { mutableFloatStateOf(1f) }
            var rotationList by remember { mutableFloatStateOf(0f) }
            var offsetList by remember { mutableStateOf(Offset.Zero) }
            val stateList = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
                scaleList *= zoomChange
                rotationList += rotationChange
                offsetList += offsetChange
            }
            AsyncImage(
                img, "",
                Modifier
                    .graphicsLayer(
                        scaleX = scaleList,
                        scaleY = scaleList,
                        rotationZ = rotationList,
                        translationX = offsetList.x,
                        translationY = offsetList.y
                    )
                    .transformable(state = stateList)
                    .fillMaxSize()
            )


            LaunchedEffect(stateList.isTransformInProgress) {
                doTouch(stateList.isTransformInProgress)
            }
        }
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
    val artistsViewModel: ArtistsViewModel = hiltViewModel()

    val coroutine = rememberCoroutineScope()
    val composableBounds = remember { mutableStateOf<Rect?>(null) }
    var settingsVisible by remember { mutableStateOf(false) }
    var settingsVisibleForce by remember { mutableStateOf(true) }
    var addMoreImageDialog by remember { mutableStateOf(false) }
    val list = remember { mutableStateListOf<String>() }
    val density = LocalDensity.current
    val view = LocalView.current
    val activity = LocalContext.current as Activity
    val pleaseWait = stringResource(id = R.string.please_wait_setting_wallpaper)
    val wallpaperSet = stringResource(id = R.string.wallpaper_set_successfully)


    Box(
        Modifier
            .fillMaxSize()
            .background(BlackColor)
    ) {
        ManipulateImage(composableBounds, s, list) {
            settingsVisible = it
        }

        fun setBitmap(forLockScreen: Boolean): Bitmap? {
            val bound = composableBounds.value ?: return null
            return try {
                val bitmap = Bitmap.createBitmap(
                    bound.width.toInt(), bound.height.toInt(), Bitmap.Config.ARGB_8888
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val r = android.graphics.Rect(
                        bound.left.toInt(),
                        bound.top.toInt(),
                        bound.right.toInt(),
                        bound.bottom.toInt()
                    )
                    PixelCopy
                        .request(activity.window, r, bitmap, {}, Handler(Looper.getMainLooper()))
                } else {
                    val canvas = Canvas(bitmap).apply {
                        translate(-bound.left, -bound.top)
                    }
                    view.draw(canvas)
                    canvas.setBitmap(null)
                }
                bitmap
            } catch (e: Exception) {
                if (forLockScreen) UtilsWallpaperImage(s).lockScreenWallpaper()
                else UtilsWallpaperImage(s).homeScreenWallpaper()
                null
            }
        }


        if (settingsVisibleForce) AnimatedVisibility(
            !settingsVisible,
            Modifier.align(Alignment.BottomCenter),
            slideInVertically { with(density) { -40.dp.roundToPx() } }
                    + expandVertically(expandFrom = Alignment.Top) + fadeIn(initialAlpha = 0.3f),
            slideOutVertically() + shrinkVertically() + fadeOut()) {
            ImageViewSettingsBar(s, {
                addMoreImageDialog = true
            }, {
                pleaseWait.toast()
                coroutine.launch {
                    settingsVisibleForce = false
                    delay(1.5.seconds)
                    val bitmap = setBitmap(it)
                    if (bitmap != null) {
                        if (it) UtilsWallpaperImage("").makeLockScreenWallpaper(bitmap)
                        else UtilsWallpaperImage("").makeWallpaper(bitmap)
                        wallpaperSet.toast()
                    }
                    settingsVisibleForce = true
                }
                setBitmap(it)
            })
        }
    }

    LaunchedEffect(Unit) {
        if (artistsViewModel.artistsImages.size <= 2)
            artistsViewModel.searchUsers(
                musicPlayerData.first()?.v?.artists?.split(",", "&") ?: emptyList()
            )
    }

    if (addMoreImageDialog) ImageSearchDialog {
        it?.let { list.add(it) }
        addMoreImageDialog = false
    }
}

@Composable
fun ImageSearchDialog(close: (String?) -> Unit) {
    val artistsViewModel: ArtistsViewModel = hiltViewModel()
    val width = LocalConfiguration.current.screenWidthDp / 3


    Dialog({ close(null) }, DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(Modifier.fillMaxSize(), RoundedCornerShape(16.dp), Color.Black) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {

                LazyVerticalGrid(
                    GridCells.Fixed(TOTAL_ITEMS_GRID), Modifier
                        .fillMaxSize()
                        .background(BlackColor)
                ) {
                    item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
                        Row(
                            Modifier
                                .padding(top = 10.dp)
                                .align(Alignment.TopStart)
                                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                        ) {
                            SmallIcons(icon = R.drawable.ic_arrow_left, 21, 0) {
                                close(null)
                            }

                            Spacer(Modifier.width(10.dp))

                            TextSemiBold(v = stringResource(R.string.suggested_image))
                        }
                    }

                    item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
                        Spacer(Modifier.height(40.dp))
                    }

                    items(
                        artistsViewModel.artistsImages,
                        span = { _ -> GridItemSpan(THREE_ITEMS_GRID) }) {
                        AsyncImage(
                            it, "", Modifier
                                .fillMaxWidth()
                                .height(width.dp)
                                .clickable {
                                    close(it)
                                },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ImageViewSettingsBar(s: String, doMore: () -> Unit, set: (forLockScreen: Boolean) -> Unit) {
    val wholeHomeScreen = stringResource(R.string.set_whole_image_as_home_screen_wallpaper)
    val editedHomeScreen = stringResource(R.string.set_edited_image_as_home_screen_wallpaper)
    val wholeLockScreen = stringResource(R.string.set_whole_image_as_lock_screen_wallpaper)
    val editedLockScreen = stringResource(R.string.set_edited_image_as_lock_screen_wallpaper)
    val wallpaperSet = stringResource(id = R.string.wallpaper_set_successfully)

    Column(
        Modifier
            .padding(8.dp)
            .padding(bottom = 25.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MainColor)
    ) {
        Spacer(Modifier.height(15.dp))

        TextSemiBold(
            stringResource(R.string.pin_with_finger_for_wallpaper),
            Modifier.fillMaxWidth(),
            true,
            size = 12
        )
        Spacer(Modifier.height(20.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {

            ListOfActionButton(R.drawable.ic_wallpaper_1, editedHomeScreen) {
                set(false)
            }

            ListOfActionButton(R.drawable.ic_screen_lock, editedLockScreen) {
                set(true)
            }

            ListOfActionButton(R.drawable.ic_layer_add, stringResource(R.string.add_more_images)) {
                doMore()
            }

            ListOfActionButton(R.drawable.ic_wallpaper_1, wholeHomeScreen) {
                wallpaperSet.toast()
                UtilsWallpaperImage(s).homeScreenWallpaper()
            }

            ListOfActionButton(R.drawable.ic_screen_lock, wholeLockScreen) {
                wallpaperSet.toast()
                UtilsWallpaperImage(s).lockScreenWallpaper()
            }
        }

        Spacer(Modifier.height(15.dp))
    }
}


@Composable
fun ListOfActionButton(icon: Int, txt: String, click: () -> Unit) {
    Column(
        Modifier
            .padding(horizontal = 15.dp)
            .clickable { click() },
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        SmallIcons(icon, 26, 4)
        Spacer(Modifier.height(5.dp))
        TextThin(txt.substringBefore("\n"), Modifier.fillMaxWidth(), true, size = 12)
        Spacer(Modifier.height(3.dp))
        if (txt.contains("\n"))
            TextThin(txt.substringAfter("\n"), Modifier.fillMaxWidth(), true, size = 12)
    }
}