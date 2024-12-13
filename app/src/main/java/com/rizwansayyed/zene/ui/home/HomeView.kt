package com.rizwansayyed.zene.ui.home

import android.Manifest
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.db.DataStoreManager.userInfoDB
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.ui.home.view.HomeArtistsSimilarLoading
import com.rizwansayyed.zene.ui.home.view.HomeArtistsSimilarToView
import com.rizwansayyed.zene.ui.home.view.HomeHeaderView
import com.rizwansayyed.zene.ui.home.view.HorizontalArtistsView
import com.rizwansayyed.zene.ui.home.view.HorizontalSongView
import com.rizwansayyed.zene.ui.home.view.HorizontalVideoView
import com.rizwansayyed.zene.ui.home.view.ReviewAppDialog
import com.rizwansayyed.zene.ui.home.view.SponsorsAdsBottomView
import com.rizwansayyed.zene.ui.home.view.SponsorsAdsView
import com.rizwansayyed.zene.ui.home.view.StyleSize
import com.rizwansayyed.zene.ui.home.view.TextSize
import com.rizwansayyed.zene.ui.home.view.UpdateAvailableView
import com.rizwansayyed.zene.ui.home.view.UpdateAvailableViewLoading
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.view.AdsBannerView
import com.rizwansayyed.zene.ui.view.LoadingView
import com.rizwansayyed.zene.ui.view.NewUserCards
import com.rizwansayyed.zene.ui.view.SongDynamicCards
import com.rizwansayyed.zene.ui.view.TextPoppinsLight
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.ui.view.isScreenBig
import com.rizwansayyed.zene.ui.zeneconnect.ZeneConnectHomeView
import com.rizwansayyed.zene.utils.Utils.THREE_GRID_SIZE
import com.rizwansayyed.zene.utils.Utils.TOTAL_GRID_SIZE
import com.rizwansayyed.zene.utils.Utils.TWO_GRID_SIZE
import com.rizwansayyed.zene.utils.Utils.isPermissionDisabled
import com.rizwansayyed.zene.utils.Utils.isUpdateAvailableFunction
import com.rizwansayyed.zene.viewmodel.HomeNavModel
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


