package com.rizwansayyed.zene.ui.main.connect.connectchat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ConnectUserInfoResponse
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.viewmodel.ConnectSocketChatViewModel
import java.util.Locale

@Composable
fun ConnectTopProfileView(
    user: ConnectUserInfoResponse, socket: ConnectSocketChatViewModel, close: () -> Unit
) {
    val isInLobby by remember { derivedStateOf { socket.inLobby } }
    val justLeftLobby by remember { derivedStateOf { socket.justLeft } }

    Row(Modifier.background(MainColor).fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
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

            if (isInLobby) {
                Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                    Spacer(
                        Modifier
                            .padding(horizontal = 7.dp)
                            .size(10.dp)
                            .clip(RoundedCornerShape(100))
                            .background(if (justLeftLobby) Color.Red else Color.Green)
                    )

                    if (justLeftLobby)
                        TextViewNormal(stringResource(R.string.just_left_conversation), size = 14)
                    else
                        TextViewNormal(stringResource(R.string.active_in_conversation), size = 14)
                }
            } else {
                user.user?.getLastSeen()?.let {
                    TextViewNormal(
                        String.format(
                            Locale.getDefault(), stringResource(R.string.last_seen), it
                        ), 14, center = true
                    )
                }
            }
        }

        Box(Modifier
            .padding(5.dp)
            .clickable { close() }) {
            ImageIcon(R.drawable.ic_more_vertical_circle, 21)
        }
    }
}