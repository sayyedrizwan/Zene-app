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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.loginUser
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.LoadingStateBar
import com.rizwansayyed.zene.presenter.ui.RoundBorderButtonsView
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.dialog.SimpleTextDialog
import com.rizwansayyed.zene.presenter.util.UiUtils
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.TOTAL_ITEMS_GRID
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.TWO_ITEMS_GRID
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.service.songparty.SongPartyService
import com.rizwansayyed.zene.service.songparty.Utils.Action.generatePartyRoomId
import com.rizwansayyed.zene.service.songparty.Utils.Action.partyRoomId
import com.rizwansayyed.zene.service.songparty.Utils.ActionFunctions.closeParty
import com.rizwansayyed.zene.service.songparty.Utils.groupMusicUsersList
import com.rizwansayyed.zene.utils.GoogleSignInUtils
import com.rizwansayyed.zene.utils.Utils.AppUrl.appPartyJoinUrl
import com.rizwansayyed.zene.utils.Utils.shareTxt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun MyMusicGroupMusicParty() {
    var isInParty by remember { mutableStateOf(false) }

    Column {
        Column(Modifier.padding(horizontal = 9.dp)) {
            TopInfoWithSeeMore(R.string.music_group_party, null, 50) {}
        }

        LazyHorizontalGrid(
            GridCells.Fixed(TOTAL_ITEMS_GRID), Modifier
                .fillMaxWidth()
                .height(360.dp)
        ) {
            item(key = 1, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
                HostPartyButtonCard()
            }
            if (isInParty && groupMusicUsersList.isEmpty())
                item(key = 2, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
                    Column(
                        Modifier
                            .padding(start = 10.dp)
                            .size(250.dp, 350.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        Arrangement.Center,
                        Alignment.CenterHorizontally
                    ) {
                        TextSemiBold(v = stringResource(R.string.no_one_is_at_the_party))
                    }
                }

            items(
                groupMusicUsersList,
                key = { i -> i.email },
                span = { GridItemSpan(TWO_ITEMS_GRID) }) {
                Column(
                    Modifier
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                        .size(200.dp, 175.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(BlackColor),
                    Arrangement.Center,
                    Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        it.image, it.name,
                        Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    Spacer(Modifier.height(15.dp))

                    TextSemiBold(it.name, Modifier.fillMaxWidth(), true)
                    Spacer(Modifier.height(7.dp))
                    TextThin(
                        stringResource(R.string.is_in_the_party),
                        Modifier.fillMaxWidth(), true, size = 13
                    )
                }
            }
        }
    }

    LaunchedEffect(partyRoomId) {
        isInParty = partyRoomId != null
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
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startService(this)
                }
                showPartyDialog = false
            }, {
                showPartyDialog = false
            })
    }
}