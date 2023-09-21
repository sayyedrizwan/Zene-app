package com.rizwansayyed.zene.data.onlinesongs.radio

import com.rizwansayyed.zene.data.utils.RadioOnlineAPI.RADIO_SEARCH_API
import com.rizwansayyed.zene.domain.OnlineRadioResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OnlineRadioService {

    @GET(RADIO_SEARCH_API)
    suspend fun radioSearch(
        @Query("countrycode") countryCode: String
    ): OnlineRadioResponse

}
