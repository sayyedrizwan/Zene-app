package com.rizwansayyed.zene.data.onlinesongs.pinterest.implementation

import com.rizwansayyed.zene.domain.pinterest.PinterestSearchResponse
import kotlinx.coroutines.flow.Flow

interface PinterestAPIImplInterface {
    suspend fun search(name: String, artist: String): Flow<MutableList<String>>
}