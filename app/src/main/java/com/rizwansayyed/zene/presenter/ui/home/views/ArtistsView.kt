package com.rizwansayyed.zene.presenter.ui.home.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.ui.home.artists.ArtistsSongURL
import com.rizwansayyed.zene.presenter.ui.home.artists.TopArtistsImageView
import com.rizwansayyed.zene.presenter.ui.home.artists.WebViewForArtistsVideo
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.viewmodel.ArtistsViewModel
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel


@Composable
fun ArtistsView() {
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
            ArtistsSongURL(videoLink)
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