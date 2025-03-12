package com.rizwansayyed.zene.ui.view

import android.os.Build
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.jakewharton.processphoenix.ProcessPhoenix
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.MusicDataTypes
import com.rizwansayyed.zene.data.model.PodcastPlaylistResponse
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.data.model.ZeneMusicDataList
import com.rizwansayyed.zene.datastore.DataStorageManager.musicPlayerDB
import com.rizwansayyed.zene.datastore.model.MusicPlayerData
import com.rizwansayyed.zene.service.player.PlayerForegroundService.Companion.getPlayerS
import com.rizwansayyed.zene.ui.main.view.share.ShareDataView
import com.rizwansayyed.zene.ui.theme.BlackGray
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.theme.proximanOverFamily
import com.rizwansayyed.zene.ui.view.PlaylistsType.PLAYLIST_ALBUMS
import com.rizwansayyed.zene.ui.view.PlaylistsType.PODCAST
import com.rizwansayyed.zene.utils.MainUtils.formatDurationsForVideo
import com.rizwansayyed.zene.utils.SnackBarManager
import com.rizwansayyed.zene.utils.share.GenerateShortcuts.generateHomeScreenShortcut
import com.rizwansayyed.zene.utils.share.MediaContentUtils.TEMP_ZENE_MUSIC_DATA_LIST
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

