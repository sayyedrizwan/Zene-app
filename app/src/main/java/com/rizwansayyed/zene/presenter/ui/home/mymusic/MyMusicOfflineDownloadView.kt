package com.rizwansayyed.zene.presenter.ui.home.mymusic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.offlinedownload.OfflineDownloadedEntity
import com.rizwansayyed.zene.data.db.offlinedownload.asMusicDataList
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.home.online.GlobalTrendingPagerItems
import com.rizwansayyed.zene.service.player.utils.Utils
import com.rizwansayyed.zene.service.player.utils.Utils.addAllPlayer
import com.rizwansayyed.zene.viewmodel.MyMusicViewModel

@Composable
fun MyMusicOfflineDownload(myMusic: MyMusicViewModel) {
    val width = LocalConfiguration.current.screenWidthDp.dp - 40.dp
    val offlineDownloadList by myMusic.offlineSongsLists.collectAsState(emptyList())
    Column(Modifier, Arrangement.Center) {
        Column(Modifier.padding(horizontal = 9.dp)) {
            TopInfoWithSeeMore(R.string.offline_downloaded, null, 50) {

            }
        }

        if (offlineDownloadList.isEmpty())
            Box(Modifier.padding(horizontal = 9.dp, vertical = 20.dp)) {
                TextThin(
                    stringResource(id = R.string.no_offline_downloaded_song_available),
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    doCenter = true
                )
            }
        else
            LazyRow(Modifier.fillMaxWidth()) {
                itemsIndexed(offlineDownloadList) { i, m ->
                    Box(Modifier.width(width)) {
                        GlobalTrendingPagerItems(m.asMusicData(), true) {
                            addAllPlayer(offlineDownloadList.asMusicDataList().toTypedArray(), i)
                        }

                        if (m.progress < 100)
                            CircularProgressIndicator(
                                m.progress.toFloat() / 100,
                                Modifier
                                    .padding(10.dp)
                                    .size(15.dp)
                                    .align(Alignment.TopEnd),
                                MainColor, 2.dp, Color.White,
                            )
                    }
                }
            }

    }
}