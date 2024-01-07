package com.rizwansayyed.zene.data.onlinesongs.giphy


import com.rizwansayyed.zene.data.utils.GiphyAPI.GIPHY_SEARCH_API
import com.rizwansayyed.zene.domain.giphy.GiphyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyService {

    @GET(GIPHY_SEARCH_API)
    suspend fun search(
        @Query("api_key") apiKey: String?,
        @Query("q") query: String,
        @Query("sort") sort: String = "sort",
        @Query("type") type: String = "gifs",
        @Query("offset") offset: String = "offset",
        @Query("rating") rating: String = "pg-13",
    ): GiphyResponse

}
