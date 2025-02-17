package com.rizwansayyed.zene.ui.musicplayer

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.datastore.model.MusicPlayerData
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.viewmodel.NavigationViewModel
import kotlin.math.absoluteValue

@Composable
fun MusicPlayerView(navViewModel: NavigationViewModel) {
    val player by DataStorageManager.musicPlayerDB.collectAsState(null)
    AnimatedVisibility(
        navViewModel.showMusicPlayer,
        enter = slideInVertically(initialOffsetY = { it / 2 }),
        exit = slideOutVertically(targetOffsetY = { it / 2 })
    ) {
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        val screenWidth = (configuration.screenWidthDp / 1.1).dp
        val topPadding = (screenHeight * 0.29f)
        val bottomPadding = (screenHeight * 0.16f)
        val pagerState = rememberPagerState(pageCount = { player?.lists?.size ?: 0 })

        Box(
            Modifier
                .fillMaxSize()
                .background(MainColor)
        ) {
            VerticalPager(
                pagerState,
                Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = bottomPadding, top = topPadding)
            ) { page ->
                MusicPlayingView(player, page, pagerState, screenWidth)
            }

            PlayerControlPanel(Modifier.align(Alignment.BottomCenter))
        }

        LaunchedEffect(player?.data?.id) {
            val i = player?.lists?.indexOfFirst { it?.id == player?.data?.id }
            pagerState.scrollToPage(i ?: 0)
        }

        BackHandler {
            navViewModel.setMusicPlayer(false)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MusicPlayingView(player: MusicPlayerData?, page: Int, pagerState: PagerState, screenWidth: Dp) {
    Column(
        Modifier
            .offset(
                y = if (page == pagerState.currentPage) (-100).dp else 0.dp
            )
            .animateContentSize()
            .fillMaxSize()
            .graphicsLayer {
                val pageOffset =
                    (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

                alpha = lerp(0.5f, 1f, 1f - pageOffset.absoluteValue.coerceIn(0f, 1f))
            }, Arrangement.Top, Alignment.CenterHorizontally
    ) {
        Box(Modifier.size(screenWidth, screenWidth)) {
            GlideImage(
                player?.lists?.get(page)?.thumbnail,
                player?.lists?.get(page)?.name,
                Modifier.fillMaxSize().clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Fit
            )

            Box(
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(15.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color.Black)
                    .padding(10.dp)
            ) {
                if (player?.data?.id == player?.lists?.get(page)?.id) {
                    GlideImage(
                        R.raw.song_playing_wave,
                        "",
                        Modifier.size(25.dp),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    ImageIcon(R.drawable.ic_play, 20)
                }
            }
        }

        AnimatedVisibility(page == pagerState.currentPage) {
            Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.End) {
                Spacer(Modifier.height(6.dp))
                TextViewBold(player?.lists?.get(page)?.name ?: "", 25, center = true, line = 2)
                TextViewNormal(player?.lists?.get(page)?.artists ?: "", 13, center = true, line = 1)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerControlPanel(modifier: Modifier = Modifier) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    Column(
        modifier
            .padding(horizontal = 15.dp, vertical = 10.dp)
            .padding(bottom = 70.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.Black)
            .padding(horizontal = 12.dp), Arrangement.Center, Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(40.dp))
        Slider(value = sliderPosition,
            onValueChange = {
                sliderPosition = it
            },
            valueRange = 0f..100f,
            colors = SliderDefaults.colors(Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp),
            track = { sliderState ->
                SliderDefaults.Track(
                    modifier = Modifier.height(4.dp),
                    sliderState = sliderState,
                    drawStopIndicator = null,
                    thumbTrackGapSize = 0.dp,
                    colors = SliderDefaults.colors(
                        Color.White, MainColor, MainColor, Color.Gray, Color.Gray
                    )
                )
            })

        Spacer(Modifier.height(5.dp))

        Row(Modifier.fillMaxWidth()) {
            TextViewSemiBold("01:22", size = 13)
            Spacer(Modifier.weight(1f))
            TextViewSemiBold("01:11", size = 13)
        }
        Spacer(Modifier.height(9.dp))

        Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly, Alignment.CenterVertically) {
            ImageIcon(R.drawable.ic_thumbs_up, 20)
            Box(Modifier.rotate(180f)) {
                ImageIcon(R.drawable.ic_forward, 27)
            }
            Box(
                Modifier
                    .padding(15.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color.White)
                    .padding(10.dp)
            ) {
                ImageIcon(R.drawable.ic_play, 27, Color.Black)
            }
            Box(Modifier) {
                ImageIcon(R.drawable.ic_forward, 27)
            }

            Box(Modifier) {
                ImageIcon(R.drawable.ic_timer , 27)
            }
        }


        Spacer(Modifier.height(40.dp))
    }
}