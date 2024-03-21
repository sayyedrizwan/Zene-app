package com.rizwansayyed.zene.presenter.ui.home.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.userAuthData
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.ui.GlobalNativeFullAds
import com.rizwansayyed.zene.presenter.ui.home.mymusic.AppleMusicLoginDialog
import com.rizwansayyed.zene.presenter.ui.home.mymusic.FollowUs
import com.rizwansayyed.zene.presenter.ui.home.mymusic.HistoryItemsList
import com.rizwansayyed.zene.presenter.ui.home.mymusic.ImportPlaylistAppleMusic
import com.rizwansayyed.zene.presenter.ui.home.mymusic.ImportPlaylistSpotify
import com.rizwansayyed.zene.presenter.ui.home.mymusic.ImportPlaylistYoutubeMusic
import com.rizwansayyed.zene.presenter.ui.home.mymusic.LinkedToBrowser
import com.rizwansayyed.zene.presenter.ui.home.mymusic.MyMusicAlbumList
import com.rizwansayyed.zene.presenter.ui.home.mymusic.MyMusicDownloadView
import com.rizwansayyed.zene.presenter.ui.home.mymusic.MyMusicGroupMusicParty
import com.rizwansayyed.zene.presenter.ui.home.mymusic.MyMusicOfflineDownload
import com.rizwansayyed.zene.presenter.ui.home.mymusic.MyMusicPlaylistsList
import com.rizwansayyed.zene.presenter.ui.home.mymusic.SelectedPlaylistView
import com.rizwansayyed.zene.presenter.ui.home.mymusic.SpotifyLoginDialog
import com.rizwansayyed.zene.presenter.ui.home.mymusic.TopListenedSong
import com.rizwansayyed.zene.presenter.ui.home.mymusic.TopMyMusicHeader
import com.rizwansayyed.zene.presenter.ui.home.mymusic.YoutubeMusicLoginDialog
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.TOTAL_ITEMS_GRID
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.TWO_ITEMS_GRID
import com.rizwansayyed.zene.utils.FirebaseEvents
import com.rizwansayyed.zene.utils.FirebaseEvents.registerEvent
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.MyMusicViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


@Composable
fun MyMusicView() {
    val myMusic: MyMusicViewModel = hiltViewModel()
    val homeNavViewModel: HomeNavViewModel = hiltViewModel()

    var spotifyLoginDialog by remember { mutableStateOf(false) }
    var youtubeMusicLoginDialog by remember { mutableStateOf(false) }
    var appleMusicLoginDialog by remember { mutableStateOf(false) }

    val userAuth by userAuthData.collectAsState(initial = runBlocking(Dispatchers.IO) { userAuthData.first() })

    LazyVerticalGrid(
        GridCells.Fixed(TOTAL_ITEMS_GRID), Modifier
            .fillMaxSize()
            .background(DarkGreyColor)
    ) {
        item(key = 1, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column(Modifier.padding(horizontal = 9.dp), Arrangement.Center) {
                TopMyMusicHeader(userAuth)

                Spacer(Modifier.height(22.dp))
            }
        }

        item(key = 2, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            LinkedToBrowser(userAuth)
        }
        item(key = 3, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            HistoryItemsList(myMusic)
        }
        item(key = 4, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                MyMusicPlaylistsList(myMusic, homeNavViewModel)

                GlobalNativeFullAds()
            }
        }
        item(key = 5, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            MyMusicAlbumList(myMusic)
        }
        item(key = 6, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            MyMusicOfflineDownload(myMusic)
        }
        item(key = 7, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            TopListenedSong(myMusic)
        }
        item(key = 8, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Spacer(Modifier.height(50.dp))
        }
        item(key = 9, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            GlobalNativeFullAds()
        }
        item(key = 10, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Spacer(Modifier.height(50.dp))
        }
        item(key = 11, span = { GridItemSpan(TWO_ITEMS_GRID) }) {
            ImportPlaylistSpotify {
                registerEvent(FirebaseEvents.FirebaseEvent.OPEN_IMPORT_SPOTIFY)
                spotifyLoginDialog = true
            }
        }
        item(key = 12, span = { GridItemSpan(TWO_ITEMS_GRID) }) {
            ImportPlaylistYoutubeMusic {
                registerEvent(FirebaseEvents.FirebaseEvent.OPEN_IMPORT_YOUTUBE_MUSIC)
                youtubeMusicLoginDialog = true
            }
        }
        item(key = 13, span = { GridItemSpan(TWO_ITEMS_GRID) }) {
            ImportPlaylistAppleMusic {
                registerEvent(FirebaseEvents.FirebaseEvent.OPEN_IMPORT_APPLE_MUSIC)
                appleMusicLoginDialog = true
            }
        }
        item(key = 14, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            GlobalNativeFullAds()
        }

        item(key = 15, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            MyMusicGroupMusicParty()
        }

        item(key = 16, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            FollowUs()
        }

        item(key = 17, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            MyMusicDownloadView(homeNavViewModel)
        }

        item(key = 100, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Spacer(Modifier.height(200.dp))
        }
    }

    AnimatedVisibility(homeNavViewModel.selectMyMusicPlaylists.value != null) {
        SelectedPlaylistView()
    }

    if (spotifyLoginDialog) SpotifyLoginDialog {
        spotifyLoginDialog = false
    }

    if (youtubeMusicLoginDialog) YoutubeMusicLoginDialog {
        youtubeMusicLoginDialog = false
    }

    if (appleMusicLoginDialog) AppleMusicLoginDialog {
        appleMusicLoginDialog = false
    }

    LaunchedEffect(Unit) {
        myMusic.init()
        registerEvent(FirebaseEvents.FirebaseEvent.OPEN_MY_MUSIC)
        homeNavViewModel.getAppDownloadResponse()
    }
}