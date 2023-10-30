package com.rizwansayyed.zene.service.player.playeractions

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import com.rizwansayyed.zene.data.onlinesongs.downloader.implementation.SongDownloaderInterface
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicPlayerData
import com.rizwansayyed.zene.domain.MusicPlayerList
import com.rizwansayyed.zene.service.player.utils.Utils.toMediaItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlayerServiceAction @Inject constructor(
    private val player: ExoPlayer,
    private val songDownloader: SongDownloaderInterface
) : PlayerServiceActionInterface {

    override suspend fun addMultipleItemsAndPlay(list: Array<MusicData?>?, position: Int) {
        val music = if ((list?.size ?: 0) >= position) list?.get(position) else return
        startPlaying(music, list, position)
    }

    override suspend fun updatePlaying(mediaItem: MediaItem?) {
        mediaItem ?: return
        val musicPlayerDataLocal = musicPlayerData.first()

        val music = musicPlayerDataLocal?.songsLists?.first { it?.pId == mediaItem.mediaId }
            ?: return

        val position =
            musicPlayerDataLocal.songsLists.indexOfFirst { it?.pId == mediaItem.mediaId }

        startPlaying(music, emptyArray(), position)
    }

    override suspend fun startPlaying(music: MusicData?, list: Array<MusicData?>?, position: Int) {
        withContext(Dispatchers.Main) {
            player.pause()
            player.stop()

            if (list?.isNotEmpty() == true) player.clearMediaItems()
        }
        withContext(Dispatchers.IO) {
            val currentPlayer =
                MusicPlayerList(music?.name, music?.artists, music?.pId, music?.thumbnail)

            val d = musicPlayerData.first()?.apply {
                v = currentPlayer
                if (list?.isNotEmpty() == true) songsLists = list.toList()
            }

            musicPlayerData = flowOf(d)
        }

        withContext(Dispatchers.Main) {
            if (list?.isNotEmpty() == true) list.forEach { m ->
                m?.toMediaItem(m.pId ?: "")?.let { player.addMediaItem(it) }
            }
        }

        val url = withContext(Dispatchers.IO) {
            try {
                songDownloader.download(music?.pId!!).first()
            } catch (e: Exception) {
                null
            }
        }

        withContext(Dispatchers.Main) {
            player.replaceMediaItem(position, music!!.toMediaItem(url!!))
            player.seekTo(position, 0)

            player.playWhenReady = true
            player.prepare()
            player.play()
        }
    }


}