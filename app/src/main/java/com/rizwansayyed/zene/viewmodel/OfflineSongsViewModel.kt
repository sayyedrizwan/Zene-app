package com.rizwansayyed.zene.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.SongDataResponse
import com.rizwansayyed.zene.data.offlinesongs.OfflineSongsReadInterface
import com.rizwansayyed.zene.domain.OfflineSongsDetailsResult
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfflineSongsViewModel @Inject constructor(private val offlineSong: OfflineSongsReadInterface) :
    ViewModel() {

    var allSongs =
        mutableStateOf<SongDataResponse<List<OfflineSongsDetailsResult>>>(SongDataResponse.Empty)
        private set


    var top20Songs by mutableStateOf<List<OfflineSongsDetailsResult>>(emptyList())
        private set


    fun songsList() = viewModelScope.launch(Dispatchers.IO) {
        offlineSong.readAllSongs(0, 100).onStart {
            allSongs.value = SongDataResponse.Loading
        }.catch {
            it.message?.toast()
            allSongs.value = SongDataResponse.Error(it)
        }.collectLatest {
            it.size?.toast()
            allSongs.value = SongDataResponse.Success(it)
        }
    }

    fun latest20Songs() = viewModelScope.launch(Dispatchers.IO) {
        offlineSong.readAllSongs(0, 80).onStart {
            top20Songs = emptyList()
        }.catch {
            top20Songs = emptyList()
        }.collectLatest {
            top20Songs = it
        }
    }
}