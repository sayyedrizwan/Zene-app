package com.rizwansayyed.zene.ui.phoneverifier

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.FragmentActivity
import com.rizwansayyed.zene.ui.phoneverifier.TrueCallerUtils.initTrueCaller
import com.rizwansayyed.zene.ui.phoneverifier.view.TopPhoneVerifierView
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.utils.Utils.toast
import com.rizwansayyed.zene.viewmodel.PhoneVerificationViewModel
import com.truecaller.android.sdk.common.TrueException
import com.truecaller.android.sdk.common.VerificationCallback
import com.truecaller.android.sdk.common.VerificationDataBundle
import com.truecaller.android.sdk.common.models.TrueProfile
import com.truecaller.android.sdk.oAuth.TcOAuthCallback
import com.truecaller.android.sdk.oAuth.TcOAuthData
import com.truecaller.android.sdk.oAuth.TcOAuthError
import com.truecaller.android.sdk.oAuth.TcSdk
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrueCallerActivity : FragmentActivity() {

    private val viewModel: PhoneVerificationViewModel by viewModels()
    private var codeVerifier: String? = null

    private val tcCallback: TcOAuthCallback = object : TcOAuthCallback {
        override fun onSuccess(tcOAuthData: TcOAuthData) {
            viewModel.getNumber(tcOAuthData.authorizationCode, codeVerifier ?: "")
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
                        .background(Color.Black),
                    Alignment.Center
                ) {
                    TopPhoneVerifierView()

                    LaunchedEffect(Unit) {
                        initTrueCaller(this@TrueCallerActivity, tcCallback)
                    }
                }
            }
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

        var verificationCallback: VerificationCallback? = null

        verificationCallback = object : VerificationCallback {
            override fun onRequestSuccess(
                callbackType: Int, verificationDataBundle: VerificationDataBundle?
            ) {
                when (callbackType) {
                    VerificationCallback.TYPE_MISSED_CALL_INITIATED -> {
                        if (verificationDataBundle != null) {
                            val key =
                                verificationDataBundle.getString(VerificationDataBundle.KEY_TTL)
                            val key2 =
                                verificationDataBundle.getString(VerificationDataBundle.KEY_REQUEST_NONCE)

                            Log.d("TAG", "onRequestSuccess: data $key $key2")
                        }
                    }

                    VerificationCallback.TYPE_MISSED_CALL_RECEIVED -> {
                        val profile = TrueProfile.Builder("Shabnam", "Sayyed").build()
                        TcSdk.getInstance().verifyMissedCall(profile, verificationCallback!!)
                        Log.d("TAG", "onRequestSuccess: data on about")
                    }

                    VerificationCallback.TYPE_IM_OTP_INITIATED -> {
                        if (verificationDataBundle != null) {
                            val ttl =
                                verificationDataBundle.getString(VerificationDataBundle.KEY_TTL);
                            val requestNonce =
                                verificationDataBundle.getString(VerificationDataBundle.KEY_REQUEST_NONCE)

                            Log.d("TAG", "onRequestSuccess: data on about qq $ttl $requestNonce")
                        }
                    }

                    VerificationCallback.TYPE_IM_OTP_RECEIVED -> {
                        val otp = verificationDataBundle?.getString(VerificationDataBundle.KEY_OTP)
                        val profile = TrueProfile.Builder("Shabnam", "Sayyed").build()
                        Log.d("TAG", "onRequestSuccess: data on about xxxa $otp")
                        TcSdk.getInstance().verifyOtp(profile, otp ?: "", verificationCallback!!)
                    }

                    VerificationCallback.TYPE_VERIFICATION_COMPLETE -> {
                        Log.d("TAG", "onRequestSuccess: data on about donne ver")
                    }

                    VerificationCallback.TYPE_PROFILE_VERIFIED_BEFORE -> {
                        Log.d("TAG", "onRequestSuccess: data on about donne before")
                    }

                    VerificationCallback.TYPE_OTP_INITIATED -> {
                        Log.d("TAG", "onRequestSuccess: data on about donne before otp iin")
                    }

                    VerificationCallback.TYPE_OTP_RECEIVED -> {
                        Log.d("TAG", "onRequestSuccess: data on about donne before otp rec")
                    }
                }
            }

            override fun onRequestFailure(p0: Int, p1: TrueException) {
                p1.exceptionMessage.toast()
            }

        }

//        TcSdk.getInstance().requestVerification("IN", "9323922026", verificationCallback, this);

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
