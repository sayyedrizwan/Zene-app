package com.rizwansayyed.zene.ui.musicplayer

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.LikeItemType
import com.rizwansayyed.zene.data.model.MusicDataTypes
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.datastore.DataStorageManager.isLoopDB
import com.rizwansayyed.zene.datastore.DataStorageManager.isPlayerGridDB
import com.rizwansayyed.zene.datastore.DataStorageManager.isShuffleDB
import com.rizwansayyed.zene.datastore.model.MusicPlayerData
import com.rizwansayyed.zene.datastore.model.YoutubePlayerState
import com.rizwansayyed.zene.service.player.PlayerForegroundService.Companion.getPlayerS
import com.rizwansayyed.zene.ui.main.home.HomeSectionSelector
import com.rizwansayyed.zene.ui.main.home.view.LuxCards
import com.rizwansayyed.zene.ui.main.view.AddToPlaylistsView
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.theme.proximanOverFamily
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.CircularLoadingViewSmall
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.ImageWithBorder
import com.rizwansayyed.zene.ui.view.ItemCardView
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_MAIN_PAGE
import com.rizwansayyed.zene.utils.NavigationUtils.triggerHomeNav
import com.rizwansayyed.zene.viewmodel.NavigationViewModel
import com.rizwansayyed.zene.viewmodel.PlayerViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch


val delimiters = arrayOf(",", "&", " and ")

@Composable
fun MusicPlayerView(navViewModel: NavigationViewModel) {
    val player by DataStorageManager.musicPlayerDB.collectAsState(null)
    val playViewModel: PlayerViewModel = hiltViewModel()

    AnimatedVisibility(
        navViewModel.showMusicPlayer,
        enter = slideInVertically(initialOffsetY = { it / 2 }),
        exit = slideOutVertically(targetOffsetY = { it / 2 })
    ) {
        val coroutines = rememberCoroutineScope()
        val pagerState = rememberPagerState(pageCount = { player?.lists?.size ?: 0 })

        LazyColumn(
            Modifier
                .fillMaxSize()
                .background(MainColor)
        ) {
            item {
                Spacer(Modifier.height(40.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    Arrangement.Center,
                    Alignment.CenterVertically
                ) {
                    IconButton({
                        navViewModel.setMusicPlayer(false)
                    }) {
                        ImageIcon(R.drawable.ic_arrow_down, 26)
                    }

                    Spacer(Modifier.weight(1f))

                    LuxCards {
                        coroutines.launch {
                            triggerHomeNav(NAV_MAIN_PAGE)
                            delay(500)
                            navViewModel.setHomeSections(HomeSectionSelector.LUX)
                            navViewModel.setMusicPlayer(false)
                        }
                    }

                    IconButton({

                    }) {
                        ImageIcon(R.drawable.ic_more_vertical_circle, 24)
                    }
                }
            }

            item {
                Spacer(Modifier.height(40.dp))
                SongSlider(player, pagerState)
            }

            item {
                Spacer(Modifier.height(40.dp))
                if ((player?.lists?.size ?: 0) > 0) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        Arrangement.Center,
                        Alignment.CenterVertically
                    ) {
                        SongTextAndArtists(player?.lists, pagerState, Modifier.weight(1f))

                        LikeSongView(player, playViewModel, pagerState)
                    }
                }
            }

            item {
                Spacer(Modifier.height(15.dp))
                PlayerControlView(player)
            }

            item {
                Spacer(Modifier.height(5.dp))
                PlayerButtonControl(player)
            }
        }

        BackHandler { navViewModel.setMusicPlayer(false) }

        LaunchedEffect(pagerState.currentPage) {
            val id = player?.lists?.get(pagerState.currentPage)?.id
            val type = player?.lists?.get(pagerState.currentPage)?.type()!!
            playViewModel.likedMediaItem(id, type)
        }


        LaunchedEffect(player?.data?.id) {
            val i = player?.lists?.indexOfFirst { it?.id == player?.data?.id }
            coroutines.launch {
                pagerState.animateScrollToPage(i ?: 0)
            }
        }
    }

    LaunchedEffect(player?.data?.id) {
        if (player?.data?.id != null && player?.data?.type() != null) {
            playViewModel.likedMediaItem(player?.data?.id, player?.data?.type()!!)
            playViewModel.playerSimilarSongs(player?.data?.id)

            if (player?.data?.type() == MusicDataTypes.AI_MUSIC) playViewModel.getAISongLyrics(
                player?.data?.id!!
            )
            else if (player?.data?.type() == MusicDataTypes.PODCAST_AUDIO) playViewModel.playerPodcastInfo(
                player?.data?.id!!, player?.data?.path!!
            )
            else if (player?.data?.type() == MusicDataTypes.RADIO) playViewModel.playerRadioInfo(
                player?.data?.id!!
            )
            else playViewModel.getSongLyrics()
        }
    }
}

