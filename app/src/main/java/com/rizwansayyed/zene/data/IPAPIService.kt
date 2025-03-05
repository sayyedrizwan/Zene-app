package com.rizwansayyed.zene.data

import com.rizwansayyed.zene.data.model.IPResponse
import com.rizwansayyed.zene.utils.URLSUtils.IP_JSON_API
import retrofit2.http.POST

interface IPAPIService {

    @POST(IP_JSON_API)
    suspend fun get(): IPResponse
}