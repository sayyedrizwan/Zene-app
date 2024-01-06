package com.rizwansayyed.zene.presenter.ui.musicplayer

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.favouriteRadioList
import com.rizwansayyed.zene.domain.MusicPlayerList
import com.rizwansayyed.zene.domain.MusicType
import com.rizwansayyed.zene.domain.asMusicPlayerList
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.musicplayer.view.DeleteOfflineDialog
import com.rizwansayyed.zene.presenter.ui.musicplayer.view.MusicPlaylistDialog
import com.rizwansayyed.zene.service.player.utils.Utils.addAllPlayer
import com.rizwansayyed.zene.service.player.utils.Utils.addToEndPlayer
import com.rizwansayyed.zene.service.player.utils.Utils.playNextPlayer
import com.rizwansayyed.zene.service.player.utils.Utils.playRadioOnPlayer
import com.rizwansayyed.zene.service.workmanager.OfflineDownloadManager
import com.rizwansayyed.zene.utils.FirebaseEvents
import com.rizwansayyed.zene.utils.FirebaseEvents.registerEvent
import com.rizwansayyed.zene.utils.Utils.AppUrl.appUrlRadioShare
import com.rizwansayyed.zene.utils.Utils.AppUrl.appUrlSongShare
import com.rizwansayyed.zene.utils.Utils.shareTxt
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.PlayerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicDialogSheet(homeNavModel: HomeNavViewModel, close:() -> Unit) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        close, Modifier.fillMaxWidth(), sheetState,
        containerColor = MainColor, contentColor = BlackColor
    ) {
        Column(Modifier.fillMaxWidth()) {
            MusicDialogView(homeNavModel)
        }
    }
}

