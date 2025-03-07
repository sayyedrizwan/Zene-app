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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.LikeItemType
import com.rizwansayyed.zene.data.model.MusicDataTypes
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.datastore.model.MusicPlayerData
import com.rizwansayyed.zene.ui.main.home.HomeSectionSelector
import com.rizwansayyed.zene.ui.main.home.view.LuxCards
import com.rizwansayyed.zene.ui.main.view.AddToPlaylistsView
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.theme.proximanOverFamily
import com.rizwansayyed.zene.ui.view.CircularLoadingViewSmall
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_MAIN_PAGE
import com.rizwansayyed.zene.utils.NavigationUtils.triggerHomeNav
import com.rizwansayyed.zene.viewmodel.NavigationViewModel
import com.rizwansayyed.zene.viewmodel.PlayerViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


val delimiters = arrayOf(",", "&", " and ")

@Composable
fun MusicPlayerView(navViewModel: NavigationViewModel) {
    val player by DataStorageManager.musicPlayerDB.collectAsState(null)
    val viewModel: PlayerViewModel = hiltViewModel()

    AnimatedVisibility(
        navViewModel.showMusicPlayer,
        enter = slideInVertically(initialOffsetY = { it / 2 }),
        exit = slideOutVertically(targetOffsetY = { it / 2 })
    ) {
        val coroutines = rememberCoroutineScope()
        val pagerState = rememberPagerState(pageCount = { player?.lists?.size ?: 0 })
        val columnState = rememberScrollState()

        Column(
            Modifier
                .verticalScroll(columnState)
                .fillMaxSize()
                .background(MainColor)
        ) {
            Spacer(Modifier.height(50.dp))
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
                    }
                }

                IconButton({

                }) {
                    ImageIcon(R.drawable.ic_more_vertical_circle, 24)
                }
            }

            Spacer(Modifier.height(40.dp))
            SongSlider(player, pagerState)

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

                    LikeSongView(player, viewModel, pagerState)
                }
            }


            Spacer(Modifier.height(15.dp))
            PlayerControlView(player)


            Spacer(Modifier.height(5.dp))
            PlayerButtonControl(player)


            Spacer(Modifier.height(5.dp))
            PlayerItemButtonView(player, viewModel)


            if (player?.data?.type() == MusicDataTypes.SONGS || player?.data?.type() == MusicDataTypes.AI_MUSIC) {
                Spacer(Modifier.height(25.dp))
                MusicPlayerLyricsView(viewModel, player?.currentDuration)
            }


            if (player?.data?.type() == MusicDataTypes.PODCAST_AUDIO) {
                Spacer(Modifier.height(25.dp))
                MusicPlayerPodcastInfoView(viewModel)
            }


            if (player?.data?.type() == MusicDataTypes.RADIO) {
                Spacer(Modifier.height(25.dp))
                MusicPlayerRadioInfoView(viewModel)
            }


            if (player?.data?.type() == MusicDataTypes.SONGS) {
                Spacer(Modifier.height(25.dp))
                MusicPlayerSimilarSongsView(viewModel)
            }


            if (player?.data?.type() == MusicDataTypes.RADIO) {
                Spacer(Modifier.height(25.dp))
                MusicPlayerSimilarRadiosView(viewModel)
            }

            if (player?.data?.type() == MusicDataTypes.PODCAST_AUDIO) {
                Spacer(Modifier.height(25.dp))
                MusicPlayerSimilarPodcastView(viewModel)
            }

            if (player?.data?.type() == MusicDataTypes.AI_MUSIC) {
                Spacer(Modifier.height(25.dp))
                MusicPlayerSimilarAIViewView(viewModel)
            }


            Spacer(Modifier.height(150.dp))
        }

        BackHandler { navViewModel.setMusicPlayer(false) }

        LaunchedEffect(pagerState.currentPage) {
            try {
                val id = player?.lists?.get(pagerState.currentPage)?.id
                val type = player?.lists?.get(pagerState.currentPage)?.type()!!
                viewModel.likedMediaItem(id, type)
            }catch (e: Exception){
                e.message
            }
        }


        LaunchedEffect(player?.data?.id) {
            val i = player?.lists?.indexOfFirst { it?.id == player?.data?.id }
            coroutines.launch {
                pagerState.animateScrollToPage(i ?: 0)
            }
            columnState.animateScrollTo(0)
        }

    }

    LaunchedEffect(player?.data?.id) {
        if (player?.data?.id != null && player?.data?.type() != null) {
            viewModel.likedMediaItem(player?.data?.id, player?.data?.type()!!)
            viewModel.playerSimilarSongs(player?.data?.id)

            if (player?.data?.type() == MusicDataTypes.SONGS)
                viewModel.playerVideoForSongs(player?.data)
            else if (player?.data?.type() == MusicDataTypes.PODCAST_AUDIO)
                viewModel.similarPodcasts(player?.data)
            else if (player?.data?.type() == MusicDataTypes.RADIO)
                viewModel.similarRadio(player?.data)
            else if (player?.data?.type() == MusicDataTypes.AI_MUSIC)
                viewModel.similarAIMusic(player?.data)

            if (player?.data?.type() == MusicDataTypes.AI_MUSIC) {
                viewModel.getAISongLyrics(player?.data?.id!!)
            } else if (player?.data?.type() == MusicDataTypes.PODCAST_AUDIO) {
                viewModel.playerPodcastInfo(player?.data?.id!!, player?.data?.path!!)
            } else if (player?.data?.type() == MusicDataTypes.RADIO) {
                viewModel.playerRadioInfo(player?.data?.id!!)
            } else viewModel.getSongLyrics()
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
            Color.White, 23.sp, null, FontWeight.Bold, proximanOverFamily
        )

        Spacer(Modifier.height(1.dp))

        if (data?.get(pagerState.currentPage)?.type() == MusicDataTypes.SONGS
            || data?.get(pagerState.currentPage)?.type() == MusicDataTypes.RADIO
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                Arrangement.Start,
                Alignment.CenterVertically
            ) {
                data[pagerState.currentPage]?.artists?.split(*delimiters)?.forEach {
                    TextViewNormal(it, 14)
                    Spacer(Modifier.width(8.dp))
                }
            }
        }

        if (data?.get(pagerState.currentPage)?.type() == MusicDataTypes.PODCAST_AUDIO) {
            Row(Modifier.fillMaxWidth(), Arrangement.Start, Alignment.CenterVertically) {
                TextViewNormal(data[pagerState.currentPage]?.artists ?: "", 14, line = 1)
            }
        }

    }
}

@Composable
fun LikeSongView(player: MusicPlayerData?, viewModel: PlayerViewModel, pagerState: PagerState) {
    var addToPlaylistView by remember { mutableStateOf(false) }

    Box(Modifier
        .padding(start = 10.dp)
        .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }) {
            val isLiked =
                when (viewModel.isItemLiked[player?.lists?.get(pagerState.currentPage)?.id]) {
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
