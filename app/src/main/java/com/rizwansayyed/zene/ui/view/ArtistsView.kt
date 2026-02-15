package com.rizwansayyed.zene.ui.view

import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.jakewharton.processphoenix.ProcessPhoenix
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.ArtistsResponse
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.service.notification.NavigationUtils
import com.rizwansayyed.zene.service.notification.NavigationUtils.NAV_MAIN_PAGE
import com.rizwansayyed.zene.ui.main.view.share.ShareDataView
import com.rizwansayyed.zene.ui.theme.BlackTransparent
import com.rizwansayyed.zene.utils.ads.InterstitialAdsUtils
import com.rizwansayyed.zene.utils.safeLaunch
import com.rizwansayyed.zene.utils.share.GenerateShortcuts.generateHomeScreenShortcut
import com.rizwansayyed.zene.utils.share.MediaContentUtils.startMedia
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue
import kotlin.time.Duration.Companion.seconds

@Composable
fun ArtistsView(artistsID: String) {
    val viewModel: HomeViewModel = hiltViewModel()
    val context = LocalActivity.current

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        when (val v = viewModel.artistsInfo) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> {
                val height = LocalConfiguration.current.screenHeightDp
                Column(Modifier.fillMaxWidth()) {
                    ShimmerEffect(
                        Modifier
                            .fillMaxWidth()
                            .height((height.absoluteValue / 1.5).dp)
                            .shadow(85.dp)
                    )

                    Spacer(Modifier.height(20.dp))
                    ShimmerEffect(
                        Modifier
                            .padding(horizontal = 3.dp)
                            .size(200.dp, 10.dp), durationMillis = 1000
                    )
                    Spacer(Modifier.height(12.dp))
                    ShimmerEffect(
                        Modifier
                            .padding(horizontal = 3.dp)
                            .size(100.dp, 8.dp), durationMillis = 1000
                    )
                }
            }

            is ResponseResult.Success -> {
                val height = LocalConfiguration.current.screenHeightDp

                if (v.data.data?.id != null) Box(Modifier.fillMaxSize()) {
                    GlideFullImage(v.data.data, height, Modifier.align(Alignment.TopCenter))

                    LazyColumn(Modifier.fillMaxSize()) {
                        item {
                            Spacer(
                                Modifier
                                    .height((height.absoluteValue / 1.6).dp)
                                    .background(Color.Transparent)
                            )
                        }

                        item { ArtistsDetailsInfo(v.data, viewModel) }

                        if (v.data.songs?.isNotEmpty() == true) item {
                            ArtistsHeaderText(R.string.songs)
                        }

                        items(v.data.songs?.chunked(20) ?: emptyList()) {
                            LazyRow(
                                Modifier
                                    .fillMaxWidth()
                                    .background(Color.Black)
                                    .padding(bottom = 30.dp)
                            ) {
                                items(it) { songs -> ItemCardView(songs) }
                            }
                        }

                        if (v.data.videos?.isNotEmpty() == true) item {
                            ArtistsHeaderText(R.string.videos)
                        }

                        items(v.data.videos?.chunked(20) ?: emptyList()) {
                            LazyRow(
                                Modifier
                                    .fillMaxWidth()
                                    .background(Color.Black)
                                    .padding(bottom = 30.dp)
                            ) {
                                items(it) { v -> VideoCardView(v) }
                            }
                        }

                        if (v.data.albums?.isNotEmpty() == true) item {
                            ArtistsHeaderText(R.string.albums)
                        }

                        items(v.data.albums?.chunked(20) ?: emptyList()) {
                            LazyRow(
                                Modifier
                                    .fillMaxWidth()
                                    .background(Color.Black)
                                    .padding(bottom = 30.dp)
                            ) {
                                items(it) { a -> ItemCardView(a) }
                            }
                        }

                        if (v.data.singles?.isNotEmpty() == true) item {
                            ArtistsHeaderText(R.string.singles)
                        }

                        items(v.data.singles?.chunked(20) ?: emptyList()) {
                            LazyRow(
                                Modifier
                                    .fillMaxWidth()
                                    .background(Color.Black)
                                    .padding(bottom = 30.dp)
                            ) {
                                items(it) { a -> ItemCardView(a) }
                            }
                        }

                        if (v.data.playlists?.isNotEmpty() == true) item {
                            ArtistsHeaderText(R.string.playlists)
                        }

                        item {
                            LazyRow(
                                Modifier
                                    .fillMaxWidth()
                                    .background(Color.Black)
                                    .padding(bottom = 30.dp)
                            ) {
                                items(v.data.playlists ?: emptyList()) { a -> ItemCardView(a) }
                            }
                        }

                        if (v.data.artists?.isNotEmpty() == true) item {
                            ArtistsHeaderText(R.string.similar_artists)
                        }

                        item {
                            LazyRow(
                                Modifier
                                    .fillMaxWidth()
                                    .background(Color.Black)
                                    .padding(bottom = 30.dp)
                            ) {
                                items(v.data.artists ?: emptyList()) { a -> ItemArtistsCardView(a) }
                            }
                        }

                        item {
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .background(Color.Black)
                            ) {
                                Spacer(Modifier.height(350.dp))
                            }
                        }
                    }
                }
                else Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
                    TextViewBold(stringResource(R.string.no_artist_found), 25)
                }

                LaunchedEffect(Unit) {
                    viewModel.isFollowing = v.data.isFollowing == true
                }
            }
        }

        ButtonArrowBack()
    }

    LaunchedEffect(Unit) {
        if (artistsID.length <= 3) {
            NavigationUtils.triggerHomeNav(NAV_MAIN_PAGE)
            return@LaunchedEffect
        }

        context?.let { InterstitialAdsUtils(it) }

        if (viewModel.artistsInfo !is ResponseResult.Success) viewModel.artistsInfo(artistsID) {
            CoroutineScope(Dispatchers.IO).safeLaunch {
                delay(5.seconds)
                ProcessPhoenix.triggerRebirth(context)
            }
        }
    }
}

