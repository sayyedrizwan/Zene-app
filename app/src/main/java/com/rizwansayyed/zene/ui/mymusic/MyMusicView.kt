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
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.model.likedMusicData
import com.rizwansayyed.zene.data.roomdb.offlinesongs.model.asMusicData
import com.rizwansayyed.zene.ui.mymusic.MyMusicType.HISTORY
import com.rizwansayyed.zene.ui.mymusic.MyMusicType.OFFLINE_SONGS
import com.rizwansayyed.zene.ui.mymusic.MyMusicType.PLAYLISTS
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
fun MyMusicView() {
    val isThreeGrid = isScreenBig()
    val viewModelPlaylists: ZeneViewModel = hiltViewModel()
    val viewModelOffline: ZeneViewModel = hiltViewModel()
    val viewModelHistory: ZeneViewModel = hiltViewModel()

    var type by remember { mutableStateOf(PLAYLISTS) }
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
                page = 0
                type = it
            }) {
                addPlaylist = true
            }
        }

        item(3, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Spacer(Modifier.height(40.dp))
        }

        when (type) {
            PLAYLISTS -> {
                item(
                    25,
                    span = { GridItemSpan(if (isThreeGrid) THREE_GRID_SIZE else TWO_GRID_SIZE) }) {
                    PlaylistsDynamicCards(likedMusicData())
                }

                if (viewModelPlaylists.zeneSavedPlaylists.isEmpty() && !viewModelPlaylists.dataIsLoading) item(
                    21,
                    { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    TextPoppins(
                        stringResource(R.string.you_have_not_created_or_saved_a_playlists),
                        true,
                        size = 16
                    )
                }
                else items(viewModelPlaylists.zeneSavedPlaylists,
                    span = { GridItemSpan(if (isThreeGrid) THREE_GRID_SIZE else TWO_GRID_SIZE) }) {
                    PlaylistsDynamicCards(it)
                }

                item(4, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    Spacer(Modifier.height(40.dp))
                }

                if (viewModelPlaylists.dataIsLoading) item(5, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    LoadingView(Modifier.size(32.dp))
                }

                item(6, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    Spacer(Modifier.height(40.dp))
                }

                if (viewModelPlaylists.doShowMoreLoading)
                    item(7, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                        LoadMoreView {
                            page += 1
                            viewModelPlaylists.playlists(page)
                        }
                    }
            }

            HISTORY -> {
                if (viewModelHistory.songHistory.isEmpty() && !viewModelHistory.dataIsLoading) item(
                    20,
                    { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    TextPoppins(stringResource(R.string.you_have_no_song_history), true, size = 16)
                }
                else items(viewModelHistory.songHistory,
                    span = { GridItemSpan(if (isThreeGrid) THREE_GRID_SIZE else TWO_GRID_SIZE) }) {
                    SongDynamicCards(it.asMusicData(), listOf(it.asMusicData()))
                }

                item(4, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    Spacer(Modifier.height(40.dp))
                }

                if (viewModelHistory.dataIsLoading) item(5, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    LoadingView(Modifier.size(32.dp))
                }

                if (viewModelHistory.doShowMoreLoading)
                    item(7, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                        LoadMoreView {
                            page += 1
                            viewModelHistory.songHistory(page)
                        }
                    }
            }

            OFFLINE_SONGS -> {
                if (viewModelOffline.offlineSongs.isEmpty() && !viewModelOffline.dataIsLoading) item(
                    20,
                    { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    TextPoppins(stringResource(R.string.you_have_no_song_cached), true, size = 16)
                }
                else items(viewModelOffline.offlineSongs,
                    span = { GridItemSpan(if (isThreeGrid) THREE_GRID_SIZE else TWO_GRID_SIZE) }) {
                    SongDynamicCards(
                        it.asMusicData(),
                        viewModelOffline.offlineSongs.toTypedArray().asMusicData()
                    )
                }

                item(4, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    Spacer(Modifier.height(40.dp))
                }

                if (viewModelOffline.dataIsLoading) item(5, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    LoadingView(Modifier.size(32.dp))
                }

                if (viewModelOffline.doShowMoreLoading)
                    item(7, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                        LoadMoreView {
                            page += 1
                            viewModelOffline.offlineSongsLists(page)
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

    if (addPlaylist) AddPlaylistDialog(viewModelPlaylists) {
        page = 0
        addPlaylist = false
        type = PLAYLISTS
        viewModelPlaylists.playlists(0)
    }


    LaunchedEffect(type) {
        page = 0
        when (type) {
            PLAYLISTS -> {
                logEvents(FirebaseLogEvents.FirebaseEvents.MY_MUSIC_PERSONAL_PLAYLISTS)
                viewModelPlaylists.playlists(0)
            }

            OFFLINE_SONGS -> {
                logEvents(FirebaseLogEvents.FirebaseEvents.MY_MUSIC_OFFLINE_SONGS)
                viewModelOffline.offlineSongsLists(0)
            }

            else -> {
                logEvents(FirebaseLogEvents.FirebaseEvents.MY_MUSIC_SONG_HISTORY)
                viewModelHistory.songHistory(0)
            }
        }
    }

    DisposableEffect(Unit) {
        val listener = object : RemovedCacheSongs {
            override fun onRemoved(songID: String) {
                if (type == OFFLINE_SONGS) viewModelOffline.removeOfflineSongsLists(songID)
            }
        }

        GlobalRemovedCacheSongsProvider.registerListener(listener)
        onDispose {
            GlobalRemovedCacheSongsProvider.unregisterListener()
        }
    }
}

@Composable
fun LoadMoreView(click: () -> Unit) {
    Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
        SmallButtonBorderText(R.string.load_more) {
           click()
        }
    }
}