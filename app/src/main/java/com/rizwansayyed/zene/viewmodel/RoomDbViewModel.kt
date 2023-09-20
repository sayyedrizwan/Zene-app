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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class RoomDbViewModel @Inject constructor(private val roomDBImpl: RoomDBImpl) : ViewModel() {

    init {
        viewModelScope.launch {
            delay(500)
            recentSixPlayedSongs()

            delay(1500)
            tempInsert()
        }
    }

    var recentSongPlayed by mutableStateOf<Flow<List<RecentPlayedEntity>?>>(flowOf(null))
        private set



    private fun recentSixPlayedSongs() = viewModelScope.launch(Dispatchers.IO) {
        roomDBImpl.recentSixPlayed().onStart {
            recentSongPlayed = flowOf(null)
        }.catch {
            recentSongPlayed = flowOf(emptyList())
        }.collectLatest {
            recentSongPlayed = it
        }
    }

    private fun tempInsert() = viewModelScope.launch(Dispatchers.IO) {

//        val insert = RecentPlayedEntity(
//            null, "10,000 Hours", "Dan + Shay & Justin Bieber", 3, "ids", "https://lh3.googleusercontent.com/pGBh7nsGr5Ztbb9uW2SNHBZbGy2iFf8LlemrY4oc_CkTKSRGm5UHWuKfj11_THKqfvT8A3DoR_tUztbV_g=w847-h847-l90-rj", System.currentTimeMillis(), 0, 0
//        )
//
//        roomDBImpl.insert(insert).collect()
    }
}