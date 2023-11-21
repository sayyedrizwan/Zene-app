package com.rizwansayyed.zene.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import com.rizwansayyed.zene.data.onlinesongs.downloader.implementation.SongDownloaderInterface
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.subtitles.SubtitlesScrapsImpl
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.domain.MusicPlayerData
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
    private val youtubeAPI: YoutubeAPIImplInterface,
    private val songDownloader: SongDownloaderInterface
) : ViewModel() {

    fun init(data: MusicPlayerData) = viewModelScope.launch(Dispatchers.IO) {
        val d = musicPlayerData.first()
        musicPlayerData = flowOf(d?.apply { songID = data.v?.songID ?: "" })

        SubtitlesScrapsImpl()
            .searchSubtitles(d?.v?.songName ?: "",d?.v?.artists ?: "").catch {
                it.message?.toast()
            }.collectLatest {

            }
    }

}