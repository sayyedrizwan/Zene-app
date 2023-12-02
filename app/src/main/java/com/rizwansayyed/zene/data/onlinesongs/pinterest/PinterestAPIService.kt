package com.rizwansayyed.zene.data.onlinesongs.pinterest


import com.rizwansayyed.zene.data.utils.PinterestAPI.PINTEREST_SEARCH_API
import com.rizwansayyed.zene.domain.pinterest.PinterestSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PinterestAPIService {

    @GET(PINTEREST_SEARCH_API)
    suspend fun searchPosts(@Query("data") search: String): PinterestSearchResponse

}
