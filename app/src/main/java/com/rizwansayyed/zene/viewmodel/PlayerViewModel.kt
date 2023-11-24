package com.rizwansayyed.zene.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.subtitles.SubtitlesScrapsImplInterface
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.youtubescrap.YoutubeScrapsImpl
import com.rizwansayyed.zene.data.onlinesongs.youtube.YoutubeMusicAPIService
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicPlayerData
import com.rizwansayyed.zene.domain.SongsSuggestionsData
import com.rizwansayyed.zene.domain.subtitles.GeniusLyricsWithInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val subtitlesScraps: SubtitlesScrapsImplInterface,
    private val youtubeAPI: YoutubeAPIImplInterface
) : ViewModel() {

    var lyricsInfo by mutableStateOf<GeniusLyricsWithInfo?>(null)
        private set

    var relatedSongs by mutableStateOf<DataResponse<List<MusicData>>>(DataResponse.Empty)
        private set

    fun init(data: MusicPlayerData) = viewModelScope.launch(Dispatchers.IO) {
        val d = musicPlayerData.first()
        musicPlayerData = flowOf(d?.apply { songID = data.v?.songID ?: "" })
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
}