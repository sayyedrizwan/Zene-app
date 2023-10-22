package com.rizwansayyed.zene.presenter.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.domain.HomeNavigation
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.ui.home.HomepageTopView
import com.rizwansayyed.zene.presenter.ui.home.online.AlbumsItems
import com.rizwansayyed.zene.presenter.ui.home.online.ArtistsYouMayLikeWithSongs
import com.rizwansayyed.zene.presenter.ui.home.online.CityRadioViewList
import com.rizwansayyed.zene.presenter.ui.home.online.CurrentMostPlayingSong
import com.rizwansayyed.zene.presenter.ui.home.online.FreshAddedSongsList
import com.rizwansayyed.zene.presenter.ui.home.online.GlobalTrendingPagerItems
import com.rizwansayyed.zene.presenter.ui.home.online.LoadingAlbumsCards
import com.rizwansayyed.zene.presenter.ui.home.online.MoodTopics
import com.rizwansayyed.zene.presenter.ui.home.online.RelatedAlbums
import com.rizwansayyed.zene.presenter.ui.home.online.SimilarArtists
import com.rizwansayyed.zene.presenter.ui.home.online.SongsForYouToExplore
import com.rizwansayyed.zene.presenter.ui.home.online.SongsSuggestionsForYou
import com.rizwansayyed.zene.presenter.ui.home.online.SongsYouMayLikeView
import com.rizwansayyed.zene.presenter.ui.home.online.TopArtistsCountryList
import com.rizwansayyed.zene.presenter.ui.home.online.TopArtistsList
import com.rizwansayyed.zene.presenter.ui.home.online.TopGlobalSongsList
import com.rizwansayyed.zene.presenter.ui.home.online.TrendingSongsCountryList
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.THREE_ITEMS_GRID
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.TOTAL_ITEMS_GRID
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.TWO_ITEMS_GRID
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel

@Composable
fun MainHomepageOnlineNew() {
    val homeViewModel: HomeApiViewModel = hiltViewModel()
    val roomDbViewModel: RoomDbViewModel = hiltViewModel()

    LazyVerticalGrid(
        GridCells.Fixed(TOTAL_ITEMS_GRID),
        Modifier
            .fillMaxSize()
            .background(DarkGreyColor)
    ) {
        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            HomepageTopView()
        }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                CurrentMostPlayingSong()
                CityRadioViewList()
            }
        }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                TopArtistsList()
                MoodTopics()
                FreshAddedSongsList()
            }
        }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                TopGlobalSongsList()
                TrendingSongsCountryList()
            }
        }
        when (val v = homeViewModel.topCountryTrendingSongs) {
            is DataResponse.Success -> items(v.item, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
                GlobalTrendingPagerItems(it, false)
            }

            else -> {}
        }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                SongsYouMayLikeView()
                TopArtistsCountryList()
            }
        }


        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                RelatedAlbums()
            }
        }

        when (val v = roomDbViewModel.albumsYouMayLike) {
            is DataResponse.Success -> items(v.item, span = { GridItemSpan(TWO_ITEMS_GRID) }) {
                AlbumsItems(it)
            }

            DataResponse.Loading -> items(6, span = { GridItemSpan(TWO_ITEMS_GRID) }) {
                LoadingAlbumsCards()
            }

            else -> {}
        }


        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                SongsSuggestionsForYou()
                SimilarArtists()
                ArtistsYouMayLikeWithSongs()
            }
        }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                SongsForYouToExplore()
            }
        }

//        when (val v = roomDbViewModel.songsSuggestionForUsers) {
//            is DataResponse.Success -> items(v.item, span = { GridItemSpan(THREE_ITEMS_GRID) }) {
//                Column {
//                    AsyncImage(
//                        it.thumbnail,
//                        it.name,
//                        Modifier
//                            .padding(2.dp)
//                            .fillMaxWidth()
//                    )
//
//                    TextThin(it.name ?: "")
//                }
//            }
//
//            DataResponse.Loading -> items(6, span = { GridItemSpan(THREE_ITEMS_GRID) }) {
//                LoadingAlbumsCards()
//            }
//
//            else -> {}
//        }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                Spacer(Modifier.height(180.dp))
            }
        }


    }
}

@Composable
fun BottomNavBar(modifier: Modifier) {
    val nav: HomeNavViewModel = hiltViewModel()
    Row(
        modifier
            .padding(bottom = 25.dp)
            .padding(12.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(80))
            .background(BlackColor),
        Arrangement.SpaceEvenly,
        Alignment.CenterVertically
    ) {
        HomeNavigation.entries.forEach {
            Column(
                Modifier
                    .padding(vertical = 15.dp)
                    .clickable {
                        nav.setHomeNav(it)
                    },
                Arrangement.Center,
                Alignment.CenterHorizontally
            ) {
                Image(
                    painterResource(it.img),
                    "",
                    Modifier.size(25.dp),
                    colorFilter = ColorFilter.tint(if (nav.homeNav.value == it) Color.White else Color.Gray)
                )

                Spacer(Modifier.height(5.dp))

                TextRegular(
                    it.n, Modifier, if (nav.homeNav.value == it) Color.White else Color.Gray,
                    doCenter = true, size = 10
                )
            }
        }
    }
}