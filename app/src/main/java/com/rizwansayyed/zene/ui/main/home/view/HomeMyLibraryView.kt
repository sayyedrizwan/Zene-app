package com.rizwansayyed.zene.ui.main.home.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.MusicDataTypes
import com.rizwansayyed.zene.data.model.MusicHistoryResponse
import com.rizwansayyed.zene.data.model.MyLibraryTypes
import com.rizwansayyed.zene.data.model.SavedPlaylistsPodcastsResponseItem
import com.rizwansayyed.zene.ui.main.view.CreateAPlaylistsView
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.view.ButtonWithBorder
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.ImageWithBorder
import com.rizwansayyed.zene.ui.view.TextAlertDialog
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.utils.share.MediaContentUtils.startMedia
import com.rizwansayyed.zene.viewmodel.MyLibraryViewModel
import com.rizwansayyed.zene.viewmodel.PlayerViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeMyLibraryView() {
    val viewModel: MyLibraryViewModel = hiltViewModel()
    val playerViewModel: PlayerViewModel = hiltViewModel()
    var selectedType by remember { mutableStateOf(MyLibraryTypes.HISTORY) }

    var historyInfo by remember { mutableStateOf(false) }
    var addNewPlaylists by remember { mutableStateOf(false) }

    val state = rememberLazyListState()
    var isBottomTriggered by remember { mutableStateOf(false) }


    LazyColumn(Modifier.fillMaxSize(), state) {
        item { Spacer(Modifier.height(10.dp)) }

        stickyHeader {
            Row(
                Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .background(DarkCharcoal)
                    .padding(vertical = 5.dp),
                Arrangement.Start, Alignment.CenterVertically
            ) {
                MyLibraryTypes.entries.forEach {
                    ButtonWithBorder(
                        it.names, if (selectedType == it) Color.White else Color.DarkGray
                    ) {
                        selectedType = it
                    }

                    if (it == MyLibraryTypes.HISTORY)
                        AnimatedVisibility(selectedType == MyLibraryTypes.HISTORY) {
                            ImageWithBorder(R.drawable.ic_information_circle) {
                                historyInfo = true
                            }
                        }


                    if (it == MyLibraryTypes.MY_PLAYLISTS)
                        AnimatedVisibility(selectedType == MyLibraryTypes.MY_PLAYLISTS) {
                            ImageWithBorder(R.drawable.ic_plus_sign) {
                                addNewPlaylists = true
                            }
                        }
                }
            }
        }

        item { Spacer(Modifier.height(30.dp)) }

        when (selectedType) {
            MyLibraryTypes.HISTORY -> {
                items(viewModel.historyList) { HistoryCardItems(it) }

                item { if (viewModel.historyIsLoading) CircularLoadingView() }
            }

            MyLibraryTypes.SAVED -> {
                items(viewModel.savedList) { SavedPlaylistsPodcastView(it) }

                item { if (viewModel.savedIsLoading) CircularLoadingView() }
            }

            MyLibraryTypes.MY_PLAYLISTS -> {

            }
        }

        item { Spacer(Modifier.height(300.dp)) }
    }


    LaunchedEffect(state) {
        snapshotFlow { state.layoutInfo }
            .collect { layoutInfo ->
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemsCount = layoutInfo.totalItemsCount

                if (lastVisibleItemIndex >= totalItemsCount - 1 && !isBottomTriggered) {
                    isBottomTriggered = true
                    when (selectedType) {
                        MyLibraryTypes.HISTORY -> viewModel.songHistoryList()
                        MyLibraryTypes.SAVED -> viewModel.savedPlaylistsList()
                        MyLibraryTypes.MY_PLAYLISTS -> {}
                    }

                    viewModel.savedPlaylistsList()
                } else if (lastVisibleItemIndex < totalItemsCount - 1) {
                    isBottomTriggered = false
                }
            }
    }

    if (historyInfo) TextAlertDialog(R.string.history, R.string.history_desc) {
        historyInfo = false
    }

    if (addNewPlaylists) CreateAPlaylistsView(playerViewModel, null) {
        addNewPlaylists = false
        if (it) {
//            info?.id?.let { playerViewModel.playlistSongCheckList(page, it) }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HistoryCardItems(data: MusicHistoryResponse) {
    Row(Modifier
        .padding(10.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .background(Color.Black)
        .clickable { startMedia(data.asMusicData()) }
        .padding(10.dp),
        Arrangement.Center,
        Alignment.CenterVertically) {
        GlideImage(
            data.thumbnail,
            data.name,
            Modifier
                .padding(horizontal = 5.dp)
                .height(90.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(14.dp)),
            contentScale = ContentScale.Fit
        )
        Column(
            Modifier
                .padding(horizontal = 4.dp)
                .weight(1f), Arrangement.Center, Alignment.Start
        ) {
            TextViewBold(data.name ?: "", 13, line = 2)
            if ((data.artists?.length ?: 0) > 3) Box(Modifier.offset(y = (-2).dp)) {
                TextViewNormal(data.artists!!, 12, line = 1)
            }
        }

        if (data.asMusicData().type() == MusicDataTypes.SONGS) {
            ImageIcon(R.drawable.ic_music_note, 20)
        } else if (data.asMusicData().type() == MusicDataTypes.AI_MUSIC) {
            ImageIcon(R.drawable.ic_robot_singing, 20)
        } else if (data.asMusicData().type() == MusicDataTypes.PODCAST) {
            ImageIcon(R.drawable.ic_podcast, 20)
        } else if (data.asMusicData().type() == MusicDataTypes.RADIO) {
            ImageIcon(R.drawable.ic_radio, 20)
        } else if (data.asMusicData().type() == MusicDataTypes.VIDEOS) {
            ImageIcon(R.drawable.ic_video_replay, 20)
        }

    }
}

@Composable
fun SavedPlaylistsPodcastView(modifier: SavedPlaylistsPodcastsResponseItem) {

}