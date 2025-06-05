package com.rizwansayyed.zene.ui.login.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.datastore.DataStorageManager.signInWithEmailAddress
import com.rizwansayyed.zene.ui.login.utils.LoginUtils
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.theme.proximanOverFamily
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginEmailSheet(login: LoginUtils, close: () -> Unit) {
    ModalBottomSheet(close, contentColor = MainColor, containerColor = MainColor) {
        var userEmail by remember { mutableStateOf("") }
        val focusManager = LocalFocusManager.current

        val coroutine = rememberCoroutineScope()

        val loginViaEmail by remember { derivedStateOf { login.loginViaEmail } }

        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp)
        ) {
            Spacer(Modifier.height(5.dp))

            TextViewBold(stringResource(R.string.continue_with_email), 16)

            Spacer(Modifier.height(10.dp))

            when (val v = loginViaEmail) {
                ResponseResult.Empty -> TextField(
                    userEmail,
                    { userEmail = if (it.length <= 200) it else it.take(200) },
                    Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                    keyboardActions = KeyboardActions {
                        if (userEmail.trim().length > 3 && userEmail.trim().contains("@")) {
                            focusManager.clearFocus()
                            coroutine.launch {
                                signInWithEmailAddress = flowOf(userEmail)
                            }
                            login.startEmailLogin(userEmail)
                        }
                    },
                    placeholder = {
                        TextViewNormal(
                            stringResource(R.string.enter_your_email_address), 16, Color.DarkGray
                        )
                    },
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontFamily = proximanOverFamily,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start
                    ),
                    trailingIcon = {
                        if (userEmail.trim().length > 3 && userEmail.trim().contains("@")) {
                            IconButton({
                                focusManager.clearFocus()
                                coroutine.launch {
                                    signInWithEmailAddress = flowOf(userEmail)
                                }
                                login.startEmailLogin(userEmail)
                            }) {
                                ImageIcon(R.drawable.ic_arrow_right, 24, Color.White)
                            }
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Black,
                        unfocusedContainerColor = Color.Black,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        cursorColor = MainColor
                    ),
                    singleLine = true
                )

                is ResponseResult.Error -> {
                    Spacer(Modifier.height(15.dp))

                    TextViewNormal(
                        stringResource(R.string.error_while_sending_email_try_again),
                        16,
                        center = true
                    )

                    Spacer(Modifier.height(10.dp))
                }

                ResponseResult.Loading -> CircularLoadingView()
                is ResponseResult.Success -> {
                    if (v.data) {
                        Spacer(Modifier.height(15.dp))

                        TextViewNormal(
                            stringResource(R.string.email_sent_successfully_login_auth),
                            16,
                            center = true
                        )

                        Spacer(Modifier.height(10.dp))
                    } else {
                        Spacer(Modifier.height(15.dp))

                        TextViewNormal(
                            stringResource(R.string.error_while_sending_email_try_again),
                            16,
                            center = true
                        )

                        Spacer(Modifier.height(10.dp))
                    }
                }
            }

            Spacer(Modifier.height(70.dp))

            LaunchedEffect(Unit) {
                login.resetEmailLogin()
            }
        }
    }
}