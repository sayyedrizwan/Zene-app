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
import com.rizwansayyed.zene.presenter.ui.GlobalHiddenNativeAds
import com.rizwansayyed.zene.presenter.ui.GlobalNativeFullAds
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
import java.util.UUID


@Composable
fun HomeOfflineView() {
    val homeNavModel: HomeNavViewModel = hiltViewModel()

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
        item(key = 1, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            HomepageTopView(homeNavModel)
        }

        item(key = 2, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            GlobalHiddenNativeAds()
        }

        item(key = 3, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                CurrentMostPlayingSong()
            }
        }

        item(key = 4, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                CityRadioViewList()
            }
        }


        item(key = 6, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                TopArtistsList()
                GlobalNativeFullAds()
            }
        }

        item(key = 8, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                MoodTopics()
            }
        }

        item(key = 10, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                FreshAddedSongsList()
            }
        }
        item(key = 11, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            GlobalHiddenNativeAds()
        }

        item(key = 12, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                TopGlobalSongsList()
            }
        }


        item(key = 14, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                TrendingSongsCountryList()
            }
        }
        when (val v = homeViewModel.topCountryTrendingSongs) {
            is DataResponse.Success ->
                itemsIndexed(
                    v.item,
                    key = { _, m -> m?.pId ?: "" },
                    span = { _, _ -> GridItemSpan(TOTAL_ITEMS_GRID) }) { i, item ->
                    GlobalTrendingPagerItems(item, false) {
                        addAllPlayer(v.item.toTypedArray(), i)
                    }
                }

            else -> {}
        }

        item(key = 15, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            GlobalHiddenNativeAds()
        }


        item(key = 16, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                GlobalNativeFullAds()
                SongsYouMayLikeView()
            }
        }



        item(key = 18, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                TopArtistsCountryList()
            }
        }


        item(key = 19, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            GlobalHiddenNativeAds()
        }

        item(key = 20, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                RelatedAlbums()
            }
        }

        when (val v = roomDbViewModel.albumsYouMayLike) {
            is DataResponse.Success -> items(
                v.item,
                key = { m -> m.pId ?: "" },
                span = { GridItemSpan(TWO_ITEMS_GRID) }) {
                AlbumsItems(it) {
                    homeNavModel.setAlbum(it.pId ?: "")
                }
            }

            DataResponse.Loading -> items(
                6,
                key = { UUID.randomUUID() },
                span = { GridItemSpan(TWO_ITEMS_GRID) }) {
                LoadingAlbumsCards()
            }

            else -> {}
        }

        item(key = 22, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            GlobalHiddenNativeAds()
        }


        item(key = 23, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                SongsSuggestionsForYou()
            }
        }

        item(key = 25, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            GlobalHiddenNativeAds()
        }


        item(key = 26, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                SimilarArtists()
            }
        }


        item(key = 27, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                ArtistsYouMayLikeWithSongs()
            }
        }

        item(key = 29, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            GlobalHiddenNativeAds()
        }

        item(key = 30, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                GlobalNativeFullAds()
                SongsForYouToExplore()
            }
        }

        item(key = 32, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            GlobalHiddenNativeAds()
        }

        when (val v = roomDbViewModel.songsSuggestionForUsers) {
            is DataResponse.Success -> itemsIndexed(
                v.item,
                key = { _, m -> m.pId ?: "" },
                span = { _, _ -> GridItemSpan(TWO_ITEMS_GRID) }) { i, m ->
                SongsExploreItems(m) {
                    addAllPlayer(v.item.toTypedArray(), i)
                }
            }

            DataResponse.Loading -> items(20, key = { UUID.randomUUID() }, span = { GridItemSpan(TWO_ITEMS_GRID) }) {
                LoadingAlbumsCards()
            }

            else -> {}
        }

        item(key = 33, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            GlobalHiddenNativeAds()
        }

        item(key = 34, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            GlobalHiddenNativeAds()
        }


        item(key = 35, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
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