package com.rizwansayyed.zene.presenter.ui.home.mymusic

import android.app.Activity
import android.provider.Settings.Global.getString
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.presenter.ui.RoundBorderButtonsView
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.utils.GoogleSignInUtils
import kotlinx.coroutines.launch

@Composable
fun MyMusicMusicGroupView() {
    var songSyncDialog by remember { mutableStateOf(false) }

    Column(
        Modifier
            .padding(top = 40.dp, bottom = 30.dp)
            .padding(7.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(BlackColor),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(40.dp))

        TextSemiBold(
            v = stringResource(R.string.song_group_party),
            Modifier.fillMaxWidth(), true, size = 23
        )

        Spacer(Modifier.height(20.dp))

        TextThin(
            v = stringResource(R.string.song_group_party_desc),
            Modifier.fillMaxWidth(), true, size = 13
        )

        Spacer(Modifier.height(20.dp))

        SmallIcons(icon = R.drawable.ic_vynil, 45, 0)

        Spacer(Modifier.height(30.dp))

        Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
            RoundBorderButtonsView(stringResource(R.string.get_started)) {
                songSyncDialog = true
            }
        }

        Spacer(Modifier.height(40.dp))
    }
    if (songSyncDialog) MusicSyncDialog {
        songSyncDialog = false
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicSyncDialog(close: () -> Unit) {
    val context = LocalContext.current as Activity
    val googleSignIn = GoogleSignInUtils(context)
    val coroutines = rememberCoroutineScope()

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
                TextSemiBold(
                    v = stringResource(R.string.sign_in_for_song_group_party),
                    Modifier.fillMaxWidth(), true
                )

                Spacer(Modifier.height(20.dp))

                RoundBorderButtonsView(text = stringResource(R.string.continue_with_google)) {
                    coroutines.launch {
                        googleSignIn.startLogin()
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