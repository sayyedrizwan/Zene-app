package com.rizwansayyed.zene.ui.main.connect.connectchat

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
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
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.viewmodel.ConnectViewModel

@Composable
fun ConnectProfileMessagingView(
    user: ConnectUserInfoResponse, viewModel: ConnectViewModel, close: () -> Unit
) {
    val context = LocalContext.current
    val windowManager =
        remember { context.getSystemService(Context.WINDOW_SERVICE) as WindowManager }

    val metrics = DisplayMetrics().apply { windowManager.defaultDisplay.getRealMetrics(this) }
    val (width, height) = with(LocalDensity.current) {
        Pair(metrics.widthPixels.toDp(), metrics.heightPixels.toDp())
    }

    Dialog(close, DialogProperties(usePlatformDefaultWidth = false)) {
        Column(
            Modifier
                .fillMaxSize()
                .requiredSize(width, height)
                .background(MainColor)
        ) {
            ConnectProfileMessageButton(user, viewModel, close)
        }
    }
}

@Composable
fun ConnectProfileMessageButton(
    user: ConnectUserInfoResponse, viewModel: ConnectViewModel, close: () -> Unit
) {
    var messageText by remember { mutableStateOf("") }

    TextViewBold(stringResource(R.string.chats), 19, Color.White)
    Spacer(Modifier.height(25.dp))

    Spacer(Modifier.height(15.dp))

    if (user.isConnected() == FRIENDS) TextField(
        messageText,
        {
            if (it.length <= 140) messageText = it
        },
        Modifier
            .padding(top = 20.dp)
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
        placeholder = {
            TextViewNormal(
                stringResource(R.string.enter_your_message), 14, color = Color.Black
            )
        },
        trailingIcon = {
            if (messageText.length > 3) {
                IconButton({
                    viewModel.sendConnectMessage(user.user?.email, messageText)
                    close()
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
        singleLine = true
    )

    Spacer(Modifier.height(80.dp))
}
