package com.rizwansayyed.zene.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import com.rizwansayyed.zene.data.onlinesongs.downloader.implementation.SongDownloaderInterface
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.subtitles.SubtitlesScrapsImpl
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.subtitles.SubtitlesScrapsImplInterface
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.domain.MusicPlayerData
import com.rizwansayyed.zene.domain.subtitles.GeniusLyricsWithInfo
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val subtitlesScraps: SubtitlesScrapsImplInterface
) : ViewModel() {

    var lyricsInfo by mutableStateOf<GeniusLyricsWithInfo?>(null)
        private set

    fun init(data: MusicPlayerData) = viewModelScope.launch(Dispatchers.IO) {
        lyricsInfo = null
        val d = musicPlayerData.first()
        musicPlayerData = flowOf(d?.apply { songID = data.v?.songID ?: "" })
    }

    fun searchLyrics(d: MusicPlayerData) = viewModelScope.launch(Dispatchers.IO) {
        subtitlesScraps.searchSubtitles(d.v?.songName ?: "", d.v?.artists ?: "").catch {
            lyricsInfo = GeniusLyricsWithInfo("", "", false)
        }.collectLatest {
            lyricsInfo = it
        }
    }

}