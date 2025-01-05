package com.rizwansayyed.zene.ui.main.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rizwansayyed.zene.data.model.UserInfoResponse
import com.rizwansayyed.zene.ui.main.home.view.HomeScreenTopView
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import com.rizwansayyed.zene.viewmodel.NavigationViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeMusicView(
    viewModel: HomeViewModel, navViewModel: NavigationViewModel, userInfo: UserInfoResponse?
) {
    LazyColumn(Modifier.fillMaxSize()) {
        stickyHeader {
            HomeScreenTopView(navViewModel, userInfo)
        }

    }

}