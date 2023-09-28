package com.rizwansayyed.zene.data.onlinesongs.youtube

import com.rizwansayyed.zene.data.utils.YoutubeAPI.YT_BROWSE_API
import com.rizwansayyed.zene.data.utils.YoutubeAPI.YT_NEXT_API
import com.rizwansayyed.zene.data.utils.YoutubeAPI.YT_SEARCH_API
import com.rizwansayyed.zene.domain.yt.BrowserIdYTResponse
import com.rizwansayyed.zene.domain.yt.YoutubeMusicMainSearchResponse
import com.rizwansayyed.zene.domain.yt.YoutubeMusicRelatedResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

interface YoutubeMusicAPIService {

    @POST(YT_SEARCH_API)
    suspend fun youtubeSearchResponse(
        @Body body: RequestBody,
        @Query("key") key: String,
        @Query("prettyPrint") prettyPrint: Boolean = false,
    ): YoutubeMusicMainSearchResponse

    @POST(YT_NEXT_API)
    suspend fun youtubeNextSearchResponse(
        @Body body: RequestBody,
        @Query("key") key: String,
        @Query("prettyPrint") prettyPrint: Boolean = false,
    ): BrowserIdYTResponse

    @POST(YT_BROWSE_API)
    suspend fun youtubeBrowseResponse(
        @Body body: RequestBody,
        @Query("key") key: String,
        @Query("prettyPrint") prettyPrint: Boolean = false,
    ): YoutubeMusicRelatedResponse

}
