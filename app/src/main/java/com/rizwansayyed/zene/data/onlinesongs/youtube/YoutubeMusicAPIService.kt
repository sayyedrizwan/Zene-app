package com.rizwansayyed.zene.data.onlinesongs.youtube

import com.rizwansayyed.zene.data.utils.YoutubeAPI.YT_BROWSE_API
import com.rizwansayyed.zene.data.utils.YoutubeAPI.YT_NEXT_API
import com.rizwansayyed.zene.data.utils.YoutubeAPI.YT_PLAYER_API
import com.rizwansayyed.zene.data.utils.YoutubeAPI.YT_SEARCH_API
import com.rizwansayyed.zene.data.utils.YoutubeAPI.YT_SEARCH_SUGGESTION_API
import com.rizwansayyed.zene.data.utils.YoutubeAPI.YT_SUGGESTIONS_API
import com.rizwansayyed.zene.domain.yt.BrowserIdYTResponse
import com.rizwansayyed.zene.domain.yt.YoutubeMusicAllSongsResponse
import com.rizwansayyed.zene.domain.yt.YoutubeMusicMainSearchResponse
import com.rizwansayyed.zene.domain.yt.YoutubeMusicRelatedResponse
import com.rizwansayyed.zene.domain.yt.YoutubeMusicReleaseResponse
import com.rizwansayyed.zene.domain.yt.YoutubeMusicSongDetailResponse
import com.rizwansayyed.zene.domain.yt.YoutubePlaylistAlbumsResponse
import com.rizwansayyed.zene.domain.yt.YoutubeSearchSuggestionResponse
import com.rizwansayyed.zene.domain.yt.YoutubeSearchTextSuggestionResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface YoutubeMusicAPIService {

    @POST(YT_SEARCH_API)
    suspend fun youtubeSearchResponse(
        @Body body: RequestBody,
        @Query("key") key: String,
        @Query("prettyPrint") prettyPrint: Boolean = false,
    ): YoutubeMusicMainSearchResponse

    @POST(YT_BROWSE_API)
    suspend fun youtubeBrowsePlaylist(
        @Body body: RequestBody,
        @Query("key") key: String,
        @Query("prettyPrint") prettyPrint: Boolean = false,
    ): YoutubePlaylistAlbumsResponse

    @POST(YT_SEARCH_API)
    suspend fun youtubeSearchAllSongsResponse(
        @Body body: RequestBody,
        @Query("key") key: String,
        @Query("prettyPrint") prettyPrint: Boolean = false,
    ): YoutubeMusicAllSongsResponse

    @POST(YT_SEARCH_API)
    suspend fun youtubeMoreSearchAllSongsResponse(
        @Body body: RequestBody,
        @Query("ctoken") ctoken: String,
        @Query("continuation") continuation: String,
        @Query("itct") itct: String,
        @Query("key") key: String,
        @Query("type") type: String = "next",
        @Query("prettyPrint") prettyPrint: Boolean = false,
    ): YoutubeMusicAllSongsResponse

    @POST(YT_NEXT_API)
    suspend fun youtubeNextSearchResponse(
        @Body body: RequestBody,
        @Query("key") key: String,
        @Query("prettyPrint") prettyPrint: Boolean = false,
    ): BrowserIdYTResponse

    @POST(YT_SUGGESTIONS_API)
    suspend fun youtubeSearchSuggestion(
        @Body body: RequestBody,
        @Query("key") key: String,
        @Query("prettyPrint") prettyPrint: Boolean = false,
    ): YoutubeSearchSuggestionResponse

    @POST(YT_BROWSE_API)
    suspend fun youtubeBrowseResponse(
        @Body body: RequestBody,
        @Query("key") key: String,
        @Query("prettyPrint") prettyPrint: Boolean = false,
    ): YoutubeMusicRelatedResponse

    @POST(YT_BROWSE_API)
    suspend fun youtubeReleaseResponse(
        @Body body: RequestBody,
        @Query("key") key: String,
        @Query("prettyPrint") prettyPrint: Boolean = false,
    ): YoutubeMusicReleaseResponse

    @POST(YT_SEARCH_SUGGESTION_API)
    suspend fun youtubeSearchTextSuggestionResponse(
        @Body body: RequestBody,
        @Query("key") key: String,
        @Query("prettyPrint") prettyPrint: Boolean = false,
    ): YoutubeSearchTextSuggestionResponse

    @POST(YT_PLAYER_API)
    suspend fun songDetail(
        @Body body: RequestBody,
        @Query("key") key: String,
        @Query("prettyPrint") prettyPrint: Boolean = false,
    ): YoutubeMusicSongDetailResponse

}
