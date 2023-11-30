package com.rizwansayyed.zene.data.onlinesongs.youtube


import com.rizwansayyed.zene.data.utils.SpotifyAPI.ACCOUNT_SPOTIFY_API
import com.rizwansayyed.zene.data.utils.SpotifyAPI.ACCOUNT_SPOTIFY_C_ID
import com.rizwansayyed.zene.data.utils.SpotifyAPI.ACCOUNT_SPOTIFY_C_SECRET
import com.rizwansayyed.zene.data.utils.SpotifyAPI.SPOTIFY_API_PLAYLIST
import com.rizwansayyed.zene.data.utils.SpotifyAPI.SPOTIFY_API_SEARCH
import com.rizwansayyed.zene.data.utils.YoutubeAPI
import com.rizwansayyed.zene.data.utils.YoutubeAPI.YT_MAIN_GUIDE
import com.rizwansayyed.zene.data.utils.YoutubeAPI.YT_NEXT_API
import com.rizwansayyed.zene.data.utils.YoutubeAPI.YT_SEARCH
import com.rizwansayyed.zene.domain.OnlineRadioResponse
import com.rizwansayyed.zene.domain.spotify.SpotifyAccessTokenResponse
import com.rizwansayyed.zene.domain.spotify.SpotifyPlaylistResponse
import com.rizwansayyed.zene.domain.spotify.SpotifyPlaylistSongsResponse
import com.rizwansayyed.zene.domain.yt.BrowserIdYTResponse
import com.rizwansayyed.zene.domain.yt.YoutubeLatestYearResponse
import com.rizwansayyed.zene.domain.yt.YoutubeMerchandiseResponse
import com.rizwansayyed.zene.domain.yt.YoutubePageResponse
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface YoutubeAPIService {

    @POST(YT_MAIN_GUIDE)
    suspend fun youtubePageResponse(
        @Body body: RequestBody,
        @Query("key") key: String,
        @Query("prettyPrint") prettyPrint: Boolean = false,
    ): YoutubePageResponse

    @POST(YT_SEARCH)
    suspend fun youtubeSearchResponse(
        @Body body: RequestBody,
        @Query("key") key: String,
        @Query("prettyPrint") prettyPrint: Boolean = false,
    ): YoutubeLatestYearResponse

    @POST(YT_NEXT_API)
    suspend fun youtubeArtistsMerchandiseResponse(
        @Body body: RequestBody,
        @Query("key") key: String,
        @Query("prettyPrint") prettyPrint: Boolean = false,
    ): YoutubeMerchandiseResponse

}
