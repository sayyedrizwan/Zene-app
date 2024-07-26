package com.rizwansayyed.zene.ui.artists

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.ui.artists.view.ArtistsTopView
import com.rizwansayyed.zene.ui.artists.view.ArtistsTopViewLoading
import com.rizwansayyed.zene.ui.artists.view.FollowArtists
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.viewmodel.ZeneViewModel

@Composable
fun ArtistsView(viewModel: ZeneViewModel, id: String?, close: () -> Unit) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(DarkCharcoal)
    ) {
        item(1) {
            Row(Modifier.padding(top = 50.dp, start = 8.dp, bottom = 25.dp)) {
                ImageIcon(R.drawable.ic_arrow_left, close)

                Spacer(Modifier.weight(1f))
            }
        }

        when (val v = viewModel.artistsInfo) {
            APIResponse.Empty -> {}
            is APIResponse.Error -> {}
            APIResponse.Loading -> item(2) {
                ArtistsTopViewLoading()
            }

            is APIResponse.Success -> {
                item(3) {
                    ArtistsTopView(v.data)
                }
                item(4) {
                    FollowArtists(v.data, viewModel)
                }
            }


        }

        item(1000) {
            Spacer(Modifier.height(200.dp))
        }
    }

    LaunchedEffect(Unit) {
        if (id == null) close()
        else {
            viewModel.artistsInfo(id)
            viewModel.artistsData(id)
        }
    }

    BackHandler {
        close()
    }

}