@UnstableApi
@Composable
fun HomeView(
    notificationPermission: ManagedActivityResultLauncher<String, Boolean>,
    homeViewModel: HomeViewModel, isNotificationOff: () -> Unit
) {
    val homeNavModel: HomeNavModel = hiltViewModel()
    var topHeaderDialog by remember { mutableStateOf<String?>(null) }

    val isThreeGrid = isScreenBig()

    LazyVerticalGrid(
        GridCells.Fixed(TOTAL_GRID_SIZE),
        Modifier
            .fillMaxSize()
            .background(DarkCharcoal)
    ) {
        item(1, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Column {
                HomeHeaderView()

                if (topHeaderDialog != null) {
                    Spacer(Modifier.height(20.dp))
                    TextPoppinsLight(
                        topHeaderDialog!!, true, Color.Red,
                    )
                    Spacer(Modifier.height(30.dp))
                }
            }
        }

        item(2001, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            when (val v = homeViewModel.isAppUpdateAvailable) {
                APIResponse.Empty -> {}
                is APIResponse.Error -> {}
                APIResponse.Loading -> UpdateAvailableViewLoading()
                is APIResponse.Success -> if (isUpdateAvailableFunction(v.data.appVersion ?: "")) {
                    UpdateAvailableView(v.data)
                }
            }
        }

        item(2002, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            when (val v = homeViewModel.songsYouMayLike) {
                is APIResponse.Success -> {
                    if (v.data.isEmpty()) NewUserCards()

                    if (v.data.size > 4) ReviewAppDialog()
                }

                else -> {}
            }
        }
        if (homeViewModel.loadFirstUI) {
//            item(2050, { GridItemSpan(TOTAL_GRID_SIZE) }) {
//                Column(Modifier.fillMaxWidth()) {
//                    Spacer(Modifier.height(40.dp))
//                    ZeneConnectHomeView()
//                    Spacer(Modifier.height(30.dp))
//                }
//            }

            item(2, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                Column {
                    Spacer(Modifier.height(10.dp))
                    HorizontalSongView(
                        homeViewModel.recommendedPlaylists,
                        Pair(TextSize.BIG, R.string.recommended_playlists),
                        StyleSize.HIDE_AUTHOR,
                        showGrid = false, true
                    )
                }
            }

            item(2003, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                SponsorsAdsView()
            }

            item(3, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                Column {
                    Spacer(Modifier.height(60.dp))
                }
            }

            item(4, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                Column {
                    HorizontalSongView(
                        homeViewModel.recommendedAlbums,
                        Pair(TextSize.SMALL, R.string.albums_picked_for_you),
                        StyleSize.SHOW_AUTHOR,
                        showGrid = false, true
                    )
                }
            }

            item(2005, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                AdsBannerView()
            }

            item(5, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                Column {
                    Spacer(Modifier.height(60.dp))
                }
            }
            item(6, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                Column {
                    HorizontalVideoView(
                        homeViewModel.recommendedVideo, R.string.videos_you_may_like
                    )
                }
            }
            item(7, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                Column {
                    Spacer(Modifier.height(60.dp))
                }
            }
            item(8, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                Column {
                    HorizontalSongView(
                        homeViewModel.songsYouMayLike,
                        Pair(TextSize.SMALL, R.string.songs_you_may_like),
                        StyleSize.SHOW_AUTHOR,
                        showGrid = true, false
                    )
                }
            }

            item(2004, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                SponsorsAdsBottomView()
            }

            item(9, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                AdsBannerView()
            }

            item(10, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                Column {
                    HorizontalSongView(
                        homeViewModel.moodList,
                        Pair(TextSize.SMALL, R.string.pick_your_mood),
                        StyleSize.ONLY_TEXT,
                        showGrid = true, false
                    )
                }
            }
            item(11, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                Column {
                    Spacer(Modifier.height(60.dp))
                }
            }
            item(12, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                Column {
                    HorizontalSongView(
                        homeViewModel.latestReleases,
                        Pair(TextSize.MEDIUM, R.string.latest_release),
                        StyleSize.SHOW_AUTHOR,
                        showGrid = true, false
                    )
                }
            }
        }

        if (homeViewModel.loadSecondUI) {
            item(20, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                Column {
                    Spacer(Modifier.height(60.dp))
                }
            }
            item(21, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                Column {
                    HorizontalSongView(
                        homeViewModel.topMostListeningSong,
                        Pair(TextSize.SMALL, R.string.most_listening_songs_zene),
                        StyleSize.SONG_WITH_LISTENER,
                        showGrid = true, false
                    )
                }
            }
            item(22, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                AdsBannerView()
            }

            item(23, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                Column {
                    Spacer(Modifier.height(60.dp))
                }
            }
            item(24, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                Column {
                    HorizontalArtistsView(
                        homeViewModel.topMostListeningArtists,
                        Pair(TextSize.SMALL, R.string.global_top_artists),
                        showGrid = true
                    )
                }
            }
            item(15, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                AdsBannerView()
            }

            when (val v = homeViewModel.favArtistsLists) {
                APIResponse.Empty -> {}
                is APIResponse.Error -> {}
                APIResponse.Loading -> items(5, span = { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    HomeArtistsSimilarLoading()
                }

                is APIResponse.Success -> itemsIndexed(v.data,
                    span = { _, _ -> GridItemSpan(TOTAL_GRID_SIZE) }) { i, item ->
                    Column {
                        HomeArtistsSimilarToView(item)

                        if (i % 2 == 0) {
                            AdsBannerView()
                        }
                    }
                }
            }
        }

        if (homeViewModel.loadThirdUI) {
            item(17, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                AdsBannerView()
            }

            when (val v = homeViewModel.suggestedSongsForYou) {
                APIResponse.Empty -> {}
                is APIResponse.Error -> {}
                APIResponse.Loading -> {
                    item(19, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                        Row(Modifier.padding(start = 5.dp, bottom = 7.dp)) {
                            TextPoppinsSemiBold(
                                stringResource(R.string.songs_for_you),
                                size = 15
                            )
                        }
                    }

                    item(50, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                        LoadingView(Modifier.size(32.dp))
                    }
                }

                is APIResponse.Success -> {
                    if (v.data.isNotEmpty()) {
                        item(27, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                            Row(Modifier.padding(start = 5.dp, bottom = 7.dp)) {
                                TextPoppinsSemiBold(
                                    stringResource(R.string.songs_for_you),
                                    size = 15
                                )
                            }
                        }
                        items(v.data,
                            span = { GridItemSpan(if (isThreeGrid) THREE_GRID_SIZE else TWO_GRID_SIZE) }) {
                            SongDynamicCards(it, listOf(it))
                        }
                    }
                }
            }
        }

        if (!homeViewModel.loadFirstUI || !homeViewModel.loadSecondUI || !homeViewModel.loadThirdUI) {
            item(900, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                Box(Modifier.fillMaxWidth()) {
                    Spacer(Modifier.height(9.dp))
                    LoadingView(
                        Modifier
                            .align(Alignment.Center)
                            .size(35.dp)
                    )
                }
            }
        }

        item(key = 1000, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Spacer(Modifier.height(250.dp))
        }

    }


    LaunchedEffect(homeNavModel.topAlertHeader) {
        topHeaderDialog = if (homeNavModel.topAlertHeader.length > 4) homeNavModel.topAlertHeader
        else null
    }

    LaunchedEffect(Unit) {
        homeViewModel.init(false)
        homeViewModel.checkSubscription()
        checkNotificationPermissionAndAsk(notificationPermission, isNotificationOff)
        homeNavModel.getAlertHeader()

        if (!homeViewModel.loadFirstUI) {
            delay(5.seconds)
            homeViewModel.loadFirstUI = true
        }
        if (!homeViewModel.loadSecondUI) {
            delay(4.seconds)
            homeViewModel.loadSecondUI = true
        }
        if (!homeViewModel.loadThirdUI) {
            delay(4.seconds)
            homeViewModel.loadThirdUI = true
        }
    }
}


fun checkNotificationPermissionAndAsk(
    permission: ManagedActivityResultLauncher<String, Boolean>, isNotificationOff: () -> Unit
) = CoroutineScope(Dispatchers.Main).launch {
    delay(3.seconds)
    try {
        val isLoggedIn = userInfoDB.firstOrNull()?.isLoggedIn() ?: false
        if (!isLoggedIn) return@launch
        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            isNotificationOff()
            return@launch
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return@launch

        if (isPermissionDisabled(Manifest.permission.POST_NOTIFICATIONS))
            permission.launch(Manifest.permission.POST_NOTIFICATIONS)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}