package com.rizwansayyed.zene.data.api.zene

import com.rizwansayyed.zene.data.api.model.StatusResponse
import com.rizwansayyed.zene.data.api.model.ZeneUsersResponse
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_USER_API
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ZeneAPIService {

    @POST(ZENE_USER_API)
    suspend fun updateUser(@Body body: RequestBody): StatusResponse

    @GET(ZENE_USER_API)
    suspend fun getUser(@Query("user") user: String): ZeneUsersResponse
}