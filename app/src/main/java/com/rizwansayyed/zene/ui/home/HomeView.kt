package com.rizwansayyed.zene.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.ui.home.view.HomeHeaderView
import com.rizwansayyed.zene.ui.home.view.RecommendedAlbumsView
import com.rizwansayyed.zene.ui.home.view.RecommendedPlaylistView
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.view.LoadingCardView
import com.rizwansayyed.zene.ui.view.SimpleCardsView
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsThin
import com.rizwansayyed.zene.viewmodel.HomeViewModel


@Composable
fun HomeView() {
    val homeViewModel: HomeViewModel = viewModel()
    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(DarkCharcoal)
    ) {
        item(key = 1) {
            HomeHeaderView()
        }
        item(key = 2) {
            RecommendedPlaylistView(homeViewModel)
        }
        item(key = 3) {
            Spacer(Modifier.height(60.dp))
        }
        item(key = 4) {
            RecommendedAlbumsView(homeViewModel)
        }
        item(key = 5) {
            Spacer(Modifier.height(60.dp))
        }
        item(key = 1000) {
            Spacer(Modifier.height(100.dp))
        }

    }

    LaunchedEffect(Unit) {
        homeViewModel.recommendedPlaylists()
        homeViewModel.recommendedAlbums()
    }
}