package com.rizwansayyed.zene.ui.main.connect.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ConnectUserInfoResponse
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewLight
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.viewmodel.ConnectViewModel

@Composable
fun ConnectSettingsView(response: ConnectUserInfoResponse, viewModel: ConnectViewModel) {
    Spacer(Modifier.height(20.dp))
    TextViewBold(stringResource(R.string.settings_), 20)

    Spacer(Modifier.height(30.dp))
    SettingsViewSwitchItems(
        R.drawable.ic_music_note,
        R.string.share_your_music,
        R.string.share_your_music_desc,
        response.myStatus?.lastListeningSong
    ) {
        response.myStatus?.lastListeningSong = it
        viewModel.updateSettingsStatus(response)
    }

    Spacer(Modifier.height(30.dp))
    SettingsViewSwitchItems(
        R.drawable.ic_location,
        R.string.share_your_location,
        R.string.share_your_location_desc,
        response.myStatus?.locationSharing
    ) {
        response.myStatus?.locationSharing = it
        viewModel.updateSettingsStatus(response)

    }

    Spacer(Modifier.height(30.dp))
    SettingsViewSwitchItems(
        R.drawable.ic_notification_off,
        R.string.share_notification,
        R.string.share_notification_desc,
        response.myStatus?.silentNotification
    ) {
        response.myStatus?.silentNotification = it
        viewModel.updateSettingsStatus(response)
    }

    Spacer(Modifier.height(30.dp))
}

@Composable
fun SettingsViewSwitchItems(
    img: Int,
    title: Int,
    desc: Int,
    value: Boolean?,
    change: (Boolean) -> Unit
) {
    Row(
        Modifier
            .padding(2.dp)
            .fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically
    ) {
        ImageIcon(img, 35)

        Column(
            Modifier
                .padding(horizontal = 9.dp)
                .weight(1f)
        ) {
            TextViewSemiBold(stringResource(title), 18)
            Spacer(Modifier.height(2.dp))
            TextViewLight(stringResource(desc), 13)
        }

        Switch(checked = value ?: false, onCheckedChange = {
            change(it)
        }, thumbContent = if (value == true) {
            {
                ImageIcon(R.drawable.ic_tick, size = SwitchDefaults.IconSize.value.toInt())
            }
        } else {
            null
        })
    }
}


@Composable
fun SettingsViewSimpleItems(img: Int, title: Int, click: () -> Unit) {
    Row(
        Modifier
            .padding(2.dp)
            .fillMaxWidth()
            .clickable { click() }, Arrangement.Center, Alignment.CenterVertically
    ) {
        ImageIcon(img, 24)

        Column(
            Modifier
                .padding(horizontal = 10.dp)
                .weight(1f)
                .offset(y = 2.dp),
            Arrangement.Center, Alignment.Start
        ) {
            TextViewLight(stringResource(title), 16)
        }

        Box(Modifier.rotate(-90f)) {
            ImageIcon(R.drawable.ic_arrow_down, 20)
        }
    }
}
