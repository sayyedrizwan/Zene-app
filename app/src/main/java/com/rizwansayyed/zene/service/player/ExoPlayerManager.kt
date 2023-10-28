package com.rizwansayyed.zene.service.player

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.ExoPlayer
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

    suspend fun downloadAndStartPlaying(positionStartToPlay: Int) {
        withContext(Dispatchers.IO) {
            try {
                val music = musicData?.get(positionStartToPlay)
                music?.pId ?: return@withContext ""
                val url = songDownloader.download(music.pId!!).first()
                Log.d("TAG", "downloadAndStartPlaying: data ee ${e.message}")

                CoroutineScope(Dispatchers.Main).launch {
                    player.apply {
                        url?.let { u ->
                            playWhenReady = true
                            setMediaItem(music.toMediaItem(u))
                            prepare()
                            play()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d("TAG", "downloadAndStartPlaying: data ee ${e.message}")
            }
        }
    }


    private fun MusicData.toMediaItem(url: String): MediaItem {
        val metadata = MediaMetadata.Builder().setTitle(this.name)
            .setDisplayTitle(this.name).setArtist(this.artists)
            .setArtworkUri(this.thumbnail?.toUri()).build()

        return MediaItem.Builder()
            .setUri(url)
            .setMediaId(this.pId ?: "")
            .setMediaMetadata(metadata)
            .build()
    }

    fun stop() {
        mediaLibrarySession?.run {
            player.release()
            release()
            mediaLibrarySession = null
        }
    }
}