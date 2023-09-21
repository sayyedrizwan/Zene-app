package com.rizwansayyed.zene.data.onlinesongs.radio.implementation

import com.rizwansayyed.zene.data.onlinesongs.cache.responseCache
import com.rizwansayyed.zene.data.onlinesongs.cache.returnFromCache
import com.rizwansayyed.zene.data.onlinesongs.cache.writeToCacheFile
import com.rizwansayyed.zene.data.onlinesongs.ip.IpJsonService
import com.rizwansayyed.zene.data.onlinesongs.radio.OnlineRadioService
import com.rizwansayyed.zene.data.utils.CacheFiles.radioList
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.domain.OnlineRadioCacheResponse
import com.rizwansayyed.zene.domain.toCache
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OnlineRadioImpl @Inject constructor(
    private val onlineRadio: OnlineRadioService,
    private val ipJson: IpJsonService
) : OnlineRadioImplInterface {

    override suspend fun onlineRadioSearch() = flow {
        val cache = responseCache(radioList, OnlineRadioCacheResponse::class.java)
        if (cache != null) {
            if (returnFromCache(cache.cacheTime) && cache.list.isNotEmpty()) {
                emit(cache.list)
                return@flow
            }
        }

        val ipDetails = ipJson.ip().countryCode ?: "us"
        val response = onlineRadio.radioSearch(ipDetails)
        val writeCache = writeToCacheFile(radioList, response.toCache())

        emit(response)
    }

}