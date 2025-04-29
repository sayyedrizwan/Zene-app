package com.rizwansayyed.zene.ui.main.home.view

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.MusicDataTypes
import com.rizwansayyed.zene.data.model.MusicHistoryResponse
import com.rizwansayyed.zene.data.model.MyLibraryTypes
import com.rizwansayyed.zene.data.model.SavedPlaylistsPodcastsResponseItem
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.service.notification.NavigationUtils
import com.rizwansayyed.zene.service.notification.NavigationUtils.NAV_MY_PLAYLIST_PAGE
import com.rizwansayyed.zene.service.notification.NavigationUtils.NAV_PLAYLIST_PAGE
import com.rizwansayyed.zene.service.notification.NavigationUtils.NAV_PODCAST_PAGE
import com.rizwansayyed.zene.ui.main.view.CreateAPlaylistsView
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.view.ButtonWithBorder
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.CircularLoadingViewSmall
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.ImageWithBorder
import com.rizwansayyed.zene.ui.view.TextAlertDialog
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.utils.RefreshPlaylistManager.RefreshPlaylistListener
import com.rizwansayyed.zene.utils.RefreshPlaylistManager.setRefreshPlaylistState
import com.rizwansayyed.zene.utils.URLSUtils.LIKED_SONGS_ON_ZENE
import com.rizwansayyed.zene.utils.ads.InterstitialAdsUtils
import com.rizwansayyed.zene.utils.share.MediaContentUtils.startMedia
import com.rizwansayyed.zene.viewmodel.MyLibraryViewModel
import com.rizwansayyed.zene.viewmodel.PlayerViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@Composable
fun HomeMyLibraryView() {
    val viewModel: MyLibraryViewModel = hiltViewModel()
    val playerViewModel: PlayerViewModel = hiltViewModel()
    val activity = LocalActivity.current

    var historyInfo by remember { mutableStateOf(false) }
    var addNewPlaylists by remember { mutableStateOf(false) }

    val coroutine = rememberCoroutineScope()
    val state = rememberLazyGridState()
    var isBottomTriggered by remember { mutableStateOf(false) }

    val screenWidth = LocalConfiguration.current.screenWidthDp
    val columns = remember {
        when {
            screenWidth < 600 -> 2
            screenWidth < 900 -> 3
            else -> 4
        }
    }

    Box(Modifier.fillMaxSize()) {
        LazyVerticalGrid(GridCells.Fixed(columns), Modifier.fillMaxSize(), state) {
            item(span = { GridItemSpan(maxLineSpan) }) { Spacer(Modifier.height(60.dp)) }

            when (viewModel.selectedType) {
                MyLibraryTypes.HISTORY -> {
                    items(viewModel.historyList) { HistoryCardItems(it) }

                    if (!viewModel.historyIsLoading && viewModel.historyList.isEmpty()) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Spacer(Modifier.height(60.dp))
                        }

                        item(span = { GridItemSpan(maxLineSpan) }) {
                            TextViewNormal(stringResource(R.string.no_history), 15, center = true)
                        }
                    }

                    item(span = { GridItemSpan(maxLineSpan) }) { if (viewModel.historyIsLoading) CircularLoadingView() }
                }

                MyLibraryTypes.SAVED -> {
                    items(viewModel.savedList) {
                        SavedPlaylistsPodcastView(it, Modifier.fillMaxWidth())
                    }

                    if (!viewModel.savedIsLoading && viewModel.savedList.isEmpty()) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Spacer(Modifier.height(60.dp))
                        }
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            TextViewNormal(
                                stringResource(R.string.no_saved_and_save), 15, center = true
                            )
                        }
                    }

                    item(span = { GridItemSpan(maxLineSpan) }) { if (viewModel.savedIsLoading) CircularLoadingView() }
                }

                MyLibraryTypes.MY_PLAYLISTS -> {
                    item { LikedPlaylistsView(viewModel) }
                    items(viewModel.myList) {
                        SavedPlaylistsPodcastView(it, Modifier.fillMaxWidth())
                    }

                    if (!viewModel.myIsLoading && viewModel.myList.isEmpty()) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Spacer(Modifier.height(60.dp))
                        }

                        item(span = { GridItemSpan(maxLineSpan) }) {
                            TextViewNormal(stringResource(R.string.no_playlists), 15, center = true)
                        }
                    }

                    item(span = { GridItemSpan(maxLineSpan) }) { if (viewModel.myIsLoading) CircularLoadingView() }
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) { Spacer(Modifier.height(300.dp)) }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .background(DarkCharcoal)
                .padding(vertical = 5.dp), Arrangement.Start, Alignment.CenterVertically
        ) {
            MyLibraryTypes.entries.forEach {
                ButtonWithBorder(
                    it.names, if (viewModel.selectedType == it) Color.White else Color.DarkGray
                ) { viewModel.setType(it) }

                if (it == MyLibraryTypes.HISTORY) AnimatedVisibility(viewModel.selectedType == MyLibraryTypes.HISTORY) {
                    ImageWithBorder(R.drawable.ic_information_circle) {
                        historyInfo = true
                    }
                }

                if (it == MyLibraryTypes.MY_PLAYLISTS) AnimatedVisibility(viewModel.selectedType == MyLibraryTypes.MY_PLAYLISTS) {
                    ImageWithBorder(R.drawable.ic_plus_sign) {
                        addNewPlaylists = true
                    }
                }
            }
        }

        LaunchedEffect(state) {
            when (viewModel.selectedType) {
                MyLibraryTypes.HISTORY -> viewModel.songHistoryList()
                MyLibraryTypes.SAVED -> viewModel.savedPlaylistsList()
                MyLibraryTypes.MY_PLAYLISTS -> viewModel.myPlaylistsList()
            }
        }

        LaunchedEffect(state) {
            snapshotFlow { state.layoutInfo }.collect { layoutInfo ->
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemsCount = layoutInfo.totalItemsCount

                if (lastVisibleItemIndex >= totalItemsCount - 1 && !isBottomTriggered) {
                    isBottomTriggered = true
                    when (viewModel.selectedType) {
                        MyLibraryTypes.HISTORY -> viewModel.songHistoryList()
                        MyLibraryTypes.SAVED -> viewModel.savedPlaylistsList()
                        MyLibraryTypes.MY_PLAYLISTS -> viewModel.myPlaylistsList()
                    }
                } else if (lastVisibleItemIndex < totalItemsCount - 1) {
                    isBottomTriggered = false
                }
            }
        }

        if (historyInfo) TextAlertDialog(R.string.history, null, R.string.history_desc) {
            historyInfo = false
        }

        if (addNewPlaylists) CreateAPlaylistsView(playerViewModel, null) {
            addNewPlaylists = false
            if (it) coroutine.launch {
                viewModel.myPlaylistsList(true)
                state.animateScrollToItem(0)
            }
        }

        LaunchedEffect(Unit) {
            viewModel.likedItemCount()
            activity?.let { InterstitialAdsUtils(it) }

            setRefreshPlaylistState(object : RefreshPlaylistListener {
                override fun refresh() {
                    coroutine.launch {
                        viewModel.myPlaylistsList(true)
                        state.animateScrollToItem(0)
                    }
                }
            })
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun HistoryCardItems(data: MusicHistoryResponse) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .padding(bottom = 15.dp)
            .combinedClickable(
                onLongClick = { NavigationUtils.triggerInfoSheet(data.asMusicData()) },
                onClick = { startMedia(data.asMusicData()) }), Alignment.Center
    ) {

        Column(Modifier.fillMaxWidth()) {
            GlideImage(
                data.thumbnail,
                data.name,
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(7.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(5.dp))
            TextViewBold(data.name ?: "", 14, line = 2)
            TextViewNormal(data.artists ?: "", 14, line = 1)
        }

        Box(
            Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .clip(RoundedCornerShape(100))
                .background(Color.Black)
                .padding(5.dp)
        ) {
            if (data.asMusicData().type() == MusicDataTypes.SONGS) {
                ImageIcon(R.drawable.ic_music_note, 20)
            } else if (data.asMusicData().type() == MusicDataTypes.AI_MUSIC) {
                ImageIcon(R.drawable.ic_robot_singing, 20)
            } else if (data.asMusicData().type() == MusicDataTypes.PODCAST_AUDIO) {
                ImageIcon(R.drawable.ic_podcast, 20)
            } else if (data.asMusicData().type() == MusicDataTypes.RADIO) {
                ImageIcon(R.drawable.ic_radio, 20)
            } else if (data.asMusicData().type() == MusicDataTypes.VIDEOS) {
                ImageIcon(R.drawable.ic_video_replay, 20)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SavedPlaylistsPodcastView(data: SavedPlaylistsPodcastsResponseItem, modifier: Modifier) {
    Column(
        modifier
            .padding(5.dp)
            .padding(bottom = 15.dp)
            .clickable {
                if (data.isUserPlaylist()) {
                    NavigationUtils.triggerHomeNav("$NAV_MY_PLAYLIST_PAGE${data.id}")
                } else {
                    if (data.isPodcast()) NavigationUtils.triggerHomeNav("$NAV_PODCAST_PAGE${data.id}")
                    else NavigationUtils.triggerHomeNav("$NAV_PLAYLIST_PAGE${data.id}")
                }
            }) {
        GlideImage(
            data.img,
            data.name,
            Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(7.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(5.dp))
        TextViewBold(data.name ?: "", 14, line = 2)
        Box(Modifier.offset(y = (-3).dp)) {
            if (data.isUserPlaylist()) {
                if (data.isConnectUser())
                    TextViewNormal(stringResource(R.string.shared_playlist), 14, line = 1)
                else
                    TextViewNormal(stringResource(R.string.my_playlist), 14, line = 1)
            } else {
                if (data.isPodcast()) TextViewNormal(
                    stringResource(R.string.podcasts), 14, line = 1
                )
                else if (data.id?.startsWith("VL") == true) TextViewNormal(
                    stringResource(R.string.playlist), 14, line = 1
                )
                else TextViewNormal(stringResource(R.string.album), 14, line = 1)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LikedPlaylistsView(data: MyLibraryViewModel) {
    val coroutine = rememberCoroutineScope()
    Column(
        Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .padding(bottom = 15.dp)
            .clickable {
                coroutine.launch {
                    val email = DataStorageManager.userInfo.firstOrNull()?.email
                    val id = "${email}${LIKED_SONGS_ON_ZENE}"
                    NavigationUtils.triggerHomeNav("$NAV_MY_PLAYLIST_PAGE${id}")
                }
            }) {
        GlideImage(
            "https://i.ibb.co/xq4CWHCz/liked-thumb-img.png",
            stringResource(R.string.liked_items),
            Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(7.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(5.dp))
        TextViewBold(stringResource(R.string.liked_items), 14, line = 2)
        Box(Modifier.offset(y = (-3).dp)) {
            when (val v = data.likedItemsCount) {
                ResponseResult.Empty -> {}
                is ResponseResult.Error -> {}
                ResponseResult.Loading -> CircularLoadingViewSmall()
                is ResponseResult.Success -> TextViewNormal(
                    "${v.data.count ?: 0} ${stringResource(R.string.items)}", 14, line = 1
                )
            }
        }
    }
}