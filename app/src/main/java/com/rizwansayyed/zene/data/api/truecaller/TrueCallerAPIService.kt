package com.rizwansayyed.zene.data.api.truecaller

import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.data.api.model.IpJsonResponse
import com.rizwansayyed.zene.data.api.model.TrueCallerResponse
import com.rizwansayyed.zene.data.api.model.TrueCallerUserInfoResponse
import com.rizwansayyed.zene.utils.Utils.URLS.TRUE_CALLER_TOKEN_API
import com.rizwansayyed.zene.utils.Utils.URLS.TRUE_CALLER_USER_INFO_API
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface TrueCallerAPIService {

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST(TRUE_CALLER_TOKEN_API)
    suspend fun token(
        @Field("code") code: String,
        @Field("code_verifier") codeVerifier: String,
        @Field("grant_type") type: String = "authorization_code",
        @Field("client_id") clientID: String = BuildConfig.TRUE_CALLER_CLIENT_ID,
    ): TrueCallerResponse


    @GET(TRUE_CALLER_USER_INFO_API)
    suspend fun userInfo(@Header("Authorization") auth: String): TrueCallerUserInfoResponse
}