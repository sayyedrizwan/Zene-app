package com.rizwansayyed.zene.ui.artists

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.ui.artists.artistviewmodel.ArtistsViewModel
import com.rizwansayyed.zene.ui.artists.view.ShowInstagramInfo
import com.rizwansayyed.zene.ui.artists.view.ShowTwitterInfo
import com.rizwansayyed.zene.ui.artists.view.TopArtistsInfo
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavViewModel

@Composable
fun ArtistsInfo(
    artistsViewModel: ArtistsViewModel = hiltViewModel(),
    homeNavViewModel: HomeNavViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        artistsViewModel.toDefault()
    }

    LazyColumn(Modifier.fillMaxSize()) {
        item {
            TopArtistsInfo(artistsViewModel)
        }
        item {
            ShowInstagramInfo(artistsViewModel)
        }
        item {
            ShowTwitterInfo(artistsViewModel)
        }
        item {
            Spacer(Modifier.height(154.dp))
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