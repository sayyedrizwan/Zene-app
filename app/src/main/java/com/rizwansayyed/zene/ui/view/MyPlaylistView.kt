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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.MusicDataTypes
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.datastore.DataStorageManager.musicPlayerDB
import com.rizwansayyed.zene.datastore.model.MusicPlayerData
import com.rizwansayyed.zene.ui.theme.BlackGray
import com.rizwansayyed.zene.utils.URLSUtils.LIKED_SONGS_ON_ZENE
import com.rizwansayyed.zene.utils.share.MediaContentUtils.startMedia
import com.rizwansayyed.zene.viewmodel.MyLibraryViewModel

@Composable
fun MyPlaylistView(id: String) {
    val myLibraryViewModel: MyLibraryViewModel = hiltViewModel()
    val playerInfo by musicPlayerDB.collectAsState(null)

    val state = rememberLazyListState()
    var isBottomTriggered by remember { mutableStateOf(false) }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 5.dp),
        state,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Spacer(Modifier.height(100.dp)) }

        if (id.contains(LIKED_SONGS_ON_ZENE)) {
            item { TopLikedView() }
        }

        itemsIndexed(myLibraryViewModel.myPlaylistSongsList) { i, v ->
            MyPlaylistItemView(v, playerInfo, myLibraryViewModel.myPlaylistSongsList) { status ->
                if (status) myLibraryViewModel.removeMyPlaylistItems(id, v, i)
            }
        }

        if (!myLibraryViewModel.myPlaylistSongsIsLoading && myLibraryViewModel.myPlaylistSongsList.isEmpty()) item {
            Spacer(Modifier.height(60.dp))
            TextViewNormal(stringResource(R.string.no_items_in_your_playlists), 15, center = true)
        }

        item {
            if (myLibraryViewModel.myPlaylistSongsIsLoading) CircularLoadingView()
        }

        item { Spacer(Modifier.height(300.dp)) }
    }

    LaunchedEffect(Unit) { myLibraryViewModel.myPlaylistSongsData(id) }


    LaunchedEffect(state) {
        snapshotFlow { state.layoutInfo }.collect { layoutInfo ->
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItemsCount = layoutInfo.totalItemsCount

            if (lastVisibleItemIndex >= totalItemsCount - 1 && !isBottomTriggered) {
                isBottomTriggered = true
                myLibraryViewModel.myPlaylistSongsData(id)
            } else if (lastVisibleItemIndex < totalItemsCount - 1) {
                isBottomTriggered = false
            }
        }
    }
}

@Composable
fun TopLikedView() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp)
            .padding(bottom = 40.dp)
    ) {
        TextViewBoldBig(stringResource(R.string.liked_songs), 55)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MyPlaylistItemView(
    data: ZeneMusicData,
    info: MusicPlayerData?,
    playlistSongs: SnapshotStateList<ZeneMusicData>,
    remove: (Boolean) -> Unit
) {
    var confirmationSheet by remember { mutableStateOf(false) }

    Box(Modifier
        .padding(top = 15.dp)
        .padding(horizontal = 5.dp, vertical = 10.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(13.dp))
        .background(BlackGray)
        .clickable {
            val list = playlistSongs.filter { it.type() != MusicDataTypes.VIDEOS }
            startMedia(data, list)
        }
        .padding(horizontal = 10.dp, vertical = 15.dp), Alignment.Center) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp, vertical = 10.dp),
            Arrangement.Center,
            Alignment.CenterVertically
        ) {

            Box {
                GlideImage(
                    data.thumbnail,
                    data.name,
                    Modifier
                        .padding(end = 10.dp)
                        .size(90.dp),
                    contentScale = ContentScale.Crop
                )

                if (info?.data?.id == data.id) GlideImage(
                    R.raw.song_playing_wave,
                    "",
                    Modifier
                        .size(30.dp)
                        .align(Alignment.Center),
                    contentScale = ContentScale.Crop
                )

                Box(
                    Modifier
                        .align(Alignment.BottomStart)
                        .padding(2.dp)
                        .clip(RoundedCornerShape(100))
                        .background(Color.Black)
                        .padding(5.dp)
                ) {
                    if (data.type() == MusicDataTypes.SONGS) {
                        ImageIcon(R.drawable.ic_music_note, 20)
                    } else if (data.type() == MusicDataTypes.AI_MUSIC) {
                        ImageIcon(R.drawable.ic_robot_singing, 20)
                    } else if (data.type() == MusicDataTypes.PODCAST_AUDIO) {
                        ImageIcon(R.drawable.ic_podcast, 20)
                    } else if (data.type() == MusicDataTypes.RADIO) {
                        ImageIcon(R.drawable.ic_radio, 20)
                    } else if (data.type() == MusicDataTypes.VIDEOS) {
                        ImageIcon(R.drawable.ic_video_replay, 20)
                    }
                }
            }

            Column(Modifier.weight(1f), Arrangement.Center, Alignment.Start) {
                TextViewSemiBold(data.name ?: "", 15, line = 2)
                if ((data.artists?.trim()?.length ?: 0) > 3) {
                    TextViewNormal(data.artists!!, 13, line = 1)
                }

//                Spacer(Modifier.height(14.dp))

//                Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly, Alignment.CenterVertically) {
//                    if (info?.data?.id == data.id) GlideImage(
//                        R.raw.song_playing_wave,
//                        "",
//                        Modifier.size(24.dp),
//                        contentScale = ContentScale.Crop
//                    )
//
//                    Box(Modifier.clickable { confirmationSheet = true }) {
//                        ImageIcon(R.drawable.ic_delete, 20)
//                    }
//
//                    if (data.type() == MusicDataTypes.SONGS) {
//                        ImageIcon(R.drawable.ic_music_note, 20)
//                    } else if (data.type() == MusicDataTypes.AI_MUSIC) {
//                        ImageIcon(R.drawable.ic_robot_singing, 20)
//                    } else if (data.type() == MusicDataTypes.PODCAST_AUDIO) {
//                        ImageIcon(R.drawable.ic_podcast, 20)
//                    } else if (data.type() == MusicDataTypes.RADIO) {
//                        ImageIcon(R.drawable.ic_radio, 20)
//                    } else if (data.type() == MusicDataTypes.VIDEOS) {
//                        ImageIcon(R.drawable.ic_video_replay, 20)
//                    }
//                }
            }
        }


        Box(Modifier
            .align(Alignment.TopEnd)
            .clickable { confirmationSheet = true }) {
            ImageIcon(R.drawable.ic_delete, 20)
        }
    }

    if (confirmationSheet) TextAlertDialog(R.string.remove_media_from_playlist,
        R.string.remove_media_from_playlist_desc,
        {
            confirmationSheet = false
            remove(false)
        },
        {
            confirmationSheet = false
            remove(true)
        })


}