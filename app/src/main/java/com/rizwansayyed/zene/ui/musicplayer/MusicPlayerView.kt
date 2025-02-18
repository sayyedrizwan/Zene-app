package com.rizwansayyed.zene.ui.musicplayer

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.datastore.model.MusicPlayerData
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.utils.MediaContentUtils
import com.rizwansayyed.zene.viewmodel.NavigationViewModel
import com.rizwansayyed.zene.viewmodel.PlayerViewModel
import kotlin.math.absoluteValue

@Composable
fun MusicPlayerView(navViewModel: NavigationViewModel) {
    val player by DataStorageManager.musicPlayerDB.collectAsState(null)
    val playViewModel: PlayerViewModel = hiltViewModel()

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

            MusicPlayerControlPanel(Modifier.align(Alignment.BottomCenter), player, playViewModel)
        }

        LaunchedEffect(player?.data?.id) {
            val i = player?.lists?.indexOfFirst { it?.id == player?.data?.id }
            pagerState.animateScrollToPage(i ?: 0)
        }

        BackHandler {
            navViewModel.setMusicPlayer(false)
        }
    }

    LaunchedEffect(player?.data?.id) {
        if (player?.data?.id != null && player?.data?.type() != null)
            playViewModel.likedMediaItem(player?.data?.id, player?.data?.type()!!)
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
                Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(14.dp)),
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
                    Box(Modifier.clickable {
                        MediaContentUtils.startMedia(
                            player?.lists?.get(page), player?.lists ?: emptyList()
                        )
                    }) {
                        ImageIcon(R.drawable.ic_play, 20)
                    }
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
