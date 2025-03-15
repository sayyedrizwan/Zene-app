package com.rizwansayyed.zene.ui.phoneverification

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.FragmentActivity
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.main.connect.view.VerifyPhoneNumberOTPView
import com.rizwansayyed.zene.ui.phoneverification.view.TrueCallerVerifyView
import com.rizwansayyed.zene.ui.phoneverification.view.VerifyPhoneNumberView
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.ui.view.ButtonArrowBack
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import com.rizwansayyed.zene.viewmodel.PhoneNumberViewModel
import com.truecaller.android.sdk.oAuth.TcSdk
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhoneVerificationActivity : FragmentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    private val viewModel: PhoneNumberViewModel by viewModels()

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
                    when (val v = viewModel.isPhoneNumberWasVerifiedLatest) {
                        ResponseResult.Empty -> {}
                        is ResponseResult.Error -> TextViewBold(
                            stringResource(R.string.error_while_connecting_please_try_again_later),
                            20, center = true
                        )

                        ResponseResult.Loading -> CircularLoadingView()
                        is ResponseResult.Success -> {
                            if (v.data) TextViewBold(
                                stringResource(R.string.number_already_verified_this_week),
                                20, center = true
                            ) else
                                FullVerify()
                        }
                    }

                    LaunchedEffect(Unit) { viewModel.checkLastOTPVerifiedInWeek() }

                    ButtonArrowBack(Modifier.align(Alignment.TopStart)) { finish() }
                }
            }
        }
    }

    @Composable
    fun FullVerify() {
        var isTrueCaller by remember { mutableStateOf(true) }
        val tooManyAttempt = stringResource(R.string.too_many_attempts_to_phone_number)

        if (isTrueCaller) {
            TrueCallerVerifyView(homeViewModel) {
                isTrueCaller = false
            }
        } else {
            when (val v = viewModel.phoneNumberVerify) {
                ResponseResult.Empty -> VerifyPhoneNumberView(viewModel)
                is ResponseResult.Error -> {}
                ResponseResult.Loading -> Box(
                    Modifier.fillMaxSize(), Alignment.Center
                ) {
                    CircularLoadingView()
                }

                is ResponseResult.Success -> {
                    if (v.data.status == true)
                        VerifyPhoneNumberOTPView(viewModel)
                    else
                        VerifyPhoneNumberView(viewModel)

                    LaunchedEffect(Unit) {
                        if (v.data.status == false && v.data.message?.contains("too many attempts") == true) {
                            tooManyAttempt.toast()
                        }
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            isTrueCaller = homeViewModel.trueCallerUtils.isTrueCallerInstalled()
            homeViewModel.trueCallerUtils.start(this@PhoneVerificationActivity)
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TcSdk.SHARE_PROFILE_REQUEST_CODE) {
            TcSdk.getInstance().onActivityResultObtained(this, requestCode, resultCode, data)
        }
    }

}