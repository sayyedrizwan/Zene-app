package com.rizwansayyed.zene.data.onlinesongs.instagram


import com.rizwansayyed.zene.data.utils.InstagramAPI.INSTAGRAM_PROFILE_API
import com.rizwansayyed.zene.domain.instagram.InstagramDataResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface InstagramInfoService {

    @GET(INSTAGRAM_PROFILE_API)
    suspend fun instagramInfo(
        @Header("x-ig-app-id") apsongId: String,
        @Query("username") username: String,
        @Query("hl") hl: String = "en"
    ): InstagramDataResponse

}
