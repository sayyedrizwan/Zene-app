package com.rizwansayyed.zene.presenter.ui.home.views

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
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.exoplayer.ExoPlayer
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.domain.HomeNavigation
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.home.HomepageTopView
import com.rizwansayyed.zene.presenter.ui.home.online.AlbumsItems
import com.rizwansayyed.zene.presenter.ui.home.online.ArtistsYouMayLikeWithSongs
import com.rizwansayyed.zene.presenter.ui.home.online.CurrentMostPlayingSong
import com.rizwansayyed.zene.presenter.ui.home.online.FreshAddedSongsList
import com.rizwansayyed.zene.presenter.ui.home.online.GlobalTrendingPagerItems
import com.rizwansayyed.zene.presenter.ui.home.online.LoadingAlbumsCards
import com.rizwansayyed.zene.presenter.ui.home.online.MoodTopics
import com.rizwansayyed.zene.presenter.ui.home.online.RelatedAlbums
import com.rizwansayyed.zene.presenter.ui.home.online.SimilarArtists
import com.rizwansayyed.zene.presenter.ui.home.online.SongsExploreItems
import com.rizwansayyed.zene.presenter.ui.home.online.SongsForYouToExplore
import com.rizwansayyed.zene.presenter.ui.home.online.SongsSuggestionsForYou
import com.rizwansayyed.zene.presenter.ui.home.online.SongsYouMayLikeView
import com.rizwansayyed.zene.presenter.ui.home.online.TopArtistsCountryList
import com.rizwansayyed.zene.presenter.ui.home.online.TopArtistsList
import com.rizwansayyed.zene.presenter.ui.home.online.TopGlobalSongsList
import com.rizwansayyed.zene.presenter.ui.home.online.TrendingSongsCountryList
import com.rizwansayyed.zene.presenter.ui.home.online.radio.CityRadioViewList
import com.rizwansayyed.zene.presenter.ui.musicplayer.BottomNavImage
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.TOTAL_ITEMS_GRID
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.TWO_ITEMS_GRID
import com.rizwansayyed.zene.service.player.utils.Utils.addAllPlayer
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce


@OptIn(FlowPreview::class)
@Composable
fun HomeOfflineView() {
    val homeNavModel: HomeNavViewModel = hiltViewModel()
    val homeViewModel: HomeApiViewModel = hiltViewModel()
    val roomDbViewModel: RoomDbViewModel = hiltViewModel()

    LazyVerticalGrid(
        GridCells.Fixed(TOTAL_ITEMS_GRID),
        Modifier
            .fillMaxSize()
            .background(DarkGreyColor)
    ) {
        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            HomepageTopView(homeNavModel)
        }
    }

}

@OptIn(FlowPreview::class)
@Composable
fun HomeView() {
    val homeNavModel: HomeNavViewModel = hiltViewModel()
    val homeViewModel: HomeApiViewModel = hiltViewModel()
    val roomDbViewModel: RoomDbViewModel = hiltViewModel()
    val listState =
        rememberLazyGridState(initialFirstVisibleItemIndex = homeNavModel.homeScrollPosition.intValue)

    LazyVerticalGrid(
        GridCells.Fixed(TOTAL_ITEMS_GRID),
        Modifier
            .fillMaxSize()
            .background(DarkGreyColor), listState
    ) {
        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            HomepageTopView(homeNavModel)
        }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                CurrentMostPlayingSong()
            }
        }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                CityRadioViewList()
            }
        }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                TopArtistsList()
            }
        }
        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                MoodTopics()
            }
        }
        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                FreshAddedSongsList()
            }
        }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                TopGlobalSongsList()
            }
        }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                TrendingSongsCountryList()
            }
        }
        when (val v = homeViewModel.topCountryTrendingSongs) {
            is DataResponse.Success ->
                itemsIndexed(
                    v.item, span = { _, _ -> GridItemSpan(TOTAL_ITEMS_GRID) }) { i, item ->
                    GlobalTrendingPagerItems(item, false) {
                        addAllPlayer(v.item.toTypedArray(), i)
                    }
                }

            else -> {}
        }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                SongsYouMayLikeView()
            }
        }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
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
                AlbumsItems(it) {
                    homeNavModel.setAlbum(it.pId ?: "")
                }
            }

            DataResponse.Loading -> items(6, span = { GridItemSpan(TWO_ITEMS_GRID) }) {
                LoadingAlbumsCards()
            }

            else -> {}
        }


        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                SongsSuggestionsForYou()
            }
        }


        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                SimilarArtists()
            }
        }


        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                ArtistsYouMayLikeWithSongs()
            }
        }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                SongsForYouToExplore()
            }
        }

        when (val v = roomDbViewModel.songsSuggestionForUsers) {
            is DataResponse.Success -> itemsIndexed(
                v.item, span = { _, _ -> GridItemSpan(TWO_ITEMS_GRID) }) { i, m ->
                SongsExploreItems(m) {
                    addAllPlayer(v.item.toTypedArray(), i)
                }
            }

            DataResponse.Loading -> items(20, span = { GridItemSpan(TWO_ITEMS_GRID) }) {
                LoadingAlbumsCards()
            }

            else -> {}
        }


        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Spacer(Modifier.height(230.dp))
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex
        }.debounce(500L).collectLatest {
            homeNavModel.setHomeScrollPosition(it)
        }
    }
}

@Composable
fun BottomNavBar(modifier: Modifier, player: ExoPlayer) {
    val nav: HomeNavViewModel = hiltViewModel()
    Column(modifier.fillMaxWidth()) {
        BottomNavImage(player)

        Row(
            Modifier
                .padding(bottom = 25.dp)
                .padding(12.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(80))
                .background(BlackColor),
            Arrangement.SpaceEvenly,
            Alignment.CenterVertically
        ) {
            HomeNavigation.entries.forEach {
                if (!it.doShow) return@forEach

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
                        colorFilter = ColorFilter.tint(if (nav.homeNavV == it) Color.White else Color.Gray)
                    )

                    Spacer(Modifier.height(5.dp))

                    TextRegular(
                        it.n, Modifier, if (nav.homeNavV == it) Color.White else Color.Gray,
                        doCenter = true, size = 10
                    )
                }
            }
        }
    }
}