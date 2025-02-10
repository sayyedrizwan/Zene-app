package com.rizwansayyed.zene.ui.main.connect.profile

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.ui.view.ItemCardView
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@Composable
fun ConnectTopListenedView(topSongs: List<ZeneMusicData>) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    Spacer(Modifier.height(30.dp))
    when (val v = homeViewModel.homeRecent) {
        ResponseResult.Empty -> {}
        is ResponseResult.Error -> {}
        ResponseResult.Loading -> {}
        is ResponseResult.Success -> {
            if (v.data.topSongs?.isNotEmpty() == true) {
                TextViewBold(stringResource(R.string.top_played_songs_this_month), 20)
                Spacer(Modifier.height(12.dp))

                LazyRow(Modifier.fillMaxWidth()) {
                    items(topSongs) {
                        ItemCardView(it)
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        homeViewModel.homeRecentData {}
    }
}