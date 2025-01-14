package com.rizwansayyed.zene.ui.phoneverification.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.view.ButtonHeavy
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun TrueCallerVerifyView(homeViewModel: HomeViewModel, showNumber: () -> Unit) {
    val activity = LocalContext.current as FragmentActivity
    val trueCallerUtils = homeViewModel.trueCallerUtils
    val errorVerifyingNumber = stringResource(R.string.error_while_verifying_phone_number_via_truecaller)

    Box(
        Modifier
            .fillMaxSize()
            .padding(10.dp), Alignment.Center
    ) {
        Column(Modifier.fillMaxWidth()) {
            TextViewSemiBold(stringResource(R.string.verify_phone_number), 19)
            Spacer(Modifier.height(7.dp))
            TextViewNormal(stringResource(R.string.verify_phone_number_via_truecaller_desc), 16)
            Spacer(Modifier.height(55.dp))

            when (val v = trueCallerUtils.info) {
                ResponseResult.Empty, is ResponseResult.Error -> {
                    ButtonHeavy(stringResource(R.string.verify)) {
                        trueCallerUtils.invoke(activity)
                    }
                    LaunchedEffect(Unit) {
                        if (trueCallerUtils.info is ResponseResult.Error) {
                            "Error truecaller, Try Again.".toast()
                        }
                    }
                }

                ResponseResult.Loading -> CircularLoadingView()
                is ResponseResult.Success -> {
                    LaunchedEffect(Unit) {
                        if (v.data) {
                            homeViewModel.userInfo()
                            delay(2.seconds)
                            activity.finish()
                        } else {
                            errorVerifyingNumber.toast()
                            showNumber()
                        }
                    }
                }
            }

        }
    }
}