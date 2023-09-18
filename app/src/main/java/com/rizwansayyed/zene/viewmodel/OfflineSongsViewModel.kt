package com.rizwansayyed.zene.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.SongDataResponse
import com.rizwansayyed.zene.data.offlinesongs.OfflineSongsReadInterface
import com.rizwansayyed.zene.domain.OfflineSongsDetailsResult
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

    var allOfflineSongs =
        mutableStateOf<SongDataResponse<List<OfflineSongsDetailsResult>>>(SongDataResponse.Empty)
        private set


    fun offlineSongsList() = viewModelScope.launch(Dispatchers.IO) {
        offlineSong.readAllSongs().onStart {
            allOfflineSongs.value = SongDataResponse.Loading
        }.catch {
            Log.d("TAG", "offlineSongsList: data ${it.message}")

//            allOfflineSongs.value = SongDataResponse.Error(it)
        }.collectLatest {
            allOfflineSongs.value = SongDataResponse.Success(it)
        }
    }
}