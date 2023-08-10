package com.rizwansayyed.zene

import com.rizwansayyed.zene.domain.model.UrlResponse
import com.rizwansayyed.zene.presenter.model.AlbumsHeadersResponse
import kotlinx.coroutines.flow.Flow

interface ApiInterfaceImplInterface {

    suspend fun albumsWithHeaders(): Flow<UrlResponse>

    suspend fun albumsWithYTHeaders(url: String): Flow<AlbumsHeadersResponse>
}