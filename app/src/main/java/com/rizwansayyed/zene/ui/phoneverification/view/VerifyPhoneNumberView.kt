package com.rizwansayyed.zene.ui.phoneverification.view

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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ButtonHeavy
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.viewmodel.PhoneNumberVerificationViewModel

@Composable
fun VerifyPhoneNumberView(viewModel: PhoneNumberVerificationViewModel) {
    var menuPick by remember { mutableStateOf(false) }
    var phoneNumberText by remember { mutableStateOf("") }

    Box(
        Modifier
            .fillMaxSize()
            .padding(10.dp), Alignment.Center
    ) {
        Column(Modifier.fillMaxWidth()) {
            TextViewSemiBold(stringResource(R.string.verify_phone_number), 19)
            Spacer(Modifier.height(7.dp))
            TextViewNormal(stringResource(R.string.verify_phone_number_desc), 16)
            Spacer(Modifier.height(55.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                Box(Modifier.weight(2f)) {
                    ButtonHeavy("+${viewModel.countryCode}") {
                        menuPick = true
                    }
                    DropdownMenu(menuPick, { menuPick = false }) {
                        viewModel.countryCodeLists.forEach {
                            DropdownMenuItem({ TextViewNormal("+${it}", 14) },
                                {
                                    viewModel.setUserCountryCode(it)
                                    menuPick = false
                                })
                        }
                    }
                }

                TextField(
                    phoneNumberText, {
                        if (it.length <= 15) {
                            phoneNumberText = it
                            viewModel.setUserPhoneNumber(it)
                        }
                    },
                    Modifier
                        .weight(8f)
                        .padding(vertical = 4.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    placeholder = {
                        TextViewNormal(stringResource(R.string.enter_your_phone_number), 14)
                    },
                    trailingIcon = {
                        if (phoneNumberText.length > 6) {
                            IconButton({ viewModel.sendNumberVerification(phoneNumberText) }) {
                                ImageIcon(R.drawable.ic_arrow_right, 24)
                            }
                        }
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

    LaunchedEffect(Unit) {
        viewModel.getUserCountryCode()
    }
}