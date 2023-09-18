package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.SongDataResponse
import com.rizwansayyed.zene.domain.OfflineSongsDetailsResult
import com.rizwansayyed.zene.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class OfflineSongsViewModel: ViewModel() {

     var allOfflineSongs = mutableStateOf<SongDataResponse<SongDataResponse.Empty>>(SongDataResponse.Empty)
         private set



    fun checkAndSetOnlineStatus() = viewModelScope.launch(Dispatchers.IO) {
        allOfflineSongs.value = SongDataResponse.Loading
    }
}