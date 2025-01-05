package com.rizwansayyed.zene.ui.main.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.data.model.UserInfoResponse
import com.rizwansayyed.zene.ui.main.home.view.HomeMusicView
import com.rizwansayyed.zene.ui.main.home.view.HomeRadioView
import com.rizwansayyed.zene.ui.main.home.view.HomeScreenTopView
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import com.rizwansayyed.zene.viewmodel.NavigationViewModel

@Composable
fun HomeView(viewModel: NavigationViewModel, userInfo: UserInfoResponse?) {
    val homeViewModel: HomeViewModel = hiltViewModel()

    Column(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .weight(1.2f)
                .fillMaxWidth()
        ) {
            HomeScreenTopView(viewModel, userInfo)
        }

        Box(
            Modifier
                .weight(8.8f)
                .fillMaxWidth()
        ) {
            if (viewModel.homeSection == HomeSectionSelector.MUSIC) {
                HomeMusicView(homeViewModel)
            } else if (viewModel.homeSection == HomeSectionSelector.RADIO) {
                HomeRadioView(homeViewModel)
            }
        }
    }

    LaunchedEffect(Unit) {
        homeViewModel.homeRecentData()
    }
}