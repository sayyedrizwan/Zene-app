package com.rizwansayyed.zene.ui.phoneverification

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.fragment.app.FragmentActivity
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.phoneverification.view.TrueCallerVerifyView
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.truecaller.android.sdk.common.models.TrueProfile
import com.truecaller.android.sdk.legacy.ITrueCallback
import com.truecaller.android.sdk.legacy.TrueError
import com.truecaller.android.sdk.legacy.TruecallerSDK
import com.truecaller.android.sdk.legacy.TruecallerSdkScope
import com.truecaller.android.sdk.oAuth.CodeVerifierUtil
import com.truecaller.android.sdk.oAuth.TcOAuthCallback
import com.truecaller.android.sdk.oAuth.TcOAuthData
import com.truecaller.android.sdk.oAuth.TcOAuthError
import com.truecaller.android.sdk.oAuth.TcSdk
import com.truecaller.android.sdk.oAuth.TcSdkOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.math.BigInteger
import java.security.SecureRandom
import kotlin.time.Duration.Companion.seconds


@AndroidEntryPoint
class PhoneVerificationActivity : FragmentActivity() {

    private val tcOAuthCallback = object : TcOAuthCallback {
        override fun onSuccess(tcOAuthData: TcOAuthData) {
            tcOAuthData.authorizationCode.toast()
        }

        override fun onVerificationRequired(tcOAuthError: TcOAuthError?) {
            tcOAuthError?.errorMessage?.toast()
        }

        override fun onFailure(tcOAuthError: TcOAuthError) {
            tcOAuthError.errorMessage.toast()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZeneTheme {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {

                    if (isTrueCallerInstalled()) {
                        TrueCallerVerifyView()
                    }

                    LaunchedEffect(Unit) {
                        initTrueCaller()
                    }
                }
            }
        }
    }

    private suspend fun initTrueCaller() {
        if (!isTrueCallerInstalled()) return

        val tcSdkOptions =
            TcSdkOptions.Builder(this, tcOAuthCallback).buttonColor(MainColor.toArgb())
                .buttonTextColor(Color.White.toArgb())
                .loginTextPrefix(TcSdkOptions.SDK_CONSENT_HEADING_PROCEED_WITH)
                .ctaText(TcSdkOptions.CTA_TEXT_CONTINUE)
                .buttonShapeOptions(TcSdkOptions.BUTTON_SHAPE_ROUNDED)
                .footerType(TcSdkOptions.FOOTER_TYPE_SKIP).consentTitleOption(R.string.home)
                .sdkOptions(TcSdkOptions.OPTION_VERIFY_ONLY_TC_USERS).build()
        TcSdk.init(tcSdkOptions)

        TcSdk.getInstance().setOAuthScopes(arrayOf("phone"))

       val stateRequested = BigInteger(130, SecureRandom()).toString(32)
        TcSdk.getInstance().setOAuthState(stateRequested)

        val codeVerifier = CodeVerifierUtil.generateRandomCodeVerifier()
        val codeChallenge = CodeVerifierUtil.getCodeChallenge(codeVerifier)
        codeChallenge?.let { TcSdk.getInstance().setCodeChallenge(it) }
        TcSdk.getInstance().isOAuthFlowUsable
        delay(5.seconds)
        TcSdk.getInstance().getAuthorizationCode(this);
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TcSdk.SHARE_PROFILE_REQUEST_CODE) {
            TcSdk.getInstance().onActivityResultObtained(this, requestCode, resultCode, data)
        }
    }

    private fun isTrueCallerInstalled(): Boolean {
        try {
            this.packageManager.getPackageInfo("com.truecaller", 0)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
    }
}