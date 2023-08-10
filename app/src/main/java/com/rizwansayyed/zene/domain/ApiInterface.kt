package com.rizwansayyed.zene.domain

import com.rizwansayyed.zene.domain.model.UrlResponse
import com.rizwansayyed.zene.presenter.model.TopArtistsResponseApi
import com.rizwansayyed.zene.utils.Utils.URL.ALBUMS_WITH_HEADERS
import com.rizwansayyed.zene.utils.Utils.URL.TOP_ARTIST_THIS_WEEK
import com.rizwansayyed.zene.utils.Utils.URL.TOP_GLOBAL_SONGS_THIS_WEEK
import retrofit2.http.GET

interface ApiInterface {

    @GET(ALBUMS_WITH_HEADERS)
    suspend fun albumsWithHeaders(): UrlResponse


    @GET(TOP_ARTIST_THIS_WEEK)
    suspend fun topArtistOfWeek(): TopArtistsResponseApi


    @GET(TOP_GLOBAL_SONGS_THIS_WEEK)
    suspend fun topGlobalSongsThisWeek(): TopArtistsResponseApi
}