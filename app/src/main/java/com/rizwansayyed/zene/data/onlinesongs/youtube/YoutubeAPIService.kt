package com.rizwansayyed.zene.data.onlinesongs.youtube


import com.rizwansayyed.zene.data.utils.YoutubeAPI.YT_MAIN_GUIDE
import com.rizwansayyed.zene.data.utils.YoutubeAPI.YT_NEXT_API
import com.rizwansayyed.zene.data.utils.YoutubeAPI.YT_SEARCH
import com.rizwansayyed.zene.domain.yt.YoutubeLatestYearResponse
import com.rizwansayyed.zene.domain.yt.YoutubeMerchandiseResponse
import com.rizwansayyed.zene.domain.yt.YoutubePageResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

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
