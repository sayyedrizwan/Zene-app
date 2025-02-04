package com.rizwansayyed.zene.ui.main.connect.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ConnectUserInfoResponse
import com.rizwansayyed.zene.ui.view.ButtonWithImageAndBorder
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.utils.MainUtils.getAddressFromLatLong
import com.rizwansayyed.zene.viewmodel.ConnectViewModel
import kotlinx.coroutines.tasks.await

@Composable
fun ConnectProfileMessageButton(
    user: ConnectUserInfoResponse, viewModel: ConnectViewModel, close: () -> Unit
) {
    var messageText by remember { mutableStateOf("") }

    TextViewBold(stringResource(R.string.chats), 19, Color.White)
    Spacer(Modifier.height(25.dp))

    if ((user.message?.message?.length ?: 0) > 3) {
        if (user.message?.fromCurrentUser == true) {
            TextViewNormal("Me: ${user.message.message}", 15, Color.White)
        } else TextViewNormal("${user.user?.name}: ${user.message?.message}", 15, Color.White)
    }

    Spacer(Modifier.height(15.dp))

    if (user.message?.fromCurrentUser != true || (user.message.message?.length ?: 0) < 3) TextField(
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

    Spacer(Modifier.height(50.dp))

    TextViewNormal(
        stringResource(R.string.you_cant_send_a_new_message_until_you_got_reply),
        15,
        Color.White,
        true
    )


    Spacer(Modifier.height(80.dp))
}

@SuppressLint("MissingPermission")
@Composable
fun ConnectLocationButton(
    user: ConnectUserInfoResponse, viewModel: ConnectViewModel, close: () -> Unit
) {
    val context = LocalContext.current.applicationContext
    var areaName by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    TextViewBold(stringResource(R.string.send_location), 19, Color.White)
    Spacer(Modifier.height(25.dp))

    TextViewNormal(areaName, 16, Color.White, true)
    Spacer(Modifier.height(45.dp))

    if (isLoading) CircularLoadingView()

    if (areaName.trim().length > 2) ButtonWithImageAndBorder(
        R.drawable.ic_location, R.string.send_location, Color.White, Color.White
    ) {
        viewModel.sendConnectLocation(user.user?.email)
        close()
    }

    LaunchedEffect(Unit) {
        isLoading = true
        val l = LocationServices.getFusedLocationProviderClient(context)
        try {
            val token = CancellationTokenSource().token
            val location = l.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, token).await()
            areaName = try {
                getAddressFromLatLong(location.latitude, location.longitude) ?: ""
            } catch (e: Exception) {
                ""
            }
            isLoading = false
        } catch (e: Exception) {
            isLoading = false
        }
    }

    Spacer(Modifier.height(80.dp))
}