package com.rizwansayyed.zene.ui.artists

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.ui.artists.artistviewmodel.ArtistsViewModel
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavViewModel
import com.rizwansayyed.zene.utils.QuickSandSemiBold

@Composable
fun ArtistsInfo(artistsViewModel: ArtistsViewModel, homeNavViewModel: HomeNavViewModel) {

    LaunchedEffect(Unit) {
        artistsViewModel.toDefault()
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
    ) {

        Spacer(modifier = Modifier.height(90.dp))

        QuickSandSemiBold(
            artistsViewModel.topInfo.value ?: "",
            size = 17,
            modifier = Modifier.fillMaxWidth()
        )


        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {

            artistsViewModel.artistsImages.value.forEach {
                AsyncImage(
                    model = it,
                    contentDescription = "",
                    Modifier.size(380.dp)
                )
            }
        }


        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {

            artistsViewModel.artistsTopSongs.value.forEach {
                Column {
                    AsyncImage(
                        model = it.image.replace("avatar170s", "770x0"),
                        contentDescription = "",
                        Modifier.size(380.dp)
                    )

                    QuickSandSemiBold(it.name, size = 19, modifier = Modifier.padding(5.dp))
                    QuickSandSemiBold(it.listeners, size = 19, modifier = Modifier.padding(5.dp))
                }
            }
        }


        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {

            artistsViewModel.artistsAllTimeSongs.value.forEach {
                Column {
                    AsyncImage(
                        model = it.img,
                        contentDescription = "",
                        Modifier.size(380.dp)
                    )

                    QuickSandSemiBold(it.name ?: "", size = 19, modifier = Modifier.padding(5.dp))
                    QuickSandSemiBold(it.artist ?: "", size = 19, modifier = Modifier.padding(5.dp))
                }
            }
        }


        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {

            artistsViewModel.artistsTopAlbums.value.forEach {
                Column {
                    AsyncImage(
                        model = it.image,
                        contentDescription = "",
                        Modifier.size(380.dp)
                    )

                    QuickSandSemiBold(it.name, size = 19, modifier = Modifier.padding(5.dp))
                    QuickSandSemiBold(it.listeners, size = 19, modifier = Modifier.padding(5.dp))
                    QuickSandSemiBold(it.songsSize, size = 19, modifier = Modifier.padding(5.dp))
                    QuickSandSemiBold(it.date, size = 19, modifier = Modifier.padding(5.dp))
                }
            }
        }


        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {

            artistsViewModel.artistsSimilar.value.forEach {
                Column {
                    AsyncImage(
                        model = it.img,
                        contentDescription = "",
                        Modifier.size(380.dp)
                    )

                    QuickSandSemiBold(it.name ?: "", size = 19, modifier = Modifier.padding(5.dp))
                    QuickSandSemiBold(it.artist ?: "", size = 19, modifier = Modifier.padding(5.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(190.dp))
    }
}