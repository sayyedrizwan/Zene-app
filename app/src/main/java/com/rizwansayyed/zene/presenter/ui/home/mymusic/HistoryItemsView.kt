package com.rizwansayyed.zene.presenter.ui.home.mymusic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.recentplay.asMusicDataList
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.home.online.SongYouMayLikeItems
import com.rizwansayyed.zene.presenter.ui.home.online.SongsYouMayLikeList
import com.rizwansayyed.zene.viewmodel.MyMusicViewModel

@Composable
fun HistoryItemsList(myMusic: MyMusicViewModel) {
    val historyList by myMusic.recentSongPlayed.collectAsState(emptyList())

    Column(Modifier, Arrangement.Center) {
        Column(Modifier.padding(horizontal = 9.dp)) {
            TopInfoWithSeeMore(R.string.history, null, 50) {}
        }

        if (historyList.isEmpty()) Box(Modifier.padding(horizontal = 9.dp, vertical = 20.dp)) {
            TextThin(
                stringResource(id = R.string.no_song_in_your_history),
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                doCenter = true
            )
        } else {
            LazyRow(Modifier.fillMaxWidth()) {
                itemsIndexed(historyList.asMusicDataList()) { i, item ->
                    SongYouMayLikeItems(i, item, historyList.asMusicDataList())
                }

                if (historyList.size >= 50 && myMusic.recentSongPlayedLoadMore) item {
                    LoadMoreCircleButtonForHistory()
                }
            }
        }
    }
}

@Composable
fun LoadMoreCircleButtonForHistory() {
    Column {
        Box(Modifier.clip(RoundedCornerShape(100)).background(Color.White)) {
            SmallIcons(R.drawable.ic_arrow_left, 25, 10)
        }
    }
}


