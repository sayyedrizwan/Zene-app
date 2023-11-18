package com.rizwansayyed.zene.presenter.ui.musicplayer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch


@Composable
fun MusicPlayerView() {
    val p by musicPlayerData.collectAsState(initial = null)
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AsyncImage(
            p?.v?.thumbnail, p?.v?.songName,
            Modifier
                .fillMaxSize()
                .blur(116.dp),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun BottomNavImage(player: ExoPlayer) {
    val p by musicPlayerData.collectAsState(initial = null)
    val coroutine = rememberCoroutineScope()
    var isPlaying by remember { mutableStateOf(false) }

    Row(Modifier.fillMaxWidth()) {
        Spacer(Modifier.weight(1f))
        Box(Modifier.clickable {
            coroutine.launch(Dispatchers.IO) {
                val playerData = musicPlayerData.first()?.apply {
                    show = true
                }
                musicPlayerData = flowOf(playerData)
            }
        }) {
            p?.v?.thumbnail?.let {
                AsyncImage(p?.v?.thumbnail, p?.v?.songName, Modifier.size(50.dp))
            }
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
            override fun onIsPlayingChanged(p: Boolean) {
                super.onIsPlayingChanged(p)
                isPlaying = p
            }
        }
        player.addListener(playerListener)
        isPlaying = player.isPlaying

        onDispose {
            player.removeListener(playerListener)
        }
    }
}
