package com.rizwansayyed.zene.data.onlinesongs.giphy.implementation

import com.rizwansayyed.zene.data.onlinesongs.giphy.GiphyService
import com.rizwansayyed.zene.data.onlinesongs.instagram.SaveFromInstagramService
import com.rizwansayyed.zene.data.utils.config.RemoteConfigInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GiphyImpl @Inject constructor(
    private val giphyService: GiphyService,
    private val remoteConfig: RemoteConfigInterface
) : GiphyImplInterface {

    override suspend fun search(q: String) = flow {
        val apiKey = remoteConfig.allApiKeys()?.giphy
        val response = giphyService.search(apiKey, "cute $q")
        val randomGifId = response.data?.random()?.id
        emit(randomGifId)
    }.flowOn(Dispatchers.IO)
}