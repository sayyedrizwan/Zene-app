package com.rizwansayyed.zene.presenter.ui.home.views


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.home.artists.ArtistsButtonView
import com.rizwansayyed.zene.presenter.ui.home.artists.ArtistsImagesView
import com.rizwansayyed.zene.presenter.ui.home.artists.ArtistsNameWithDescription
import com.rizwansayyed.zene.presenter.ui.home.artists.ArtistsSongURL
import com.rizwansayyed.zene.presenter.ui.home.artists.TopArtistsImageView
import com.rizwansayyed.zene.presenter.ui.home.artists.WebViewForArtistsVideo
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.service.player.ArtistsThumbnailVideoPlayer
import com.rizwansayyed.zene.viewmodel.ArtistsViewModel
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel


@Composable
fun ArtistsView(artistsThumbnailPlayer: ArtistsThumbnailVideoPlayer) {
    val artistsViewModel: ArtistsViewModel = hiltViewModel()
    val homeNav: HomeNavViewModel = hiltViewModel()
    val context = LocalContext.current.applicationContext

    var videoLink by remember { mutableStateOf("") }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(DarkGreyColor)
//            .verticalScroll(rememberScrollState())
    ) {
        item {
            Column(Modifier.fillMaxWidth()) {
                if (videoLink.isEmpty())
                    TopArtistsImageView()
                else
                    ArtistsSongURL(videoLink, artistsThumbnailPlayer)
            }
        }
        item {
            Column(Modifier.fillMaxWidth()) {
                ArtistsNameWithDescription()
                Spacer(Modifier.height(30.dp))
            }
        }
        item {
            Column(Modifier.fillMaxWidth()) {
                ArtistsButtonView()
                Spacer(Modifier.height(40.dp))
            }
        }
        item {
            Column(Modifier.fillMaxWidth()) {
                ArtistsImagesView()
            }
        }
        item {
            Spacer(Modifier.height(190.dp))
        }
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