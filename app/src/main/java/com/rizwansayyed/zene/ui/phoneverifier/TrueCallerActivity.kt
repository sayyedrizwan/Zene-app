package com.rizwansayyed.zene.ui.phoneverifier

import android.content.Intent
import android.os.Bundle
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
import com.rizwansayyed.zene.ui.phoneverifier.TrueCallerUtils.codeVerifier
import com.rizwansayyed.zene.ui.phoneverifier.TrueCallerUtils.initTrueCaller
import com.rizwansayyed.zene.ui.phoneverifier.view.TopPhoneVerifierView
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.utils.Utils.toast
import com.rizwansayyed.zene.viewmodel.PhoneVerificationViewModel
import com.truecaller.android.sdk.oAuth.TcOAuthCallback
import com.truecaller.android.sdk.oAuth.TcOAuthData
import com.truecaller.android.sdk.oAuth.TcOAuthError
import com.truecaller.android.sdk.oAuth.TcSdk
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TrueCallerActivity : FragmentActivity() {

    private val viewModel: PhoneVerificationViewModel by viewModels()

    private val tcCallback: TcOAuthCallback = object : TcOAuthCallback {
        override fun onSuccess(tcOAuthData: TcOAuthData) {
            viewModel.getNumber(tcOAuthData.authorizationCode, codeVerifier)
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
                        .background(Color.Black), Alignment.Center
                ) {
                    TopPhoneVerifierView(viewModel)

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
        if (requestCode == TcSdk.SHARE_PROFILE_REQUEST_CODE) TcSdk.getInstance()
            .onActivityResultObtained(this, requestCode, resultCode, data)
    }
}
