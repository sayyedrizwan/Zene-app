package com.rizwansayyed.zene.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.SongsViewModel
import com.rizwansayyed.zene.presenter.model.ApiResponse.*
import com.rizwansayyed.zene.ui.artists.artistviewmodel.ArtistsViewModel
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavViewModel
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavigationStatus
import com.rizwansayyed.zene.ui.home.homeui.ArtistsView
import com.rizwansayyed.zene.ui.home.homeui.TrendingSongsViewShortText
import com.rizwansayyed.zene.ui.search.view.SearchBarView
import com.rizwansayyed.zene.utils.QuickSandBold
import com.rizwansayyed.zene.utils.QuickSandSemiBold


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchMusicArtistView(
    songsViewModel: SongsViewModel,
    nav: HomeNavViewModel,
    artistsViewModel: ArtistsViewModel
) {
    val listState = rememberLazyGridState()
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(listState.isScrollInProgress){
        if (listState.isScrollInProgress) {
            keyboardController?.hide()
        }
    }

    LazyVerticalGrid(columns = GridCells.Fixed(2), Modifier.fillMaxSize(), state = listState) {
        item(span = { GridItemSpan(2) }) {
            Spacer(Modifier.height(40.dp))
        }
        item(span = { GridItemSpan(2) }) {
            SearchBarView(nav) {
                songsViewModel.searchSongs(it.trim())
                songsViewModel.searchArtists(it.trim())
            }
        }

        when (songsViewModel.searchArtists?.apiResponse) {
            SUCCESS -> {
                item(span = { GridItemSpan(2) }) {
                    QuickSandBold(
                        stringResource(id = R.string.artists),
                        size = 16,
                        modifier = Modifier
                            .padding(vertical = 20.dp, horizontal = 10.dp)
                            .fillMaxWidth(),
                        align = TextAlign.Start
                    )
                }
                item(span = { GridItemSpan(2) }) {
                    LazyRow {
                        items(songsViewModel.searchArtists?.data!!) {
                            ArtistsView(it) { a ->
                                nav.homeNavigationView(HomeNavigationStatus.SELECT_ARTISTS)
                                artistsViewModel.searchArtists(a.trim().lowercase())
                            }
                        }
                    }
                }
            }

            LOADING -> item(span = { GridItemSpan(2) }) {
                Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                    CircularProgressIndicator(Modifier.size(30.dp), Color.White)
                }
            }

            ERROR -> {}
            null -> {}
        }

        when (songsViewModel.searchSongs?.apiResponse) {
            SUCCESS -> {
                item(span = { GridItemSpan(2) }) {
                    QuickSandBold(
                        stringResource(id = R.string.songs),
                        size = 16,
                        modifier = Modifier
                            .padding(vertical = 20.dp, horizontal = 10.dp)
                            .fillMaxWidth(),
                        align = TextAlign.Start
                    )
                }
                items(songsViewModel.searchSongs?.data!!) {
                    TrendingSongsViewShortText(it) { thumbnail, name, artists ->
                        nav.showMusicPlayer()
                        songsViewModel.songsPlayingDetails(thumbnail, name, artists)
                    }
                }
            }

            LOADING -> item(span = { GridItemSpan(2) }) {
                Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                    CircularProgressIndicator(Modifier.size(30.dp), Color.White)
                }
            }

            ERROR -> item(span = { GridItemSpan(2) }) {
                QuickSandSemiBold(stringResource(id = R.string.error_loading_songs), size = 17)
            }

            null -> {}
        }

        item(span = { GridItemSpan(2) }) {
            Spacer(Modifier.height(210.dp))
        }
    }
}