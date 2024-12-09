package com.rizwansayyed.zene.ui.player

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.api.model.MusicType
import com.rizwansayyed.zene.data.db.model.MusicPlayerData
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.OPEN_PLAYER
import com.rizwansayyed.zene.service.MusicServiceUtils.sendWebViewCommand
import com.rizwansayyed.zene.ui.home.view.HorizontalSongView
import com.rizwansayyed.zene.ui.home.view.StyleSize
import com.rizwansayyed.zene.ui.home.view.TextSize
import com.rizwansayyed.zene.ui.player.view.ButtonsView
import com.rizwansayyed.zene.ui.player.view.ExtraButtonsData
import com.rizwansayyed.zene.ui.player.view.LyricsView
import com.rizwansayyed.zene.ui.player.view.SleepTimerSheet
import com.rizwansayyed.zene.ui.player.view.SongSliderData
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.CardRoundLoading
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.LoadingView
import com.rizwansayyed.zene.ui.view.SongDynamicCards
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.ui.view.imgBuilder
import com.rizwansayyed.zene.ui.view.isScreenBig
import com.rizwansayyed.zene.utils.FirebaseLogEvents
import com.rizwansayyed.zene.utils.FirebaseLogEvents.logEvents
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_ARTISTS
import com.rizwansayyed.zene.utils.NavigationUtils.sendNavCommand
import com.rizwansayyed.zene.utils.Utils.THREE_GRID_SIZE
import com.rizwansayyed.zene.utils.Utils.TOTAL_GRID_SIZE
import com.rizwansayyed.zene.utils.Utils.TWO_GRID_SIZE
import com.rizwansayyed.zene.viewmodel.MusicPlayerViewModel
import kotlinx.coroutines.delay
import java.util.UUID
import kotlin.time.Duration.Companion.seconds


@Composable
fun MusicPlayerView(
    playerInfo: MusicPlayerData?,
    musicPlayerViewModel: MusicPlayerViewModel,
    inOnLock: Boolean = false,
    close: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    val pagerState = rememberPagerState(pageCount = { playerInfo?.list?.size ?: 0 })
    var alarmSheet by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var artists by remember { mutableStateOf("") }

    val isThreeGrid = isScreenBig()

    LazyVerticalGrid(
        GridCells.Fixed(TOTAL_GRID_SIZE),
        Modifier
            .fillMaxSize()
            .background(MainColor)
    ) {
        item(1, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Column {
                Spacer(Modifier.height(60.dp))
                Box(Modifier.fillMaxWidth(), Alignment.Center) {
                    Row(
                        Modifier
                            .padding(start = 5.dp)
                            .align(Alignment.CenterStart)
                            .rotate(if (inOnLock) 0f else -90f)
                            .clickable {
                                close()
                            }) {
                        ImageIcon(R.drawable.ic_arrow_left, 26)
                    }

                    TextPoppins(stringResource(R.string.zene_music_player), true, size = 17)

                    Row(
                        Modifier
                            .padding(end = 9.dp)
                            .align(Alignment.CenterEnd)
                            .clickable {
                                alarmSheet = true
                            }) {
                        ImageIcon(R.drawable.ic_alarm_clock, 22)
                    }
                }
                Spacer(Modifier.height(20.dp))
            }
        }

        item(2, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Column {
                MusicListCards(pagerState, playerInfo, name, artists, close)
            }
        }

        if (playerInfo?.player?.type() == MusicType.SONGS || playerInfo?.player?.type() == MusicType.OFFLINE_SONGS) {
            item(3, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                Column {
                    SongSliderData(playerInfo)
                }
            }
        }

        item(4, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Column {
                ButtonsView(playerInfo)
            }
        }


        item(8, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            if (playerInfo?.player?.type() == MusicType.RADIO && playerInfo.isBuffering == true)
                Column(
                    Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(40.dp))
                    TextPoppins(
                        stringResource(R.string.issue_with_radio_buffering), true, size = 13
                    )
                }
        }


        if (playerInfo?.player?.type() == MusicType.SONGS || playerInfo?.player?.type() == MusicType.OFFLINE_SONGS) {
            item(5, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                Column {
                    ExtraButtonsData(musicPlayerViewModel, playerInfo)
                }
            }

            item(6, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                when (val v = musicPlayerViewModel.lyrics) {
                    APIResponse.Empty -> {}
                    is APIResponse.Error -> {}
                    APIResponse.Loading -> CardRoundLoading()
                    is APIResponse.Success -> Column {
                        LyricsView(v.data, playerInfo)
                    }
                }
            }

            item(7, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                Column {
                    Spacer(Modifier.height(40.dp))
                }
            }


            item(9, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                Column {
                    Column {
                        HorizontalSongView(
                            musicPlayerViewModel.storeData,
                            Pair(TextSize.SMALL, R.string.artists_merchandise),
                            StyleSize.SHOW_AUTHOR,
                            showGrid = true, true
                        )
                    }
                }
            }

            item(10, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                Column {
                    Spacer(Modifier.height(40.dp))
                }
            }

            when (val v = musicPlayerViewModel.similarSongs) {
                APIResponse.Empty -> {}
                is APIResponse.Error -> {}
                APIResponse.Loading -> {
                    item(19, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                        Row(Modifier.padding(start = 5.dp, bottom = 7.dp)) {
                            TextPoppinsSemiBold(
                                stringResource(R.string.similar_songs_you_may_like), size = 15
                            )

                        }
                    }

                    items(1, key = { UUID.randomUUID() }, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                        LoadingView(Modifier.size(32.dp))
                    }
                }

                is APIResponse.Success -> {
                    if (v.data.isNotEmpty()) item(19, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                        Row(Modifier.padding(start = 5.dp, bottom = 7.dp)) {
                            TextPoppinsSemiBold(
                                stringResource(R.string.similar_songs_you_may_like), size = 15
                            )

                        }
                    }

                    items(v.data,
                        span = { GridItemSpan(if (isThreeGrid) THREE_GRID_SIZE else TWO_GRID_SIZE) }) {
                        SongDynamicCards(it, v.data)
                    }
                }
            }
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        try {
            name = playerInfo?.list?.get(pagerState.currentPage)?.name ?: ""
            artists = playerInfo?.list?.get(pagerState.currentPage)?.artists ?: ""
        } catch (e: Exception) {
            e.message
        }
    }

    suspend fun scrollThumbnailCard() {
        playerInfo?.list?.forEachIndexed { index, z ->
            try {
                if (z.id != playerInfo.player?.id) return@forEachIndexed
                pagerState.animateScrollToPage(index)
                name = z.name ?: ""
                artists = z.artists ?: ""
            } catch (e: Exception) {
                e.message
            }
        }
    }

    LaunchedEffect(playerInfo?.player?.id) {
        if (playerInfo?.player?.type() == MusicType.SONGS || playerInfo?.player?.type() == MusicType.OFFLINE_SONGS) {
            playerInfo.player.id?.let { musicPlayerViewModel.similarSongs(it) }
            playerInfo.player.let {
                musicPlayerViewModel.lyrics(it)
                musicPlayerViewModel.videoPlayerData(it)
                musicPlayerViewModel.storeData(it)
            }
        }
        delay(1.seconds)
        scrollThumbnailCard()
    }

    LaunchedEffect(lifecycleState) {
        if (lifecycleState == Lifecycle.State.RESUMED || lifecycleState == Lifecycle.State.STARTED) {
            sendWebViewCommand(OPEN_PLAYER)
            scrollThumbnailCard()
            if (playerInfo?.player?.type() == MusicType.SONGS || playerInfo?.player?.type() == MusicType.OFFLINE_SONGS) {
                playerInfo.player.id?.let { musicPlayerViewModel.similarSongs(it) }
                playerInfo.player.let {
                    logEvents(FirebaseLogEvents.FirebaseEvents.MUSIC_PLAYER_STARTED)
                    musicPlayerViewModel.lyrics(it)
                    musicPlayerViewModel.videoPlayerData(it)
                    musicPlayerViewModel.storeData(it)
                }
            }
        }

    }

    BackHandler {
        close()
    }

    if (alarmSheet) SleepTimerSheet {
        alarmSheet = false
    }
}

