package com.rizwansayyed.zene.ui.mymusic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.model.asMusicData
import com.rizwansayyed.zene.data.api.model.likedMusicData
import com.rizwansayyed.zene.data.roomdb.offlinesongs.model.asMusicData
import com.rizwansayyed.zene.ui.mymusic.playlists.AddPlaylistDialog
import com.rizwansayyed.zene.ui.mymusic.view.MyMusicWebCardView
import com.rizwansayyed.zene.ui.mymusic.view.TopHeaderSwitch
import com.rizwansayyed.zene.ui.mymusic.view.TopMusicHeaders
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.view.LoadingView
import com.rizwansayyed.zene.ui.view.PlaylistsDynamicCards
import com.rizwansayyed.zene.ui.view.SmallButtonBorderText
import com.rizwansayyed.zene.ui.view.SongDynamicCards
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.isScreenBig
import com.rizwansayyed.zene.utils.FirebaseLogEvents
import com.rizwansayyed.zene.utils.FirebaseLogEvents.logEvents
import com.rizwansayyed.zene.utils.Utils.THREE_GRID_SIZE
import com.rizwansayyed.zene.utils.Utils.TOTAL_GRID_SIZE
import com.rizwansayyed.zene.utils.Utils.TWO_GRID_SIZE
import com.rizwansayyed.zene.viewmodel.ZeneViewModel

enum class MyMusicType {
    PLAYLISTS, HISTORY, OFFLINE_SONGS
}

@Composable
fun MyMusicView(viewModel: ZeneViewModel) {
    val isThreeGrid = isScreenBig()

    var type by remember { mutableStateOf(MyMusicType.PLAYLISTS) }
    var page by remember { mutableIntStateOf(0) }
    var addPlaylist by remember { mutableStateOf(false) }

    LazyVerticalGrid(
        GridCells.Fixed(TOTAL_GRID_SIZE),
        Modifier
            .fillMaxSize()
            .background(DarkCharcoal)
    ) {
        item(1, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Column {
                TopMusicHeaders()
            }
        }

        item(5003, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            MyMusicWebCardView()
        }

        item(2, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            TopHeaderSwitch(type, {
                type = it
            }) {
                addPlaylist = true
            }
        }

        item(3, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Spacer(Modifier.height(40.dp))
        }

        if (type == MyMusicType.PLAYLISTS) item(25,
            span = { GridItemSpan(if (isThreeGrid) THREE_GRID_SIZE else TWO_GRID_SIZE) }) {
            PlaylistsDynamicCards(likedMusicData())
        }

        if (viewModel.songHistory.isEmpty() && type == MyMusicType.HISTORY && !viewModel.songHistoryIsLoading) item(
            20,
            { GridItemSpan(TOTAL_GRID_SIZE) }) {
            TextPoppins(stringResource(R.string.you_have_no_song_history), true, size = 16)
        }
        else items(viewModel.songHistory,
            span = { GridItemSpan(if (isThreeGrid) THREE_GRID_SIZE else TWO_GRID_SIZE) }) {
            SongDynamicCards(it.asMusicData(), viewModel.songHistory.toTypedArray().asMusicData())
        }

        if (viewModel.offlineSongs.isEmpty() && type == MyMusicType.OFFLINE_SONGS && !viewModel.songHistoryIsLoading) item(
            20,
            { GridItemSpan(TOTAL_GRID_SIZE) }) {
            TextPoppins(stringResource(R.string.you_have_no_song_cached), true, size = 16)
        }
        else items(viewModel.offlineSongs,
            span = { GridItemSpan(if (isThreeGrid) THREE_GRID_SIZE else TWO_GRID_SIZE) }) {
            SongDynamicCards(it.asMusicData(), viewModel.offlineSongs.toTypedArray().asMusicData())
        }

        if (viewModel.zeneSavedPlaylists.isEmpty() && type == MyMusicType.PLAYLISTS && !viewModel.songHistoryIsLoading) item(
            21,
            { GridItemSpan(TOTAL_GRID_SIZE) }) {
            TextPoppins(
                stringResource(R.string.you_have_not_created_or_saved_a_playlists), true, size = 16
            )
        }
        else items(viewModel.zeneSavedPlaylists,
            span = { GridItemSpan(if (isThreeGrid) THREE_GRID_SIZE else TWO_GRID_SIZE) }) {
            PlaylistsDynamicCards(it)
        }

        item(4, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Spacer(Modifier.height(40.dp))
        }

        if (viewModel.songHistoryIsLoading) item(5, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            LoadingView(Modifier.size(32.dp))
        }

        item(6, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Spacer(Modifier.height(40.dp))
        }

        if (viewModel.doShowMoreLoading) item(7, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                SmallButtonBorderText(R.string.load_more) {
                    page += 1
                    when (type) {
                        MyMusicType.PLAYLISTS -> viewModel.playlists(page)
                        MyMusicType.OFFLINE_SONGS -> viewModel.offlineSongsLists(page)
                        else -> viewModel.songHistory(page)
                    }
                }
            }
        }


        item(1000, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Spacer(Modifier.height(70.dp))
        }

        item(1003, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Spacer(Modifier.height(210.dp))
        }
    }

    if (addPlaylist) AddPlaylistDialog(viewModel) {
        page = 0
        addPlaylist = false
        type = MyMusicType.PLAYLISTS
        viewModel.playlists(0)
    }


    LaunchedEffect(type) {
        page = 0
        when (type) {
            MyMusicType.PLAYLISTS -> {
                logEvents(FirebaseLogEvents.FirebaseEvents.MY_MUSIC_PERSONAL_PLAYLISTS)
                viewModel.playlists(0)
            }

            MyMusicType.OFFLINE_SONGS -> {
                logEvents(FirebaseLogEvents.FirebaseEvents.MY_MUSIC_OFFLINE_SONGS)
                viewModel.offlineSongsLists(0)
            }

            else -> {
                logEvents(FirebaseLogEvents.FirebaseEvents.MY_MUSIC_SONG_HISTORY)
                viewModel.songHistory(0)
            }
        }
    }

    DisposableEffect(Unit) {
        val listener = object : RemovedCacheSongs {
            override fun onRemoved(songID: String) {
                if (type == MyMusicType.OFFLINE_SONGS) viewModel.removeOfflineSongsLists(songID)
            }
        }

        GlobalRemovedCacheSongsProvider.registerListener(listener)
        onDispose {
            GlobalRemovedCacheSongsProvider.unregisterListener()
        }
    }
}