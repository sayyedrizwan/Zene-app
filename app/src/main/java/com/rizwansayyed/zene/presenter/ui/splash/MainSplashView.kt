package com.rizwansayyed.zene.presenter.ui.splash


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.TextAntroSemiBold
import com.rizwansayyed.zene.presenter.ui.TextMedium
import kotlinx.coroutines.launch


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

    var hideTempSplash by remember { mutableStateOf(false) }
    var showLogin by remember { mutableStateOf(false) }

    val pagerState = rememberPagerState(pageCount = { 3 })
    val appName = stringResource(R.string.app_name)

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
                        else
                            showLogin = true
                    }
                }
            }

            if (showLogin) LoginCard(Modifier.align(Alignment.BottomCenter))
        }
    else
        SplashScreenMain {
            hideTempSplash = true
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