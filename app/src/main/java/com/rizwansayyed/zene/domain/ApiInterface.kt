package com.rizwansayyed.zene.domain

import com.rizwansayyed.zene.domain.model.UrlResponse
import com.rizwansayyed.zene.utils.Utils.URL.ALBUMS_WITH_HEADERS
import retrofit2.http.GET

interface ApiInterface {

    @GET(ALBUMS_WITH_HEADERS)
    suspend fun albumsWithHeaders(): UrlResponse
}