@Composable
fun SongTextAndArtists(data: List<ZeneMusicData?>?, pagerState: PagerState, modifier: Modifier) {
    Column(modifier) {
        Text(
            data?.get(pagerState.currentPage)?.name ?: "",
            Modifier
                .animateContentSize()
                .basicMarquee(),
            Color.White,
            23.sp,
            null,
            FontWeight.Bold,
            proximanOverFamily
        )
        Spacer(Modifier.height(1.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            Arrangement.Start,
            Alignment.CenterVertically
        ) {
            data?.get(pagerState.currentPage)?.artists?.split(*delimiters)?.forEach {
                TextViewNormal(it, 14)
            }
        }
    }
}

@Composable
fun LikeSongView(player: MusicPlayerData?, viewModel: PlayerViewModel, pagerState: PagerState) {
    var addToPlaylistView by remember { mutableStateOf(false) }

    Box(Modifier.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        val isLiked = when (viewModel.isItemLiked[player?.lists?.get(pagerState.currentPage)?.id]) {
            LikeItemType.LIKE -> true
            LikeItemType.LOADING, LikeItemType.NONE, null -> false
        }
        viewModel.likeAItem(player?.lists?.get(pagerState.currentPage), !isLiked)
    }, Alignment.Center
    ) {
        when (viewModel.isItemLiked[player?.lists?.get(pagerState.currentPage)?.id]) {
            LikeItemType.LOADING -> CircularLoadingViewSmall()
            LikeItemType.LIKE -> ImageIcon(R.drawable.ic_thumbs_up, 25, Color.Red)
            LikeItemType.NONE, null -> ImageIcon(R.drawable.ic_thumbs_up, 25)
        }
    }

    Box(Modifier
        .padding(start = 5.dp)
        .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }) {
            addToPlaylistView = true
        }, Alignment.Center
    ) {
        ImageIcon(R.drawable.ic_playlist, 24)
    }

    if (addToPlaylistView) AddToPlaylistsView(player?.lists?.get(pagerState.currentPage)) {
        addToPlaylistView = false
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SongSlider(data: MusicPlayerData?, pagerState: PagerState) {
    HorizontalPager(
        pagerState, Modifier.fillMaxSize(), contentPadding = PaddingValues(horizontal = 20.dp)
    ) { page ->
        GlideImage(
            data?.lists?.get(page)?.thumbnail,
            data?.lists?.get(page)?.name,
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp),
            contentScale = ContentScale.Crop
        )
    }

    LaunchedEffect(Unit) {
        val index = data?.lists?.indexOfFirst { it?.id == data.data?.id } ?: 0
        pagerState.scrollToPage(index)
    }
}

