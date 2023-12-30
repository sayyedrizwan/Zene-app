package com.rizwansayyed.zene.data.onlinesongs.spotify.users


import com.rizwansayyed.zene.data.utils.SpotifyAPI.SPOTIFY_USER_API_PLAYLIST
import com.rizwansayyed.zene.domain.spotify.playlist.SpotifyUserPlaylistResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifyUsersAPIService {

    @GET(SPOTIFY_USER_API_PLAYLIST)
    suspend fun userPlaylist(
        @Header("Authorization") auth: String,
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 50
    ): SpotifyUserPlaylistResponse


}
