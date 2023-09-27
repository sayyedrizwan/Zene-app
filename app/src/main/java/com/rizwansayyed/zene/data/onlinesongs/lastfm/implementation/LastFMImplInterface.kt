package com.rizwansayyed.zene.data.onlinesongs.lastfm.implementation

import com.rizwansayyed.zene.domain.MusicData
import kotlinx.coroutines.flow.Flow

interface LastFMImplInterface {
    suspend fun topRecentPlayingSongs(): Flow<MusicData?>
}