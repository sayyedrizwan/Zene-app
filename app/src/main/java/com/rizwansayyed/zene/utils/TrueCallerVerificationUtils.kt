package com.rizwansayyed.zene.utils

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.fragment.app.FragmentActivity
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.utils.Utils.toast
import com.truecaller.android.sdk.oAuth.CodeVerifierUtil
import com.truecaller.android.sdk.oAuth.TcOAuthCallback
import com.truecaller.android.sdk.oAuth.TcOAuthData
import com.truecaller.android.sdk.oAuth.TcOAuthError
import com.truecaller.android.sdk.oAuth.TcSdk
import com.truecaller.android.sdk.oAuth.TcSdkOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.security.SecureRandom
import kotlin.time.Duration.Companion.seconds

class TrueCallerVerificationUtils(private val context: FragmentActivity) {
    fun start() = CoroutineScope(Dispatchers.Main).launch {
        delay(2.seconds)
        val isUsable = TcSdk.getInstance().isOAuthFlowUsable

        TcSdk.getInstance().getAuthorizationCode(context)
        if (isUsable) {
            "yes".toast()
        } else {
            "no".toast()
        }
    }
}