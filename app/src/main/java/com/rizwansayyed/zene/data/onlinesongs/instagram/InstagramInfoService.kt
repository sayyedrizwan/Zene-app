package com.rizwansayyed.zene.data.onlinesongs.instagram


import com.rizwansayyed.zene.data.utils.InstagramAPI.INSTAGRAM_PROFILE_API
import com.rizwansayyed.zene.domain.InstagramDataResponse
import com.rizwansayyed.zene.domain.OnlineRadioResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

interface InstagramInfoService {

    @GET(INSTAGRAM_PROFILE_API)
    suspend fun instagramInfo(
        @Header("x-ig-app-id") appId: String,
        @Query("username") username: String,
        @Query("hl") hl: String = "en"
    ): InstagramDataResponse

}
