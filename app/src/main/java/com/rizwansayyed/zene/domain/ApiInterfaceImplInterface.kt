package com.rizwansayyed.zene.domain

import com.rizwansayyed.zene.domain.model.SongDetailsResponse
import com.rizwansayyed.zene.domain.model.UrlResponse
import com.rizwansayyed.zene.presenter.model.AlbumsHeadersResponse
import com.rizwansayyed.zene.presenter.model.ArtistsInstagramPostResponse
import com.rizwansayyed.zene.presenter.model.ArtistsTwitterInfoResponse
import com.rizwansayyed.zene.presenter.model.IpJSONResponse
import com.rizwansayyed.zene.presenter.model.SongLyricsResponse
import com.rizwansayyed.zene.presenter.model.TopArtistsResponseApi
import kotlinx.coroutines.flow.Flow

interface ApiInterfaceImplInterface {

    suspend fun albumsWithHeaders(): Flow<UrlResponse>

    suspend fun albumsWithYTHeaders(url: String): Flow<AlbumsHeadersResponse>

    suspend fun topArtistOfWeek(): Flow<TopArtistsResponseApi>

    suspend fun topGlobalSongsThisWeek(): Flow<TopArtistsResponseApi>

    suspend fun topCountrySongs(): Flow<TopArtistsResponseApi>

    suspend fun ipAddressDetails(): Flow<IpJSONResponse>

    suspend fun trendingSongsTop50(): Flow<TopArtistsResponseApi>
    suspend fun trendingSongsTopKPop(): Flow<TopArtistsResponseApi>
    suspend fun trendingSongsTop50KPop(): Flow<TopArtistsResponseApi>

    suspend fun songPlayDetails(name: String): Flow<SongDetailsResponse>
    suspend fun videoPlayDetails(name: String): Flow<SongDetailsResponse>
    suspend fun songLyrics(name: String): Flow<SongLyricsResponse>
    suspend fun artistsData(name: String): Flow<UrlResponse>
    suspend fun searchSongs(q: String): Flow<TopArtistsResponseApi>
    suspend fun similarArtists(name: String): Flow<TopArtistsResponseApi>
    suspend fun artistsInstagramPosts(name: String): Flow<ArtistsInstagramPostResponse>
    suspend fun artistsTwitterTweets(name: String): Flow<ArtistsTwitterInfoResponse>
}