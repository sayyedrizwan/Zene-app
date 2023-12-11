package com.rizwansayyed.zene.presenter.ui.home.mymusic

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.offlinedownload.OfflineDownloadedEntity
import com.rizwansayyed.zene.data.db.offlinedownload.asMusicDataList
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.theme.Purple80
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.home.online.GlobalTrendingPagerItems
import com.rizwansayyed.zene.presenter.ui.musicplayer.view.ForceOfflineDialog
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.service.player.utils.Utils.addAllPlayer
import com.rizwansayyed.zene.service.workmanager.OfflineDownloadManager.Companion.startOfflineDownloadWorkManager
import com.rizwansayyed.zene.utils.Utils.OFFSET_LIMIT
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.MyMusicViewModel

@Composable
fun MyMusicOfflineDownload(myMusic: MyMusicViewModel) {
    val offlineDownloadList by myMusic.offlineSongsLists.collectAsState(emptyList())
    var page by remember { mutableIntStateOf(0) }

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
                    OfflineMusicDownloadItems(m) {
                        addAllPlayer(offlineDownloadList.asMusicDataList().toTypedArray(), i)
                    }
                }
                itemsIndexed(myMusic.offlineSongsLoadList) { i, m ->
                    OfflineMusicDownloadItems(m) {
                        addAllPlayer(offlineDownloadList.asMusicDataList().toTypedArray(), i)
                    }
                }

                if (offlineDownloadList.size >= OFFSET_LIMIT && myMusic.offlineSongsLoadMore) item {
                    LoadMoreCircleButtonForHistory {
                        page += 1
                        myMusic.offlineDownloadSongs(page * 50)
                    }
                }
            }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OfflineMusicDownloadItems(m: OfflineDownloadedEntity, click: () -> Unit) {
    val homeNavViewModel: HomeNavViewModel = hiltViewModel()
    val width = (LocalConfiguration.current.screenWidthDp / 1.5).dp
    val downloading = stringResource(id = R.string.downloading_please_wait)
    var forceDownloadDialog by remember { mutableStateOf(false) }

    Column(
        Modifier
            .width(width)
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MainColor)
            .combinedClickable(
                onLongClick = { homeNavViewModel.setSongDetailsDialog(m.asMusicData()) },
                onClick = {
                    if (m.progress < 100)
                        downloading.toast()
                    else
                        click()
                })
            .padding(5.dp), Arrangement.Center, Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(30.dp))

        AsyncImage(
            m.thumbnail, m.songName,
            Modifier
                .size(width)
                .padding(20.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        TextSemiBold(m.songName, Modifier.fillMaxWidth(), true, singleLine = true)
        Spacer(Modifier.height(5.dp))
        TextThin(m.songArtists, Modifier.fillMaxWidth(), true, singleLine = true)

        if (m.progress == -1) Box(
            Modifier
                .padding(10.dp)
                .size(35.dp)
        ) {
            Spacer(Modifier.height(9.dp))

            FilledTonalButton({
                forceDownloadDialog = true
            }, colors = ButtonDefaults.buttonColors(Color.Black)) {
                TextRegular(stringResource(id = R.string.waiting_for_wifi))
            }
        } else if (m.progress < 100) Box(
            Modifier
                .padding(10.dp)
                .size(35.dp)
        ) {
            CircularProgressIndicator(
                m.progress.toFloat() / 100,
                Modifier
                    .align(Alignment.Center)
                    .fillMaxSize(),
                Purple80, 2.dp, Color.White,
            )

            TextRegular("${m.progress}%", Modifier.align(Alignment.Center), size = 13)
        } else {
            Spacer(Modifier.height(9.dp))

            FilledTonalButton({
                homeNavViewModel.setSongDetailsDialog(m.asMusicData())
            }, colors = ButtonDefaults.buttonColors(Color.Black)) {
                TextRegular(stringResource(id = R.string.menu))
            }
        }

        Spacer(Modifier.height(30.dp))
    }


    if (forceDownloadDialog) ForceOfflineDialog({
        startOfflineDownloadWorkManager(m.songId, forceDownloadDialog)
        forceDownloadDialog = false
    }, {
        forceDownloadDialog = false
    })
}