@Composable
fun PlaylistView(id: String, type: PlaylistsType) {
    val homeViewModel: HomeViewModel = hiltViewModel(key = id)
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
                ResponseResult.Loading -> item { CircularLoadingView() }
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PodcastItemView(data: ZeneMusicData, info: MusicPlayerData?, list: ZeneMusicDataList) {
    Row(Modifier
        .padding(top = 15.dp)
        .padding(horizontal = 5.dp, vertical = 10.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(13.dp))
        .background(BlackGray)
        .clickable { startMedia(data, list) }
        .padding(horizontal = 15.dp, vertical = 25.dp),
        Arrangement.Center,
        Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            TextViewSemiBold(data.name ?: "", 16, line = 3)
            Spacer(Modifier.height(10.dp))
            TextViewNormal(data.artists ?: "", 13, line = 3)
            Spacer(Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.Start, Alignment.CenterVertically) {
                Spacer(Modifier.width(2.dp))
                ImageIcon(R.drawable.ic_clock, 16)
                Spacer(Modifier.width(5.dp))
                TextViewNormal(data.timeAgo(), 15)

                Spacer(Modifier.weight(1f))
                ImageIcon(R.drawable.ic_play, 17)
                Spacer(Modifier.width(5.dp))
                TextViewNormal(formatDurationsForVideo(data.extraInfo?.toFloatOrNull() ?: 0f), 15)
            }
        }

        if (info?.data?.id == data.id) GlideImage(
            R.raw.song_playing_wave, "", Modifier.size(24.dp), contentScale = ContentScale.Crop
        )
        else ImageIcon(R.drawable.ic_play, 25)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PlaylistsItemView(data: ZeneMusicData, info: MusicPlayerData?, list: ZeneMusicDataList) {
    Row(Modifier
        .padding(top = 15.dp)
        .padding(horizontal = 5.dp, vertical = 10.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(13.dp))
        .background(BlackGray)
        .clickable { startMedia(data, list) }
        .padding(horizontal = 15.dp, vertical = 25.dp),
        Arrangement.Center,
        Alignment.CenterVertically) {
        GlideImage(
            data.thumbnail,
            data.name,
            Modifier
                .padding(end = 10.dp)
                .size(60.dp),
            contentScale = ContentScale.Crop
        )

        Column(Modifier.weight(1f), Arrangement.Center, Alignment.Start) {
            TextViewSemiBold(data.name ?: "", 15, line = 3)
            Spacer(Modifier.height(2.dp))
            TextViewNormal(data.artists ?: "", 12, line = 1)
        }

        if (info?.data?.id == data.id) GlideImage(
            R.raw.song_playing_wave, "", Modifier.size(24.dp), contentScale = ContentScale.Crop
        )
        else ImageIcon(R.drawable.ic_play, 25)
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
        if (viewModel.isPlaylistAdded) Box(Modifier
            .padding(horizontal = 7.dp)
            .clickable {
                SnackBarManager.showMessage(removedLibrary)
                viewModel.addToPlaylists(false, data, type)
            }) {
            ImageIcon(R.drawable.ic_tick, 24)
        }
        else Box(Modifier
            .padding(horizontal = 7.dp)
            .clickable {
                SnackBarManager.showMessage(addedToLibrary)
                viewModel.addToPlaylists(true, data, type)
            }) {
            ImageIcon(R.drawable.ic_layer_add, 24)
        }

        Box(Modifier
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

        Box(Modifier
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

    if (showAddToHomeScreen && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) TextAlertDialog(R.string.add_to_home_screen,
        R.string.add_to_home_screen_desc,
        {
            showAddToHomeScreen = false
        },
        {
            generateHomeScreenShortcut(data.info)
            showAddToHomeScreen = false
        })
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PlaylistTopView(v: ZeneMusicData, type: PlaylistsType) {
    val width = (LocalConfiguration.current.screenWidthDp / 1.5).dp
    var fullDesc by remember { mutableStateOf(false) }
    var shouldShowArrow by remember { mutableStateOf(false) }

    GlideImage(
        v.thumbnail, v.name,
        modifier = Modifier
            .size(width)
            .clip(RoundedCornerShape(14.dp)),
        contentScale = ContentScale.Crop
    )
    Spacer(Modifier.height(15.dp))
    TextViewBoldBig(v.name ?: "", 40, center = true)
    Spacer(Modifier.height(15.dp))
    when (type) {
        PODCAST -> TextViewSemiBold(stringResource(R.string.podcast), 17, center = true)
        else -> {
            TextViewSemiBold(
                stringResource(
                    if (v.type() == MusicDataTypes.ALBUMS) R.string.album else R.string.playlist
                ), 17, center = true
            )

            if (v.type() == MusicDataTypes.ALBUMS) {
                Spacer(Modifier.height(15.dp))
                TextViewNormal(v.extra ?: "", 17, center = true)
            }
        }
    }

    if ((v.artists?.trim()?.length ?: 0) > 5) {
        Spacer(Modifier.height(15.dp))
        Text(
            v.artists ?: "",
            Modifier
                .fillMaxWidth()
                .animateContentSize(),
            Color.White, 14.sp, null, FontWeight.Normal, proximanOverFamily,
            textAlign = TextAlign.Center, maxLines = if (fullDesc) 1000 else 3,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult ->
                shouldShowArrow = textLayoutResult.lineCount > 2
            },
        )

        Spacer(Modifier.height(5.dp))

        if (shouldShowArrow) Box(Modifier
            .rotate(if (fullDesc) 180f else 0f)
            .clickable { fullDesc = !fullDesc }) {
            ImageIcon(R.drawable.ic_arrow_down, 28)
        }
    }
    Spacer(Modifier.height(30.dp))
}

@Composable
fun AddSongToQueue(data: PodcastPlaylistResponse, close: () -> Unit) {
    AlertDialog(title = {
        TextViewNormal(stringResource(R.string.add_to_queue), 17, line = 2, center = false)
    }, text = {
        TextViewNormal(stringResource(R.string.queue_desc), 16, center = false)
    }, onDismissRequest = {
        close()
    }, confirmButton = {
        Row {
            TextButton(onClick = {
                close()
                TEMP_ZENE_MUSIC_DATA_LIST.clear()
                TEMP_ZENE_MUSIC_DATA_LIST.addAll(data.list?.toTypedArray() ?: emptyArray())
                if (getPlayerS() == null)
                    startMedia(data.list?.first(), data.list?.toList() ?: emptyList())
                else
                    getPlayerS()?.addListsToNext(data.list?.toList() ?: emptyList())
            }) {
                TextViewNormal(stringResource(R.string.play_next), 15)
            }
            Spacer(Modifier.width(10.dp))
            TextButton(onClick = {
                close()
                TEMP_ZENE_MUSIC_DATA_LIST.clear()
                TEMP_ZENE_MUSIC_DATA_LIST.addAll(data.list?.toTypedArray() ?: emptyArray())

                if (getPlayerS() == null)
                    startMedia(data.list?.first(), data.list?.toList() ?: emptyList())
                else
                    getPlayerS()?.addListsToQueue(data.list?.toList() ?: emptyList())
            }) {
                TextViewNormal(stringResource(R.string.add_to_queue), 15)
            }
        }
    }, dismissButton = {
        TextButton(onClick = {
            close()
        }) {
            TextViewNormal(stringResource(R.string.cancel), 15)
        }
    })
}