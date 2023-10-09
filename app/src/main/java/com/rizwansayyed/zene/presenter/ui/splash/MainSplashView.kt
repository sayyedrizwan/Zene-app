package com.rizwansayyed.zene.presenter.ui.splash


import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.doShowSplashScreen
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.selectedFavouriteArtistsSongs
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.TextAntroSemiBold
import com.rizwansayyed.zene.presenter.ui.TextMedium
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.utils.Utils.isInternetAvailable
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


val textSplashScreen = listOf(
    context.resources.getText(R.string.enjoy_free_access_one),
    context.resources.getText(R.string.enjoy_free_access_two),
    context.resources.getText(R.string.enjoy_free_access_three)
)


@OptIn(ExperimentalFoundationApi::class)
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun MainSplashView() {
    val coroutine = rememberCoroutineScope()
    val activity = LocalContext.current as Activity
    val density = LocalDensity.current
    val room: RoomDbViewModel = hiltViewModel()

    val artists by selectedFavouriteArtistsSongs.collectAsState(initial = emptyArray())
    val selectArtists = remember { mutableStateListOf("") }

    var hideTempSplash by remember { mutableStateOf(false) }
    var showLogin by remember { mutableStateOf(false) }
    var showStartListeningBtn by remember { mutableStateOf(false) }

    val pagerState = rememberPagerState(pageCount = { 3 })
    val appName = stringResource(R.string.app_name)
    val noInternet = stringResource(R.string.internet_not_available)

    val animateBtnColor by animateColorAsState(
        if (pagerState.canScrollForward) Color.White else MainColor, label = "color"
    )

    if (hideTempSplash)
        Box(Modifier.fillMaxSize()) {
            SplashPlayerView(Modifier.align(Alignment.Center))

            HorizontalPager(pagerState, Modifier.fillMaxSize()) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(
                            when (it) {
                                0 -> MainColor.copy(0.5f)
                                1 -> BlackColor.copy(0.5f)
                                else -> DarkGreyColor.copy(0.5f)
                            }
                        )
                ) {}
            }

            TextAntroSemiBold(
                appName,
                Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .animateContentSize()
                    .padding(vertical = 50.dp),
                color = Color.White,
                singleLine = true,
                doCenter = true,
                size = 85
            )

            Column(
                Modifier
                    .padding(bottom = 40.dp)
                    .padding(11.dp)
                    .align(Alignment.BottomCenter)
            ) {

                TextMedium(
                    v = textSplashScreen[pagerState.currentPage].toString(),
                    Modifier.fillMaxWidth(), size = 20
                )

                Spacer(Modifier.height(25.dp))

                Row {
                    for (i in 0 until pagerState.pageCount) {
                        Column(
                            Modifier
                                .animateContentSize()
                                .padding(bottom = 14.dp)
                                .padding(horizontal = 4.dp)
                                .size(if (pagerState.currentPage == i) 26.dp else 3.dp, 3.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(Color.White)
                        ) {}
                    }
                }

                CardButton(
                    if (pagerState.canScrollForward) stringResource(R.string.next) else
                        stringResource(R.string.get_started),
                    animateBtnColor,
                    if (pagerState.canScrollForward) Color.Black else Color.White, Modifier
                ) {
                    coroutine.launch {
                        if (pagerState.canScrollForward)
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        else {
                            if (!isInternetAvailable()) {
                                noInternet.toast()
                                return@launch
                            }

                            if ((artists?.size ?: 0) == 0)
                                showLogin = true
                            else
                                doShowSplashScreen = flowOf(false)
                        }
                    }
                }
            }

            AnimatedVisibility(
                showLogin, Modifier.align(Alignment.BottomCenter),
                enter = slideInVertically {
                    with(density) { 40.dp.roundToPx() }
                } + expandVertically(
                    expandFrom = Alignment.Bottom
                ) + fadeIn(
                    initialAlpha = 0.3f
                ),
                exit = slideOutVertically() + shrinkVertically() + fadeOut()
            ) {
                SelectArtistsCard(selectArtists) {
                    showStartListeningBtn = it
                }
            }

            if (showLogin && showStartListeningBtn) CardButton(
                stringResource(R.string.start_listening), MainColor, Color.White,
                Modifier
                    .padding(vertical = 40.dp, horizontal = 5.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                room.updateList(selectArtists)
                doShowSplashScreen = flowOf(false)
            }
        }
    else
        SplashScreenMain {
            hideTempSplash = true
        }


    BackHandler {
        if (showLogin) {
            showLogin = false
            return@BackHandler
        }

        activity.finish()
    }
}


@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun SplashPlayerView(modifier: Modifier) {
    val context = LocalContext.current.applicationContext
    val uri = RawResourceDataSource.buildRawResourceUri(R.raw.bg_musics)

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).also { p ->
                p.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                p.useController = false
                val player = ExoPlayer.Builder(context).build().apply {
                    p.player = this
                    videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
                    setMediaItem(MediaItem.fromUri(uri))
                    repeatMode = Player.REPEAT_MODE_ALL
                    prepare()
                    playWhenReady = true
                }
                player.play()
            }
        }, modifier.fillMaxSize()
    )
}