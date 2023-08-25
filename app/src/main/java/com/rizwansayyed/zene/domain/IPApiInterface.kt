package com.rizwansayyed.zene.domain

import com.rizwansayyed.zene.presenter.model.IpJSONResponse
import com.rizwansayyed.zene.utils.Utils.URL.IP_JSON
import retrofit2.http.GET

interface IPApiInterface {

    @GET(IP_JSON)
    suspend fun ip(): IpJSONResponse
}