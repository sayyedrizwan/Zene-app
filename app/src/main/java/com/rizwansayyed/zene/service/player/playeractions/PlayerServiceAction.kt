package com.rizwansayyed.zene.service.player.playeractions

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.setWallpaperSettings
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.songSpeedSettings
import com.rizwansayyed.zene.data.db.datastore.SetWallpaperInfo
import com.rizwansayyed.zene.data.db.datastore.SongSpeed
import com.rizwansayyed.zene.data.onlinesongs.downloader.implementation.SongDownloaderInterface
import com.rizwansayyed.zene.data.onlinesongs.lastfm.implementation.LastFMImplInterface
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicPlayerList
import com.rizwansayyed.zene.domain.MusicType
import com.rizwansayyed.zene.domain.OnlineRadioResponseItem
import com.rizwansayyed.zene.domain.toMusicData
import com.rizwansayyed.zene.presenter.util.UiUtils.ContentTypes.RADIO_NAME
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.presenter.util.UtilsWallpaperImage
import com.rizwansayyed.zene.service.player.utils.Utils.toMediaItem
import com.rizwansayyed.zene.service.songparty.Utils.ActionFunctions.sendRadioChange
import com.rizwansayyed.zene.service.songparty.Utils.ActionFunctions.sendSongChange
import com.rizwansayyed.zene.service.workmanager.OfflineDownloadManager.Companion.songDownloadPath
import com.rizwansayyed.zene.utils.Utils.printStack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

var playerSongSearchJob: Job? = null

