package com.rizwansayyed.zene.ui.main.connect.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.rizwansayyed.zene.ui.view.ItemCardView
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@Composable
fun ConnectTopListenedView() {
    val homeViewModel: HomeViewModel = hiltViewModel()
    Spacer(Modifier.height(30.dp))
    when (val v = homeViewModel.homeRecent) {
        ResponseResult.Empty -> {}
        is ResponseResult.Error -> {}
        ResponseResult.Loading -> {}
        is ResponseResult.Success -> {
            Box(Modifier.padding(horizontal = 6.dp)) {
                TextViewBold(stringResource(R.string.most_played_songs_this_month), 23)
            }
            Spacer(Modifier.height(12.dp))
            LazyRow(Modifier.fillMaxWidth()) {
                items(v.data.topSongs ?: emptyList()) {
                    ItemCardView(it)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        homeViewModel.homeRecentData {

        }
    }
}