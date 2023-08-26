package com.rizwansayyed.zene.ui.artists

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.SongsViewModel
import com.rizwansayyed.zene.ui.artists.artistviewmodel.ArtistsViewModel
import com.rizwansayyed.zene.ui.artists.view.ArtistsAlbum
import com.rizwansayyed.zene.ui.artists.view.ArtistsAllSongsView
import com.rizwansayyed.zene.ui.artists.view.TopArtistsInfo
import com.rizwansayyed.zene.ui.artists.view.TopArtistsSongs
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavViewModel
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavigationStatus
import com.rizwansayyed.zene.ui.home.homeui.TopHeaderOf
import com.rizwansayyed.zene.utils.QuickSandSemiBold

@Composable
fun ArtistsInfo(
    artistsViewModel: ArtistsViewModel = hiltViewModel(),
    homeNavViewModel: HomeNavViewModel = hiltViewModel(),
    songsViewModel: SongsViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        artistsViewModel.toDefault()
    }

    LazyVerticalGrid(columns = GridCells.Fixed(2), Modifier.fillMaxSize()) {
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
            if (artistsViewModel.artistsAllTimeSongs.isNotEmpty()) {
                LazyRow {
                    items(artistsViewModel.artistsAllTimeSongs) { artists ->
                        ArtistsAllSongsView(artists) {
                            homeNavViewModel.homeNavigationView(HomeNavigationStatus.SELECT_ARTISTS)
                            artistsViewModel.searchArtists(it.trim().lowercase())
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                    }
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
            Spacer(Modifier.height(254.dp))
        }
    }
//    Column(
//        Modifier
//            .fillMaxSize()
//            .background(Color.Black)
//            .verticalScroll(rememberScrollState())
//    ) {
//
//        Spacer(modifier = Modifier.height(90.dp))
//
//        QuickSandSemiBold(
//            artistsViewModel.topInfo.value ?: "",
//            size = 17,
//            modifier = Modifier.fillMaxWidth()
//        )
//
//
//        Row(
//            Modifier
//                .fillMaxWidth()
//                .horizontalScroll(rememberScrollState())
//        ) {
//
//            artistsViewModel.artistsImages.value.forEach {
//                AsyncImage(
//                    model = it,
//                    contentDescription = "",
//                    Modifier.size(380.dp)
//                )
//            }
//        }
//
//
//        Row(
//            Modifier
//                .fillMaxWidth()
//                .horizontalScroll(rememberScrollState())
//        ) {
//
//            artistsViewModel.artistsTopSongs.value.forEach {
//                Column {
//                    AsyncImage(
//                        model = it.image.replace("avatar170s", "770x0"),
//                        contentDescription = "",
//                        Modifier.size(380.dp)
//                    )
//
//                    QuickSandSemiBold(it.name, size = 19, modifier = Modifier.padding(5.dp))
//                    QuickSandSemiBold(it.listeners, size = 19, modifier = Modifier.padding(5.dp))
//                }
//            }
//        }
//
//
//        Row(
//            Modifier
//                .fillMaxWidth()
//                .horizontalScroll(rememberScrollState())
//        ) {
//
//            artistsViewModel.artistsAllTimeSongs.value.forEach {
//                Column {
//                    AsyncImage(
//                        model = it.img,
//                        contentDescription = "",
//                        Modifier.size(380.dp)
//                    )
//
//                    QuickSandSemiBold(it.name ?: "", size = 19, modifier = Modifier.padding(5.dp))
//                    QuickSandSemiBold(it.artist ?: "", size = 19, modifier = Modifier.padding(5.dp))
//                }
//            }
//        }
//
//
//        Row(
//            Modifier
//                .fillMaxWidth()
//                .horizontalScroll(rememberScrollState())
//        ) {
//
//            artistsViewModel.artistsTopAlbums.value.forEach {
//                Column {
//                    AsyncImage(it.image, "", Modifier.size(380.dp))
//
//                    QuickSandSemiBold(it.name, size = 19, modifier = Modifier.padding(5.dp))
//                    QuickSandSemiBold(it.listeners, size = 19, modifier = Modifier.padding(5.dp))
//                    QuickSandSemiBold(it.songsSize, size = 19, modifier = Modifier.padding(5.dp))
//                    QuickSandSemiBold(it.date, size = 19, modifier = Modifier.padding(5.dp))
//                }
//            }
//        }
//
//        QuickSandSemiBold(
//            artistsViewModel.artistsInstagramPosts.value?.bio ?: "",
//            size = 19,
//            modifier = Modifier.padding(5.dp)
//        )
//
//        QuickSandSemiBold(
//            artistsViewModel.artistsInstagramPosts.value?.followers.toString(),
//            size = 19,
//            modifier = Modifier.padding(5.dp)
//        )
//
//        AsyncImage(
//            model = artistsViewModel.artistsInstagramPosts.value?.profilePic,
//            contentDescription = "",
//            Modifier.size(120.dp)
//        )
//
//        Row(
//            Modifier
//                .fillMaxWidth()
//                .horizontalScroll(rememberScrollState())
//        ) {
//            artistsViewModel.artistsInstagramPosts.value?.posts?.forEach {
//                Column {
//                    AsyncImage(
//                        model = it.postImage,
//                        contentDescription = "",
//                        Modifier.size(380.dp)
//                    )
//
//                    QuickSandSemiBold(
//                        it.isVideo.toString(),
//                        size = 19,
//                        modifier = Modifier.padding(5.dp)
//                    )
//                    QuickSandSemiBold(
//                        it.postId.toString(),
//                        size = 19,
//                        modifier = Modifier.padding(5.dp)
//                    )
//                    QuickSandSemiBold(
//                        it.timestamp.toString(),
//                        size = 19,
//                        modifier = Modifier.padding(5.dp)
//                    )
//                    QuickSandSemiBold(
//                        it.likeCount.toString(),
//                        size = 19,
//                        modifier = Modifier.padding(5.dp)
//                    )
//                    QuickSandSemiBold(
//                        it.commentCount.toString(),
//                        size = 19,
//                        modifier = Modifier.padding(5.dp)
//                    )
//                    QuickSandSemiBold(
//                        it.totalImages.toString(),
//                        size = 19,
//                        modifier = Modifier.padding(5.dp)
//                    )
//                }
//            }
//        }
//        QuickSandSemiBold(
//            artistsViewModel.artistsTwitterInfo.value?.name ?: "",
//            size = 19,
//            modifier = Modifier.padding(5.dp)
//        )
//
//        QuickSandSemiBold(
//            artistsViewModel.artistsTwitterInfo.value?.description ?: "",
//            size = 19,
//            modifier = Modifier.padding(5.dp)
//        )
//
//        Row(
//            Modifier
//                .fillMaxWidth()
//                .horizontalScroll(rememberScrollState())
//        ) {
//
//            artistsViewModel.artistsSimilar.value.forEach {
//                Column {
//                    AsyncImage(
//                        model = it.img,
//                        contentDescription = "",
//                        Modifier.size(380.dp)
//                    )
//
//                    QuickSandSemiBold(it.name ?: "", size = 19, modifier = Modifier.padding(5.dp))
//                    QuickSandSemiBold(it.artist ?: "", size = 19, modifier = Modifier.padding(5.dp))
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(190.dp))
//    }
}