@Composable
fun PlayerButtonControl(player: MusicPlayerData?) {
    val isLoopEnabled by isLoopDB.collectAsState(false)
    val isShuffleEnabled by isShuffleDB.collectAsState(false)

    Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly, Alignment.CenterVertically) {
        Box(Modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }) {
            isLoopDB = flowOf(!isLoopEnabled)
        }) {
            ImageIcon(
                if (isLoopEnabled) R.drawable.ic_repeat_one else R.drawable.ic_repeat, 22
            )
        }

        Box(Modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }) {
            getPlayerS()?.toBackSong()
        }) {
            ImageIcon(R.drawable.ic_backward, 27)
        }

        Box(Modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(100))
            .clickable {
                if (player!!.state == YoutubePlayerState.PLAYING) getPlayerS()?.pause()
                else getPlayerS()?.play()
            }
            .background(Color.White)
            .padding(10.dp)) {
            when (player?.state) {
                YoutubePlayerState.PLAYING -> ImageIcon(
                    R.drawable.ic_pause, 27, Color.Black
                )

                YoutubePlayerState.BUFFERING -> CircularProgressIndicator(
                    Modifier.size(26.dp), Color.White, 4.dp, MainColor
                )

                else -> ImageIcon(R.drawable.ic_play, 27, Color.Black)
            }
        }

        Box(Modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }) {
            getPlayerS()?.toNextSong()
        }) {
            ImageIcon(R.drawable.ic_forward, 27)
        }

        Box(Modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }) {
            isShuffleDB = flowOf(!isShuffleEnabled)
        }) {
            ImageIcon(
                if (isShuffleEnabled) R.drawable.ic_shuffle_square else R.drawable.ic_shuffle, 22
            )
        }
    }
    Spacer(Modifier.height(10.dp))
    Row(
        Modifier
            .padding(horizontal = 20.dp, vertical = 15.dp)
            .fillMaxWidth(),
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        ImageIcon(R.drawable.ic_video_replay, 22)
        Spacer(Modifier.width(25.dp))
        ImageIcon(R.drawable.ic_teaching, 22)

        Spacer(Modifier.weight(1f))

        ImageIcon(R.drawable.ic_dashboard_speed, 22)
        Spacer(Modifier.width(25.dp))
        ImageIcon(R.drawable.ic_timer, 22)
        Spacer(Modifier.width(25.dp))
        ImageIcon(R.drawable.ic_share, 22)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerControlView(player: MusicPlayerData?) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    val interactionSource by remember { mutableStateOf(MutableInteractionSource()) }

    if (player?.data?.type() == MusicDataTypes.RADIO) {
        TextViewSemiBold(stringResource(R.string.live_streaming), size = 14, center = true)
    } else {
        if (player?.totalDuration?.contains("-") == false && player.currentDuration?.contains("-") == false) {
            Slider(
                value = sliderPosition,
                onValueChange = { sliderPosition = it },
                onValueChangeFinished = { getPlayerS()?.seekTo(sliderPosition.toLong()) },
                valueRange = 0f..(player.totalDuration?.toFloatOrNull() ?: 1f),
                colors = SliderDefaults.colors(Color.White),
                modifier = Modifier
                    .padding(horizontal = 11.dp)
                    .fillMaxWidth(),
                track = { sliderState ->
                    SliderDefaults.Track(
                        modifier = Modifier.height(7.dp),
                        sliderState = sliderState,
                        drawStopIndicator = null,
                        thumbTrackGapSize = 2.dp,
                        colors = SliderDefaults.colors(
                            Color.White, Color.White, MainColor, Color.Gray, Color.Gray
                        )
                    )
                },
                interactionSource = interactionSource,
                thumb = {
                    SliderDefaults.Thumb(
                        interactionSource = interactionSource,
                        thumbSize = DpSize(6.dp, 24.dp),
                        colors = SliderDefaults.colors(Color.White)
                    )
                },
            )
        }

        Row(
            Modifier
                .offset(y = (-10).dp)
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
        ) {
            TextViewSemiBold(player?.currentDuration() ?: "", size = 13)
            Spacer(Modifier.weight(1f))
            TextViewSemiBold(player?.totalDuration() ?: "", size = 13)
        }
    }
    Spacer(Modifier.height(9.dp))


    LaunchedEffect(player?.currentDuration) {
        sliderPosition = player?.currentDuration?.toFloatOrNull() ?: 1f
    }

}


