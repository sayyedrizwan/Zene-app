package com.rizwansayyed.zene.api

import com.rizwansayyed.zene.api.model.ablumsheader.AlbumsHeadersReponse
import com.rizwansayyed.zene.utils.Utils.URL.ALBUMS_WITH_HEADERS
import retrofit2.http.GET

interface ApiInterface {

    @GET(ALBUMS_WITH_HEADERS)
    suspend fun albumsWithHeaders(): AlbumsHeadersReponse
}