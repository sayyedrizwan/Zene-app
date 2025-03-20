package com.rizwansayyed.zene.ui.main.connect.connectchat

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ConnectUserInfoResponse
import com.rizwansayyed.zene.data.model.ConnectedUserStatus.FRIENDS
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.ImageWithBgRound
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.viewmodel.ConnectViewModel

@Composable
fun ConnectProfileMessagingView(
    user: ConnectUserInfoResponse, viewModel: ConnectViewModel, close: () -> Unit
) {

    Dialog(close, DialogProperties(usePlatformDefaultWidth = false)) {
        val window = LocalActivity.current?.window
        Column(
            Modifier
                .fillMaxSize()
                .background(MainColor)
        ) {
            Column(Modifier.weight(8f)) {

            }

            if (user.isConnected() == FRIENDS) Column(
                Modifier
                    .padding(horizontal = 5.dp)
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                ConnectProfileMessage(viewModel, user)
            }
        }

        DisposableEffect(Unit) {
            window?.statusBarColor = MainColor.toArgb()
            window?.navigationBarColor = MainColor.toArgb()
            onDispose {
                window?.statusBarColor = Color.Transparent.toArgb()
                window?.navigationBarColor = Color.Transparent.toArgb()
            }
        }
    }
}

@Composable
fun ConnectProfileMessage(
    viewModel: ConnectViewModel, user: ConnectUserInfoResponse
) {
    var messageText by remember { mutableStateOf("") }

    Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
        TextField(
            messageText,
            { messageText = if (it.length <= 250) it else it.take(250) },
            Modifier
                .padding(horizontal = 5.dp)
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            placeholder = {
                TextViewNormal(
                    stringResource(R.string.enter_your_message), 14, color = Color.Black
                )
            },
            trailingIcon = {
                if (messageText.isNotEmpty()) {
                    IconButton({
                        viewModel.sendConnectMessage(user.user?.email, messageText)
                    }) {
                        ImageIcon(R.drawable.ic_sent, 24, Color.Black)
                    }
                }
            },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                cursorColor = Color.Black,
                focusedTextColor = Color.Black,
                disabledTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            singleLine = false,
            maxLines = 4
        )

        AnimatedVisibility(messageText.isEmpty()) {
            Row(Modifier, Arrangement.Center, Alignment.CenterVertically) {
                ImageWithBgRound(R.drawable.ic_camera) {

                }

                ImageWithBgRound(R.drawable.ic_gif) {

                }

                ImageWithBgRound(R.drawable.ic_file_add) {

                }
            }
        }
    }
}