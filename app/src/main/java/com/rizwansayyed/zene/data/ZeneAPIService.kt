package com.rizwansayyed.zene.data

import com.rizwansayyed.zene.data.model.UserInfoResponse
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_UPDATE_API
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ZeneAPIService {

    @POST(ZENE_USER_UPDATE_API)
    suspend fun updateUser(@Body data: UserInfoResponse): UserInfoResponse
}