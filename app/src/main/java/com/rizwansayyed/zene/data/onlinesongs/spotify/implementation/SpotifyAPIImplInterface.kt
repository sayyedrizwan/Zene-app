package com.rizwansayyed.zene.data.onlinesongs.spotify.implementation

import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.spotify.SpotifyItem
import kotlinx.coroutines.flow.Flow

interface SpotifyAPIImplInterface {

    suspend fun globalTrendingSongs(): Flow<List<MusicData>>

    suspend fun topSongsInCountry(): Flow<List<MusicData>>
}