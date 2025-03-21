package com.rizwansayyed.zene.ui.main.connect.connectchat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ConnectUserInfoResponse
import com.rizwansayyed.zene.ui.view.CircularLoadingViewSmall
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.ImageWithBgRound
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.viewmodel.ConnectViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ConnectProfileMessageView(
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
                        messageText = ""
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

        if (viewModel.sendConnectMessageLoading) CircularLoadingViewSmall()

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
