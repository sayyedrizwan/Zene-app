package com.rizwansayyed.zene.service.player

import android.content.Context
import android.net.Uri
import android.util.Log
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
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.session.MediaSession
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import java.io.File
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


@UnstableApi
class ArtistsThumbnailVideoPlayer @Inject constructor(@ApplicationContext private val c: Context) :
    Player.Listener {

    private val ePlayer by lazy {
        ExoPlayer.Builder(c).build().apply {
            volume = 0f
            addListener(this@ArtistsThumbnailVideoPlayer)
        }
    }
    private val mediaSession by lazy { MediaSession.Builder(c, ePlayer).build() }
    private var type: TYPE = TYPE.ARTISTS_THUMBNAIL

    @Composable
    fun AlbumsArtistsVideo(url: String) {
        val lifecycleOwner = LocalLifecycleOwner.current
        var doLoop = true

        AndroidView({ ctx ->
            PlayerView(ctx).apply {
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                player = ePlayer
                player?.setMediaItem(MediaItem.fromUri(Uri.parse(url)))
                useController = false

                setKeepContentOnPlayerReset(true)
                setShutterBackgroundColor(Color.Transparent.toArgb())
                player?.prepare()
                player?.playWhenReady = true
            }
        }, Modifier.fillMaxSize())

        LaunchedEffect(Unit) {
            ePlayer.pause()
            type = TYPE.ARTISTS_THUMBNAIL

            while (doLoop) {
                delay(1.seconds)
                if (ePlayer.currentPosition / 1000 < 2) {
                    ePlayer.seekTo(ePlayer.duration / 100 * 3)
                    ePlayer.play()
                }

                if ((ePlayer.duration / 1000 - ePlayer.currentPosition / 1000) <= 14) {
                    ePlayer.seekTo(0)
                }
            }
        }

        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    ePlayer.play()
                }
                if (event == Lifecycle.Event.ON_PAUSE) {
                    ePlayer.pause()
                }
                if (event == Lifecycle.Event.ON_DESTROY) {
                    destroy()
                }
            }

            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                destroy()
                lifecycleOwner.lifecycle.removeObserver(observer)
                doLoop = false
            }
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        if (playbackState == Player.STATE_READY && type == TYPE.ARTISTS_THUMBNAIL) {
            Log.d("TAG", "onPlaybackStateChanged: changed ")
        }
    }

    fun muteUnMute() {
        if (ePlayer.volume == 0f) ePlayer.volume = 1f
        else ePlayer.volume = 0f

    }

    private fun destroy() {
        ePlayer.clearMediaItems()
        try {
            mediaSession.release()
            ePlayer.release()
        } catch (e: Exception) {
            ePlayer.pause()
        }
    }

    enum class TYPE {
        ARTISTS_THUMBNAIL
    }
}