package com.rizwansayyed.zene.ui.main.connect.connectchat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ConnectUserResponse
import com.rizwansayyed.zene.ui.main.connect.profile.SettingsViewSwitchItems
import com.rizwansayyed.zene.ui.theme.LuxColor
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.viewmodel.ConnectChatViewModel

@Composable
fun ConnectChatSettingsView(user: ConnectUserResponse?, close: () -> Unit) {
    Dialog(close, DialogProperties(usePlatformDefaultWidth = false)) {
        val viewModel: ConnectChatViewModel = hiltViewModel()
        val width = LocalConfiguration.current.screenWidthDp.dp
        var lastListeningSong by remember { mutableStateOf(false) }

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
            SettingsViewSwitchItems(
                R.drawable.ic_music_note,
                R.string.share_your_music,
                R.string.share_your_music_desc,
                lastListeningSong
            ) {
            }

        }
    }
}
