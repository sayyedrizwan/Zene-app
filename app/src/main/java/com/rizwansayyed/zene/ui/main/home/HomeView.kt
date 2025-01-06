package com.rizwansayyed.zene.ui.main.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.jakewharton.processphoenix.ProcessPhoenix
import com.rizwansayyed.zene.data.model.UserInfoResponse
import com.rizwansayyed.zene.ui.main.home.HomeSectionSelector.MUSIC
import com.rizwansayyed.zene.ui.main.home.HomeSectionSelector.MY_LIBRARY
import com.rizwansayyed.zene.ui.main.home.HomeSectionSelector.PODCAST
import com.rizwansayyed.zene.ui.main.home.HomeSectionSelector.RADIO
import com.rizwansayyed.zene.ui.main.home.view.HomeMusicView
import com.rizwansayyed.zene.ui.main.home.view.HomePodcastView
import com.rizwansayyed.zene.ui.main.home.view.HomeRadioView
import com.rizwansayyed.zene.ui.main.home.view.HomeScreenTopView
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import com.rizwansayyed.zene.viewmodel.NavigationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


@Composable
fun HomeView(viewModel: NavigationViewModel, userInfo: UserInfoResponse?) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val context = LocalContext.current.applicationContext

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
            when (viewModel.homeSection) {
                MUSIC -> HomeMusicView(homeViewModel)
                RADIO -> HomeRadioView(homeViewModel)
                PODCAST -> HomePodcastView(homeViewModel)
                MY_LIBRARY -> {}
            }
        }
    }

    LaunchedEffect(Unit) {
        homeViewModel.homeRecentData {
            CoroutineScope(Dispatchers.IO).launch {
                delay(3.seconds)
                ProcessPhoenix.triggerRebirth(context)
            }
        }
        homeViewModel.homePodcastData()
        homeViewModel.homeRadioData()
    }
}