package com.rizwansayyed.zene.ui.home.homeui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.SongsViewModel
import com.rizwansayyed.zene.utils.QuickSandSemiBold
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds

@Composable
fun HomepageView(songsViewModel: SongsViewModel = hiltViewModel()) {
    val header by dataStoreManager.albumHeaderData.collectAsState(initial = runBlocking(Dispatchers.IO) { dataStoreManager.albumHeaderData.first() })
    val topArtists by dataStoreManager.topArtistsOfWeekData
        .collectAsState(initial = runBlocking(Dispatchers.IO) { dataStoreManager.topArtistsOfWeekData.first() })
    val globalSongs by dataStoreManager.topGlobalSongsData
        .collectAsState(initial = runBlocking(Dispatchers.IO) { dataStoreManager.topGlobalSongsData.first() })

    val recentPlayedSongs =
        songsViewModel.recentPlayedSongs.value?.collectAsState(initial = emptyList())

    LazyColumn {
        item {
            header?.let { TopHeaderPager(it) }
        }

        if (recentPlayedSongs?.value?.isNotEmpty() == true)
            item {
                TopHeaderOf(stringResource(id = R.string.recently_played))
                Spacer(modifier = Modifier.height(8.dp))
            }

        item {
            TopHeaderOf(stringResource(id = R.string.top_artist_of_week))
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            LazyRow {
                if (topArtists != null) items(topArtists!!) { artists ->
                    ArtistsView(artists)
                }
            }
        }

        item {
            TopHeaderOf(stringResource(id = R.string.global_trending_songs))
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            LazyRow {
                if (globalSongs != null) items(globalSongs!!) { song ->
                    TrendingSongsView(song)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(300.dp))
        }
    }

}


@Composable
fun TopHeaderOf(v: String) {
    Spacer(modifier = Modifier.height(65.dp))

    QuickSandSemiBold(v, size = 19, modifier = Modifier.padding(5.dp))
}