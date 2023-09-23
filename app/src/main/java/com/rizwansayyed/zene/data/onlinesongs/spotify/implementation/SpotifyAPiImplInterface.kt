package com.rizwansayyed.zene.data.onlinesongs.spotify.implementation

import com.rizwansayyed.zene.domain.spotify.SpotifyPlaylistResponse
import com.rizwansayyed.zene.domain.spotify.SpotifyPlaylistSongsResponse
import kotlinx.coroutines.flow.Flow

interface SpotifyAPiImplInterface {

    suspend fun globalTrendingSongs(): Flow<List<SpotifyPlaylistSongsResponse.Tracks.SpotifyItem>?>
}