package com.rizwansayyed.zene.ui.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.view.ArtistsCardView
import com.rizwansayyed.zene.ui.view.FeedNewsItemView
import com.rizwansayyed.zene.ui.view.LoadingArtistsCardView
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.viewmodel.ZeneViewModel

@Composable
fun FeedView(viewModel: ZeneViewModel) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(DarkCharcoal)
    ) {
        item(1) {
            when (val v = viewModel.feedItems) {
                APIResponse.Empty -> {}
                is APIResponse.Error -> {}
                APIResponse.Loading -> {
                    Column {
                        Spacer(Modifier.height(90.dp))
                        Row(Modifier.padding(horizontal = 7.dp)) {
                            TextPoppins(stringResource(R.string.following), size = 24)
                        }
                        Spacer(Modifier.height(30.dp))
                        LazyRow(Modifier.fillMaxWidth()) {
                            items(10) {
                                LoadingArtistsCardView()
                            }
                        }
                    }
                }

                is APIResponse.Success -> {
                    if (v.data.artists?.isNotEmpty() == true) Column {
                        Spacer(Modifier.height(90.dp))
                        Row(Modifier.padding(horizontal = 7.dp)) {
                            TextPoppins(stringResource(R.string.following), size = 24)
                        }
                        Spacer(Modifier.height(30.dp))
                        LazyRow(Modifier.fillMaxWidth()) {
                            items(v.data.artists) {
                                ArtistsCardView(it, v.data.artists)
                            }
                        }
                    }
                }
            }
        }

        when (val v = viewModel.feedItems) {
            APIResponse.Empty -> {}
            is APIResponse.Error -> {}
            APIResponse.Loading -> {}
            is APIResponse.Success -> {
                if (v.data.artists?.isNotEmpty() == true) {
                    item(3) {
                        Spacer(Modifier.height(40.dp))
                        Row(Modifier.padding(horizontal = 7.dp)) {
                            TextPoppins(stringResource(R.string.news), size = 24)
                        }
                        Spacer(Modifier.height(10.dp))
                    }

                    items(v.data.posts ?: emptyList()) {
                        FeedNewsItemView(it)
                    }

                    item(3) {
                        Spacer(Modifier.height(140.dp))
                    }
                } else
                    item(5) {
                        Spacer(Modifier.height(190.dp))
                        TextPoppins(
                            stringResource(R.string.you_haven_t_follow_artists_yet), true, size = 17
                        )
                        Spacer(Modifier.height(30.dp))
                    }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.startGettingFeed()
    }
}