package com.rizwansayyed.zene.ui.main.connect.view

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.MainUtils.timeDifferenceInMinutes
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.viewmodel.PhoneNumberViewModel

@Composable
fun VerifyPhoneNumberOTPView(viewModel: PhoneNumberViewModel) {
    val context = LocalActivity.current
    val focusManager = LocalFocusManager.current
    val verificationSendOn by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var textOTP by remember { mutableStateOf("") }
    val optAlreadySend = stringResource(R.string.otp_already_send_please_try_again)
    val tooManyAttempt = stringResource(R.string.too_many_attempts_to_phone_number)
    val wrongPasswordTryAgain = stringResource(R.string.wrong_password_try_again)

    Box(
        Modifier
            .fillMaxSize()
            .padding(10.dp), Alignment.Center
    ) {
        Column(Modifier.fillMaxWidth()) {
            TextViewSemiBold(stringResource(R.string.verify_phone_number), 19)
            Spacer(Modifier.height(7.dp))
            TextViewNormal(stringResource(R.string.otp_send_to_phone_number), 16)
            Spacer(Modifier.height(55.dp))

            TextField(
                textOTP,
                {
                    textOTP = it
                    if (it.length >= 6) {
                        focusManager.clearFocus()
                        viewModel.verifyOTPNumber(textOTP)
                    }
                },
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                placeholder = {
                    TextViewNormal(
                        stringResource(R.string.enter_otp_send_to_your_phone_number),
                        14,
                        center = true
                    )
                },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MainColor,
                    unfocusedContainerColor = MainColor,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.White
                ),
                singleLine = true
            )
            Spacer(Modifier.height(15.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                OutlinedButton({
                    if (timeDifferenceInMinutes(verificationSendOn) < 1) {
                        optAlreadySend.toast()
                        return@OutlinedButton
                    }
                    viewModel.resetVerify()
                }) {
                    TextViewSemiBold(stringResource(R.string.resend), 15)
                }
            }
            Spacer(Modifier.height(25.dp))
            when (val v = viewModel.optVerify) {
                ResponseResult.Empty -> {}
                is ResponseResult.Error -> {}
                ResponseResult.Loading -> CircularLoadingView()
                is ResponseResult.Success -> {
                    LaunchedEffect(Unit) {
                        if (v.data.status == true) {
                            context?.finish()
                        } else {
                            if (v.data.message?.contains("too many attempts") == true) {
                                tooManyAttempt.toast()
                            } else if (v.data.message?.contains("wrong otp") == true) {
                                wrongPasswordTryAgain.toast()
                            } else {
                                "Error, Try Again.".toast()
                            }
                        }
                    }
                }
            }
        }
    }
}