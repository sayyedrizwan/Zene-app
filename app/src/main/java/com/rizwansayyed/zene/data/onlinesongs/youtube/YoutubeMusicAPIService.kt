package com.rizwansayyed.zene.data.onlinesongs.youtube

import com.rizwansayyed.zene.data.utils.YoutubeAPI.YT_SEARCH_API
import com.rizwansayyed.zene.domain.yt.YoutubeMusicMainSearchResponse
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

}
