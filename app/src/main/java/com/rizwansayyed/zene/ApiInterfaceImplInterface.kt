package com.rizwansayyed.zene

import com.rizwansayyed.zene.api.model.ablumsheader.AlbumsHeadersReponse
import kotlinx.coroutines.flow.Flow

interface ApiInterfaceImplInterface {

    suspend fun albumsWithHeaders(): Flow<AlbumsHeadersReponse>
}