package com.rizwansayyed.zene.domain

import com.rizwansayyed.zene.domain.model.UrlResponse
import com.rizwansayyed.zene.presenter.model.IpJSONResponse
import com.rizwansayyed.zene.presenter.model.TopArtistsResponseApi
import com.rizwansayyed.zene.utils.Utils.URL.ALBUMS_WITH_HEADERS
import com.rizwansayyed.zene.utils.Utils.URL.IP_JSON
import com.rizwansayyed.zene.utils.Utils.URL.SONG_SUGGESTIONS
import com.rizwansayyed.zene.utils.Utils.URL.TOP_ARTIST_THIS_WEEK
import com.rizwansayyed.zene.utils.Utils.URL.TOP_COUNTRY_SONGS
import com.rizwansayyed.zene.utils.Utils.URL.TOP_GLOBAL_SONGS_THIS_WEEK
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface IPApiInterface {

    @GET(IP_JSON)
    suspend fun ip(): IpJSONResponse
}