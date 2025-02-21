package com.rizwansayyed.zene.ui.view

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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.jakewharton.processphoenix.ProcessPhoenix
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.data.model.ZeneMusicDataList
import com.rizwansayyed.zene.datastore.DataStorageManager.musicPlayerDB
import com.rizwansayyed.zene.datastore.model.MusicPlayerData
import com.rizwansayyed.zene.ui.theme.BlackGray
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.PlaylistsType.ALBUMS
import com.rizwansayyed.zene.ui.view.PlaylistsType.PLAYLIST
import com.rizwansayyed.zene.ui.view.PlaylistsType.PODCAST
import com.rizwansayyed.zene.utils.MainUtils.formatDurationsForVideo
import com.rizwansayyed.zene.utils.MediaContentUtils.startMedia
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

enum class PlaylistsType {
    PODCAST, PLAYLIST, ALBUMS
}

@Composable
fun PlaylistView(id: String, type: PlaylistsType) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val context = LocalContext.current.applicationContext
    val playerInfo by musicPlayerDB.collectAsState(null)

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Spacer(Modifier.height(70.dp)) }
        when (val v = homeViewModel.podcastData) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> item { CircularLoadingView() }
            is ResponseResult.Success -> {
                item { PlaylistTopView(v.data, type) }

                when (val list = homeViewModel.podcastList) {
                    ResponseResult.Empty -> {}
                    is ResponseResult.Error -> {}
                    ResponseResult.Loading -> {}
                    is ResponseResult.Success -> {
                        item { PlaylistsItemView(list.data) }
                        item { Spacer(Modifier.height(20.dp)) }
                        items(list.data) {
                            when (type) {
                                PODCAST -> PodcastItemView(it, playerInfo, list.data)
                                PLAYLIST, ALBUMS -> {}
                            }
                        }
                    }
                }
            }

        }

        item { Spacer(Modifier.height(300.dp)) }
    }

    LaunchedEffect(Unit) {
        homeViewModel.podcastData(id) {
            CoroutineScope(Dispatchers.IO).launch {
                delay(5.seconds)
                ProcessPhoenix.triggerRebirth(context)
            }
        }
        homeViewModel.podcastDataList(id)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PodcastItemView(data: ZeneMusicData, info: MusicPlayerData?, list: ZeneMusicDataList) {
    Row(
        Modifier
            .padding(top = 15.dp)
            .padding(horizontal = 5.dp, vertical = 10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(13.dp))
            .background(BlackGray)
            .clickable { startMedia(data, list) }
            .padding(horizontal = 15.dp, vertical = 25.dp),
        Arrangement.Center, Alignment.CenterVertically
    ) {
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

        if (info?.data?.id == data.id)
            GlideImage(
                R.raw.song_playing_wave,
                "",
                Modifier.size(24.dp),
                contentScale = ContentScale.Crop
            )
        else
            ImageIcon(R.drawable.ic_play, 25)
    }
}

@Composable
fun PlaylistsItemView(data: ZeneMusicDataList) {
    Row(
        Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically
    ) {
        Box(Modifier
            .padding(horizontal = 6.dp)
            .clickable { }) {
            ImageIcon(R.drawable.ic_download, 27)
        }
        Box(Modifier
            .padding(horizontal = 7.dp)
            .clickable { }) {
            ImageIcon(R.drawable.ic_layer_add, 24)
        }
        Box(Modifier
            .padding(horizontal = 7.dp)
            .clickable { }) {
            ImageIcon(R.drawable.ic_share, 24)
        }
        Spacer(Modifier.weight(1f))

        if (data.isNotEmpty()) MiniWithImageAndBorder(
            R.drawable.ic_play, R.string.play, MainColor
        ) {
            startMedia(data.first(), data)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PlaylistTopView(v: ZeneMusicData, type: PlaylistsType) {
    val width = (LocalConfiguration.current.screenWidthDp / 1.5).dp
    var fullDesc by remember { mutableStateOf(false) }

    GlideImage(
        v.thumbnail,
        v.name,
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
        PLAYLIST -> TextViewSemiBold(stringResource(R.string.playlist), 17, center = true)
        ALBUMS -> TextViewSemiBold(stringResource(R.string.album), 17, center = true)
    }

    Spacer(Modifier.height(15.dp))
    TextViewNormal(
        v.artists ?: "", 14, center = true, line = if (fullDesc) 1000 else 3
    )
    Spacer(Modifier.height(5.dp))

    Box(Modifier
        .rotate(if (fullDesc) 180f else 0f)
        .clickable { fullDesc = !fullDesc }) {
        ImageIcon(R.drawable.ic_arrow_down, 28)
    }

    Spacer(Modifier.height(30.dp))
}