class PlayerServiceAction @Inject constructor(
    private val player: ExoPlayer,
    private val songDownloader: SongDownloaderInterface,
    private val lastFM: LastFMImplInterface
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
        val mediaId = mediaItem.mediaId
        withContext(Dispatchers.IO) {
            val musicPlayerDataLocal = musicPlayerData.first()

            val music = musicPlayerDataLocal?.songsLists?.first { it?.songId == mediaId }
                ?: return@withContext

            val position =
                musicPlayerDataLocal.songsLists.indexOfFirst { it?.songId == mediaId }

            startPlaying(music, emptyArray(), position, true)
        }
    }

    override suspend fun startPlaying(
        music: MusicData?, list: Array<MusicData?>?, position: Int, doPlay: Boolean
    ) {
        playerSongSearchJob?.cancel()
        playerSongSearchJob = CoroutineScope(Dispatchers.IO).launch {
            sendSongChange(music?.songId)
            withContext(Dispatchers.Main) {
                player.pause()
                player.stop()

                if (list?.isNotEmpty() == true) player.clearMediaItems()
            }
            withContext(Dispatchers.IO) {
                val currentPlayer =
                    MusicPlayerList(music?.name, music?.artists, music?.songId, music?.thumbnail)

                val d = musicPlayerData.first()?.apply {
                    v = currentPlayer
                    playType = MusicType.MUSIC
                    if (list?.isNotEmpty() == true) songsLists = list.toList()
                }

                musicPlayerData = flowOf(d)
            }

            withContext(Dispatchers.Main) {
                if (list?.isNotEmpty() == true) list.forEach { m ->
                    m?.toMediaItem(m.songId ?: "")?.let { player.addMediaItem(it) }
                }

                player.seekTo(position, 0)
            }

            val url = withContext(Dispatchers.IO) {
                try {
                    if (File(songDownloadPath, "${music?.songId}.mp3").exists())
                        File(songDownloadPath, "${music?.songId}.mp3").path
                    else
                        songDownloader.download(music?.songId!!).first()
                } catch (e: Exception) {
                    null
                }
            } ?: return@launch

            withContext(Dispatchers.Main) {
                player.replaceMediaItem(position, music!!.toMediaItem(url))
                player.seekTo(position, 0)
                if (doPlay) player.playWhenReady = true
                player.prepare()

                delay(1.seconds)

                if (doPlay) player.play()
                else player.pause()
            }
        }
    }


    @UnstableApi
    override suspend fun playLiveRadio(radio: OnlineRadioResponseItem) {
        withContext(Dispatchers.Main) {
            player.pause()
            player.stop()
        }

        withContext(Dispatchers.IO) {
            sendRadioChange(radio.stationuuid)
            val i =
                radio.favicon?.ifEmpty { "https://cdn-icons-png.flaticon.com/512/7999/7999266.png" }
            val currentPlayer = MusicPlayerList(radio.name, RADIO_NAME, radio.stationuuid, i)
            val musicData =
                MusicData(i, radio.name, RADIO_NAME, radio.stationuuid, MusicType.RADIO, "")

            val d = musicPlayerData.first()?.apply {
                v = currentPlayer
                playType = MusicType.RADIO
                songsLists = listOf(musicData)
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
        val presentOn = lists?.indexOfFirst { it?.songId == music.songId }

        if ((presentOn ?: -1) >= 0) {
            tempLists.removeAt(presentOn!!)

            withContext(Dispatchers.Main) {
                player.removeMediaItem(presentOn)
            }
        }

        withContext(Dispatchers.Main) {
            val currentIndex = player.currentMediaItemIndex + 1
            tempLists.add(currentIndex, music)

            player.addMediaItem(currentIndex, music.toMediaItem(music.songId ?: ""))
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
        val presentOn = lists?.indexOfFirst { it?.songId == music.songId }

        if ((presentOn ?: -1) >= 0) {
            tempLists.removeAt(presentOn!!)

            withContext(Dispatchers.Main) {
                player.removeMediaItem(presentOn)
            }
        }

        withContext(Dispatchers.Main) {
            tempLists.add(tempLists.size, music)

            player.addMediaItem(tempLists.size, music.toMediaItem(music.songId ?: ""))
        }
        withContext(Dispatchers.IO) {
            val d = musicPlayerData.first()?.apply {
                songsLists = tempLists
            }
            musicPlayerData = flowOf(d)
        }

        withContext(Dispatchers.Main) {
            player.addMediaItem(player.mediaItemCount, music.toMediaItem(music.songId ?: ""))
        }
    }

    override suspend fun seekTo(ts: Long) {
        withContext(Dispatchers.Main) {
            player.seekTo(ts)
        }
    }

    override suspend fun updatePlaybackSpeed() {
        val songSpeedSettings = withContext(Dispatchers.IO) { songSpeedSettings.first() }
        withContext(Dispatchers.Main) {
            val speed = when (songSpeedSettings) {
                SongSpeed.ZERO_TWO_FIVE.v -> 0.25f
                SongSpeed.ZERO_FIVE.v -> 0.5f
                SongSpeed.ZERO_SEVEN_FIVE.v -> 0.75f
                SongSpeed.ONE_TWO_FIVE.v -> 1.25f
                SongSpeed.ONE_FIVE.v -> 1.5f
                SongSpeed.ONE_SEVEN_FIVE.v -> 1.75f
                SongSpeed.TWO.v -> 2.0f
                else -> 1.0f
            }

            player.playbackParameters = PlaybackParameters(speed)
        }
    }

    override suspend fun updateSongsWallpaper() = CoroutineScope(Dispatchers.IO).launch {
        delay(1.seconds)
        when (setWallpaperSettings.first()) {
            SetWallpaperInfo.SONG_THUMBNAIL.v -> try {
                UtilsWallpaperImage(musicPlayerData.first()?.v?.thumbnail).homeScreenWallpaper()
            } catch (e: Exception) {
                e.printStack()
            }

            SetWallpaperInfo.ARTIST_IMAGE.v -> try {
                val userName =
                    lastFM.artistsUsername(musicPlayerData.first()?.v?.artists ?: "").first()
                val artistImage = lastFM.artistsImages(userName, 20).first().random()
                UtilsWallpaperImage(artistImage).homeScreenWallpaper()
            } catch (e: Exception) {
                e.printStack()
            }

        }

        if (isActive) cancel()
    }

}