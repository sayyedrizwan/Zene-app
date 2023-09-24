package com.rizwansayyed.zene.data.onlinesongs.spotify.implementation

import com.rizwansayyed.zene.domain.spotify.SpotifyItem
import kotlinx.coroutines.flow.Flow

interface SpotifyAPIImplInterface {

    suspend fun globalTrendingSongs(): Flow<List<SpotifyItem>?>
    suspend fun topSongsInCountry(): Flow<List<SpotifyItem>?>
}