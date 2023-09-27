package com.rizwansayyed.zene.data.onlinesongs.lastfm.implementation

import com.rizwansayyed.zene.domain.MusicData
import kotlinx.coroutines.flow.Flow

interface LastFMImplInterface {

    suspend fun topRecentPlayingSongs(): Flow<Pair<MusicData?, String?>>

    suspend fun artistsImages(name: String, limit: Int = 40): Flow<MutableList<String>>
}