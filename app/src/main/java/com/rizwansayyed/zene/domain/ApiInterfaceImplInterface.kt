package com.rizwansayyed.zene.domain


import com.rizwansayyed.zene.domain.model.UrlResponse
import com.rizwansayyed.zene.domain.model.VideoDetailsResponse
import com.rizwansayyed.zene.presenter.jsoup.model.YTSearchData
import com.rizwansayyed.zene.presenter.model.AlbumsHeadersResponse
import com.rizwansayyed.zene.presenter.model.ArtistsInstagramPostResponse
import com.rizwansayyed.zene.presenter.model.ArtistsTwitterInfoResponse
import com.rizwansayyed.zene.presenter.model.IpJSONResponse
import com.rizwansayyed.zene.presenter.model.TopArtistsResponseApi
import com.rizwansayyed.zene.presenter.model.MusicsHeader
import com.rizwansayyed.zene.presenter.model.TopArtistsSongs
import kotlinx.coroutines.flow.Flow

interface ApiInterfaceImplInterface {

    suspend fun topArtistOfWeek(): Flow<TopArtistsResponseApi>

    suspend fun topGlobalSongsThisWeek(): Flow<TopArtistsResponseApi>

    suspend fun ipAddressDetails(): Flow<IpJSONResponse>


    suspend fun trendingSongsTop50KPop(): Flow<TopArtistsResponseApi>

    suspend fun artistsData(name: String): Flow<UrlResponse>

    suspend fun searchSongs(q: String): Flow<TopArtistsResponseApi>

    suspend fun songPlayDetails(list: List<TopArtistsSongs>): Flow<ArrayList<MusicsHeader>>
    suspend fun searchArtists(q: String): Flow<TopArtistsResponseApi>

    suspend fun songPlayDetails(name: String): Flow<YTSearchData?>
    suspend fun videoPlayDetails(name: String): Flow<VideoDetailsResponse>
}