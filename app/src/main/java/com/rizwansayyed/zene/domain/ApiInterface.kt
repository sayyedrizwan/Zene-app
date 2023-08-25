package com.rizwansayyed.zene.domain

import com.rizwansayyed.zene.domain.model.SongDetailsResponse
import com.rizwansayyed.zene.domain.model.UrlResponse
import com.rizwansayyed.zene.presenter.model.ArtistsInstagramPostResponse
import com.rizwansayyed.zene.presenter.model.ArtistsTwitterInfoResponse
import com.rizwansayyed.zene.presenter.model.SongLyricsResponse
import com.rizwansayyed.zene.presenter.model.TopArtistsResponseApi
import com.rizwansayyed.zene.utils.Utils.URL.ARTISTS_DATA
import com.rizwansayyed.zene.utils.Utils.URL.ARTISTS_INSTAGRAM_POSTS
import com.rizwansayyed.zene.utils.Utils.URL.ARTISTS_TWITTER_TWEETS
import com.rizwansayyed.zene.utils.Utils.URL.SEARCH_SONGS
import com.rizwansayyed.zene.utils.Utils.URL.SIMILAR_ARTISTS
import com.rizwansayyed.zene.utils.Utils.URL.SONG_LYRICS
import com.rizwansayyed.zene.utils.Utils.URL.SONG_PLAY_DETAILS
import com.rizwansayyed.zene.utils.Utils.URL.SONG_SUGGESTIONS
import com.rizwansayyed.zene.utils.Utils.URL.SONG_SUGGESTIONS_FOR_YOU
import com.rizwansayyed.zene.utils.Utils.URL.TOP_ARTIST_THIS_WEEK
import com.rizwansayyed.zene.utils.Utils.URL.TOP_COUNTRY_SONGS
import com.rizwansayyed.zene.utils.Utils.URL.TOP_GLOBAL_SONGS_THIS_WEEK
import com.rizwansayyed.zene.utils.Utils.URL.TRENDING_SONGS_APPLE
import com.rizwansayyed.zene.utils.Utils.URL.TRENDING_SONGS_TOP_50_K_POP
import com.rizwansayyed.zene.utils.Utils.URL.TRENDING_SONGS_TOP_K_POP
import com.rizwansayyed.zene.utils.Utils.URL.VIDEO_PLAY_DETAILS
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {

    @GET(TOP_ARTIST_THIS_WEEK)
    suspend fun topArtistOfWeek(): TopArtistsResponseApi


    @GET(TOP_GLOBAL_SONGS_THIS_WEEK)
    suspend fun topGlobalSongsThisWeek(): TopArtistsResponseApi

    @FormUrlEncoded
    @POST(TOP_COUNTRY_SONGS)
    suspend fun topCountrySongs(@Field("q") q: String): TopArtistsResponseApi

    @FormUrlEncoded
    @POST(TRENDING_SONGS_APPLE)
    suspend fun trendingSongsTop50(@Field("q") q: String): TopArtistsResponseApi

    @FormUrlEncoded
    @POST(TRENDING_SONGS_TOP_K_POP)
    suspend fun trendingSongsTopKPop(@Field("q") q: String): TopArtistsResponseApi

    @FormUrlEncoded
    @POST(TRENDING_SONGS_TOP_50_K_POP)
    suspend fun trendingSongsTop50KPop(@Field("q") q: String): TopArtistsResponseApi

    @FormUrlEncoded
    @POST(SIMILAR_ARTISTS)
    suspend fun similarArtists(@Field("q") q: String): TopArtistsResponseApi

    @FormUrlEncoded
    @POST(ARTISTS_INSTAGRAM_POSTS)
    suspend fun artistsInstagramPosts(@Field("q") q: String): ArtistsInstagramPostResponse


    @FormUrlEncoded
    @POST(ARTISTS_TWITTER_TWEETS)
    suspend fun artistsTwitterTweets(@Field("q") q: String): ArtistsTwitterInfoResponse


    @FormUrlEncoded
    @POST(SONG_SUGGESTIONS)
    suspend fun songSuggestions(
        @Field("ip") ip: String, @Field("id") id: String,
    ): TopArtistsResponseApi

    @FormUrlEncoded
    @POST(SONG_LYRICS)
    suspend fun songLyrics(@Field("q") id: String): SongLyricsResponse


    @FormUrlEncoded
    @POST(SONG_SUGGESTIONS_FOR_YOU)
    suspend fun songSuggestionsForYou(
        @Field("ip") ip: String, @Field("id") id: String,
    ): TopArtistsResponseApi


    @FormUrlEncoded
    @POST(SEARCH_SONGS)
    suspend fun searchSongs(
        @Field("ip") ip: String, @Field("q") id: String,
    ): TopArtistsResponseApi


    @FormUrlEncoded
    @POST(SONG_PLAY_DETAILS)
    suspend fun songPlayDetails(@Field("ip") ip: String, @Field("q") q: String): SongDetailsResponse

    @FormUrlEncoded
    @POST(VIDEO_PLAY_DETAILS)
    suspend fun videoPlayDetails(
        @Field("ip") ip: String, @Field("q") q: String
    ): SongDetailsResponse
}