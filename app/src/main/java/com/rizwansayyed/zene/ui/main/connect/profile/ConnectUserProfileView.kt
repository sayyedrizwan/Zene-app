package com.rizwansayyed.zene.ui.main.connect.profile

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.lifecycleScope
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.view.ButtonArrowBack
import com.rizwansayyed.zene.ui.view.FullUsersShimmerLoadingCard
import com.rizwansayyed.zene.ui.view.ShimmerEffect
import com.rizwansayyed.zene.utils.ads.InterstitialAdsUtils
import com.rizwansayyed.zene.utils.safeLaunch
import com.rizwansayyed.zene.viewmodel.ConnectViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectUserProfileView(email: String) {
    val connectViewModel: ConnectViewModel = hiltViewModel()
    var job by remember { mutableStateOf<Job?>(null) }
    val activity = LocalActivity.current

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        val screenHeight = LocalConfiguration.current.screenHeightDp.dp

        when (val v = connectViewModel.connectUserInfo) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> ConnectUserLoading()
            is ResponseResult.Success -> {
                BottomSheetScaffold(
                    { ConnectProfileDetailsView(v.data, connectViewModel) },
                    Modifier.fillMaxSize(),
                    sheetPeekHeight = (screenHeight * 0.6f),
                    sheetContentColor = Color.Black,
                    sheetContainerColor = Color.Black
                ) {
                    ConnectProfileProfilePhotoView(v.data.user)
                }
            }
        }

        ButtonArrowBack()
    }

    LifecycleResumeEffect(Unit) {
        job?.cancel()
        activity?.let { InterstitialAdsUtils(it) }
        job = lifecycleScope.safeLaunch {
            while (true) {
                connectViewModel.connectUserInfo(email)
                delay(9.seconds)
            }
        }
        onPauseOrDispose {
            job?.cancel()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectUserLoading() {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    BottomSheetScaffold(
        {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp)
            ) {
                FullUsersShimmerLoadingCard()
            }
        },
        Modifier.fillMaxSize(),
        sheetPeekHeight = (screenHeight * 0.6f),
        sheetContentColor = Color.Black,
        sheetContainerColor = Color.Black
    ) {
        Box(Modifier.fillMaxSize()) {
            ShimmerEffect(
                Modifier
                    .align(Alignment.TopCenter)
                    .padding(bottom = 10.dp)
                    .fillMaxWidth()
                    .height(screenHeight * 0.55f),
            )
        }
    }
}