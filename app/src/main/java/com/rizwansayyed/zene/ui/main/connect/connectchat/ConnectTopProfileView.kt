package com.rizwansayyed.zene.ui.main.connect.connectchat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ConnectUserInfoResponse
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewSemiBold

@Composable
fun ConnectTopProfileView(modifier: Modifier, user: ConnectUserInfoResponse, close: () -> Unit) {
    Row(modifier, Arrangement.Center, Alignment.CenterVertically) {
        Box(Modifier
            .padding(5.dp)
            .clickable { close() }) {
            ImageIcon(R.drawable.ic_cancel_close, 21)
        }

        Column(
            Modifier
                .padding(5.dp)
                .weight(1f)
        ) {
            TextViewSemiBold(user.user?.name ?: "", 16, center = true)
        }

        Box(Modifier
            .padding(5.dp)
            .clickable { close() }) {
            ImageIcon(R.drawable.ic_more_vertical_circle, 21)
        }
    }
}