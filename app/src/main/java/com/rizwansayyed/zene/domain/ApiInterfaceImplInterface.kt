package com.rizwansayyed.zene.domain

import com.rizwansayyed.zene.domain.model.SongDetailsResponse
import com.rizwansayyed.zene.domain.model.UrlResponse
import com.rizwansayyed.zene.presenter.model.AlbumsHeadersResponse
import com.rizwansayyed.zene.presenter.model.ArtistsInstagramPostResponse
import com.rizwansayyed.zene.presenter.model.ArtistsTwitterInfoResponse
import com.rizwansayyed.zene.presenter.model.IpJSONResponse
import com.rizwansayyed.zene.presenter.model.SocialMediaCombine
import com.rizwansayyed.zene.presenter.model.SongLyricsResponse
import com.rizwansayyed.zene.presenter.model.TopArtistsResponseApi
import com.rizwansayyed.zene.presenter.jsoup.model.NewsResponse
import com.rizwansayyed.zene.presenter.model.MusicsHeader
import com.rizwansayyed.zene.presenter.model.TopArtistsSongs
import kotlinx.coroutines.flow.Flow

interface ApiInterfaceImplInterface {

    suspend fun topArtistOfWeek(): Flow<TopArtistsResponseApi>

    suspend fun topGlobalSongsThisWeek(): Flow<TopArtistsResponseApi>

    suspend fun ipAddressDetails(): Flow<IpJSONResponse>


    suspend fun trendingSongsTop50KPop(): Flow<TopArtistsResponseApi>

    suspend fun songPlayDetails(name: String): Flow<SongDetailsResponse>
    suspend fun videoPlayDetails(name: String): Flow<SongDetailsResponse>
    suspend fun songLyrics(name: String): Flow<SongLyricsResponse>
    suspend fun artistsData(name: String): Flow<UrlResponse>
    suspend fun searchSongs(q: String): Flow<TopArtistsResponseApi>

    suspend fun artistsInstagramPosts(name: String): Flow<SocialMediaCombine?>
    suspend fun songPlayDetails(list: List<TopArtistsSongs>): Flow<ArrayList<MusicsHeader>>
}