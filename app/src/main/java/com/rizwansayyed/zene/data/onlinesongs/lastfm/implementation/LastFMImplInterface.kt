package com.rizwansayyed.zene.data.onlinesongs.lastfm.implementation

import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.lastfm.TopRecentPlaySongsResponse
import kotlinx.coroutines.flow.Flow

interface LastFMImplInterface {

    suspend fun artistsImages(name: String, limit: Int = 40): Flow<MutableList<String>>


    suspend fun topRecentPlayingSongs(): Flow<MutableList<MusicData>>
}