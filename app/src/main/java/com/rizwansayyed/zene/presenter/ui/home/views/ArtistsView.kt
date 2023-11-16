package com.rizwansayyed.zene.presenter.ui.home.views


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.home.artists.ArtistsAlbumsList
import com.rizwansayyed.zene.presenter.ui.home.artists.ArtistsButtonView
import com.rizwansayyed.zene.presenter.ui.home.artists.ArtistsEvents
import com.rizwansayyed.zene.presenter.ui.home.artists.ArtistsImagesView
import com.rizwansayyed.zene.presenter.ui.home.artists.ArtistsLatestSongs
import com.rizwansayyed.zene.presenter.ui.home.artists.ArtistsNameWithDescription
import com.rizwansayyed.zene.presenter.ui.home.artists.ArtistsProfilePin
import com.rizwansayyed.zene.presenter.ui.home.artists.ArtistsSimilarList
import com.rizwansayyed.zene.presenter.ui.home.artists.ArtistsSocialMedia
import com.rizwansayyed.zene.presenter.ui.home.artists.ArtistsSongURL
import com.rizwansayyed.zene.presenter.ui.home.artists.ArtistsTopSongs
import com.rizwansayyed.zene.presenter.ui.home.artists.TopArtistsImageView
import com.rizwansayyed.zene.service.player.ArtistsThumbnailVideoPlayer
import com.rizwansayyed.zene.viewmodel.ArtistsViewModel
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel


@Composable
fun ArtistsView(artistsThumbnailPlayer: ArtistsThumbnailVideoPlayer) {
    val artistsViewModel: ArtistsViewModel = hiltViewModel()
    val homeNav: HomeNavViewModel = hiltViewModel()

    val scrollState = rememberScrollState()

    Box(
        Modifier
            .fillMaxSize()
            .background(DarkGreyColor)
    ) {
        Column(Modifier.verticalScroll(scrollState)) {
            if (artistsViewModel.artistsVideoId.isEmpty())
                TopArtistsImageView()
            else
                ArtistsSongURL(artistsViewModel.artistsVideoId, artistsThumbnailPlayer)

            ArtistsNameWithDescription()
            Spacer(Modifier.height(90.dp))

            ArtistsProfilePin()
            Spacer(Modifier.height(10.dp))

            ArtistsButtonView()
            Spacer(Modifier.height(40.dp))

            ArtistsSocialMedia()
            Spacer(Modifier.height(40.dp))

            ArtistsImagesView()
            Spacer(Modifier.height(40.dp))

            ArtistsEvents()
            Spacer(Modifier.height(40.dp))

            ArtistsTopSongs()
            Spacer(Modifier.height(40.dp))

            ArtistsAlbumsList()
            Spacer(Modifier.height(40.dp))

            ArtistsLatestSongs()
            Spacer(Modifier.height(40.dp))

            ArtistsSimilarList()

            Spacer(Modifier.height(190.dp))
        }

        Column {
            Spacer(Modifier.height(15.dp))

            SmallIcons(icon = R.drawable.ic_arrow_left, 28, 10) {
                homeNav.setArtists("")
            }
        }
    }

    LaunchedEffect(homeNav.selectedArtists) {
        if (homeNav.selectedArtists.isNotEmpty()) {
            artistsViewModel.init(homeNav.selectedArtists)
            scrollState.scrollTo(0)
        }
    }
}