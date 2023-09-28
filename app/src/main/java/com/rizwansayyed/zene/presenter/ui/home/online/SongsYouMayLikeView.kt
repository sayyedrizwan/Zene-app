package com.rizwansayyed.zene.presenter.ui.home.online

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel

@Composable
fun SongsYouMayLike() {
    val roomDb: RoomDbViewModel = hiltViewModel()
    val recentSongs by roomDb.recentSongPlayed.collectAsState(initial = emptyList())
    roomDb.songsYouMayLike

    if (recentSongs.isEmpty())
        SelectArtistsView()
    else
        SongForUsersView()
}


@Composable
fun SelectArtistsView() {

}

@Composable
fun SongForUsersView() {
    
}