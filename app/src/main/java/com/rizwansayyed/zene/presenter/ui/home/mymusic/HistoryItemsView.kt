package com.rizwansayyed.zene.presenter.ui.home.mymusic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.impl.RoomDBImpl
import com.rizwansayyed.zene.data.db.recentplay.asMusicDataList
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.home.online.SongsYouMayLikeList
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel

@Composable
fun HistoryItemsList() {
    val roomDb: RoomDbViewModel = hiltViewModel()
    val historyList by roomDb.recentSongPlayed.collectAsState(emptyList())

    Column(Modifier, Arrangement.Center) {
        Column(Modifier.padding(horizontal = 9.dp)) {
            TopInfoWithSeeMore(
                R.string.history, if (historyList.size > 45) R.string.view_all else null, 50
            ) {

            }
        }

        if (historyList.isEmpty()) Box(Modifier.padding(horizontal = 9.dp, vertical = 20.dp)) {
            TextThin(stringResource(id = R.string.no_song_in_your_history), doCenter = true)
        } else
            SongsYouMayLikeList(listOf(historyList.asMusicDataList()))

    }
}


