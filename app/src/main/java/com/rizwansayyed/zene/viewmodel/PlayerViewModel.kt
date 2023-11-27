package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import com.rizwansayyed.zene.data.db.impl.RoomDBInterface
import com.rizwansayyed.zene.data.db.offlinedownload.OfflineDownloadedEntity
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.subtitles.SubtitlesScrapsImplInterface
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicPlayerData
import com.rizwansayyed.zene.domain.MusicPlayerList
import com.rizwansayyed.zene.domain.subtitles.GeniusLyricsWithInfo
import com.rizwansayyed.zene.domain.yt.PlayerVideoDetailsData
import com.rizwansayyed.zene.presenter.ui.musicplayer.utils.Utils
import com.rizwansayyed.zene.presenter.ui.musicplayer.utils.Utils.areSongNamesEqual
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val subtitlesScraps: SubtitlesScrapsImplInterface,
    private val youtubeAPI: YoutubeAPIImplInterface,
    private val roomDb: RoomDBInterface
) : ViewModel() {

    var lyricsInfo by mutableStateOf<GeniusLyricsWithInfo?>(null)
        private set

    var relatedSongs by mutableStateOf<DataResponse<List<MusicData>>>(DataResponse.Empty)
        private set

    var videoSongs by mutableStateOf<DataResponse<PlayerVideoDetailsData>>(DataResponse.Empty)
        private set

    var showMusicType by mutableStateOf(Utils.MusicViewType.MUSIC)

    fun init(data: MusicPlayerData) = viewModelScope.launch(Dispatchers.IO) {
        showMusicType = Utils.MusicViewType.MUSIC
        videoSongs = DataResponse.Empty

        val d = musicPlayerData.first()
        musicPlayerData = flowOf(d?.apply { songID = data.v?.songID ?: "" })
    }

    fun setMusicType(t: Utils.MusicViewType) {
        showMusicType = t
    }

    fun searchLyrics(d: MusicPlayerData) = viewModelScope.launch(Dispatchers.IO) {
        if (lyricsInfo?.songId == d.v?.songID) return@launch
        d.v?.let { data ->
            subtitlesScraps.searchSubtitles(data).onStart {
                lyricsInfo = null
            }.catch {
                lyricsInfo = GeniusLyricsWithInfo(d.v?.songID ?: "", "", "", false)
            }.collectLatest {
                lyricsInfo = it
            }
        }
    }


    fun similarSongsArtists(id: String) = viewModelScope.launch(Dispatchers.IO) {
        youtubeAPI.songsSuggestionsForUsers(listOf(id)).onStart {
            relatedSongs = DataResponse.Loading
        }.catch {
            relatedSongs = DataResponse.Error(it)
        }.collectLatest {
            val list = (it.related + it.next).shuffled()
            relatedSongs = DataResponse.Success(list)
        }
    }

    fun searchLyricsAndSongVideo(name: String?, artist: String?) =
        viewModelScope.launch(Dispatchers.IO) {
            videoSongs = DataResponse.Loading

            var searchQuery = "${name?.replace("(", "")?.replace(")", "")} - ${
                artist?.substringBefore(",")?.substringBefore("&")
            }"

            val videoList = try {
                youtubeAPI.youtubeVideoThisYearSearch(searchQuery).first()
            } catch (e: Exception) {
                null
            }
            var videoId: String? = null
            videoList?.forEachIndexed { i, musicData ->
                if (i == 0) if (areSongNamesEqual(musicData.name ?: "", searchQuery))
                    videoId = musicData.pId
            }

            searchQuery += "Lyrics Video"

            val lyricsList = try {
                youtubeAPI.youtubeVideoSearch(searchQuery).first()
            } catch (e: Exception) {
                null
            }

            var lyricsVideoId: String? = null
            lyricsList?.forEachIndexed { i, musicData ->
                if (i <= 2 && lyricsVideoId == null)
                    if (areSongNamesEqual(musicData.name ?: "", searchQuery))
                        lyricsVideoId = musicData.pId
            }

            videoSongs = DataResponse.Success(PlayerVideoDetailsData(videoId, lyricsVideoId))
        }


    fun addOfflineSong(player: MusicPlayerList) = viewModelScope.launch(Dispatchers.IO) {
        val offline = OfflineDownloadedEntity(
            player.songID ?: "",
            player.songName ?: "",
            player.artists ?: "",
            player.thumbnail ?: "",
            "", System.currentTimeMillis(), 0
        )

        roomDb.addOfflineSongDownload(offline).collect()
    }

}