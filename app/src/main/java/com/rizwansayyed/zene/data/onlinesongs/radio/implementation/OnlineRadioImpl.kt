package com.rizwansayyed.zene.data.onlinesongs.radio.implementation

import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.userIpDetails
import com.rizwansayyed.zene.data.onlinesongs.cache.responseCache
import com.rizwansayyed.zene.data.onlinesongs.cache.returnFromCache1Hour
import com.rizwansayyed.zene.data.onlinesongs.cache.writeToCacheFile
import com.rizwansayyed.zene.data.onlinesongs.ip.IpJsonService
import com.rizwansayyed.zene.data.onlinesongs.radio.OnlineRadioService
import com.rizwansayyed.zene.data.utils.CacheFiles.radioList
import com.rizwansayyed.zene.domain.OnlineRadioCacheResponse
import com.rizwansayyed.zene.domain.toTxtCache
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import kotlinx.coroutines.flow.flow
import okhttp3.internal.filterList
import javax.inject.Inject

class OnlineRadioImpl @Inject constructor(
    private val onlineRadio: OnlineRadioService,
    private val ipJson: IpJsonService
) : OnlineRadioImplInterface {

    override suspend fun onlineRadioSearch(all: Boolean) = flow {
        val cache = responseCache(radioList, OnlineRadioCacheResponse::class.java)
        if (cache != null) {
            if (returnFromCache1Hour(cache.cacheTime) && cache.list.isNotEmpty()) {
                if (all)
                    emit(cache.list)
                else
                    emit(cache.list.filter { it.state == (userIpDetails.city ?: "") })
                return@flow
            }
        }

        val ipDetails = ipJson.ip()
        userIpDetails = ipDetails
        val response = onlineRadio.radioSearch(ipDetails.countryCode ?: "us")
        response.toTxtCache()?.let { writeToCacheFile(radioList, it) }
        if (all)
            emit(response)
        else
            emit(response.filter { it.state == (userIpDetails.city ?: "") })
    }

}