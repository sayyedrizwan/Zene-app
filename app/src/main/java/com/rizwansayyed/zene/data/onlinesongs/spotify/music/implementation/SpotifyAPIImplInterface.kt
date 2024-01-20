package com.rizwansayyed.zene.data.onlinesongs.spotify.music.implementation

import com.rizwansayyed.zene.domain.MusicData
import kotlinx.coroutines.flow.Flow

interface SpotifyAPIImplInterface {

    suspend fun globalTrendingSongs(): Flow<List<MusicData>>
    suspend fun topSongsInCountry(): Flow<List<MusicData>>
    suspend fun searchSongViaPlaylists(playlistId: String): Flow<MutableList<MusicData>>
    suspend fun searchTopSongsMoodPlaylists(mood: String): Flow<List<MusicData>>
}