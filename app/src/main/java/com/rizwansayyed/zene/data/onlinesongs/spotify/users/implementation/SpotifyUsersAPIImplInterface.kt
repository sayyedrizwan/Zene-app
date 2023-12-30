package com.rizwansayyed.zene.data.onlinesongs.spotify.users.implementation

import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.spotify.playlist.SpotifyUserPlaylistResponse
import kotlinx.coroutines.flow.Flow

interface SpotifyUsersAPIImplInterface {

    suspend fun usersPlaylist(): Flow<SpotifyUserPlaylistResponse>
}