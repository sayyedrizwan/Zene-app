package com.rizwansayyed.zene.data.onlinesongs.instagram.implementation

import com.rizwansayyed.zene.domain.instagram.SaveFromInstagramStoriesResponse
import kotlinx.coroutines.flow.Flow

interface SaveFromStoriesImplInterface {

    suspend fun storiesList(username: String): Flow<List<SaveFromInstagramStoriesResponse.Result?>?>
}