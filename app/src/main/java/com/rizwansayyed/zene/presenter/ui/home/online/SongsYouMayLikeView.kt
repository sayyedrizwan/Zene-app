package com.rizwansayyed.zene.presenter.ui.home.online

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.selectedFavouriteArtistsSongs
import com.rizwansayyed.zene.data.db.recentplay.RecentPlayedEntity
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.theme.PurpleGrey80
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithImage
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.shimmerBrush
import com.rizwansayyed.zene.utils.Utils.restartTheApp
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun SongsYouMayLikeView() {
    val roomDb: RoomDbViewModel = hiltViewModel()
    val screenWidth = LocalConfiguration.current.screenWidthDp

    when (val v = roomDb.songsYouMayLike) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {
            TopInfoWithImage(stringResource(id = R.string.songs_you_may_like), null) {}

            SongsYouMayLikeLoading(screenWidth)
        }

        is DataResponse.Success -> {
            if (v.item.isNotEmpty()) {
                TopInfoWithImage(stringResource(id = R.string.songs_you_may_like), null) {}

                LazyHorizontalGrid(
                    GridCells.Fixed(2), Modifier
                        .fillMaxWidth()
                        .height((screenWidth / 1.9 * 2).dp)
                ) {
                    items(v.item) {
                        SongsYouMayLikeItems(it, screenWidth)
                    }
                }
            }
        }
    }
}

@Composable
fun SongsYouMayLikeItems(music: MusicData?, screenWidth: Int) {
    Box(
        Modifier
            .padding(4.dp)
            .size((screenWidth / 2).dp, (screenWidth / 1.9).dp)
            .clip(RoundedCornerShape(18.dp))
            .background(BlackColor)
    ) {
        AsyncImage(
            music?.thumbnail, music?.name, Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Spacer(
            Modifier
                .fillMaxSize()
                .background(MainColor.copy(0.3f))
        )

        Column(
            Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
        ) {
            TextSemiBold(
                music?.name ?: "",
                Modifier.padding(start = 8.dp),
                singleLine = true, size = 15
            )

            TextThin(
                music?.artists ?: "",
                Modifier.padding(vertical = 3.dp, horizontal = 8.dp),
                singleLine = true, size = 11
            )

            Spacer(Modifier.padding(5.dp))
        }
    }
}

@Composable
fun SongsYouMayLikeLoading(screenWidth: Int) {
    LazyHorizontalGrid(
        GridCells.Fixed(2), Modifier
            .fillMaxWidth()
            .height((screenWidth / 1.5 * 2).dp)
    ) {
        items(40) {
            Spacer(
                Modifier
                    .padding(4.dp)
                    .size((screenWidth / 1.4).dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(shimmerBrush(targetValue = 2300f, showShimmer = true))
            )
        }
    }
}

@Composable
fun SongYouMayTitle(
    recentPlayList: List<RecentPlayedEntity>,
    selectedFavouriteArtists: Array<String>?
) {
    Column {
        if (recentPlayList.isEmpty() && selectedFavouriteArtists?.isEmpty() == true) {
            TopInfoWithSeeMore(R.string.select_your_fav_artists, null) {}

            TextThin(
                stringResource(id = R.string.songs_you_may_like_desc),
                Modifier
                    .padding()
                    .fillMaxWidth(),
                true
            )

            Spacer(Modifier.height(20.dp))
        } else
            TopInfoWithSeeMore(R.string.songs_you_may_like, null) {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveArtistsButton(m: Modifier, nav: HomeNavViewModel) {
    val context = LocalContext.current as Activity

    Card(
        onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                delay(1.5.seconds)
                restartTheApp(context)
            }
            selectedFavouriteArtistsSongs = flowOf(nav.selectArtists.toTypedArray())
        }, modifier = m
            .fillMaxWidth()
            .padding(10.dp)
            .padding(bottom = 39.dp), elevation = CardDefaults.cardElevation(13.dp),
        shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(PurpleGrey80)
    ) {
        TextSemiBold(
            stringResource(id = R.string.suggest_me_now),
            Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            true, Color.Black
        )
    }
}