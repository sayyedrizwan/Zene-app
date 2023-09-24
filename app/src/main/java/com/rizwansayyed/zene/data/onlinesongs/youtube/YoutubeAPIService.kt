package com.rizwansayyed.zene.data.onlinesongs.youtube


import com.rizwansayyed.zene.data.utils.SpotifyAPI.ACCOUNT_SPOTIFY_API
import com.rizwansayyed.zene.data.utils.SpotifyAPI.ACCOUNT_SPOTIFY_C_ID
import com.rizwansayyed.zene.data.utils.SpotifyAPI.ACCOUNT_SPOTIFY_C_SECRET
import com.rizwansayyed.zene.data.utils.SpotifyAPI.SPOTIFY_API_PLAYLIST
import com.rizwansayyed.zene.data.utils.SpotifyAPI.SPOTIFY_API_SEARCH
import com.rizwansayyed.zene.domain.OnlineRadioResponse
import com.rizwansayyed.zene.domain.spotify.SpotifyAccessTokenResponse
import com.rizwansayyed.zene.domain.spotify.SpotifyPlaylistResponse
import com.rizwansayyed.zene.domain.spotify.SpotifyPlaylistSongsResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface YoutubeAPIService {

    @FormUrlEncoded
    @POST
    suspend fun spotifyAccessToken(
        @Url url: String = ACCOUNT_SPOTIFY_API,
        @Field("grant_type") type: String = "client_credentials",
        @Field("client_id") clientId: String = ACCOUNT_SPOTIFY_C_ID,
        @Field("client_secret") clientSecret: String = ACCOUNT_SPOTIFY_C_SECRET
    ): SpotifyAccessTokenResponse


    @GET(SPOTIFY_API_SEARCH)
    suspend fun spotifyPlaylistSearch(
        @Header("Authorization") auth: String,
        @Query("q") q: String,
        @Query("type") type: String = "playlist"
    ): SpotifyPlaylistResponse

    @GET("$SPOTIFY_API_PLAYLIST/{pid}")
    suspend fun spotifyPlaylistSongs(
        @Header("Authorization") auth: String,
        @Path("pid") pid: String
    ): SpotifyPlaylistSongsResponse

}
