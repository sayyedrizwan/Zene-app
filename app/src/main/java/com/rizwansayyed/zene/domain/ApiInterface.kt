package com.rizwansayyed.zene.domain

import com.rizwansayyed.zene.domain.model.UrlResponse
import com.rizwansayyed.zene.presenter.model.TopArtistsResponseApi
import com.rizwansayyed.zene.utils.Utils.URL.ALBUMS_WITH_HEADERS
import com.rizwansayyed.zene.utils.Utils.URL.SONG_SUGGESTIONS
import com.rizwansayyed.zene.utils.Utils.URL.TOP_ARTIST_THIS_WEEK
import com.rizwansayyed.zene.utils.Utils.URL.TOP_COUNTRY_SONGS
import com.rizwansayyed.zene.utils.Utils.URL.TOP_GLOBAL_SONGS_THIS_WEEK
import com.rizwansayyed.zene.utils.Utils.URL.TRENDING_SONGS_S_TOP_50
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {

    @GET(ALBUMS_WITH_HEADERS)
    suspend fun albumsWithHeaders(): UrlResponse


    @GET(TOP_ARTIST_THIS_WEEK)
    suspend fun topArtistOfWeek(): TopArtistsResponseApi


    @GET(TOP_GLOBAL_SONGS_THIS_WEEK)
    suspend fun topGlobalSongsThisWeek(): TopArtistsResponseApi


    @GET("$TOP_COUNTRY_SONGS/{country}")
    suspend fun topCountrySongs(@Path(value = "country") country: String): TopArtistsResponseApi

    @GET("$TRENDING_SONGS_S_TOP_50/{country}")
    suspend fun trendingSongsTop50(@Path(value = "country") country: String): TopArtistsResponseApi


    @FormUrlEncoded
    @POST(SONG_SUGGESTIONS)
    suspend fun songSuggestions(
        @Field("ip") ip: String,
        @Field("id") id: String,
    ): TopArtistsResponseApi
}