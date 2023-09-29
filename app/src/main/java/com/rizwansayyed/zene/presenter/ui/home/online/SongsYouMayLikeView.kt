package com.rizwansayyed.zene.presenter.ui.home.online

import android.app.Activity
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.selectedFavouriteArtistsSongs
import com.rizwansayyed.zene.data.db.recentplay.RecentPlayedEntity
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.presenter.theme.PurpleGrey80
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TextThinBig
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.utils.Utils.restartTheApp
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds


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

@Composable
fun SelectFavArtists(artists: MusicData, nav: HomeNavViewModel, click: () -> Unit) {
    Column(
        Modifier
            .padding(6.dp)
            .clickable {
                click()
            }) {
        AsyncImage(
            artists.thumbnail, artists.name,
            Modifier
                .fillMaxWidth()
                .height(LocalConfiguration.current.screenWidthDp.dp / 2 - 20.dp)
                .clip(RoundedCornerShape(50))
                .border(
                    if (nav.selectArtists.contains(artists.name?.lowercase())) 2.dp else 0.dp,
                    if (nav.selectArtists.contains(artists.name?.lowercase())) Color.Red else Color.Transparent,
                    RoundedCornerShape(50)
                ),
            contentScale = ContentScale.Crop
        )

        TextThinBig(
            artists.name ?: "",
            Modifier
                .fillMaxWidth()
                .padding(4.dp), true, singleLine = true
        )
    }
}
