package com.rizwansayyed.zene.ui.phoneverifier.view

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.api.model.CountryCodeModel
import com.rizwansayyed.zene.data.db.DataStoreManager.ipDB
import com.rizwansayyed.zene.ui.phoneverifier.TrueCallerUtils
import com.rizwansayyed.zene.ui.phoneverifier.TrueCallerUtils.startTrueCaller
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.LoadingView
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.utils.Utils.getAllPhoneCode
import com.rizwansayyed.zene.utils.Utils.toast
import com.rizwansayyed.zene.viewmodel.PhoneVerificationViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


@Composable
fun TopPhoneVerifierView(viewModel: PhoneVerificationViewModel) {
    var isTrueCallerInstalled by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp)
    ) {
        TextPoppinsSemiBold(stringResource(R.string.phone_verification), size = 29)
        Spacer(Modifier.height(5.dp))
        TextPoppins(stringResource(R.string.phone_verification_zene_connect), size = 15)

        if (isTrueCallerInstalled && !viewModel.isError) InstalledTrueCaller()
        else if (isTrueCallerInstalled && !viewModel.isError) InstalledTrueCaller()
        else NumberOTPField(viewModel)
    }

    LaunchedEffect(Unit) {
        isTrueCallerInstalled = TrueCallerUtils.isTrueCallerInstalled()
    }
}

