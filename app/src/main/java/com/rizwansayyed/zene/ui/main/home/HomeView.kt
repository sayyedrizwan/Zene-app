package com.rizwansayyed.zene.ui.main.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.UserInfoResponse
import com.rizwansayyed.zene.ui.main.home.view.HomeScreenTopView
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.ItemCardView
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import com.rizwansayyed.zene.viewmodel.NavigationViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeView(viewModel: NavigationViewModel, userInfo: UserInfoResponse?) {
    val homeViewModel: HomeViewModel = hiltViewModel()

    LazyColumn(Modifier.fillMaxSize()) {
        stickyHeader {
            HomeScreenTopView(viewModel, userInfo)
        }

        if (viewModel.homeSection == HomeSectionSelector.MUSIC) {
            item {
                if (homeViewModel.homeRecent is ResponseResult.Loading) {
                    Spacer(Modifier.height(50.dp))
                    CircularLoadingView()
                }
            }

            when (val v = homeViewModel.homeRecent) {
                ResponseResult.Empty -> {}
                is ResponseResult.Error -> {}
                ResponseResult.Loading -> {}
                is ResponseResult.Success -> {
                    item {
                        Spacer(Modifier.height(30.dp))
                        LazyRow(Modifier.fillMaxWidth()) {
                            items(v.data.topSongs ?: emptyList()) {
                                ItemCardView(it)
                            }
                        }
                    }

                    item {
                        Spacer(Modifier.height(30.dp))
                        TextViewNormal(stringResource(R.string.your_mixes), 16, line = 1)
                        Spacer(Modifier.height(10.dp))
                        LazyRow(Modifier.fillMaxWidth()) {
                            items(v.data.topPlaylists ?: emptyList()) {
                                ItemCardView(it)
                            }
                        }
                    }
                }
            }
        }
    }

//    when (viewModel.homeSection) {
//        MUSIC -> HomeMusicView(homeViewModel, viewModel, userInfo)
//        PODCAST -> HomePodcastView(homeViewModel, viewModel, userInfo)
//        RADIO -> HomeRadioView(homeViewModel, viewModel, userInfo)
//        MY_LIBRARY -> HomeMyLibraryView(homeViewModel, viewModel, userInfo)
//    }

    LaunchedEffect(Unit) {
        homeViewModel.homeRecentData()
    }
}

@Composable
fun HomeMusicView() {
    
}