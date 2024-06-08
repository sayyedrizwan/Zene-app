package com.rizwansayyed.zene.presenter.ui.home.views

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.musicplayer.utils.Utils
import com.rizwansayyed.zene.presenter.ui.musicplayer.view.MusicActionButton
import com.rizwansayyed.zene.utils.FirebaseEvents
import com.rizwansayyed.zene.utils.FirebaseEvents.registerEvent
import com.rizwansayyed.zene.utils.Utils.OFFICIAL_DOWNLOAD_APP
import com.rizwansayyed.zene.utils.Utils.ZENE_BUY_ME_A_COFFEE
import com.rizwansayyed.zene.utils.Utils.ZENE_BUY_ME_A_COFFEE_MEMBERSHIP
import com.rizwansayyed.zene.utils.Utils.customBrowser
import com.rizwansayyed.zene.utils.Utils.shareTxt
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun SupportCardView() {
    val roomDbViewModel: RoomDbViewModel = hiltViewModel()
    val coroutines = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    val share = stringResource(id = R.string.zene_share)

    if (showDialog) {
        Column(Modifier.padding(top = 70.dp)) {
            Column(
                Modifier
                    .padding(6.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MainColor)
                    .padding(6.dp)
            ) {
                Spacer(Modifier.height(30.dp))

                TextBold(
                    v = stringResource(id = R.string.donate_now_or_become_a_part_of_zene),
                    Modifier.fillMaxSize(), true, size = 24
                )

                Spacer(Modifier.height(10.dp))

                TextRegular(
                    v = stringResource(id = R.string.donate_now_or_become_a_part_of_zene_desc),
                    Modifier.fillMaxSize(), doCenter = true, size = 16
                )

                Spacer(Modifier.height(30.dp))

                Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                    MusicActionButton(R.drawable.ic_donate_heart, R.string.donate) {
                        Uri.parse(ZENE_BUY_ME_A_COFFEE).customBrowser()
                    }

                    Spacer(Modifier.height(10.dp))

                    MusicActionButton(R.drawable.ic_crown_on, R.string.become_a_member) {
                        Uri.parse(ZENE_BUY_ME_A_COFFEE_MEMBERSHIP).customBrowser()
                    }
                }
                Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                    MusicActionButton(
                        R.drawable.ic_share,
                        R.string.share_with_your_family_and_friend
                    ) {
                        registerEvent(FirebaseEvents.FirebaseEvent.SHARE_APP_VIA_DIALOG)
                        shareTxt("$share \n$OFFICIAL_DOWNLOAD_APP")
                    }
                }

                Spacer(Modifier.height(30.dp))

            }
            Spacer(Modifier.height(50.dp))
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> coroutines.launch(Dispatchers.IO) {
                    if (roomDbViewModel.roomDBList().size > 1) showDialog = true
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}