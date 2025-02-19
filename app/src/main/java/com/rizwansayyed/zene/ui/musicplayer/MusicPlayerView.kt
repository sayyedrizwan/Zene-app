package com.rizwansayyed.zene.ui.musicplayer

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.datastore.DataStorageManager.isPlayerGridDB
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageWithBorder
import com.rizwansayyed.zene.viewmodel.NavigationViewModel
import com.rizwansayyed.zene.viewmodel.PlayerViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun MusicPlayerView(navViewModel: NavigationViewModel) {
    val player by DataStorageManager.musicPlayerDB.collectAsState(null)
    val playViewModel: PlayerViewModel = hiltViewModel()
    val isPlayerGrid by isPlayerGridDB.collectAsState(false)

    AnimatedVisibility(
        navViewModel.showMusicPlayer,
        enter = slideInVertically(initialOffsetY = { it / 2 }),
        exit = slideOutVertically(targetOffsetY = { it / 2 })
    ) {
        val coroutines = rememberCoroutineScope()
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        val screenWidth = (configuration.screenWidthDp / 1.1).dp
        val topPadding = (screenHeight * 0.29f)
        val bottomPadding = (screenHeight * 0.16f)
        val pagerState = rememberPagerState(pageCount = { player?.lists?.size ?: 0 })
        val lazyListState = rememberLazyListState()

        Box(
            Modifier
                .fillMaxSize()
                .background(MainColor)
        ) {
            if (isPlayerGrid) VerticalPager(
                pagerState,
                Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = bottomPadding, top = topPadding)
            ) { page ->
                MusicPlayingGridView(player, page, pagerState, screenWidth)
            }
            else
                LazyColumn(Modifier.fillMaxSize(), lazyListState) {
                    item { Spacer(Modifier.height(100.dp)) }
                    items(player?.lists ?: emptyList()) {
                        MusicPlayingListView(player, it)
                    }
                    item { Spacer(Modifier.height(500.dp)) }
                }

            MusicPlayerControlPanel(Modifier.align(Alignment.BottomCenter), player, playViewModel)

            Row(
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 50.dp)
                    .padding(10.dp)
            ) {
                ImageWithBorder(
                    R.drawable.ic_carousel_vertical,
                    if (isPlayerGrid) Color.LightGray.copy(0.7f) else Color.White
                ) {
                    coroutines.launch { isPlayerGridDB = flowOf(true) }
                }
                ImageWithBorder(
                    R.drawable.ic_right_to_left_list_triangle,
                    if (!isPlayerGrid) Color.LightGray.copy(0.7f) else Color.White
                ) {
                    coroutines.launch { isPlayerGridDB = flowOf(false) }
                }
            }
        }

        LaunchedEffect(player?.data?.id, isPlayerGrid) {
            val i = player?.lists?.indexOfFirst { it?.id == player?.data?.id }
            coroutines.launch {
                pagerState.animateScrollToPage(i ?: 0)
            }
            coroutines.launch {
                delay(400)
                lazyListState.animateScrollToItem(i ?: 0)
            }
        }

        BackHandler {
            navViewModel.setMusicPlayer(false)
        }
    }

    LaunchedEffect(player?.data?.id) {
        if (player?.data?.id != null && player?.data?.type() != null) playViewModel.likedMediaItem(
            player?.data?.id,
            player?.data?.type()!!
        )
    }

}
