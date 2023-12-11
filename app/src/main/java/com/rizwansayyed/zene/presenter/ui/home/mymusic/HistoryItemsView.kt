package com.rizwansayyed.zene.presenter.ui.home.mymusic

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.recentplay.asMusicDataList
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.home.online.SongYouMayLikeItems
import com.rizwansayyed.zene.presenter.ui.home.online.SongsYouMayLikeList
import com.rizwansayyed.zene.utils.Utils.OFFSET_LIMIT
import com.rizwansayyed.zene.viewmodel.MyMusicViewModel

@Composable
fun HistoryItemsList(myMusic: MyMusicViewModel) {
    val historyList by myMusic.recentSongPlayed.collectAsState(emptyList())
    var page by remember { mutableIntStateOf(0) }

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
                itemsIndexed(myMusic.recentSongPlayedLoadList.asMusicDataList()) { i, item ->
                    SongYouMayLikeItems(i, item, myMusic.recentSongPlayedLoadList.asMusicDataList())
                }

                if (historyList.size >= OFFSET_LIMIT && myMusic.recentSongPlayedLoadMore) item {
                    LoadMoreCircleButtonForHistory {
                        page += 1
                        myMusic.recentPlayedLoadMore(page * OFFSET_LIMIT)
                    }
                }
            }
        }
    }
}

@Composable
fun LoadMoreCircleButtonForHistory(click: () -> Unit) {
    val width = LocalConfiguration.current.screenWidthDp

    Column(
        Modifier
            .height((width / 3 + 25).dp)
            .padding(horizontal = 25.dp)
            .clickable {
                click()
            },
        Arrangement.Center, Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(width.dp / 5))

        Box(
            Modifier
                .rotate(180f)
                .clip(RoundedCornerShape(100))
                .background(Color.White)
        ) {
            Image(
                painterResource(R.drawable.ic_arrow_left), "",
                Modifier
                    .padding(10.dp)
                    .size(25.dp),
                colorFilter = ColorFilter.tint(Color.Black)
            )
        }

        TextSemiBold(
            v = stringResource(R.string.load_more),
            Modifier.padding(top = 14.dp), size = 14
        )
    }
}


