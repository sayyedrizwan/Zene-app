package com.rizwansayyed.zene.data.onlinesongs.radio


import com.rizwansayyed.zene.domain.OnlineRadioResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface OnlineRadioService {

    @GET
    suspend fun radioSearch(
        @Url url: String,
        @Query("countrycode") countryCode: String
    ): OnlineRadioResponse

}
