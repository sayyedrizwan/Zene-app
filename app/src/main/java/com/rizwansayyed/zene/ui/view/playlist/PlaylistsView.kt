package com.rizwansayyed.zene.ui.view.playlist

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.jakewharton.processphoenix.ProcessPhoenix
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.PodcastPlaylistResponse
import com.rizwansayyed.zene.datastore.DataStorageManager.musicPlayerDB
import com.rizwansayyed.zene.ui.main.view.share.ShareDataView
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ButtonArrowBack
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.ItemCardView
import com.rizwansayyed.zene.ui.view.MiniWithImageAndBorder
import com.rizwansayyed.zene.ui.view.ShimmerEffect
import com.rizwansayyed.zene.ui.view.TextAlertDialog
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.playlist.PlaylistsType.PLAYLIST_ALBUMS
import com.rizwansayyed.zene.ui.view.playlist.PlaylistsType.PODCAST
import com.rizwansayyed.zene.utils.SnackBarManager
import com.rizwansayyed.zene.utils.share.GenerateShortcuts.generateHomeScreenShortcut
import com.rizwansayyed.zene.utils.share.MediaContentUtils.startMedia
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

enum class PlaylistsType(val type: String) {
    PODCAST("PODCAST"), PLAYLIST_ALBUMS("YT_PLAYLISTS")
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PlaylistView(id: String, type: PlaylistsType) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val context = LocalContext.current.applicationContext
    val playerInfo by musicPlayerDB.collectAsState(null)

    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(horizontal = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { Spacer(Modifier.height(100.dp)) }
            when (val v = homeViewModel.playlistsData) {
                ResponseResult.Empty -> {}
                is ResponseResult.Error -> {}
                ResponseResult.Loading -> {
                    item {
                        val width = (LocalConfiguration.current.screenWidthDp / 1.5).dp

                        ShimmerEffect(
                            Modifier
                                .size(width)
                                .clip(RoundedCornerShape(14.dp))
                        )
                    }

                    item {
                        Spacer(Modifier.height(12.dp))
                        ShimmerEffect(
                            Modifier
                                .padding(horizontal = 3.dp)
                                .size(120.dp, 5.dp), durationMillis = 1000
                        )
                        Spacer(Modifier.height(6.dp))
                        ShimmerEffect(
                            Modifier
                                .padding(horizontal = 3.dp)
                                .size(80.dp, 5.dp), durationMillis = 1000
                        )
                    }
                }

                is ResponseResult.Success -> {
                    item { v.data.info?.let { PlaylistTopView(it, type) } }

                    if (v.data.info?.id != null) {
                        item { PlaylistsButtonView(v.data, homeViewModel, type) }
                        item { Spacer(Modifier.height(30.dp)) }
                    }

                    when (val list = homeViewModel.playlistSimilarList) {
                        ResponseResult.Empty -> {}
                        is ResponseResult.Error -> {}
                        ResponseResult.Loading -> {}
                        is ResponseResult.Success -> {
                            if (list.data.isNotEmpty()) item {
                                Spacer(Modifier.height(30.dp))
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 6.dp)
                                ) {
                                    TextViewBold(stringResource(R.string.similar_podcasts), 23)
                                }
                                Spacer(Modifier.height(12.dp))
                                LazyRow(Modifier.fillMaxWidth()) {
                                    items(list.data) {
                                        ItemCardView(it)
                                    }
                                }
                                Spacer(Modifier.height(30.dp))
                            }
                        }
                    }

                    item { Spacer(Modifier.height(20.dp)) }

                    items(v.data.list ?: emptyList()) {
                        when (type) {
                            PODCAST -> PodcastItemView(it, playerInfo, v.data.list ?: emptyList())
                            PLAYLIST_ALBUMS -> PlaylistsItemView(
                                it, playerInfo, v.data.list ?: emptyList()
                            )
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(300.dp)) }
        }


        ButtonArrowBack()
    }
    LaunchedEffect(Unit) {
        if (type == PODCAST) {
            if (homeViewModel.playlistsData !is ResponseResult.Success) homeViewModel.podcastData(id) {
                CoroutineScope(Dispatchers.IO).launch {
                    delay(5.seconds)
                    ProcessPhoenix.triggerRebirth(context)
                }
            }

            if (homeViewModel.playlistSimilarList !is ResponseResult.Success) homeViewModel.similarPlaylistsData(
                id
            )

            return@LaunchedEffect
        }

        if (type == PLAYLIST_ALBUMS) {
            if (homeViewModel.playlistsData !is ResponseResult.Success) {
                homeViewModel.playlistsData(id) {
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(5.seconds)
                        ProcessPhoenix.triggerRebirth(context)
                    }
                }
            }

            if (homeViewModel.playlistSimilarList !is ResponseResult.Success) {
                homeViewModel.similarPlaylistsData(id)
            }
            return@LaunchedEffect
        }
    }
}

@Composable
fun PlaylistsButtonView(
    data: PodcastPlaylistResponse, viewModel: HomeViewModel, type: PlaylistsType
) {
    val addedToLibrary = stringResource(R.string.added_to_your_library)
    val removedLibrary = stringResource(R.string.removed_to_your_library)
    var showShareView by remember { mutableStateOf(false) }
    var showAddToQueue by remember { mutableStateOf(false) }
    var showAddToHomeScreen by remember { mutableStateOf(false) }

    Row(
        Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically
    ) {
        if (viewModel.isPlaylistAdded) Box(
            Modifier
                .padding(horizontal = 7.dp)
                .clickable {
                    SnackBarManager.showMessage(removedLibrary)
                    viewModel.addToPlaylists(false, data, type)
                }) {
            ImageIcon(R.drawable.ic_tick, 24)
        }
        else Box(
            Modifier
                .padding(horizontal = 7.dp)
                .clickable {
                    SnackBarManager.showMessage(addedToLibrary)
                    viewModel.addToPlaylists(true, data, type)
                }) {
            ImageIcon(R.drawable.ic_layer_add, 24)
        }

        Box(
            Modifier
                .padding(horizontal = 7.dp)
                .clickable { showAddToQueue = true }) {
            ImageIcon(R.drawable.ic_add_to_queue, 24)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Box(
            Modifier
                .padding(horizontal = 7.dp)
                .clickable { showAddToHomeScreen = true }) {
            ImageIcon(R.drawable.ic_screen_add_to_home, 24)
        }

        Box(
            Modifier
                .padding(horizontal = 7.dp)
                .clickable { showShareView = true }) {
            ImageIcon(R.drawable.ic_share, 24)
        }

        Spacer(Modifier.weight(1f))

        if (data.list?.isNotEmpty() == true) MiniWithImageAndBorder(
            R.drawable.ic_play, R.string.play, MainColor
        ) {
            if (data.list.isNotEmpty()) startMedia(data.list.first(), data.list)
        }
    }

    if (showShareView) ShareDataView(data.info) {
        showShareView = false
    }

    if (showAddToQueue) AddSongToQueue(data) {
        showAddToQueue = false
    }

    if (showAddToHomeScreen && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) TextAlertDialog(
        R.string.add_to_home_screen,
        R.string.add_to_home_screen_desc,
        {
            showAddToHomeScreen = false
        },
        {
            generateHomeScreenShortcut(data.info)
            showAddToHomeScreen = false
        })
}
