package com.rizwansayyed.zene.presenter.ui.dialog

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.LoadingStateBar
import com.rizwansayyed.zene.presenter.ui.RoundBorderButtonsView
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.service.player.utils.Utils
import com.rizwansayyed.zene.service.player.utils.Utils.addAllPlayer
import com.rizwansayyed.zene.service.player.utils.Utils.playRadioOnPlayer
import com.rizwansayyed.zene.service.songparty.SongPartyService
import com.rizwansayyed.zene.service.songparty.Utils.Action.partyRoomId
import com.rizwansayyed.zene.utils.EncodeDecodeGlobal
import com.rizwansayyed.zene.utils.EncodeDecodeGlobal.simpleDecode
import com.rizwansayyed.zene.utils.Utils.AppUrl.PARTY_URL_DIFFERENTIATE
import com.rizwansayyed.zene.utils.Utils.AppUrl.RADIO_URL_DIFFERENTIATE
import com.rizwansayyed.zene.utils.Utils.AppUrl.SONG_URL_DIFFERENTIATE
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun IntentsDialogView() {
    val context = LocalContext.current.applicationContext
    val coroutine = rememberCoroutineScope()
    val navViewModel: HomeNavViewModel = hiltViewModel()


    if (navViewModel.songPartyDialog.value?.trim()?.contains(PARTY_URL_DIFFERENTIATE) == true) {
        val body = if (partyRoomId == null) stringResource(R.string.join_a_new_party_new_desc)
        else stringResource(R.string.join_a_new_party_old_desc)

        var isLoading by remember { mutableStateOf(false) }

        val link =
            navViewModel.songPartyDialog.value?.substringAfter(PARTY_URL_DIFFERENTIATE)!!.trim()

        SimpleTextDialog(
            stringResource(R.string.join_a_new_party), body,
            stringResource(if (isLoading) R.string.joining__ else R.string.join), {
                if (isLoading) return@SimpleTextDialog

                Intent(context, SongPartyService::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra(Intent.EXTRA_TEXT, link)
                    context.startService(this)
                }
                isLoading = true
                coroutine.launch {
                    delay(3.seconds)
                    navViewModel.setSongPartyDialog(null)
                }
            }, { navViewModel.setSongPartyDialog(null) })
    }


    if (navViewModel.songShareDialog.value?.trim()?.contains(SONG_URL_DIFFERENTIATE) == true) {
        SongShareDialog(
            navViewModel.songShareDialog.value?.trim()?.substringAfter(SONG_URL_DIFFERENTIATE)
        )
    }


    if (navViewModel.radioShareDialog.value?.trim()?.contains(RADIO_URL_DIFFERENTIATE) == true) {
        RadioShareDialog(
            navViewModel.radioShareDialog.value?.trim()?.substringAfter(RADIO_URL_DIFFERENTIATE)
        )
    }
}


@Composable
fun SongShareDialog(songIdEncrypt: String?) {
    val navViewModel: HomeNavViewModel = hiltViewModel()
    val homeApiViewModel: HomeApiViewModel = hiltViewModel()

    val noSongFound = stringResource(R.string.no_song_found)

    Dialog(
        { navViewModel.setSongShareDialog(null) },
        DialogProperties(false, dismissOnClickOutside = false)
    ) {
        Surface(Modifier.fillMaxWidth(), RoundedCornerShape(16.dp), MainColor) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                when (val v = homeApiViewModel.ytMusicSearch) {
                    DataResponse.Empty -> {}
                    is DataResponse.Error -> LaunchedEffect(Unit) {
                        navViewModel.setSongShareDialog(null)
                        noSongFound.toast()
                    }

                    DataResponse.Loading -> Box(Modifier.align(Alignment.Center)) {
                        LoadingStateBar()
                    }

                    is DataResponse.Success -> Column(
                        Modifier
                            .padding(vertical = 60.dp)
                            .align(Alignment.Center)
                            .fillMaxWidth(),
                        Arrangement.Center,
                        Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            v.item.thumbnail, v.item.name,
                            Modifier
                                .size(150.dp)
                                .clip(RoundedCornerShape(100))
                        )
                        Spacer(Modifier.height(15.dp))

                        TextSemiBold(v.item.name ?: "", Modifier.fillMaxWidth(), true)
                        Spacer(Modifier.height(10.dp))
                        TextThin(v.item.artists ?: "", Modifier.fillMaxWidth(), true)

                        Spacer(Modifier.height(30.dp))

                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly) {
                            RoundBorderButtonsView(stringResource(R.string.close)) {
                                navViewModel.setSongShareDialog(null)
                            }

                            RoundBorderButtonsView(stringResource(R.string.play_song)) {
                                navViewModel.setSongShareDialog(null)
                                addAllPlayer(listOf(v.item).toTypedArray(), 0)
                            }
                        }

                        if (v.item.pId == null) LaunchedEffect(Unit) {
                            navViewModel.setSongShareDialog(null)
                            noSongFound.toast()
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        songIdEncrypt?.let { homeApiViewModel.ytMusicSongDetails(simpleDecode(songIdEncrypt)) }
    }
}


@Composable
fun RadioShareDialog(radioIdEncrypt: String?) {
    val navViewModel: HomeNavViewModel = hiltViewModel()
    val homeApiViewModel: HomeApiViewModel = hiltViewModel()

    val noRadioFound = stringResource(R.string.no_radio_found)

    Dialog(
        { navViewModel.setRadioShareDialog(null) },
        DialogProperties(false, dismissOnClickOutside = false)
    ) {
        Surface(Modifier.fillMaxWidth(), RoundedCornerShape(16.dp), MainColor) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                when (val v = homeApiViewModel.radioMusicSearch) {
                    DataResponse.Empty -> {}
                    is DataResponse.Error -> LaunchedEffect(Unit) {
                        navViewModel.setRadioShareDialog(null)
                        noRadioFound.toast()
                    }

                    DataResponse.Loading -> Box(Modifier.align(Alignment.Center)) {
                        LoadingStateBar()
                    }

                    is DataResponse.Success -> Column(
                        Modifier
                            .padding(vertical = 60.dp)
                            .align(Alignment.Center)
                            .fillMaxWidth(),
                        Arrangement.Center,
                        Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            v.item?.favicon?.ifEmpty { "https://cdn-icons-png.flaticon.com/512/7999/7999266.png" },
                            v.item?.name,
                            Modifier
                                .size(150.dp)
                                .clip(RoundedCornerShape(100))
                        )
                        Spacer(Modifier.height(15.dp))

                        TextSemiBold(v.item?.name ?: "", Modifier.fillMaxWidth(), true)
                        Spacer(Modifier.height(10.dp))
                        TextThin(stringResource(R.string.radio), Modifier.fillMaxWidth(), true)

                        Spacer(Modifier.height(30.dp))

                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly) {
                            RoundBorderButtonsView(stringResource(R.string.close)) {
                                navViewModel.setRadioShareDialog(null)
                            }

                            RoundBorderButtonsView(stringResource(R.string.play_radio)) {
                                navViewModel.setRadioShareDialog(null)
                                v.item?.let { playRadioOnPlayer(it) }
                            }
                        }

                        if (v.item?.stationuuid == null) LaunchedEffect(Unit) {
                            navViewModel.setRadioShareDialog(null)
                            noRadioFound.toast()
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        radioIdEncrypt?.let { homeApiViewModel.radioDetails(simpleDecode(radioIdEncrypt)) }
    }
}