package com.rizwansayyed.zene.ui.home.homeui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.domain.roomdb.recentplayed.toTopArtistsSongs
import com.rizwansayyed.zene.presenter.SongsViewModel
import com.rizwansayyed.zene.ui.ViewAllBtnView
import com.rizwansayyed.zene.utils.QuickSandLight
import com.rizwansayyed.zene.utils.QuickSandSemiBold
import com.rizwansayyed.zene.utils.Utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Composable
fun HomepageView(songsViewModel: SongsViewModel = hiltViewModel()) {
    val headerPagerData by dataStoreManager
        .albumHeaderData.collectAsState(initial = runBlocking(Dispatchers.IO) { dataStoreManager.albumHeaderData.first() })
    val topArtists by dataStoreManager.topArtistsOfWeekData
        .collectAsState(initial = runBlocking(Dispatchers.IO) { dataStoreManager.topArtistsOfWeekData.first() })
    val globalSongs by dataStoreManager.topGlobalSongsData
        .collectAsState(initial = runBlocking(Dispatchers.IO) { dataStoreManager.topGlobalSongsData.first() })

    val ip by dataStoreManager.ipData.collectAsState(initial = null)

    val topCountrySongs by dataStoreManager.topCountrySongsData.collectAsState(initial = emptyArray())
    val trendingSongsTop50 by dataStoreManager.trendingSongsTop50Data.collectAsState(initial = emptyArray())
    val suggestedSongs by dataStoreManager.songsSuggestionsData.collectAsState(initial = emptyArray())
    val trendingSongsTopKPop by dataStoreManager.trendingSongsTopKPopData.collectAsState(initial = emptyArray())
    val trendingSongsTop50KPop by dataStoreManager.trendingSongsTop50KPopData.collectAsState(initial = emptyArray())
    val songsSuggestionsForYou by dataStoreManager.songsSuggestionsForYouData.collectAsState(initial = emptyArray())
    val suggestArtists by dataStoreManager.artistsSuggestionsData.collectAsState(initial = emptyArray())
    val topArtistsSongs by dataStoreManager.topArtistsSongsData.collectAsState(initial = emptyArray())
    val allSongsForYouLists by dataStoreManager.songsAllForYouAllData.collectAsState(initial = emptyArray())

    val recentPlayedSongs =
        songsViewModel.recentPlayedSongs?.collectAsState(initial = emptyList())

    LazyColumn {
        item {
            headerPagerData?.let {
                TopHeaderPager(it) { name, artists ->
                    songsViewModel.songsPlayingDetails(name, artists)
                }
            }
        }

        if (recentPlayedSongs?.value?.isNotEmpty() == true) {
            item {
                TopHeaderOf(stringResource(id = R.string.recently_played))
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                LazyRow {
                    items(recentPlayedSongs.value) { recent ->
                        RecentPlayedItemView(recent)
                    }

                    if (recentPlayedSongs.value.size >= 14) item {
                        ViewAllBtnView {
                            "view all history".showToast()
                        }
                    }
                }
            }
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
                    TrendingSongsView(song) { name, artists ->
                        songsViewModel.songsPlayingDetails(name, artists)
                    }
                }
            }
        }

        item {
            TopHeaderOf("${stringResource(id = R.string.trending_songs_in)} ${ip?.country}")
            Spacer(modifier = Modifier.height(8.dp))
        }


        item {
            LazyHorizontalGrid(GridCells.Fixed(2), modifier = Modifier.heightIn(max = 500.dp)) {
                items(topCountrySongs?.size ?: 0) { songs ->
                    topCountrySongs?.get(songs)?.let {
                        TrendingSongsViewShortText(it) { name, artists ->
                            songsViewModel.songsPlayingDetails(name, artists)

                        }
                    }
                }
            }
        }

        if (suggestedSongs?.isNotEmpty() == true) {
            item {
                TopHeaderOf(stringResource(id = R.string.recommended_songs))
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                LazyHorizontalGrid(GridCells.Fixed(2), modifier = Modifier.heightIn(max = 500.dp)) {
                    items(suggestedSongs?.size ?: 0) { songs ->
                        suggestedSongs?.get(songs)?.let {
                            TrendingSongsViewShortText(it) { name, artists ->
                                songsViewModel.songsPlayingDetails(name, artists)

                            }
                        }
                    }
                }
            }
        }

        if (songsViewModel.topArtistsSuggestions?.isNotEmpty() == true) {
            item {
                TopHeaderOf(stringResource(id = R.string.recommended_artists))
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                LazyRow {
                    items(songsViewModel.topArtistsSuggestions!!) { artists ->
                        ArtistsView(artists.toTopArtistsSongs())
                    }
                }

            }
        }

        item {
            TopHeaderOf(stringResource(id = R.string.top_favourite))
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            LazyHorizontalGrid(GridCells.Fixed(2), modifier = Modifier.heightIn(max = 500.dp)) {
                items(trendingSongsTop50?.size ?: 0) { songs ->
                    trendingSongsTop50?.get(songs)?.let {
                        TrendingSongsViewShortText(it) { name, artists ->
                            songsViewModel.songsPlayingDetails(name, artists)

                        }
                    }
                }
            }
        }

        item {
            TopHeaderOf(stringResource(id = R.string.trending_k_pop_music))
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            LazyRow {
                if (trendingSongsTopKPop != null) items(trendingSongsTopKPop!!) { song ->
                    TrendingSongsView(song) { name, artists ->
                        songsViewModel.songsPlayingDetails(name, artists)
                    }
                }
            }
        }

        item {
            TopHeaderOf(stringResource(id = R.string.top_k_pop_music))
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            LazyRow {
                if (trendingSongsTop50KPop != null) items(trendingSongsTop50KPop!!) { song ->
                    TrendingSongsView(song) { name, artists ->
                        songsViewModel.songsPlayingDetails(name, artists)
                    }
                }
            }
        }
        if (songsSuggestionsForYou?.isNotEmpty() == true) {
            item {
                TopHeaderOf(stringResource(id = R.string.related_songs))
                Spacer(modifier = Modifier.height(8.dp))
            }


            item {
                LazyHorizontalGrid(GridCells.Fixed(2), modifier = Modifier.heightIn(max = 500.dp)) {
                    items(songsSuggestionsForYou?.size ?: 0) { songs ->
                        songsSuggestionsForYou?.get(songs)?.let {
                            TrendingSongsViewShortText(it) { name, artists ->
                                songsViewModel.songsPlayingDetails(name, artists)

                            }
                        }
                    }
                }
            }
        }

        if (suggestArtists?.isNotEmpty() == true) {
            item {
                TopHeaderOf(stringResource(id = R.string.suggested_artists))
            }


            item {
                LazyHorizontalGrid(GridCells.Fixed(3), modifier = Modifier.heightIn(max = 600.dp)) {
                    items(suggestArtists?.size ?: 0) { songs ->
                        suggestArtists?.get(songs)?.let { ArtistsViewSmallView(it) }
                    }
                }
            }
        }

        if (topArtistsSongs?.isNotEmpty() == true) {
            topArtistsSongs?.forEach {
                item {
                    it.title?.let { title ->
                        TopHeaderOf(
                            stringResource(id = R.string.for__fan).replace("----", title)
                        )
                    }
                }

                item {
                    LazyHorizontalGrid(
                        GridCells.Fixed(2), modifier = Modifier.heightIn(max = 500.dp)
                    ) {
                        items(it.details?.size ?: 0) { songs ->
                            it.details?.get(songs)?.let {
                                TrendingSongsViewShortText(it) { name, artists ->
                                    songsViewModel.songsPlayingDetails(name, artists)

                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            TopHeaderOf(stringResource(id = R.string.zene_suggested_songs_for_you))
        }

        if (allSongsForYouLists?.isNotEmpty() == true) {

            items(allSongsForYouLists!!) {
                FullCardSongView(it)
            }
        } else {
            item {
                Spacer(modifier = Modifier.height(18.dp))

                QuickSandLight(
                    stringResource(id = R.string.no_songs_suggestion_notice),
                    size = 14,
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .fillMaxWidth()
                )
            }
        }


//        if (footerAlbums?.isNotEmpty() == true)
//            items(footerAlbums!!) {
//                TopHeaderOf(it.headline!!)
//            }

        item {
            Spacer(modifier = Modifier.height(200.dp))
        }
    }

//    if (header == null) HomeFullLoadingScreenView()

}


@Composable
fun TopHeaderOf(v: String) {
    Spacer(modifier = Modifier.height(65.dp))

    QuickSandSemiBold(v, size = 19, modifier = Modifier.padding(5.dp))
}