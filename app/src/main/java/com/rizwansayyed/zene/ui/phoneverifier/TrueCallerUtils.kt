package com.rizwansayyed.zene.ui.phoneverifier

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.fragment.app.FragmentActivity
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.utils.Utils.toast
import com.truecaller.android.sdk.oAuth.CodeVerifierUtil
import com.truecaller.android.sdk.oAuth.TcOAuthCallback
import com.truecaller.android.sdk.oAuth.TcSdk
import com.truecaller.android.sdk.oAuth.TcSdkOptions
import java.math.BigInteger
import java.security.SecureRandom

object TrueCallerUtils {

    private var codeVerifier: String = ""

    fun initTrueCaller(context: Context, tcCallback: TcOAuthCallback) {
        val tcSdkOptions =
            TcSdkOptions.Builder(context, tcCallback).buttonColor(MainColor.toArgb())
                .buttonTextColor(Color.White.toArgb())
                .consentHeadingOption(TcSdkOptions.SDK_CONSENT_HEADING_PROCEED_WITH)
                .loginTextPrefix(TcSdkOptions.LOGIN_TEXT_PREFIX_TO_PROCEED)
                .ctaText(TcSdkOptions.CTA_TEXT_CONTINUE)
                .buttonShapeOptions(TcSdkOptions.BUTTON_SHAPE_ROUNDED)
                .footerType(TcSdkOptions.FOOTER_TYPE_SKIP)
                .sdkOptions(TcSdkOptions.OPTION_VERIFY_ALL_USERS).build()

        TcSdk.init(tcSdkOptions)
    }

    fun startTrueCaller(context: FragmentActivity) {
        TcSdk.getInstance().setOAuthScopes(arrayOf("phone"))
        val stateRequested = BigInteger(130, SecureRandom()).toString(32)
        TcSdk.getInstance().setOAuthState(stateRequested)

        codeVerifier = CodeVerifierUtil.generateRandomCodeVerifier()

        val codeChallenge = CodeVerifierUtil.getCodeChallenge(codeVerifier)
        codeChallenge?.let { TcSdk.getInstance().setCodeChallenge(it) }
            ?: "Code challenge is Null. Can’t proceed further".toast()

        TcSdk.getInstance().getAuthorizationCode(context)
    }

    fun isTrueCallerInstalled(): Boolean {
        try {
            context.packageManager.getPackageInfo("com.truecaller", 0)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
    }

}