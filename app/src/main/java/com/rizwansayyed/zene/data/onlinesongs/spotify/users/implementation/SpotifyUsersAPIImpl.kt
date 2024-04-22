package com.rizwansayyed.zene.data.onlinesongs.spotify.users.implementation

import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.spotifyToken
import com.rizwansayyed.zene.data.onlinesongs.spotify.users.SpotifyUsersAPIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SpotifyUsersAPIImpl @Inject constructor(
    private val spotifyAPI: SpotifyUsersAPIService,
) : SpotifyUsersAPIImplInterface {

    override suspend fun usersPlaylist() = flow {
        emit(spotifyAPI.userPlaylist(spotifyToken.first()))
    }.flowOn(Dispatchers.IO)

    override suspend fun userLiked(offset: Int) = flow {
        emit(spotifyAPI.userLiked(spotifyToken.first(), offset = offset))
    }.flowOn(Dispatchers.IO)


    override suspend fun playlistTrack(playlistId: String, offset: Int) = flow {
        emit(spotifyAPI.userPlaylistTrack(spotifyToken.first(), playlistId, offset))
    }.flowOn(Dispatchers.IO)


}