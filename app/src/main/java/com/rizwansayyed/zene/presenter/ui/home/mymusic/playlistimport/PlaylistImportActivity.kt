package com.rizwansayyed.zene.presenter.ui.home.mymusic.playlistimport

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.theme.ZeneTheme
import com.rizwansayyed.zene.presenter.ui.LoadingStateBar
import com.rizwansayyed.zene.presenter.ui.home.mymusic.playlistimport.PlaylistImportersType.*
import com.rizwansayyed.zene.presenter.ui.home.mymusic.playlistimport.view.ImportPlaylistView
import com.rizwansayyed.zene.presenter.ui.home.mymusic.playlistimport.view.PlaylistListView
import com.rizwansayyed.zene.presenter.ui.home.mymusic.playlistimport.view.PlaylistTrackList
import com.rizwansayyed.zene.presenter.ui.musicplayer.MusicDialogSheet
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.presenter.util.UiUtils.transparentStatusAndNavigation
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.PlaylistImportViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaylistImportActivity : ComponentActivity() {

    private val playlistImportViewModel: PlaylistImportViewModel by viewModels()
    private val homeNavViewModel: HomeNavViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        transparentStatusAndNavigation()
        super.onCreate(savedInstanceState)

        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(Intent.EXTRA_TEXT, PlaylistImportersType::class.java)
        } else {
            intent.getSerializableExtra(Intent.EXTRA_TEXT) as PlaylistImportersType
        } ?: return

        setContent {
            ZeneTheme {
                var offset by remember { mutableIntStateOf(140) }
                val noSongFound = stringResource(R.string.no_song_found_please_check_internet)

                Box(Modifier.fillMaxSize()) {
                    LazyColumn(
                        Modifier
                            .fillMaxSize()
                            .background(DarkGreyColor)
                    ) {
                        item {
                            ImportPlaylistView(playlistImportViewModel)
                        }

                        if (playlistImportViewModel.playlistTrackers.size > 0)
                            itemsIndexed(playlistImportViewModel.playlistTrackers) { i, m ->
                                PlaylistTrackList(m, i) {
                                    val s = "${m.songName} - ${m.artistsName?.substringBefore(",")}"
                                    playlistImportViewModel.searchSongForPlaylist(s, it)
                                }
                            }

                        item {
                            Spacer(Modifier.height(300.dp))
                        }
                    }

                    Column(
                        Modifier
                            .align(Alignment.BottomCenter)
                            .animateContentSize()
                            .offset(y = offset.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                            .background(MainColor), Arrangement.Center, Alignment.CenterHorizontally
                    ) {
                        PlaylistListView(playlistImportViewModel, offset) {
                            offset = if (offset == 0) 140 else 0
                        }
                    }

                    LaunchedEffect(Unit) {
                        when (type) {
                            SPOTIFY -> playlistImportViewModel.spotifyPlaylistInfo()
                            YOUTUBE_MUSIC -> {}
                            APPLE_MUSIC -> {}
                        }
                    }
                }

                when (val v = playlistImportViewModel.songMenu) {
                    DataResponse.Empty -> {}
                    is DataResponse.Error -> noSongFound.toast()
                    DataResponse.Loading -> FullScreenLoadingBar()
                    is DataResponse.Success -> {
                        homeNavViewModel.setSongDetailsDialog(v.item)
                    }
                }


                AnimatedVisibility(homeNavViewModel.songDetailDialog != null) {
                    MusicDialogSheet(homeNavViewModel) {
                        homeNavViewModel.setSongDetailsDialog(null)
                        playlistImportViewModel.clear()
                    }
                }

            }
        }
    }

    @Composable
    fun FullScreenLoadingBar() {
        Box(
            Modifier
                .fillMaxSize()
                .clickable { }
                .background(Color.Black.copy(0.4f))
        ) {
            Column(Modifier.align(Alignment.Center)) {
                LoadingStateBar()
            }
        }
    }
}
