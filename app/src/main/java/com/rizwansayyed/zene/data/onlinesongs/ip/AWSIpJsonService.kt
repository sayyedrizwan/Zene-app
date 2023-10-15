package com.rizwansayyed.zene.data.onlinesongs.ip

import com.rizwansayyed.zene.data.utils.IpJsonAPI.IP_API
import com.rizwansayyed.zene.data.utils.RadioOnlineAPI
import com.rizwansayyed.zene.domain.IpJsonResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AWSIpJsonService {
    @GET("/")
    suspend fun ip(): String
}