@Composable
fun MusicListCards(
    pagerState: PagerState, playerInfo: MusicPlayerData?, name: String,
    artists: String, close: () -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    Spacer(Modifier.height(60.dp))

    HorizontalPager(
        pagerState, contentPadding = PaddingValues(horizontal = 34.dp)
    ) { page ->
        Box(Modifier.fillMaxWidth()) {
            AsyncImage(
                imgBuilder(playerInfo?.list?.get(page)?.thumbnail),
                playerInfo?.list?.get(page)?.name,
                Modifier
                    .align(Alignment.Center)
                    .size((screenWidth.value / 1.3).dp)
            )

            if (playerInfo?.player?.id != playerInfo?.list?.get(page)?.id)
                Row(Modifier
                    .align(
                        Alignment.BottomEnd
                    )
                    .padding(9.dp)
                    .clickable {
                        try {
                            sendWebViewCommand(playerInfo?.list?.get(page)!!, playerInfo.list!!)
                        } catch (e: Exception) {
                            e.message
                        }
                    }
                    .clip(RoundedCornerShape(14.dp))
                    .background(MainColor)
                    .padding(8.dp)) {
                    ImageIcon(R.drawable.ic_play, 24)
                }
        }
    }

    Spacer(Modifier.height(25.dp))
    TextPoppins(name, true, size = 22)
    Spacer(Modifier.height(5.dp))

    if (playerInfo?.player?.type() == MusicType.SONGS || playerInfo?.player?.type() == MusicType.OFFLINE_SONGS) {
        LazyRow(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            items(artists.split(",", " & ", " and ")) {
                if (it.trim() == "," && it.trim() == "&" && it.trim() == " and ")
                    Spacer(Modifier.size(0.dp))
                else if (it.trim().isNotEmpty()) Row(
                    Modifier
                        .padding(4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black)
                        .clickable {
                            close()
                            sendNavCommand(NAV_ARTISTS.replace("{id}", it.trim()))
                        }
                        .padding(vertical = 5.dp, horizontal = 14.dp)
                ) {
                    TextPoppins(it, false, size = 15)
                }
            }
        }
    } else {
        Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
            TextPoppins(artists, false, size = 15)
            Spacer(Modifier.height(2.dp))
            TextPoppins(stringResource(R.string.live_broadcasting), false, size = 15)
            Spacer(Modifier.height(7.dp))
        }
    }

}