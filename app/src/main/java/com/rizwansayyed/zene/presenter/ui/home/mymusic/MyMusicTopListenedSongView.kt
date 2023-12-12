package com.rizwansayyed.zene.presenter.ui.home.mymusic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.recentplay.asMusicDataList
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.home.online.SongYouMayLikeItems
import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.viewmodel.MyMusicViewModel

@Composable
fun TopListenedSong(myMusic: MyMusicViewModel) {
    if (myMusic.isTopListenSongThisWeekList.isNotEmpty()) Column(Modifier, Arrangement.Center) {
        Column {
            Column(Modifier.padding(horizontal = 9.dp)) {
                TopInfoWithSeeMore(
                    if (myMusic.isTopListenSongThisWeek) R.string.most_listened_songs_this_week else R.string.most_listened_songs_this_month,
                    null, 50
                ) {}
            }

            LazyRow(Modifier.fillMaxWidth()) {
                itemsIndexed(myMusic.isTopListenSongThisWeekList.asMusicDataList()) { i, item ->
                    SongYouMayLikeItems(
                        i, item, myMusic.isTopListenSongThisWeekList.asMusicDataList()
                    )
                }
            }
        }
    }
}