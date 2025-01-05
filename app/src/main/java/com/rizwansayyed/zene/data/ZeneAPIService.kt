package com.rizwansayyed.zene.data

import com.rizwansayyed.zene.data.model.MusicDataResponse
import com.rizwansayyed.zene.data.model.UserInfoResponse
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_MUSIC_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_UPDATE_API
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ZeneAPIService {

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_UPDATE_API)
    suspend fun updateUser(@Body data: RequestBody): UserInfoResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_RECENT_HOME_MUSIC_API)
    suspend fun recentHome(
        @Header("token") token: String, @Body data: RequestBody
    ): MusicDataResponse
}