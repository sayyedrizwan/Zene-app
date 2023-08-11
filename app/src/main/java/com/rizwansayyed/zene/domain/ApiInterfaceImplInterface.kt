package com.rizwansayyed.zene.domain

import com.rizwansayyed.zene.domain.model.UrlResponse
import com.rizwansayyed.zene.presenter.model.AlbumsHeadersResponse
import com.rizwansayyed.zene.presenter.model.IpJSONResponse
import com.rizwansayyed.zene.presenter.model.TopArtistsResponseApi
import kotlinx.coroutines.flow.Flow

interface ApiInterfaceImplInterface {

    suspend fun albumsWithHeaders(): Flow<UrlResponse>

    suspend fun albumsWithYTHeaders(url: String): Flow<AlbumsHeadersResponse>

    suspend fun topArtistOfWeek(): Flow<TopArtistsResponseApi>

    suspend fun topGlobalSongsThisWeek(): Flow<TopArtistsResponseApi>

    suspend fun topCountrySongs(country: String): Flow<TopArtistsResponseApi>


    suspend fun ipAddressDetails(): Flow<IpJSONResponse>
}