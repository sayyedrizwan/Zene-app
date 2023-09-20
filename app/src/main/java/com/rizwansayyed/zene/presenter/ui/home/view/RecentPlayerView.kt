package com.rizwansayyed.zene.presenter.ui.home.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.recentplay.RecentPlayedEntity
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel

@Composable
fun RecentPlayList(value: MutableState<List<RecentPlayedEntity>?>) {
    val roomViewModel: RoomDbViewModel = hiltViewModel()


    if (roomViewModel.recentSongPlayed != null) Column(verticalArrangement = Arrangement.Center) {
        Spacer(Modifier.height(80.dp))

        if ((roomViewModel.recentSongPlayed?.size ?: 0) > 6)
            TopInfoWithSeeMore(stringResource(id = R.string.recent_played))
    }

    LaunchedEffect(Unit) {
        roomViewModel.recentSixPlayedSongs()
    }

    LaunchedEffect(roomViewModel.recentSongPlayed) {
        value.value = roomViewModel.recentSongPlayed
    }
}