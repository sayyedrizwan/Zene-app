package com.rizwansayyed.zene.ui.phoneverifier.view

import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.phoneverifier.TrueCallerUtils
import com.rizwansayyed.zene.ui.phoneverifier.TrueCallerUtils.startTrueCaller
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.LoadingView
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


//prelude

@Composable
fun TopPhoneVerifierView() {
    var isTrueCallerInstalled by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp)
    ) {
        TextPoppinsSemiBold(stringResource(R.string.phone_verification), size = 29)
        Spacer(Modifier.height(5.dp))
        TextPoppins(stringResource(R.string.phone_verification_zene_connect), size = 15)

        if (isTrueCallerInstalled) InstalledTrueCaller()
        else NumberOTPField()
    }

    LaunchedEffect(Unit) {
        isTrueCallerInstalled = TrueCallerUtils.isTrueCallerInstalled()
    }
}

@Composable
fun NumberOTPField() {
    Row(
        Modifier
            .padding(vertical = 47.dp, horizontal = 14.dp)
            .clickable {}
            .fillMaxWidth()
            .clip(RoundedCornerShape(13.dp))
            .background(MainColor)
            .padding(vertical = 15.dp),
        Arrangement.Center,
        Alignment.CenterVertically) {

        Box(
            Modifier
                .padding(10.dp)
                .background(MainColor)
                .clip(RoundedCornerShape(13.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp),
            Alignment.Center
        ) {

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