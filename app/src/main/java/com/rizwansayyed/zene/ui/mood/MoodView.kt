package com.rizwansayyed.zene.ui.mood

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.ui.home.view.HorizontalSongView
import com.rizwansayyed.zene.ui.home.view.StyleSize
import com.rizwansayyed.zene.ui.home.view.TextSize
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.LoadingCardView
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.utils.ShowAdsOnAppOpen
import com.rizwansayyed.zene.utils.Utils.TOTAL_GRID_SIZE
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@Composable
fun MoodView(homeViewModel: HomeViewModel, id: String?, close: () -> Unit) {
    val context = LocalContext.current as Activity

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

        when (val v = homeViewModel.moodPlaylist) {
            APIResponse.Empty -> {}
            is APIResponse.Error -> {}
            APIResponse.Loading -> items(7) {
                LazyRow {
                    items(5) {
                        LoadingCardView()
                    }
                }
            }

            is APIResponse.Success -> {
                item(3) {
                    Box(
                        Modifier
                            .padding(horizontal = 14.dp)
                            .padding(top = 15.dp, bottom = 25.dp)) {
                        TextPoppins(v.data.name ?: "", size = 36)
                    }
                }

                items(v.data.list) {
                    Row(
                        Modifier
                            .padding(horizontal = 9.dp)
                            .offset(y = 18.dp)
                    ) {
                        TextPoppinsSemiBold(it.name ?: "", size = 15)
                    }
                    HorizontalSongView(
                        APIResponse.Success(it.list),
                        Pair(TextSize.SMALL, R.string.empty),
                        StyleSize.HIDE_AUTHOR,
                        showGrid = true
                    )

                    Spacer(Modifier.height(15.dp))
                }
            }
        }

    }

    LaunchedEffect(Unit) {
        if (id == null) close()
        else {
            ShowAdsOnAppOpen(context).interstitialAds()
            homeViewModel.moodPlaylists(id)
        }
    }

    BackHandler {
        close()
    }
}