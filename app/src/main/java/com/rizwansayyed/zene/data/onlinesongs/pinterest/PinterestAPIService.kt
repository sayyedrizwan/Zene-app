package com.rizwansayyed.zene.data.onlinesongs.pinterest


import com.rizwansayyed.zene.data.utils.PinterestAPI.PINTEREST_DATA_QUERY
import com.rizwansayyed.zene.data.utils.PinterestAPI.PINTEREST_SEARCH_API
import com.rizwansayyed.zene.data.utils.SpotifyAPI.ACCOUNT_SPOTIFY_API
import com.rizwansayyed.zene.data.utils.SpotifyAPI.ACCOUNT_SPOTIFY_C_ID
import com.rizwansayyed.zene.data.utils.SpotifyAPI.ACCOUNT_SPOTIFY_C_SECRET
import com.rizwansayyed.zene.data.utils.SpotifyAPI.SPOTIFY_API_PLAYLIST
import com.rizwansayyed.zene.data.utils.SpotifyAPI.SPOTIFY_API_SEARCH
import com.rizwansayyed.zene.domain.OnlineRadioResponse
import com.rizwansayyed.zene.domain.pinterest.PinterestSearchResponse
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

interface PinterestAPIService {

    @GET(PINTEREST_SEARCH_API)
    suspend fun searchPosts(
        @Query("source_url") source: String,
        @Query("data") type: String = PINTEREST_DATA_QUERY
    ): PinterestSearchResponse

}
