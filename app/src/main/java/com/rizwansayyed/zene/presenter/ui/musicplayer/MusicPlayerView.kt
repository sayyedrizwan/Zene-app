package com.rizwansayyed.zene.presenter.ui.musicplayer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.backgroundPalette
import com.rizwansayyed.zene.presenter.ui.musicplayer.view.SongsThumbnailsWithList
import com.rizwansayyed.zene.presenter.ui.musicplayer.view.TopPlayerHeader
import com.rizwansayyed.zene.viewmodel.PlayerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@Composable
fun MusicPlayerView(player: ExoPlayer) {
    val playerViewModel: PlayerViewModel = hiltViewModel()
    val p by musicPlayerData.collectAsState(initial = runBlocking(Dispatchers.IO) { musicPlayerData.first() })

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .backgroundPalette()
    ) {
        TopPlayerHeader()

        SongsThumbnailsWithList(p)
    }

    LaunchedEffect(Unit) {
        if (p?.songID != player.currentMediaItem?.mediaId)
            p?.let { playerViewModel.init(it) }
    }
}

@Composable
fun BottomNavImage(player: ExoPlayer) {
    val p by musicPlayerData.collectAsState(initial = null)
    val coroutine = rememberCoroutineScope()
    var isPlaying by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    Row(Modifier.fillMaxWidth()) {
        Spacer(Modifier.weight(1f))
        Box(
            Modifier
                .background(Color.Black)
                .clickable {
                    coroutine.launch(Dispatchers.IO) {
                        val playerData = musicPlayerData
                            .first()
                            ?.apply {
                                show = true
                            }
                        musicPlayerData = flowOf(playerData)
                    }
                }) {
            p?.v?.thumbnail?.let {
                AsyncImage(p?.v?.thumbnail, p?.v?.songName, Modifier.size(50.dp))
            }


            if (isLoading)
                SmallLoadingSpinner(Modifier.align(Alignment.BottomCenter))
            else
                Image(
                    painterResource(if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play), "",
                    Modifier
                        .align(Alignment.Center)
                        .size(20.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
        }
        Spacer(Modifier.width(7.dp))
    }

    DisposableEffect(Unit) {
        val playerListener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                isLoading = playbackState == ExoPlayer.STATE_BUFFERING

            }

            override fun onIsPlayingChanged(p: Boolean) {
                super.onIsPlayingChanged(p)
                isPlaying = p
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                if (!mediaItem?.requestMetadata?.mediaUri.toString().contains("https://"))
                    isLoading = true
            }
        }
        player.addListener(playerListener)
        isPlaying = player.isPlaying

        onDispose {
            player.removeListener(playerListener)
        }
    }
}


@Composable
fun SmallLoadingSpinner(modifier: Modifier) {
    Column(modifier.size(50.dp), Arrangement.Center, Alignment.CenterHorizontally) {
        Spacer(Modifier.height(12.dp))

        CircularProgressIndicator(
            modifier = Modifier.width(20.dp),
            color = Color.White,
            trackColor = MainColor,
        )
    }
}