@Composable
fun MusicDialogView(homeNavModel: HomeNavViewModel) {
    val playerViewModel: PlayerViewModel = hiltViewModel()
    val offlineDownload by playerViewModel.offlineSongStatus.collectAsState(initial = null)

    val homeApiViewModel: HomeApiViewModel = hiltViewModel()

    var playlistDialog by remember { mutableStateOf<MusicPlayerList?>(null) }
    var rmDialog by remember { mutableStateOf(false) }

    val downloading = stringResource(R.string.downloading__)
    val addInQueue = stringResource(R.string.add_in_queue)
    val share = stringResource(R.string.share)
    val offlineDownloadString = stringResource(R.string.offline_download)
    val songInfo = stringResource(R.string.song_info)
    val makeFavourite = stringResource(R.string.mark_as_favourite)
    val removeFavourite = stringResource(R.string.rm_as_favourite)

    fun startDownloading() {
        homeNavModel.songDetailDialog?.let {
            playerViewModel.addOfflineSong(it)
            OfflineDownloadManager.startOfflineDownloadWorkManager(it.pId)
        }
    }


    TextAndImageSideBySide(
        homeNavModel.songDetailDialog?.name ?: "",
        homeNavModel.songDetailDialog?.artists ?: "",
        homeNavModel.songDetailDialog?.thumbnail ?: ""
    )

    Spacer(Modifier.height(30.dp))

    if (homeNavModel.songDetailDialog?.type == MusicType.RADIO) {
        val radioList by favouriteRadioList.collectAsState(runBlocking(Dispatchers.IO) { favouriteRadioList.first() })
        MusicDialogListItems(R.drawable.ic_play, stringResource(R.string.play)) {
            playRadioOnPlayer(homeNavModel.onlineRadioTemps!!)
            homeNavModel.setSongDetailsDialog(null)
        }

        MusicDialogListItems(R.drawable.ic_share, share) {
            homeNavModel.songDetailDialog?.pId?.let { shareTxt(appUrlRadioShare(it)) }
            homeNavModel.setSongDetailsDialog(null)
        }

        if (radioList?.any { it == homeNavModel.songDetailDialog?.pId } == true)
            MusicDialogListItems(R.drawable.ic_favourite_circle, removeFavourite) {
                val list = ArrayList<String>().apply { addAll(radioList!!) }
                list.remove(homeNavModel.songDetailDialog?.pId)
                favouriteRadioList = flowOf(list.toTypedArray())
                homeApiViewModel.favouriteRadios(true)
            }
        else
            MusicDialogListItems(R.drawable.ic_favourite, makeFavourite) {
                val list = ArrayList<String>().apply { addAll(radioList!!) }
                homeNavModel.songDetailDialog?.pId?.let { list.add(0, it) }
                favouriteRadioList = flowOf(list.toTypedArray())
                homeApiViewModel.favouriteRadios(true)
            }


        LaunchedEffect(Unit){
            registerEvent(FirebaseEvents.FirebaseEvent.RADIO_MENU_SHEET)
        }
    } else {
        MusicDialogListItems(R.drawable.ic_play, stringResource(R.string.play)) {
            addAllPlayer(listOf(homeNavModel.songDetailDialog).toTypedArray(), 0)
            homeNavModel.setSongDetailsDialog(null)
        }
        MusicDialogListItems(R.drawable.ic_play_next, stringResource(R.string.play_next)) {
            playNextPlayer(homeNavModel.songDetailDialog)
            homeNavModel.setSongDetailsDialog(null)
        }
        MusicDialogListItems(R.drawable.ic_play_in_queue, addInQueue) {
            addToEndPlayer(homeNavModel.songDetailDialog)
            homeNavModel.setSongDetailsDialog(null)
        }
        MusicDialogListItems(R.drawable.ic_share, share) {
            homeNavModel.songDetailDialog?.pId?.let { shareTxt(appUrlSongShare(it)) }
            homeNavModel.setSongDetailsDialog(null)
        }
        MusicDialogListItems(R.drawable.ic_playlist, stringResource(R.string.add_to_playlist)) {
            playlistDialog = homeNavModel.songDetailDialog?.asMusicPlayerList()
        }
        if (offlineDownload == null)
            MusicDialogListItems(R.drawable.ic_download, offlineDownloadString) {
                startDownloading()
            }
        else if (offlineDownload!!.progress < 100)
            MusicDialogListItems(
                R.drawable.ic_download, "$downloading (${offlineDownload!!.progress}%)"
            ) {
                startDownloading()
            }
        else if (offlineDownload!!.progress == 100)
            MusicDialogListItems(R.drawable.ic_tick, stringResource(R.string.offline_downloaded)) {
                rmDialog = true
            }

//        MusicDialogListItems(R.drawable.ic_information_circle, songInfo) {
//
//        }

        LaunchedEffect(Unit){
            registerEvent(FirebaseEvents.FirebaseEvent.SONG_MENU_SHEET)
        }

    }

    TextSemiBold(
        stringResource(R.string.close),
        Modifier
            .clickable {
                homeNavModel.setSongDetailsDialog(null)
            }
            .padding(vertical = 50.dp)
            .fillMaxWidth(),
        true,
        Color.LightGray,
        size = 17
    )

    Spacer(Modifier.height(130.dp))

    if (rmDialog) DeleteOfflineDialog({
        playerViewModel.rmDownloadSongs(homeNavModel.songDetailDialog?.pId ?: "")
        rmDialog = false
    }, {
        rmDialog = false
    })


    playlistDialog?.let { MusicPlaylistDialog(it) { playlistDialog = null } }

    LaunchedEffect(Unit) {
        homeNavModel.songDetailDialog?.pId?.let { playerViewModel.offlineSongDetails(it) }
    }
}


@Composable
fun MusicDialogListItems(icon: Int, title: String, click: () -> Unit) {
    Row(
        Modifier
            .clickable {
                click()
            }
            .padding(vertical = 14.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(icon), title,
            Modifier
                .padding(start = 19.dp, end = 6.dp)
                .size(23.dp),
            colorFilter = ColorFilter.tint(Color.LightGray)
        )

        TextRegular(
            title,
            Modifier
                .padding(start = 6.dp)
                .weight(1f), Color.LightGray, size = 17
        )
    }
}

@Composable
fun TextAndImageSideBySide(name: String, artists: String, img: String) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(
            Modifier
                .padding(10.dp)
                .weight(1f)
        ) {
            TextSemiBold(
                name, Modifier.fillMaxWidth(), size = 34
            )

            Spacer(Modifier.height(5.dp))

            TextThin(
                artists, Modifier.fillMaxWidth(), color = Color.Gray, size = 29, singleLine = true
            )
        }

        AsyncImage(
            img, name, Modifier
                .padding(end = 10.dp)
                .size(120.dp)
                .clip(RoundedCornerShape(10))
        )
    }
}