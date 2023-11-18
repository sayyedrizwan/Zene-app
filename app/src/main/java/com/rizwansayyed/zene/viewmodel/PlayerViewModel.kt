package com.rizwansayyed.zene.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import com.rizwansayyed.zene.data.onlinesongs.downloader.implementation.SongDownloaderInterface
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.domain.MusicPlayerData
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
    private val youtubeAPI: YoutubeAPIImplInterface,
    private val songDownloader: SongDownloaderInterface
) : ViewModel() {

    fun init(data: MusicPlayerData) = viewModelScope.launch(Dispatchers.IO) {
        data.v?.artists?.let { Log.d("TAG", "init: ") }

        musicPlayerData = flowOf(musicPlayerData.first()?.apply { songID = data.v?.songID ?: "" })
    }

}