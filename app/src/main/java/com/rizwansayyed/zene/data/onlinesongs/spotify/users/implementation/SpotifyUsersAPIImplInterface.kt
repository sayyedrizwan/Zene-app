package com.rizwansayyed.zene.data.onlinesongs.spotify.users.implementation

import com.rizwansayyed.zene.domain.spotify.playlist.SpotifyUserPlaylistResponse
import com.rizwansayyed.zene.domain.spotify.playlist.SpotifyUserPlaylistTrackResponse
import kotlinx.coroutines.flow.Flow

interface SpotifyUsersAPIImplInterface {

    suspend fun usersPlaylist(): Flow<SpotifyUserPlaylistResponse>

    suspend fun playlistTrack(
        playlistId: String, offset: Int
    ): Flow<SpotifyUserPlaylistTrackResponse>

    suspend fun userLiked(limit: Int): Flow<SpotifyUserPlaylistTrackResponse>
}