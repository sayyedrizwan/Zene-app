package com.rizwansayyed.zene.presenter.ui.musicplayer.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import com.rizwansayyed.zene.domain.lastfm.ArtistsShortInfo
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.home.online.SongsYouMayLikeItems
import com.rizwansayyed.zene.service.player.utils.Utils.addAllPlayer
import com.rizwansayyed.zene.utils.Utils.formatNumberToFollowers
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.PlayerViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch


@Composable
fun MusicPlayerArtists() {
    val playerViewModel: PlayerViewModel = hiltViewModel()
    val homeNav: HomeNavViewModel = hiltViewModel()
    val screenWidth = LocalConfiguration.current.screenWidthDp

    if (playerViewModel.artistsInfo.toList().isNotEmpty()) {
        Spacer(Modifier.height(30.dp))
        TopInfoWithSeeMore(stringResource(id = R.string.artist_info), null) {}

        playerViewModel.artistsInfo.toList().forEach {
            LazyRow(Modifier.fillMaxWidth()) {
                item {
                    ArtistsMainCards(it, screenWidth, homeNav)
                }

                item {
                    Box(
                        Modifier
                            .padding(4.dp)
                            .size((screenWidth / 2).dp, (screenWidth / 1.9).dp)
                    ) {
                        TextRegular(
                            stringResource(R.string.artist_top_songs__),
                            Modifier
                                .align(Alignment.Center)
                                .fillMaxWidth(),
                            doCenter = true
                        )
                    }
                }

                itemsIndexed(it.topSongs) { i, song ->
                    SongsYouMayLikeItems(song, screenWidth, homeNav) {
                        addAllPlayer(it.topSongs.toTypedArray(), i)
                    }
                }
            }
        }
    }
}


@Composable
fun ArtistsMainCards(artists: ArtistsShortInfo, screenWidth: Int, homeNav: HomeNavViewModel) {
    val coroutine = rememberCoroutineScope()
    val listeners = stringResource(R.string.listeners)
    val s = try {
        formatNumberToFollowers(artists.info.listeners?.toInt() ?: 0)
    } catch (e: Exception) {
        ""
    }
    Box(
        Modifier
            .padding(4.dp)
            .size((screenWidth / 2).dp, (screenWidth / 1.9).dp)
            .clickable {
                coroutine.launch {
                    val m = musicPlayerData.first()?.apply { show = false }
                    musicPlayerData = flowOf(m)
                }
                artists.info.name?.let { homeNav.setArtists(it) }
            }
    ) {
        AsyncImage(
            artists.info.image, artists.info.name,
            Modifier.fillMaxSize(), contentScale = ContentScale.Crop
        )

        TextThin(
            stringResource(R.string.artist_info), Modifier
                .align(Alignment.TopStart)
                .padding(6.dp), size = 15
        )

        Column(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(BlackColor)
                .padding(5.dp), Arrangement.Center
        ) {
            TextSemiBold(artists.info.name ?: "")
            Spacer(Modifier.height(4.dp))
            TextRegular("$s $listeners", size = 12)
        }
    }
}