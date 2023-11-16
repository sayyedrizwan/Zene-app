package com.rizwansayyed.zene.data.onlinesongs.news


import com.rizwansayyed.zene.data.utils.GoogleNewsAPI.GOOGLE_NEWS_API
import com.rizwansayyed.zene.domain.news.GoogleNewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleNewsService {

    @GET(GOOGLE_NEWS_API)
    suspend fun searchNews(
        @Query("q") type: String,
        @Query("hl") hl: String = "en-IN",
        @Query("gl") gl: String = "IN",
        @Query("ceid") ceid: String = "IN:en"
    ): GoogleNewsResponse

}
