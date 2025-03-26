package com.rizwansayyed.zene.ui.main.connect.connectchat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ConnectUserResponse
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.viewmodel.ConnectChatViewModel

@Composable
fun ConnectChatSettingsView(user: ConnectUserResponse?, close: () -> Unit) {
    Dialog(close) {
        val viewModel: ConnectChatViewModel = hiltViewModel()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp)),
        ) {
            TextViewSemiBold(stringResource(R.string.chat_settings), 16, line = 1)
        }
    }
}