@Composable
fun NumberOTPField(viewModel: PhoneVerificationViewModel) {
    val context = LocalContext.current as Activity
    var countryCode by remember { mutableStateOf("") }
    val countryCodeList = remember { mutableListOf<CountryCodeModel>() }
    var expandedPhoneCode by remember { mutableStateOf(false) }
    var phoneNumber by remember { mutableStateOf("") }
    var phoneOTPNumber by remember { mutableStateOf("") }
    var timerSeconds by remember { mutableIntStateOf(30) }


    if (viewModel.otpTimeout) {
        Spacer(Modifier.height(20.dp))
        TextPoppins(stringResource(R.string.timeout_tried_too_many_times), size = 15)
    } else if (viewModel.isOTPSend) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            Arrangement.Center,
            Alignment.CenterVertically
        ) {
            OutlinedTextField(value = phoneOTPNumber,
                onValueChange = { if (it.length <= 8) phoneOTPNumber = it },
                label = {
                    TextPoppins(stringResource(R.string.enter_the_otp), size = 14)
                },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                maxLines = 1
            )

            when (val v = viewModel.otpSuccess) {
                APIResponse.Empty -> Box(
                    Modifier
                        .padding(vertical = 2.dp, horizontal = 6.dp)
                        .clip(RoundedCornerShape(100))
                        .background(Color.Black)
                        .clickable {
                            if (phoneOTPNumber.length < 4) {
                                context.resources
                                    .getString(R.string.enter_a_valid_otp)
                                    .toast()
                                return@clickable
                            }
                            viewModel.updateNumber(phoneNumber, countryCode, phoneOTPNumber)
                        }
                        .border(1.dp, Color.White, RoundedCornerShape(100))
                        .padding(vertical = 9.dp, horizontal = 18.dp)) {
                    ImageIcon(R.drawable.ic_tick, size = 20)
                }

                is APIResponse.Error -> {}
                APIResponse.Loading -> LoadingView(Modifier.size(32.dp))
                is APIResponse.Success -> {
                    LaunchedEffect(Unit) {
                        if (v.data) {
                            context.finish()
                        } else {
                            context.resources.getString(R.string.enter_a_valid_otp).toast()
                        }
                    }
                }
            }
        }

        Row(
            Modifier
                .padding(top = 20.dp, start = 5.dp)
                .clickable {
                    if (timerSeconds == 0) {
                        timerSeconds = 30
                        viewModel.sendOTP(phoneNumber, countryCode)
                    }
                }, Arrangement.End
        ) {
            if (timerSeconds <= 0) {
                TextPoppins(stringResource(R.string.resend_otp), size = 14)
            } else {
                TextPoppins("${stringResource(R.string.resend_otp)}: $timerSeconds", size = 14)
            }
        }
    } else {
        Row(
            Modifier
                .padding(vertical = 47.dp, horizontal = 4.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(13.dp))
                .padding(vertical = 15.dp),
            Arrangement.Center,
            Alignment.CenterVertically
        ) {
            Row(Modifier
                .padding(top = 7.dp)
                .padding(10.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(MainColor)
                .clickable { expandedPhoneCode = true }
                .padding(horizontal = 8.dp, vertical = 10.dp),
                Arrangement.Center,
                Alignment.CenterVertically) {
                Box(Modifier.rotate(-90f)) {
                    ImageIcon(R.drawable.ic_arrow_left, 18)
                }

                TextPoppins("+${countryCode}")

                DropdownMenu(
                    expanded = expandedPhoneCode,
                    onDismissRequest = { expandedPhoneCode = false }) {
                    countryCodeList.forEach { code ->
                        DropdownMenuItem(text = {
                            Row(Modifier, Arrangement.Center, Alignment.CenterVertically) {
                                TextPoppins("${code.country} (+${code.code})", true, size = 16)
                            }
                        }, onClick = {
                            expandedPhoneCode = false
                            countryCode = code.code
                        })
                    }
                }
            }

            OutlinedTextField(value = phoneNumber,
                onValueChange = { if (it.length <= 12) phoneNumber = it },
                label = {
                    TextPoppins(stringResource(R.string.enter_your_phone_number), size = 14)
                },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                maxLines = 1
            )
        }

        if (viewModel.isOTPSendLoading) Row(
            Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically
        ) {
            LoadingView(Modifier.size(32.dp))
        } else Row(
            Modifier
                .padding(vertical = 17.dp, horizontal = 14.dp)
                .clickable {
                    if (phoneNumber.length < 6) {
                        context.resources
                            .getString(R.string.enter_a_valid_phone_number)
                            .toast()
                        return@clickable
                    }
                    timerSeconds = 30
                    viewModel.sendOTP(phoneNumber, countryCode)
                }
                .fillMaxWidth()
                .clip(RoundedCornerShape(13.dp))
                .background(MainColor)
                .padding(vertical = 15.dp), Arrangement.Center, Alignment.CenterVertically) {
            TextPoppinsSemiBold(stringResource(R.string.verify), true, size = 14)
        }
    }

    LaunchedEffect(Unit) {
        if (viewModel.isError) context.resources.getString(R.string.number_is_not_verified_in_truecaller)
            .toast()

        val json = getAllPhoneCode()
        countryCodeList.addAll(json)
        val data =
            json.firstOrNull { it.iso.lowercase() == ipDB.firstOrNull()?.countryCode?.lowercase() }
        countryCode = data?.code ?: ""
    }

    LaunchedEffect(timerSeconds) {
        if (timerSeconds > 0) {
            delay(1000L)
            timerSeconds--
        }
    }
}

@Composable
fun InstalledTrueCaller() {
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current as FragmentActivity
    val coroutine = rememberCoroutineScope()

    Row(
        Modifier
            .padding(vertical = 47.dp, horizontal = 14.dp)
            .clickable {
                if (isLoading) return@clickable
                coroutine.launch {
                    delay(3.seconds)
                    isLoading = false
                }
                isLoading = true
                startTrueCaller(context)
            }
            .fillMaxWidth()
            .clip(RoundedCornerShape(13.dp))
            .background(MainColor)
            .padding(vertical = 15.dp), Arrangement.Center, Alignment.CenterVertically) {

        if (isLoading) LoadingView(Modifier.size(32.dp))
        else TextPoppinsSemiBold(stringResource(R.string.verify_via_truecaller), size = 14)
    }
}