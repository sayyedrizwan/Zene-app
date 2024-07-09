package com.rizwansayyed.zene.data.api.ip

import com.rizwansayyed.zene.data.api.model.IpJsonResponse
import com.rizwansayyed.zene.data.api.model.StatusResponse
import com.rizwansayyed.zene.utils.Utils.URLS.JSON_IP
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface IpAPIService {

    @GET(JSON_IP)
    suspend fun ip(): IpJsonResponse
}