package com.rizwansayyed.zene.ui.artists

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.SongsViewModel
import com.rizwansayyed.zene.ui.artists.artistviewmodel.ArtistsViewModel
import com.rizwansayyed.zene.ui.artists.view.ArtistsAlbum
import com.rizwansayyed.zene.ui.artists.view.ArtistsAllSongsView
import com.rizwansayyed.zene.ui.artists.view.ArtistsNews
import com.rizwansayyed.zene.ui.artists.view.InstagramPostsPosts
import com.rizwansayyed.zene.ui.artists.view.TopArtistsInfo
import com.rizwansayyed.zene.ui.artists.view.TopArtistsSongs
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavViewModel
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavigationStatus
import com.rizwansayyed.zene.ui.home.homeui.ArtistsViewSmallView
import com.rizwansayyed.zene.utils.QuickSandSemiBold
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun ArtistsInfo(
    artistsViewModel: ArtistsViewModel = hiltViewModel(),
    homeNavViewModel: HomeNavViewModel = hiltViewModel(),
    songsViewModel: SongsViewModel = hiltViewModel()
) {

    val listState = rememberLazyGridState()

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        artistsViewModel.toDefault()
    }

    LazyVerticalGrid(columns = GridCells.Fixed(2), Modifier.fillMaxSize(), state = listState) {
        item(span = { GridItemSpan(2) }) {
            TopArtistsInfo(artistsViewModel)
        }

        item(span = { GridItemSpan(2) }) {
            Spacer(Modifier.height(54.dp))
        }

        item(span = { GridItemSpan(2) }) {
            if (artistsViewModel.artistsTopSongs.isNotEmpty()) {
                QuickSandSemiBold(
                    stringResource(id = R.string.top_songs), Modifier.padding(5.dp), size = 19,
                )
            }
        }


        item(span = { GridItemSpan(2) }) {
            if (artistsViewModel.artistsTopSongs.isNotEmpty()) {
                LazyHorizontalGrid(GridCells.Fixed(3), modifier = Modifier.heightIn(max = 270.dp)) {
                    items(artistsViewModel.artistsTopSongs) {
                        TopArtistsSongs(it) { thumbnail, name ->
                            homeNavViewModel.showMusicPlayer()
                            songsViewModel
                                .songsPlayingDetails(thumbnail, name, artistsViewModel.artistName)
                        }
                    }
                }
            }
        }

        item(span = { GridItemSpan(2) }) {
            Spacer(Modifier.height(54.dp))
        }

        item(span = { GridItemSpan(2) }) {
            if (artistsViewModel.artistsAllTimeSongs.isNotEmpty())
                QuickSandSemiBold(
                    stringResource(id = R.string.recent_songs), Modifier.padding(5.dp), size = 19,
                )
        }

        item(span = { GridItemSpan(2) }) {
            if (artistsViewModel.artistsAllTimeSongs.isNotEmpty())
                LazyRow {
                    items(artistsViewModel.artistsAllTimeSongs) { artists ->
                        ArtistsAllSongsView(artists) { name, thumbnail ->
                            homeNavViewModel.showMusicPlayer()
                            songsViewModel
                                .songsPlayingDetails(thumbnail, name, artistsViewModel.artistName)
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                    }
                }
        }

        item(span = { GridItemSpan(2) }) {
            Spacer(Modifier.height(54.dp))
        }


        item(span = { GridItemSpan(2) }) {
            if (artistsViewModel.artistsTopAlbums.isNotEmpty())
                QuickSandSemiBold(
                    stringResource(id = R.string.top_albums), Modifier.padding(5.dp), size = 19,
                )
        }

        items(artistsViewModel.artistsTopAlbums) {
            ArtistsAlbum(it)
        }

        item(span = { GridItemSpan(2) }) {
            Spacer(Modifier.height(54.dp))
        }

        item(span = { GridItemSpan(2) }) {
            if (artistsViewModel.artistsInstagramPosts?.posts?.isNotEmpty() == true)
                QuickSandSemiBold(
                    stringResource(id = R.string.artist_insta_posts),
                    Modifier.padding(5.dp),
                    size = 19,
                )
        }

        item(span = { GridItemSpan(2) }) {
            if (artistsViewModel.artistsInstagramPosts?.posts != null)
                LazyHorizontalGrid(GridCells.Fixed(3), modifier = Modifier.heightIn(max = 970.dp)) {
                    items(artistsViewModel.artistsInstagramPosts?.posts!!) {
                        InstagramPostsPosts(it)
                    }
                }
        }

        item(span = { GridItemSpan(2) }) {
            Spacer(Modifier.height(54.dp))
        }

        item(span = { GridItemSpan(2) }) {
            if (artistsViewModel.artistsImages.isNotEmpty())
                QuickSandSemiBold(
                    stringResource(id = R.string.top_images), Modifier.padding(5.dp), size = 19,
                )
        }

        item(span = { GridItemSpan(2) }) {
            if (artistsViewModel.artistsImages.isNotEmpty())
                LazyRow {
                    items(artistsViewModel.artistsImages) { images ->
                        AsyncImage(
                            images, "",
                            Modifier
                                .padding(4.dp)
                                .size(270.dp)
                        )
                    }
                }
        }

        item(span = { GridItemSpan(2) }) {
            Spacer(Modifier.height(54.dp))
        }


        item(span = { GridItemSpan(2) }) {
            if (artistsViewModel.artistsNews.isNotEmpty())
                QuickSandSemiBold(
                    stringResource(id = R.string.trending_news), Modifier.padding(5.dp), size = 19,
                )
        }


        items(artistsViewModel.artistsNews, span = { GridItemSpan(2) }) {
            ArtistsNews(it)
        }

        item(span = { GridItemSpan(2) }) {
            if (artistsViewModel.artistsSimilar.isNotEmpty())
                QuickSandSemiBold(
                    stringResource(id = R.string.similar_artists),
                    Modifier.padding(5.dp),
                    size = 19,
                )
        }

        item(span = { GridItemSpan(2) }) {
            LazyHorizontalGrid(GridCells.Fixed(3), modifier = Modifier.heightIn(max = 600.dp)) {
                items(artistsViewModel.artistsSimilar) { songs ->
                    ArtistsViewSmallView(songs) {
                        coroutineScope.launch {
                            listState.scrollToItem(0)
                        }
                        coroutineScope.launch {
                            artistsViewModel.toDefault()
                            delay(1.seconds)
                            homeNavViewModel.homeNavigationView(HomeNavigationStatus.SELECT_ARTISTS)
                            artistsViewModel.searchArtists(it.trim().lowercase())
                        }
                    }
                }
            }
        }


        item(span = { GridItemSpan(2) }) {
            Spacer(Modifier.height(254.dp))
        }
    }
}