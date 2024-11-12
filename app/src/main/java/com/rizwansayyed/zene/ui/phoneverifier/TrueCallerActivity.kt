package com.rizwansayyed.zene.ui.phoneverifier

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.fragment.app.FragmentActivity
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.utils.Utils.toast
import com.rizwansayyed.zene.viewmodel.PhoneVerificationViewModel
import com.truecaller.android.sdk.common.TrueException
import com.truecaller.android.sdk.common.VerificationCallback
import com.truecaller.android.sdk.common.VerificationDataBundle
import com.truecaller.android.sdk.oAuth.TcOAuthCallback
import com.truecaller.android.sdk.oAuth.TcOAuthData
import com.truecaller.android.sdk.oAuth.TcOAuthError
import com.truecaller.android.sdk.oAuth.TcSdk
import com.truecaller.android.sdk.oAuth.TcSdkOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrueCallerActivity : FragmentActivity() {

    private val viewModel: PhoneVerificationViewModel by viewModels()
    private var codeVerifier: String? = null

    private val tcOAuthCallback: TcOAuthCallback = object : TcOAuthCallback {
        override fun onSuccess(tcOAuthData: TcOAuthData) {
            viewModel.getNumber(tcOAuthData.authorizationCode, codeVerifier ?: "")
            Log.d("TAG", "onSuccess: runnnedd onnn ${tcOAuthData.authorizationCode}")
        }

        override fun onVerificationRequired(tcOAuthError: TcOAuthError?) {
            Log.d("TAG", "onSuccess: runnnedd verify")
        }

        override fun onFailure(tcOAuthError: TcOAuthError) {
            Log.d("TAG", "onSuccess: runnnedd faliure ${tcOAuthError.errorMessage}")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

        }
    }

    @Deprecated("")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TcSdk.SHARE_PROFILE_REQUEST_CODE) {
            TcSdk.getInstance().onActivityResultObtained(this, requestCode, resultCode, data)
        }
    }


    override fun onStart() {
        super.onStart()
        val tcSdkOptions = TcSdkOptions.Builder(this, tcOAuthCallback)
            .buttonColor(MainColor.toArgb())
            .buttonTextColor(Color.White.toArgb())
            .consentHeadingOption(TcSdkOptions.SDK_CONSENT_HEADING_PROCEED_WITH)
            .loginTextPrefix(TcSdkOptions.LOGIN_TEXT_PREFIX_TO_PROCEED)
            .ctaText(TcSdkOptions.CTA_TEXT_CONTINUE)
            .buttonShapeOptions(TcSdkOptions.BUTTON_SHAPE_ROUNDED)
            .footerType(TcSdkOptions.FOOTER_TYPE_SKIP)
            .sdkOptions(TcSdkOptions.OPTION_VERIFY_ALL_USERS)
            .build()

        TcSdk.init(tcSdkOptions)

        val isUsable = TcSdk.getInstance().isOAuthFlowUsable


        val verificationCallback = object : VerificationCallback {
            override fun onRequestSuccess(
                callbackType: Int,
                verificationDataBundle: VerificationDataBundle?
            ) {
                when (callbackType) {
                    VerificationCallback.TYPE_MISSED_CALL_INITIATED -> {
                        if (verificationDataBundle != null) {
                            verificationDataBundle.getString(VerificationDataBundle.KEY_TTL);
                            verificationDataBundle.getString(VerificationDataBundle.KEY_REQUEST_NONCE);
                        }
                    }

                    VerificationCallback.TYPE_MISSED_CALL_RECEIVED -> {

                    }

                    VerificationCallback.TYPE_IM_OTP_INITIATED -> {
                        if (verificationDataBundle != null) {
                            val ttl =
                                verificationDataBundle.getString(VerificationDataBundle.KEY_TTL);
                            val requestNonce =
                                verificationDataBundle.getString(VerificationDataBundle.KEY_REQUEST_NONCE);
                        }
                    }

                    VerificationCallback.TYPE_IM_OTP_RECEIVED -> {
                        val otp =
                            verificationDataBundle?.getString(VerificationDataBundle.KEY_OTP)
                    }

                    VerificationCallback.TYPE_VERIFICATION_COMPLETE -> {
                        //verification complete
                    }

                    VerificationCallback.TYPE_PROFILE_VERIFIED_BEFORE -> {
                        //user already verified

                    }

                    VerificationCallback.TYPE_OTP_INITIATED -> {

                    }

                    VerificationCallback.TYPE_OTP_RECEIVED -> {
                    }
                }
            }

            override fun onRequestFailure(p0: Int, p1: TrueException) {
                p1.exceptionMessage.toast()
            }

        }

        TcSdk.getInstance()
            .requestVerification("IN", "9323922026", verificationCallback, this);

//        if (isUsable) {
//            TcSdk.getInstance().setOAuthScopes(arrayOf("phone"))
//
//            val stateRequested = BigInteger(130, SecureRandom()).toString(32)
//            TcSdk.getInstance().setOAuthState(stateRequested)
//
//            codeVerifier = CodeVerifierUtil.generateRandomCodeVerifier()
//
//            val codeChallenge = CodeVerifierUtil.getCodeChallenge(codeVerifier!!)
//            codeChallenge?.let { TcSdk.getInstance().setCodeChallenge(it) }
//                ?: "Code challenge is Null. Can’t proceed further".toast()
//
//            lifecycleScope.launch(Dispatchers.Main) {
//                delay(2.seconds)
//                TcSdk.getInstance().getAuthorizationCode(this@TrueCallerActivity)
//            }
//        } else {
//            "no called".toast()
//        }

//        TrueCallerVerificationUtils(this).start()
    }

}
