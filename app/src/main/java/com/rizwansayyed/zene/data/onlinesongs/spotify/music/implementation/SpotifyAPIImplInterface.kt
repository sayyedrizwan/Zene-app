package com.rizwansayyed.zene.data.onlinesongs.spotify.music.implementation

import com.rizwansayyed.zene.domain.MusicData
import kotlinx.coroutines.flow.Flow

interface SpotifyAPIImplInterface {

    suspend fun globalTrendingSongs(): Flow<List<MusicData>>

    suspend fun topSongsInCountry(): Flow<List<MusicData>>
}