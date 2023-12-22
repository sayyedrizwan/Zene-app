package com.rizwansayyed.zene.presenter.ui.home.feed

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.artistsfeed.FeedPostType
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.ui.home.feed.view.FeedNewsItem
import com.rizwansayyed.zene.presenter.ui.home.feed.view.FeedYoutubeItem
import com.rizwansayyed.zene.presenter.ui.home.feed.view.PinnedArtistsList
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.TOTAL_ITEMS_GRID
import com.rizwansayyed.zene.service.workmanager.ArtistsInfoWorkManager.Companion.startArtistsInfoWorkManager
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel


@Composable
fun ArtistsFeedView() {
    val roomDb: RoomDbViewModel = hiltViewModel()

    val artistsList by roomDb.pinnedArtists.collectAsState(emptyList())
    val feeds by roomDb.artistsFeeds.collectAsState(emptyList())

    LazyVerticalGrid(
        GridCells.Fixed(TOTAL_ITEMS_GRID), Modifier
            .fillMaxSize()
            .background(DarkGreyColor)
    ) {
        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            FeedText()
        }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Spacer(Modifier.height(20.dp))
        }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            LazyRow(Modifier.fillMaxWidth()) {
                items(artistsList) {
                    PinnedArtistsList(it)
                }
            }
        }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Spacer(Modifier.height(20.dp))
        }

        items(feeds, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            when (it.feedType) {
                FeedPostType.INSTAGRAM -> {}
                FeedPostType.INSTAGRAM_STORIES -> {}
                FeedPostType.FACEBOOK -> {}
                FeedPostType.YOUTUBE -> FeedYoutubeItem(it)
                FeedPostType.SHORTS -> {}
                FeedPostType.NEWS -> FeedNewsItem(it)
                null -> {}
            }
        }
    }

    LaunchedEffect(Unit) {
        roomDb.artistsFeeds()
        startArtistsInfoWorkManager()
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
