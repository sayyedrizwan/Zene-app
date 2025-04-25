package com.rizwansayyed.zene.ui.main.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.datastore.DataStorageManager.isPremiumDB
import com.rizwansayyed.zene.ui.view.ArtistsRoundList
import com.rizwansayyed.zene.ui.view.ItemArtistsCardView
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.utils.ads.NativeViewAdsCard
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@Composable
fun FeedView(viewModel: HomeViewModel) {
    val isPremium by isPremiumDB.collectAsState(true)
    var isNoArtists by remember { mutableStateOf(false) }

    LazyColumn(Modifier.fillMaxSize()) {
        item {
            Spacer(Modifier.height(30.dp))
            Box(Modifier.padding(horizontal = 9.dp)) {
                TextViewBold(stringResource(R.string.feed), 35)
            }
        }

        item {
            Spacer(Modifier.height(5.dp))
            if (isNoArtists) LazyRow(Modifier.fillMaxWidth()) {
                when (val v = viewModel.feedArtists) {
                    ResponseResult.Empty -> {}
                    is ResponseResult.Error -> {}
                    ResponseResult.Loading -> items(9) {
                        ArtistsRoundList()
                    }

                    is ResponseResult.Success -> {
                        item { isNoArtists = v.data.isEmpty() }

                        itemsIndexed(v.data) { i, z ->
                            ItemArtistsCardView(z)

                            if (!isPremium) {
                                if (i == 1) NativeViewAdsCard(z.id)
                                if ((i + 1) % 6 == 0) NativeViewAdsCard(z.id)
                            }
                        }
                    }
                }
            }
        }

        if (isNoArtists) {
            item {
                Spacer(Modifier.height(50.dp))
                Box(Modifier.padding(horizontal = 9.dp)) {
                    TextViewBold(stringResource(R.string.updates), 23)
                }
                Spacer(Modifier.height(10.dp))
            }
        } else item {
            Box(Modifier.padding(horizontal = 9.dp, vertical = 20.dp)) {
                TextViewBold(stringResource(R.string.follow_artists_updates), 16, center = true)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.artistsFollowedList()
    }
}