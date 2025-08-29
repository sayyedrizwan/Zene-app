package com.rizwansayyed.zene.ui.main.connect.connectchat

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ConnectUserInfoResponse
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.ui.main.connect.profile.SettingsViewSwitchItems
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewLight
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.BioAuthMetric
import com.rizwansayyed.zene.utils.safeLaunch
import com.rizwansayyed.zene.viewmodel.ConnectViewModel
import kotlinx.coroutines.flow.firstOrNull

data class ExpiryTime(val text: String, val minutes: Int)

val messageExpiryTime = listOf(
    ExpiryTime(context.resources.getString(R.string.after_seen), 0),
    ExpiryTime(context.resources.getString(R.string.thirty_minutes), 30),
    ExpiryTime(context.resources.getString(R.string.one_hour), 60),
    ExpiryTime(context.resources.getString(R.string.three_hours), 180),
    ExpiryTime(context.resources.getString(R.string.six_hours), 360),
    ExpiryTime(context.resources.getString(R.string.twelve_hours), 720),
    ExpiryTime(context.resources.getString(R.string.one_day), 1440),
    ExpiryTime(context.resources.getString(R.string.two_days), 2880),
    ExpiryTime(context.resources.getString(R.string.four_days), 5760),
    ExpiryTime(context.resources.getString(R.string.seven_days), 10080),
)

@Composable
fun ConnectChatSettingsView(
    response: ConnectUserInfoResponse, connectViewModel: ConnectViewModel, close: () -> Unit
) {
    Dialog(close, DialogProperties(usePlatformDefaultWidth = false)) {
        val context = LocalActivity.current
        val bioAuthMetric = BioAuthMetric(R.string.authenticate_to_view_chat, context)

        val width = LocalConfiguration.current.screenWidthDp.dp
        val coroutines = rememberCoroutineScope()

        var lockChatSettings by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .padding(horizontal = 7.dp)
                .width(width)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black)
                .padding(horizontal = 20.dp, vertical = 35.dp),
        ) {
            TextViewSemiBold(stringResource(R.string.chat_settings), 16, line = 1)


            Spacer(Modifier.height(30.dp))
            ChatViewPickerItems(
                R.drawable.ic_timer,
                R.string.expire_message,
                R.string.expire_message_desc,
                response,
                messageExpiryTime
            ) {
                response.expireInMinutes = it
                connectViewModel.updateSettingsStatus(response)
            }

            Spacer(Modifier.height(30.dp))
            SettingsViewSwitchItems(
                R.drawable.ic_message_lock,
                R.string.lock_message,
                R.string.lock_message_desc,
                lockChatSettings
            ) {
                if (it) {
                    bioAuthMetric.checkAuth { auth ->
                        if (!auth) return@checkAuth
                        lockChatSettings = true
                        coroutines.safeLaunch {
                            DataStorageManager.lockChatSettings(response.user?.email ?: "", true).firstOrNull()
                        }
                    }
                }
                lockChatSettings = false
                coroutines.safeLaunch {
                    DataStorageManager.lockChatSettings(response.user?.email ?: "", false).firstOrNull()
                }
            }

            LaunchedEffect(Unit) {
                response.user?.email ?: return@LaunchedEffect
                lockChatSettings =
                    DataStorageManager.lockChatSettings(response.user.email).firstOrNull() ?: false
            }

        }
    }
}


@Composable
fun ChatViewPickerItems(
    img: Int,
    title: Int,
    desc: Int,
    v: ConnectUserInfoResponse,
    list: List<ExpiryTime>,
    change: (Int) -> Unit
) {
    Box(Modifier) {
        var expanded by remember { mutableStateOf(false) }
        Row(Modifier
            .padding(2.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) { expanded = true }
            .fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
            ImageIcon(img, 35)

            Column(
                Modifier
                    .padding(horizontal = 9.dp)
                    .weight(1f)
            ) {
                TextViewSemiBold(stringResource(title), 18)
                Spacer(Modifier.height(2.dp))
                TextViewLight(stringResource(desc), 13)
                Spacer(Modifier.height(2.dp))
            }

            Box(Modifier.rotate(-90f)) {
                ImageIcon(R.drawable.ic_arrow_down, 25)
            }
        }

        DropdownMenu(expanded, { expanded = false }) {
            list.forEach {
                DropdownMenuItem(text = {
                    Row(Modifier, Arrangement.Center, Alignment.CenterVertically) {
                        TextViewBold(it.text, 14)
                        Spacer(Modifier.weight(1f))
                        if (v.expireInMinutes == it.minutes) ImageIcon(R.drawable.ic_tick, 19)
                    }
                }, onClick = {
                    expanded = false
                    change(it.minutes)
                })
            }
        }
    }
}
