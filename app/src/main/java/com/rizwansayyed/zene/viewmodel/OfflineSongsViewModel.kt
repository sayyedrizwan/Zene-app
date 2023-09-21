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
        offlineSong.readAllSongs().onStart {
            allSongs.value = SongDataResponse.Loading
        }.catch {
            allSongs.value = SongDataResponse.Error(it)
        }.collectLatest {
            allSongs.value = SongDataResponse.Success(it)
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


}