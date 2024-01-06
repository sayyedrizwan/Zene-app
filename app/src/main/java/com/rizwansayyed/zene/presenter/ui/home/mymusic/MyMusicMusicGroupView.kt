package com.rizwansayyed.zene.presenter.ui.home.mymusic

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.loginUser
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.LoadingStateBar
import com.rizwansayyed.zene.presenter.ui.RoundBorderButtonsView
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.dialog.SimpleTextDialog
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.service.songparty.SongPartyService
import com.rizwansayyed.zene.service.songparty.Utils.Action.partyRoomId
import com.rizwansayyed.zene.service.songparty.Utils.ActionFunctions.closeParty
import com.rizwansayyed.zene.utils.GoogleSignInUtils
import com.rizwansayyed.zene.utils.Utils.AppUrl.appPartyJoinUrl
import com.rizwansayyed.zene.utils.Utils.shareTxt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun MyMusicGroupMusicParty() {
    Column {
        Column(Modifier.padding(horizontal = 9.dp)) {
            TopInfoWithSeeMore(R.string.music_group_party, null, 50) {}
        }

        LazyRow(Modifier.fillMaxWidth()) {
            item {
                HostPartyButtonCard()
            }
        }
    }
}

@Composable
fun HostPartyButtonCard() {
    val context = LocalContext.current as Activity
    val coroutine = rememberCoroutineScope()
    val userInfo by loginUser.collectAsState(initial = null)

    val linkCopyInfo = stringResource(R.string.disconnected_from_party)

    val googleSignIn = GoogleSignInUtils(context)

    var showPartyDialog by remember { mutableStateOf(false) }
    var isLoginLoading by remember { mutableStateOf(false) }
    var isInParty by remember { mutableStateOf(false) }

    Column(
        Modifier
            .padding(start = 10.dp)
            .size(250.dp, 350.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MainColor), Arrangement.Center, Alignment.CenterHorizontally
    ) {

        if (userInfo?.isLogin() == true) {
            if (isInParty) {
                TextSemiBold(
                    v = stringResource(R.string.already_in_a_party),
                    Modifier.fillMaxWidth(), true, size = 17
                )

                Spacer(Modifier.height(15.dp))

                RoundBorderButtonsView(stringResource(R.string.share).lowercase()) {
                    linkCopyInfo.toast()
                    shareTxt(appPartyJoinUrl(partyRoomId!!))
                }

                Spacer(Modifier.height(15.dp))

                RoundBorderButtonsView(stringResource(R.string.leave_party).lowercase()) {
                    closeParty()
                }
            } else {
                TextSemiBold(
                    v = stringResource(R.string.start_new_party),
                    Modifier.fillMaxWidth(), true, size = 17
                )

                Spacer(Modifier.height(15.dp))

                RoundBorderButtonsView(stringResource(R.string.start).lowercase()) {
                    showPartyDialog = true
                }
            }
            LaunchedEffect(partyRoomId) {
                isInParty = partyRoomId != null
            }
        } else {
            TextSemiBold(
                v = stringResource(R.string.start_a_group_party),
                Modifier.fillMaxWidth(), true, size = 20
            )

            Spacer(Modifier.height(25.dp))

            if (isLoginLoading)
                LoadingStateBar()
            else
                RoundBorderButtonsView(stringResource(R.string.login_to_continue)) {
                    isLoginLoading = true
                    coroutine.launch(Dispatchers.IO) {
                        googleSignIn.startLogin()
                        isLoginLoading = false
                    }
                }
        }

        if (showPartyDialog) SimpleTextDialog(
            stringResource(R.string.start_new_party),
            stringResource(R.string.song_group_party_desc),
            stringResource(R.string.start),
            {
                Intent(context, SongPartyService::class.java).apply {
                    context.startService(this)
                }
                showPartyDialog = false
            }, {
                showPartyDialog = false
            })

//        TextThin(
//            v = stringResource(R.string.song_group_party_desc),
//            Modifier.fillMaxWidth(), true, size = 13
//        )
    }
}

@Composable
fun MyMusicMusicGroupView() {
    var songSyncDialog by remember { mutableStateOf(false) }


//    Column(
//        Modifier
//            .padding(top = 40.dp, bottom = 30.dp)
//            .padding(7.dp)
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(12.dp))
//            .background(BlackColor),
//        Arrangement.Center,
//        Alignment.CenterHorizontally
//    )
//    {
//        Spacer(Modifier.height(40.dp))
//
//        TextSemiBold(
//            v = stringResource(R.string.song_group_party),
//            Modifier.fillMaxWidth(), true, size = 23
//        )
//
//        Spacer(Modifier.height(20.dp))
//
//        TextThin(
//            v = stringResource(R.string.song_group_party_desc),
//            Modifier.fillMaxWidth(), true, size = 13
//        )
//
//        Spacer(Modifier.height(20.dp))
//
//        SmallIcons(icon = R.drawable.ic_vynil, 45, 0)
//
//        Spacer(Modifier.height(30.dp))
//
//        Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
//            RoundBorderButtonsView(stringResource(R.string.get_started)) {
//                songSyncDialog = true
//            }
//        }
//
//        Spacer(Modifier.height(40.dp))
//    }
//    if (songSyncDialog) MusicSyncDialog {
//        songSyncDialog = false
//    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicSyncDialog(close: () -> Unit) {
    val context = LocalContext.current as Activity
    val googleSignIn = GoogleSignInUtils(context)
    val coroutines = rememberCoroutineScope()
    val noRoomIdFound = stringResource(R.string.no_room_found_to_share)

    val height = LocalConfiguration.current.screenHeightDp / 2
    val sheetState = rememberModalBottomSheetState()

    val userInfo by loginUser.collectAsState(initial = null)

    ModalBottomSheet(close, Modifier.height(height.dp), sheetState) {
        Column(
            Modifier
                .fillMaxWidth(),
            Arrangement.Center, Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(30.dp))

            if (userInfo?.isLogin() == true) {
                RoundBorderButtonsView(text = "share the code") {
                    coroutines.launch {
                        Intent(context, SongPartyService::class.java).apply {
                            context.startService(this)
                        }
                        delay(1.seconds)

                        if (partyRoomId == null)
                            noRoomIdFound.toast()
                        else
                            shareTxt(appPartyJoinUrl(partyRoomId!!))

                        close()
                    }
                }
            } else {
                RoundBorderButtonsView(text = stringResource(R.string.continue_with_google)) {
                    coroutines.launch {
                        googleSignIn.startLogin()
                    }
                }
            }
        }
    }
}