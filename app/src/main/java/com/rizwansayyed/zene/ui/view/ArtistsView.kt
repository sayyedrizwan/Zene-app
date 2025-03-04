package com.rizwansayyed.zene.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.utils.NavigationUtils
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_MAIN_PAGE
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@Composable
fun ArtistsView(artistsID: String) {
    val viewModel: HomeViewModel = hiltViewModel()

    when (val v = viewModel.artistsInfo) {
        ResponseResult.Empty -> {}
        is ResponseResult.Error -> {}
        ResponseResult.Loading -> CircularLoadingView()
        is ResponseResult.Success -> {

        }
    }


    LaunchedEffect(Unit) {
        if (artistsID.length <= 3) {
            NavigationUtils.triggerHomeNav(NAV_MAIN_PAGE)
            return@LaunchedEffect
        }

        if (viewModel.artistsInfo !is ResponseResult.Success)
            viewModel.artistsInfo(artistsID)
    }
}