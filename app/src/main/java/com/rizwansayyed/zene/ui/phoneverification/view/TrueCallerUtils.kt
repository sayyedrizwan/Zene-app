package com.rizwansayyed.zene.ui.phoneverification.view

import android.app.Activity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.truecaller.android.sdk.oAuth.TcOAuthCallback
import com.truecaller.android.sdk.oAuth.TcOAuthData
import com.truecaller.android.sdk.oAuth.TcOAuthError
import com.truecaller.android.sdk.oAuth.TcSdk
import com.truecaller.android.sdk.oAuth.TcSdkOptions

class TrueCallerUtils(private val activity: Activity) {

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

    fun start() {
        TcSdk.clear()
        val tcSdkOptions =
            TcSdkOptions.Builder(activity, tcOAuthCallback).buttonColor(MainColor.toArgb())
                .buttonTextColor(Color.White.toArgb())
                .loginTextPrefix(TcSdkOptions.SDK_CONSENT_HEADING_PROCEED_WITH)
                .ctaText(TcSdkOptions.CTA_TEXT_CONTINUE)
                .buttonShapeOptions(TcSdkOptions.BUTTON_SHAPE_ROUNDED)
                .footerType(TcSdkOptions.FOOTER_TYPE_SKIP).consentTitleOption(R.string.home)
                .sdkOptions(TcSdkOptions.OPTION_VERIFY_ONLY_TC_USERS).build()

        TcSdk.init(tcSdkOptions)
    }
}