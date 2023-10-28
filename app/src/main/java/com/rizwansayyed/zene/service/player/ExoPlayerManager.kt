package com.rizwansayyed.zene.service.player

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.session.MediaLibraryService
import com.rizwansayyed.zene.data.onlinesongs.downloader.implementation.SongDownloaderInterface
import com.rizwansayyed.zene.domain.MusicData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExoPlayerManager @Inject constructor(
    @ApplicationContext context: Context,
    private val songDownloader: SongDownloaderInterface
) {

    var musicData: Array<MusicData>? = null

    val player = ExoPlayer.Builder(context).build()

    var mediaLibrarySession: MediaLibraryService.MediaLibrarySession? = null
    var callback: MediaLibraryService.MediaLibrarySession.Callback =
        object : MediaLibraryService.MediaLibrarySession.Callback {

        }

    @androidx.media3.common.util.UnstableApi
    suspend fun downloadAndStartPlaying(positionStartToPlay: Int) {
        withContext(Dispatchers.IO) {
            try {
                val music = musicData?.get(positionStartToPlay)
                music?.pId ?: return@withContext ""
                val url = songDownloader.download(music.pId!!).first()

//                CoroutineScope(Dispatchers.Main).launch {
//                    player.apply {
//                        url?.let { u ->
//                            playWhenReady = true
//                            setMediaItem(music.toMediaItem(u))
//                            prepare()
//                            play()
//                        }
//                    }
//                }
            } catch (e: Exception) {
                Log.d("TAG", "downloadAndStartPlaying: data ee ${e.message}")
            }
        }
    }

    fun stop() {
        mediaLibrarySession?.run {
            player.release()
            release()
            mediaLibrarySession = null
        }
    }
}