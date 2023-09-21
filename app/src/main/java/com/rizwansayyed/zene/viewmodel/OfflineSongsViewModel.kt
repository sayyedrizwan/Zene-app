package com.rizwansayyed.zene.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.SongDataResponse
import com.rizwansayyed.zene.data.db.impl.RoomDBInterface
import com.rizwansayyed.zene.data.db.offlinedownload.OfflineDownloadedEntity
import com.rizwansayyed.zene.data.offlinesongs.OfflineSongsReadInterface
import com.rizwansayyed.zene.domain.OfflineSongsDetailsResult
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class OfflineSongsViewModel @Inject constructor(
    private val offlineSong: OfflineSongsReadInterface,
    private val room: RoomDBInterface
) : ViewModel() {

    init {
        viewModelScope.launch {
            delay(1.seconds)
            offlineDownloadedSongs()
        }
    }

    var allSongs by mutableStateOf<List<OfflineSongsDetailsResult>>(emptyList())
        private set


    var top20Songs by mutableStateOf<List<OfflineSongsDetailsResult>>(emptyList())
        private set

    var offlineDownloadedSongs
            by mutableStateOf<Flow<List<OfflineDownloadedEntity>>>(flowOf(emptyList()))
        private set


    fun songsList() = viewModelScope.launch(Dispatchers.IO) {
        offlineSong.readAllSongs().onStart {
            allSongs = emptyList()
        }.catch {
            allSongs = emptyList()
        }.collectLatest {
            allSongs = it
        }
    }

    fun latestSongs() = viewModelScope.launch(Dispatchers.IO) {
        offlineSong.readAllSongs().onStart {
            top20Songs = emptyList()
        }.catch {
            top20Songs = emptyList()
        }.collectLatest {
            if (it.size > 150) {
                top20Songs = it.subList(0, 150)
            }
        }
    }


    private fun offlineDownloadedSongs() = viewModelScope.launch(Dispatchers.IO) {
        room.offlineDownloadedSongs().onStart {
            offlineDownloadedSongs = flowOf(emptyList())
        }.catch {
            offlineDownloadedSongs = flowOf(emptyList())
        }.collectLatest {
            offlineDownloadedSongs = it
        }
    }


}