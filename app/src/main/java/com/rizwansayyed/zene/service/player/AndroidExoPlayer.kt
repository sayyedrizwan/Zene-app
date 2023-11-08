package com.rizwansayyed.zene.service.player

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@UnstableApi
class AndroidExoPlayer @Inject constructor(@ApplicationContext c: Context) {

    private val player by lazy { ExoPlayer.Builder(c).build() }
    private val mediaSession by lazy { MediaSession.Builder(c, player).build() }

    @Composable
    fun AlbumsArtistsVideo(url: String) {
        val lifecycleOwner = LocalLifecycleOwner.current

        val mediaItem = MediaItem.Builder().setUri(url).build()
        AndroidView({ ctx ->
            PlayerView(ctx).apply {
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                player = player
                player?.setMediaItem(mediaItem)
                useController = false
                setKeepContentOnPlayerReset(true)
                setShutterBackgroundColor(Color.Transparent.toArgb())
                player?.prepare()
                player?.playWhenReady = true
            }
        }, Modifier.fillMaxSize())

        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { source, event ->
                if (event == Lifecycle.Event.ON_START) {
                    player.play()
                } else if (event == Lifecycle.Event.ON_PAUSE) {
                    player.pause()
                }
            }

            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    }


    fun destroy() {
        mediaSession.release()
        player.release()
    }
}