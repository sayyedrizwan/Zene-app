package com.rizwansayyed.zene.data.onlinesongs.spotify.implementation

import com.rizwansayyed.zene.domain.spotify.SpotifyItem
import com.rizwansayyed.zene.domain.spotify.SpotifyPlaylistResponse
import com.rizwansayyed.zene.domain.spotify.SpotifyPlaylistSongsResponse
import kotlinx.coroutines.flow.Flow

interface SpotifyAPiImplInterface {

    suspend fun globalTrendingSongs(): Flow<List<SpotifyItem>?>
    suspend fun topSongsInCountry(): Flow<List<SpotifyItem>?>
}