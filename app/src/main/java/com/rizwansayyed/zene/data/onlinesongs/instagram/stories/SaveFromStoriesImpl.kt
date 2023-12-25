package com.rizwansayyed.zene.data.onlinesongs.instagram.stories

import android.util.Log
import com.rizwansayyed.zene.data.onlinesongs.instagram.SaveFromInstagramService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SaveFromStoriesImpl @Inject constructor(
    private val saveFromAPI: SaveFromInstagramService
) : SaveFromStoriesImplInterface {

    override suspend fun storiesList(username: String) = flow {
        val id = saveFromAPI.userInfoByUsername(username).result?.user?.pk
        val stories = saveFromAPI.userStories(id)
        emit(stories.result)
    }.flowOn(Dispatchers.IO)
}