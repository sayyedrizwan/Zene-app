package com.rizwansayyed.zene.service.player.playeractions

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import com.rizwansayyed.zene.data.onlinesongs.downloader.implementation.SongDownloaderInterface
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicPlayerData
import com.rizwansayyed.zene.domain.MusicPlayerList
import com.rizwansayyed.zene.domain.MusicType
import com.rizwansayyed.zene.domain.OnlineRadioResponseItem
import com.rizwansayyed.zene.domain.toMusicData
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.service.player.utils.Utils.toMediaItem
import com.rizwansayyed.zene.service.workmanager.OfflineDownloadManager.Companion.songDownloadPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class PlayerServiceAction @Inject constructor(
    private val player: ExoPlayer,
    private val songDownloader: SongDownloaderInterface
) : PlayerServiceActionInterface {

    override suspend fun addMultipleItemsAndPlay(list: Array<MusicData?>?, position: Int) {
        val music = if ((list?.size ?: 0) >= position) list?.get(position) else return
        startPlaying(music, list, position, true)
    }

    override suspend fun addMultipleItemsAndNotPlay(list: Array<MusicData?>?, position: Int) {
        val music = if ((list?.size ?: 0) >= position) list?.get(position) else return
        startPlaying(music, list, position, false)
    }

    override suspend fun updatePlaying(mediaItem: MediaItem?) {
        mediaItem ?: return
        val musicPlayerDataLocal = musicPlayerData.first()

        val music = musicPlayerDataLocal?.songsLists?.first { it?.pId == mediaItem.mediaId }
            ?: return

        val position =
            musicPlayerDataLocal.songsLists.indexOfFirst { it?.pId == mediaItem.mediaId }

        startPlaying(music, emptyArray(), position, true)
    }

    override suspend fun startPlaying(
        music: MusicData?, list: Array<MusicData?>?, position: Int, doPlay: Boolean
    ) {
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
                playType = MusicType.MUSIC
                if (list?.isNotEmpty() == true) songsLists = list.toList()
            }

            musicPlayerData = flowOf(d)
        }

        withContext(Dispatchers.Main) {
            if (list?.isNotEmpty() == true) list.forEach { m ->
                m?.toMediaItem(m.pId ?: "")?.let { player.addMediaItem(it) }
            }

            player.seekTo(position, 0)
        }

        val url = withContext(Dispatchers.IO) {
            try {
                if (File(songDownloadPath, "${music?.pId}.mp3").exists())
                    File(songDownloadPath, "${music?.pId}.mp3").path
                else
                    songDownloader.download(music?.pId!!).first()
            } catch (e: Exception) {
                null
            }
        } ?: return

        withContext(Dispatchers.Main) {
            player.replaceMediaItem(position, music!!.toMediaItem(url))
            player.seekTo(position, 0)

            player.playWhenReady = doPlay

            player.prepare()
            if (doPlay) player.play()
            else player.pause()
        }
    }


    @UnstableApi
    override suspend fun playLiveRadio(radio: OnlineRadioResponseItem) {
        withContext(Dispatchers.Main) {
            player.pause()
            player.stop()
        }

        withContext(Dispatchers.IO) {
            val i =
                radio.favicon?.ifEmpty { "https://cdn-icons-png.flaticon.com/512/7999/7999266.png" }
            val currentPlayer = MusicPlayerList(radio.name, radio.language, radio.serveruuid, i)

            val d = musicPlayerData.first()?.apply {
                v = currentPlayer
                playType = MusicType.RADIO
                songsLists = emptyList()
            }
            musicPlayerData = flowOf(d)
        }

        withContext(Dispatchers.Main) {
            val item = radio.toMusicData().toMediaItem(radio.url_resolved!!)
            if (radio.url_resolved.contains(".m3u8")) {
                val mediaSource =
                    HlsMediaSource.Factory(DefaultHttpDataSource.Factory()).createMediaSource(item)

                player.setMediaSource(mediaSource)
            } else
                player.setMediaItem(item)

            player.playWhenReady = true
            player.prepare()
            player.play()
        }
    }

    override suspend fun addItemToNext(music: MusicData) {
        val lists = withContext(Dispatchers.IO) {
            musicPlayerData.first()?.songsLists
        }
        val tempLists = ArrayList(lists ?: emptyList())
        val presentOn = lists?.indexOfFirst { it?.pId == music.pId }

        if ((presentOn ?: -1) >= 0) {
            tempLists.removeAt(presentOn!!)

            withContext(Dispatchers.Main) {
                player.removeMediaItem(presentOn)
            }
        }

        withContext(Dispatchers.Main) {
            val currentIndex = player.currentMediaItemIndex + 1
            tempLists.add(currentIndex, music)

            player.addMediaItem(currentIndex, music.toMediaItem(music.pId ?: ""))
        }
        withContext(Dispatchers.IO) {
            val d = musicPlayerData.first()?.apply {
                songsLists = tempLists
            }
            musicPlayerData = flowOf(d)
        }
    }

    override suspend fun addItemToEnd(music: MusicData) {
        val lists = withContext(Dispatchers.IO) {
            musicPlayerData.first()?.songsLists
        }
        val tempLists = ArrayList(lists ?: emptyList())
        val presentOn = lists?.indexOfFirst { it?.pId == music.pId }

        if ((presentOn ?: -1) >= 0) {
            tempLists.removeAt(presentOn!!)

            withContext(Dispatchers.Main) {
                player.removeMediaItem(presentOn)
            }
        }

        withContext(Dispatchers.Main) {
            tempLists.add(tempLists.size, music)

            player.addMediaItem(tempLists.size, music.toMediaItem(music.pId ?: ""))
        }
        withContext(Dispatchers.IO) {
            val d = musicPlayerData.first()?.apply {
                songsLists = tempLists
            }
            musicPlayerData = flowOf(d)
        }

        withContext(Dispatchers.Main) {
            player.addMediaItem(player.mediaItemCount, music.toMediaItem(music.pId ?: ""))
        }
    }

    override suspend fun seekTo(ts: Long) {
        withContext(Dispatchers.Main) {
            player.seekTo(ts)
        }
    }

}