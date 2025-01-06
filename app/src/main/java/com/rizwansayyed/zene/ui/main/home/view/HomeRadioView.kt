package com.rizwansayyed.zene.ui.main.home.view

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@Composable
fun HomeRadioView(homeViewModel: HomeViewModel) {
    LazyColumn(Modifier.fillMaxSize()) {
        when (val v = homeViewModel.homeRecent) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> item {
                Spacer(Modifier.height(50.dp))
                CircularLoadingView()
            }
            is ResponseResult.Success -> {

            }
        }
    }
}