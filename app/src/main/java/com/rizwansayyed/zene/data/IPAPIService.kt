package com.rizwansayyed.zene.data

import com.rizwansayyed.zene.data.model.IPResponse
import com.rizwansayyed.zene.data.model.UserInfoResponse
import com.rizwansayyed.zene.utils.URLSUtils.IP_JSON_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_UPDATE_API
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface IPAPIService {

    @POST(IP_JSON_API)
    suspend fun get(): IPResponse
}