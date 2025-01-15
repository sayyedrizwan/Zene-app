package com.rizwansayyed.zene.ui.main.connect.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.viewmodel.PhoneNumberVerificationViewModel

@Composable
fun VerifyPhoneNumberOTPView(viewModel: PhoneNumberVerificationViewModel) {
    val focusManager = LocalFocusManager.current
    var textOTP by remember { mutableStateOf("") }

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
                    if (it.length == 6) {
                        focusManager.clearFocus()
                        viewModel.verifyOTPNumber(textOTP)
                    } else textOTP = it
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

        }
    }
}