@Composable
fun ArtistsHeaderText(txt: Int) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.Black)
    ) {
        Spacer(Modifier.height(70.dp))
        Box(Modifier.padding(horizontal = 6.dp)) {
            TextViewBold(stringResource(txt), 23)
        }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
fun ArtistsDetailsInfo(data: ArtistsResponse, viewModel: HomeViewModel) {
    var showFullDesc by remember { mutableStateOf(false) }
    var showAddToHomeScreen by remember { mutableStateOf(false) }
    var showShareView by remember { mutableStateOf(false) }
    var showMoreFollower by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Transparent,
                        Color.Transparent,
                        BlackTransparent.copy(0.2f),
                        BlackTransparent.copy(0.4f),
                        BlackTransparent.copy(0.7f),
                        Color.Black
                    )
                )
            )
            .padding(horizontal = 7.dp)
    ) {
        Spacer(Modifier.height(40.dp))
        TextViewBoldBig(data.data?.name ?: "", 50)
    }

    Box(
        Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                showFullDesc = !showFullDesc
            }
            .padding(horizontal = 9.dp)) {
        TextViewNormal(data.data?.artists ?: "", 15, line = if (showFullDesc) 1000 else 5)
    }

    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(top = 50.dp),
        Arrangement.Start,
        Alignment.CenterVertically
    ) {
        if (viewModel.isFollowing) ButtonWithBorder(R.string.following, Color.White) {
            viewModel.followArtists(data.data?.name, false) { showMoreFollower = true }
        }
        else ButtonWithBorder(R.string.follow, Color.White) {
            viewModel.followArtists(data.data?.name, true) { showMoreFollower = true }
        }
        Spacer(Modifier.width(2.dp))

        if (viewModel.isFollowing) ImageIcon(R.drawable.ic_information_circle, 20)

        Spacer(Modifier.width(15.dp))

        Spacer(
            Modifier
                .height(35.dp)
                .width(4.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White)
        )

        Spacer(Modifier.width(20.dp))

        Box(
            Modifier
                .padding(horizontal = 7.dp)
                .clickable {
                    if (data.songs?.isNotEmpty() == true) startMedia(data.songs.first(), data.songs)
                }) {
            ImageIcon(R.drawable.ic_play, 26)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Box(
            Modifier
                .padding(horizontal = 7.dp)
                .padding(end = 7.dp)
                .clickable { showAddToHomeScreen = true }) {
            ImageIcon(R.drawable.ic_screen_add_to_home, 24)
        }

        Box(
            Modifier
                .padding(horizontal = 7.dp)
                .clickable { showShareView = true }) {
            ImageIcon(R.drawable.ic_share, 24)
        }
    }


    if (showShareView) ShareDataView(data.data) {
        showShareView = false
    }

    if (showAddToHomeScreen && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) TextAlertDialog(
        R.string.add_to_home_screen,
        R.string.add_to_home_screen_artists_desc,
        {
            showAddToHomeScreen = false
        },
        {
            generateHomeScreenShortcut(data.data)
            showAddToHomeScreen = false
        })

    if (showMoreFollower) TextAlertDialog(
        R.string.error_following_artists, null,
        R.string.a_user_can_only_follow_up_to_40_artists,
        { showMoreFollower = false })
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun GlideFullImage(data: ZeneMusicData?, height: Int, modifier: Modifier) {
    GlideImage(
        data?.thumbnail,
        data?.name,
        modifier
            .fillMaxWidth()
            .height((height.absoluteValue / 1.5).dp)
            .shadow(85.dp),
        contentScale = ContentScale.Crop
    )
}