@Composable
fun MusicPlayerViewOld(navViewModel: NavigationViewModel) {
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
        val pagerStateMain = rememberPagerState(pageCount = { 3 })
        val pagerState = rememberPagerState(pageCount = { player?.lists?.size ?: 0 })
        val lazyListState = rememberLazyListState()

        Box(
            Modifier
                .fillMaxSize()
                .background(MainColor)
        ) {
            HorizontalPager(pagerStateMain, Modifier.fillMaxSize()) { pageMain ->
                if (pageMain == 0) {
                    if (player?.data?.type() == MusicDataTypes.PODCAST_AUDIO) MusicPlayerPodcastInfoView(
                        playViewModel, navViewModel
                    )
                    else if (player?.data?.type() == MusicDataTypes.RADIO) MusicPlayerRadioInfoView(
                        playViewModel
                    )
                    else MusicPlayerLyricsView(playViewModel, player?.currentDuration)
                } else if (pageMain == 2) SimilarSongsPodcastsView(player, playViewModel)
                else {
                    if (isPlayerGrid) VerticalPager(
                        pagerState,
                        Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = bottomPadding, top = topPadding)
                    ) { page ->
                        MusicPlayingGridView(player, page, pagerState, screenWidth)
                    } else LazyColumn(Modifier.fillMaxSize(), lazyListState) {
                        item { Spacer(Modifier.height(100.dp)) }
                        items(player?.lists ?: emptyList()) {
                            MusicPlayingListView(player, it)
                        }
                        item { Spacer(Modifier.height(500.dp)) }
                    }
                }
            }

            MusicPlayerControlPanel(
                Modifier.align(Alignment.BottomCenter), player, playViewModel, pagerStateMain
            )

            Row(
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 50.dp)
                    .padding(10.dp)
            ) {
                Box(
                    Modifier
                        .weight(1f)
                        .padding(horizontal = 6.dp)
                ) {
                    when (pagerStateMain.currentPage) {
                        0 -> TextViewBold(
                            stringResource(
                                if (player?.data?.type() == MusicDataTypes.PODCAST_AUDIO || player?.data?.type() == MusicDataTypes.RADIO) R.string.info
                                else R.string.lyrics
                            ), 19
                        )

                        2 -> if (player?.data?.type() == MusicDataTypes.PODCAST_AUDIO) TextViewBold(
                            stringResource(R.string.similar_podcasts),
                            19
                        )
                        else TextViewBold(stringResource(R.string.similar_songs), 19)

                        else -> TextViewBold(stringResource(R.string.list), 19)
                    }

                }

                if (pagerStateMain.currentPage == 1) {
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
        }

        LaunchedEffect(player?.data?.id, isPlayerGrid) {
            coroutines.launch {
                pagerStateMain.animateScrollToPage(1)
            }

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
        if (player?.data?.id != null && player?.data?.type() != null) {
            playViewModel.likedMediaItem(player?.data?.id, player?.data?.type()!!)
            playViewModel.playerSimilarSongs(player?.data?.id)

            if (player?.data?.type() == MusicDataTypes.AI_MUSIC)
                playViewModel.getAISongLyrics(player?.data?.id!!)
            else if (player?.data?.type() == MusicDataTypes.PODCAST_AUDIO)
                playViewModel.playerPodcastInfo(player?.data?.id!!, player?.data?.path!!)
            else if (player?.data?.type() == MusicDataTypes.RADIO)
                playViewModel.playerRadioInfo(player?.data?.id!!)
            else playViewModel.getSongLyrics()
        }
    }
}

@Composable
fun SimilarSongsPodcastsView(player: MusicPlayerData?, playViewModel: PlayerViewModel) {
    LazyColumn(Modifier.fillMaxSize()) {
        when (val v = playViewModel.similarSongs) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> item {
                Spacer(Modifier.height(50.dp))
                CircularLoadingView()
            }

            is ResponseResult.Success -> {
                if (v.data.songs?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(50.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.song), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(v.data.songs) {
                            ItemCardView(it)
                        }
                    }
                }

                if (v.data.playlists?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(50.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.playlists), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(v.data.playlists) {
                            ItemCardView(it)
                        }
                    }
                }

                if (v.data.albums?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(50.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.albums), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(v.data.albums) {
                            ItemCardView(it)
                        }
                    }
                }
            }
        }
    }
}
