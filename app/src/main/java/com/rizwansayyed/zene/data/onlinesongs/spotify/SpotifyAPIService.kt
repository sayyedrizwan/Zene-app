package com.rizwansayyed.zene.data.onlinesongs.spotify


import com.rizwansayyed.zene.domain.OnlineRadioResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface SpotifyAPIService {

    @GET
    suspend fun radioSearch(
        @Url url: String,
        @Query("countrycode") countryCode: String
    ): OnlineRadioResponse

}
