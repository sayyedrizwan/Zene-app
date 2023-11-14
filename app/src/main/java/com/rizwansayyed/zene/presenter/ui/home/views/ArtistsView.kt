package com.rizwansayyed.zene.presenter.ui.home.views


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.ui.home.artists.ArtistsButtonView
import com.rizwansayyed.zene.presenter.ui.home.artists.ArtistsEvents
import com.rizwansayyed.zene.presenter.ui.home.artists.ArtistsImagesView
import com.rizwansayyed.zene.presenter.ui.home.artists.ArtistsNameWithDescription
import com.rizwansayyed.zene.presenter.ui.home.artists.ArtistsProfilePin
import com.rizwansayyed.zene.presenter.ui.home.artists.ArtistsSocialMedia
import com.rizwansayyed.zene.presenter.ui.home.artists.ArtistsSongURL
import com.rizwansayyed.zene.presenter.ui.home.artists.ArtistsTopSongs
import com.rizwansayyed.zene.presenter.ui.home.artists.TopArtistsImageView
import com.rizwansayyed.zene.presenter.ui.home.artists.WebViewForArtistsVideo
import com.rizwansayyed.zene.service.player.ArtistsThumbnailVideoPlayer
import com.rizwansayyed.zene.viewmodel.ArtistsViewModel
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel


@Composable
fun ArtistsView(artistsThumbnailPlayer: ArtistsThumbnailVideoPlayer) {
    val artistsViewModel: ArtistsViewModel = hiltViewModel()
    val homeNav: HomeNavViewModel = hiltViewModel()
    val context = LocalContext.current.applicationContext

    var videoLink by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .background(DarkGreyColor)
            .verticalScroll(rememberScrollState())
    ) {
        if (videoLink.isEmpty())
            TopArtistsImageView()
        else
            ArtistsSongURL(videoLink, artistsThumbnailPlayer)

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


        Spacer(Modifier.height(190.dp))
    }

    LaunchedEffect(artistsViewModel.artistsVideoId) {
        if (artistsViewModel.artistsVideoId.isNotEmpty())
            WebViewForArtistsVideo(context, artistsViewModel.artistsVideoId) {
                videoLink = it
            }
    }

    LaunchedEffect(Unit) {
        artistsViewModel.init(homeNav.selectedArtists)
    }
}