package com.rizwansayyed.zene.presenter.ui.home.feed

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.artistsfeed.FeedPostType
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.ui.GlobalNativeFullAds
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.home.feed.view.FeedNewsItem
import com.rizwansayyed.zene.presenter.ui.home.feed.view.FeedYoutubeItem
import com.rizwansayyed.zene.presenter.ui.home.feed.view.PinnedArtistsList
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.TOTAL_ITEMS_GRID
import com.rizwansayyed.zene.service.workmanager.ArtistsInfoWorkManager.Companion.startArtistsInfoWorkManager
import com.rizwansayyed.zene.utils.FirebaseEvents
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel


@Composable
fun ArtistsFeedView() {
    val roomDb: RoomDbViewModel = hiltViewModel()

    val artistsList by roomDb.pinnedArtists.collectAsState(emptyList())
    val feeds by roomDb.artistsFeeds.collectAsState(emptyList())


    if (artistsList.isEmpty()) Box(
        Modifier
            .fillMaxSize()
            .background(DarkGreyColor)
    ) {
        TextSemiBold(
            v = stringResource(R.string.no_pinned_artists_yet),
            Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(10.dp), true
        )

    } else LazyVerticalGrid(
        GridCells.Fixed(TOTAL_ITEMS_GRID), Modifier
            .fillMaxSize()
            .background(DarkGreyColor)
    ) {
        item(key = 1, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            FeedText()
        }

        item(key = 2, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Spacer(Modifier.height(20.dp))
        }

        item(key = 3, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            LazyRow(Modifier.fillMaxWidth()) {
                items(artistsList, key = { m -> m.id ?: "" }) { a ->
                    PinnedArtistsList(a)
                }
            }
        }

        item(key = 4, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Spacer(Modifier.height(20.dp))
        }

        if (feeds.isEmpty()) item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            TextSemiBold(
                v = stringResource(R.string.no_update_available_right_now),
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp), true
            )
        }

        itemsIndexed(
            feeds,
            key = { _, f -> f.id ?: 0 },
            span = { _, _ -> GridItemSpan(TOTAL_ITEMS_GRID) }) { i, f ->
            when (f.feedType) {
                FeedPostType.INSTAGRAM -> {}
                FeedPostType.INSTAGRAM_STORIES -> {}
                FeedPostType.FACEBOOK -> {}
                FeedPostType.YOUTUBE -> Column {
                    FeedYoutubeItem(f)
                    if (i % 3 == 0) GlobalNativeFullAds()
                }

                FeedPostType.SHORTS -> {}
                FeedPostType.NEWS -> Column {
                    FeedNewsItem(f)
                    if (i % 3 == 0) GlobalNativeFullAds()
                }

                null -> {}
            }
        }


        item(key = 100, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Spacer(Modifier.height(200.dp))
        }
    }

    LaunchedEffect(Unit) {
        roomDb.artistsFeeds()
        startArtistsInfoWorkManager()
        FirebaseEvents.registerEvent(FirebaseEvents.FirebaseEvent.OPEN_FEED)
    }
}

@Composable
fun FeedText() {
    TextBold(
        v = stringResource(R.string.feed),
        Modifier
            .padding(top = 55.dp)
            .padding(start = 15.dp)
            .fillMaxWidth(),
        size = 45
    )
}
