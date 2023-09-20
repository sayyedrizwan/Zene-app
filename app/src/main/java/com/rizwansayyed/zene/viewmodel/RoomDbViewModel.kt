package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.SongDataResponse
import com.rizwansayyed.zene.data.db.impl.RoomDBImpl
import com.rizwansayyed.zene.data.db.recentplay.RecentPlayedEntity
import com.rizwansayyed.zene.domain.OfflineSongsDetailsResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomDbViewModel @Inject constructor(private val roomDBImpl: RoomDBImpl) : ViewModel() {

    var recentSongPlayed by mutableStateOf<List<RecentPlayedEntity>?>(null)
        private set


    fun recentSixPlayedSongs() = viewModelScope.launch(Dispatchers.IO) {
        roomDBImpl.recentSixPlayed()
    }
}