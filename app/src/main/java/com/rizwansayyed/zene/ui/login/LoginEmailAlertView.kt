package com.rizwansayyed.zene.ui.login

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.login.flow.LoginFlowType
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.LoadingView
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginEmailAlertView(viewModel: HomeViewModel, close: () -> Unit) {
    val activity = LocalContext.current as Activity

    ModalBottomSheet(close, containerColor = Color.Black) {
        var emailValue by remember { mutableStateOf("") }
        var isMailSend by remember { mutableStateOf(false) }
        var isLoading by remember { mutableStateOf(false) }

        Column(
            Modifier
                .padding(4.dp)
                .fillMaxSize()
                .padding(4.dp)
        ) {
            Spacer(Modifier.height(20.dp))
            TextPoppinsSemiBold(stringResource(R.string.login_via_email), size = 18)
            Spacer(Modifier.height(10.dp))

            if (isMailSend) {
                TextPoppins(stringResource(R.string.email_have_sent), true, size = 13)
                Spacer(Modifier.height(20.dp))
                TextPoppins("${stringResource(R.string.send_to)} $emailValue", true, size = 13)
                Spacer(Modifier.height(20.dp))
                TextPoppins(stringResource(R.string.keep_this_sheet_view_open), true, size = 19)
                Spacer(Modifier.height(50.dp))
            } else {
                TextPoppins(stringResource(R.string.login_via_email_desc), size = 13)
                Spacer(Modifier.height(50.dp))

                TextFieldEmail(emailValue) {
                    emailValue = it
                }

                Spacer(Modifier.height(60.dp))

                if (isLoading) Row(
                    Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically
                ) {
                    LoadingView(Modifier.size(32.dp))
                } else Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (!emailValue.contains("@") && emailValue.length < 4) {
                                activity.resources.getString(R.string.enter_a_valid_email_address)
                                return@clickable
                            }
                            isLoading = true
                            viewModel.loginFlow.initEmail(emailValue, activity) {
                                isMailSend = true
                                isLoading = false
                            }
                        }, Arrangement.Center, Alignment.CenterVertically
                ) {
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(100))
                            .background(MainColor)
                            .padding(10.dp)
                    ) {
                        ImageIcon(R.drawable.ic_arrow_right, 30)
                    }
                }

                Spacer(Modifier.height(90.dp))
            }
        }

        DisposableEffect(Unit) {
            val listener = object : EmailEventListener {
                override fun onEvent(data: String) {
                    viewModel.loginFlow.emailAuth(emailValue, data)
                }
            }

            GlobalEmailEventProvider.registerListener(listener)
            onDispose {
                GlobalEmailEventProvider.unregisterListener()
            }
        }
    }
}

@Composable
fun TextFieldEmail(emailValue: String, change: (String) -> Unit) {
    TextField(
        value = emailValue,
        onValueChange = { change(it) },
        maxLines = 1,
        trailingIcon = {
            Icon(Icons.Filled.Email, "", tint = Color.White)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email, imeAction = ImeAction.Done
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(5.dp, MainColor),
        placeholder = {
            TextPoppins(
                stringResource(R.string.enter_your_email), false, Color.Gray, 16
            )
        },
        colors = TextFieldColors(
            Color.White,
            Color.White,
            Color.White,
            Color.White,
            MainColor,
            MainColor,
            MainColor,
            MainColor,
            Color.White,
            Color.White,
            TextSelectionColors(Color.White, Color.White),
            MainColor,
            MainColor,
            MainColor,
            MainColor,
            MainColor,
            MainColor,
            MainColor,
            MainColor,
            MainColor,
            MainColor,
            MainColor,
            MainColor,
            MainColor,
            MainColor,
            MainColor,
            MainColor,
            MainColor,
            MainColor,
            MainColor,
            MainColor,
            MainColor,
            MainColor,
            MainColor,
            MainColor,
            MainColor,
            MainColor,
            MainColor,
            MainColor,
            MainColor,
            MainColor,
            MainColor,
            MainColor
        )
    )
}