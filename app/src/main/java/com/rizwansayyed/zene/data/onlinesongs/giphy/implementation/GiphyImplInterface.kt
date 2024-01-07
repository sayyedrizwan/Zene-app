package com.rizwansayyed.zene.data.onlinesongs.giphy.implementation

import com.rizwansayyed.zene.domain.instagram.SaveFromInstagramStoriesResponse
import kotlinx.coroutines.flow.Flow

interface GiphyImplInterface {

    suspend fun search(q: String): Flow<String?>
}