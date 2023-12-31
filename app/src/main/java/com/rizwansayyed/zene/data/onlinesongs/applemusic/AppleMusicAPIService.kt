package com.rizwansayyed.zene.data.onlinesongs.applemusic


import com.rizwansayyed.zene.data.utils.AppleMusicAPI.APPLE_MUSIC_PLAYLIST_TRACKS_URL
import com.rizwansayyed.zene.data.utils.AppleMusicAPI.APPLE_MUSIC_PLAYLIST_URL
import com.rizwansayyed.zene.data.utils.SpotifyAPI.ACCOUNT_SPOTIFY_API
import com.rizwansayyed.zene.data.utils.SpotifyAPI.ACCOUNT_SPOTIFY_C_ID
import com.rizwansayyed.zene.data.utils.SpotifyAPI.ACCOUNT_SPOTIFY_C_SECRET
import com.rizwansayyed.zene.data.utils.SpotifyAPI.SPOTIFY_API_PLAYLIST
import com.rizwansayyed.zene.data.utils.SpotifyAPI.SPOTIFY_API_SEARCH
import com.rizwansayyed.zene.domain.applemusic.AppleMusicPlaylistItemResponse
import com.rizwansayyed.zene.domain.applemusic.AppleMusicPlaylistTracksResponse
import com.rizwansayyed.zene.domain.spotify.music.SpotifyAccessTokenResponse
import com.rizwansayyed.zene.domain.spotify.music.SpotifyPlaylistResponse
import com.rizwansayyed.zene.domain.spotify.music.SpotifyPlaylistSongsResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface AppleMusicAPIService {

    @GET(APPLE_MUSIC_PLAYLIST_URL)
    suspend fun appleMusicPlaylists(
        @Header("authorization") auth: String,
        @Header("cookie") cookie: String,
        @Query("offset") offset: Int = 0
    ): AppleMusicPlaylistItemResponse


    @GET(APPLE_MUSIC_PLAYLIST_TRACKS_URL)
    suspend fun appleMusicPlaylistsTracks(
        @Header("authorization") auth: String,
        @Header("cookie") cookie: String,
        @Path("playlist_id") playlistId: String,
        @Query("include") include: String = "catalog,artists,tracks"
    ): AppleMusicPlaylistTracksResponse


}
