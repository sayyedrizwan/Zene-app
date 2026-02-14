package com.rizwansayyed.zene.ui.main.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.jakewharton.processphoenix.ProcessPhoenix
import com.rizwansayyed.zene.data.model.UserInfoResponse
import com.rizwansayyed.zene.ui.main.home.HomeSectionSelector.AI_MUSIC
import com.rizwansayyed.zene.ui.main.home.HomeSectionSelector.LUXE
import com.rizwansayyed.zene.ui.main.home.HomeSectionSelector.MUSIC
import com.rizwansayyed.zene.ui.main.home.HomeSectionSelector.MY_LIBRARY
import com.rizwansayyed.zene.ui.main.home.HomeSectionSelector.PODCAST
import com.rizwansayyed.zene.ui.main.home.HomeSectionSelector.RADIO
import com.rizwansayyed.zene.ui.main.home.HomeSectionSelector.VIDEO
import com.rizwansayyed.zene.ui.main.home.view.HomeAIView
import com.rizwansayyed.zene.ui.main.home.view.HomeMusicView
import com.rizwansayyed.zene.ui.main.home.view.HomeMyLibraryView
import com.rizwansayyed.zene.ui.main.home.view.HomePodcastView
import com.rizwansayyed.zene.ui.main.home.view.HomeRadioView
import com.rizwansayyed.zene.ui.main.home.view.HomeScreenTopView
import com.rizwansayyed.zene.ui.main.home.view.HomeVideoView
import com.rizwansayyed.zene.ui.main.lux.LuxView
import com.rizwansayyed.zene.utils.safeLaunch
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import com.rizwansayyed.zene.viewmodel.NavigationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
                LUXE -> LuxView()
                VIDEO -> HomeVideoView(homeViewModel)
                AI_MUSIC -> HomeAIView(homeViewModel)
                MY_LIBRARY -> HomeMyLibraryView()
            }
        }
    }

    LaunchedEffect(Unit) {
        homeViewModel.homeRecentData {
            CoroutineScope(Dispatchers.IO).safeLaunch {
                delay(5.seconds)
                ProcessPhoenix.triggerRebirth(context)
            }
        }
    }
}