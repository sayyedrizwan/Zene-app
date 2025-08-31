package com.rizwansayyed.zene.ui.phoneverification.view

import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.fragment.app.FragmentActivity
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.implementation.ZeneAPIInterface
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.utils.safeLaunch
import com.truecaller.android.sdk.oAuth.CodeVerifierUtil
import com.truecaller.android.sdk.oAuth.TcOAuthCallback
import com.truecaller.android.sdk.oAuth.TcOAuthData
import com.truecaller.android.sdk.oAuth.TcOAuthError
import com.truecaller.android.sdk.oAuth.TcSdk
import com.truecaller.android.sdk.oAuth.TcSdkOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import java.math.BigInteger
import java.security.SecureRandom
import javax.inject.Inject

class TrueCallerUtils @Inject constructor(private val zeneAPI: ZeneAPIInterface) {

    private var codeVerifier: String? = null
    var info by mutableStateOf<ResponseResult<Boolean>>(ResponseResult.Empty)

    private val tcOAuthCallback = object : TcOAuthCallback {
        override fun onSuccess(tcOAuthData: TcOAuthData) {
            CoroutineScope(Dispatchers.IO).safeLaunch {
                zeneAPI.updateTrueCallerNumber(
                    codeVerifier.toString(), tcOAuthData.authorizationCode
                ).catch {
                    info = ResponseResult.Error(it)
                }.collectLatest {
                    info = ResponseResult.Success(it.status ?: false)
                }

                if (isActive) cancel()
            }
        }

        override fun onVerificationRequired(tcOAuthError: TcOAuthError?) {
            info = ResponseResult.Error(Throwable(tcOAuthError?.errorMessage))
        }

        override fun onFailure(tcOAuthError: TcOAuthError) {
            info = ResponseResult.Error(Throwable(tcOAuthError.errorMessage))
        }
    }

    suspend fun start(activity: FragmentActivity) {
        if (!isTrueCallerInstalled()) return
        TcSdk.clear()
        val tcSdkOptions =
            TcSdkOptions.Builder(activity, tcOAuthCallback).buttonColor(MainColor.toArgb())
                .buttonTextColor(Color.White.toArgb())
                .loginTextPrefix(TcSdkOptions.LOGIN_TEXT_PREFIX_TO_CONTINUE)
                .ctaText(TcSdkOptions.CTA_TEXT_CONTINUE)
                .buttonShapeOptions(TcSdkOptions.BUTTON_SHAPE_ROUNDED)
                .footerType(TcSdkOptions.FOOTER_TYPE_SKIP).consentTitleOption(R.string.home)
                .sdkOptions(TcSdkOptions.OPTION_VERIFY_ONLY_TC_USERS).build()

        TcSdk.init(tcSdkOptions)

        delay(500)
        oauth()
    }

    fun invoke(activity: FragmentActivity) {
        info = ResponseResult.Loading
        TcSdk.getInstance().getAuthorizationCode(activity)
    }

    private fun oauth() {
        TcSdk.getInstance().setOAuthScopes(arrayOf("phone", "openid"))
        val stateRequested = BigInteger(130, SecureRandom()).toString(32)
        TcSdk.getInstance().setOAuthState(stateRequested)
        codeVerifier = CodeVerifierUtil.generateRandomCodeVerifier()
        val codeChallenge = CodeVerifierUtil.getCodeChallenge(codeVerifier!!)
        codeChallenge?.let { TcSdk.getInstance().setCodeChallenge(it) }
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