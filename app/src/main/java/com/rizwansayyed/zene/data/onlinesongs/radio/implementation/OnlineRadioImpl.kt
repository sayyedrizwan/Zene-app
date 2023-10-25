package com.rizwansayyed.zene.data.onlinesongs.radio.implementation

import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.userIpDetails
import com.rizwansayyed.zene.data.onlinesongs.cache.responseCache
import com.rizwansayyed.zene.data.onlinesongs.cache.returnFromCache1Hour
import com.rizwansayyed.zene.data.onlinesongs.cache.writeToCacheFile
import com.rizwansayyed.zene.data.onlinesongs.ip.IpJsonService
import com.rizwansayyed.zene.data.onlinesongs.radio.OnlineRadioService
import com.rizwansayyed.zene.data.onlinesongs.radio.activeRadioBaseURL
import com.rizwansayyed.zene.data.utils.CacheFiles.favRadio
import com.rizwansayyed.zene.data.utils.CacheFiles.radioList
import com.rizwansayyed.zene.data.utils.RadioOnlineAPI.RADIO_BASE_URL
import com.rizwansayyed.zene.data.utils.RadioOnlineAPI.radioSearchAPI
import com.rizwansayyed.zene.data.utils.RadioOnlineAPI.radioUUIDSearchAPI
import com.rizwansayyed.zene.domain.OnlineRadioCacheResponse
import com.rizwansayyed.zene.domain.toTxtCache
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okhttp3.internal.filterList
import javax.inject.Inject

class OnlineRadioImpl @Inject constructor(
    private val onlineRadio: OnlineRadioService
) : OnlineRadioImplInterface {

    override suspend fun onlineRadioSearch(all: Boolean) = flow {
        val cache = responseCache(radioList, OnlineRadioCacheResponse::class.java)
        if (cache != null) {
            if (returnFromCache1Hour(cache.cacheTime) && cache.list.isNotEmpty()) {
                if (all)
                    emit(cache.list)
                else {
                    val stateRadio =
                        cache.list.filter { it.state == (userIpDetails.first()?.city ?: "") }
                    if (stateRadio.isEmpty())
                        if (cache.list.size > 9) {
                            emit(cache.list.subList(0, 9))
                        } else
                            emit(cache.list)
                    else
                        emit(stateRadio)
                }
                return@flow
            }
        }

        val ipDetails = userIpDetails.first()

        val baseURL = activeRadioBaseURL().ifEmpty { RADIO_BASE_URL }
        val response =
            onlineRadio.radioSearch(radioSearchAPI(baseURL), ipDetails?.countryCode ?: "us")
        response.toTxtCache()?.let { writeToCacheFile(radioList, it) }
        if (all)
            emit(response)
        else {
            val stateRadio =
                response.filter { it.state == (userIpDetails.first()?.city ?: "") }

            if (stateRadio.isEmpty())
                if (response.size > 9) {
                    emit(response.subList(0, 9))
                } else
                    emit(response)
            else
                emit(stateRadio)
        }
    }.flowOn(Dispatchers.IO)


    override suspend fun favouriteRadioLists(uuid: String) = flow {
        val cache = responseCache(favRadio, OnlineRadioCacheResponse::class.java)
        if (cache != null) {
            if (returnFromCache1Hour(cache.cacheTime) && cache.list.isNotEmpty()) {
                emit(cache.list)
                return@flow
            }
        }

        val baseURL = activeRadioBaseURL().ifEmpty { RADIO_BASE_URL }
        val response = onlineRadio.favouriteRadio(radioUUIDSearchAPI(baseURL), uuid)
        response.toTxtCache()?.let { writeToCacheFile(favRadio, it) }
        emit(response)
    }.flowOn(Dispatchers.IO)

}