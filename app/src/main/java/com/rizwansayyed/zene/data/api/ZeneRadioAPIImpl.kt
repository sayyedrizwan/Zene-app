package com.rizwansayyed.zene.data.api

import com.rizwansayyed.zene.data.api.ApiCache.ApiCacheKeys.MOOD_LIST_CACHE
import com.rizwansayyed.zene.data.api.ApiCache.ApiCacheKeys.RADIO_COUNTRIES_CACHE
import com.rizwansayyed.zene.data.api.ApiCache.ApiCacheKeys.RADIO_LANGUAGES_CACHE
import com.rizwansayyed.zene.data.api.ApiCache.ApiCacheKeys.RADIO_MOOD_LIST_CACHE
import com.rizwansayyed.zene.data.api.ApiCache.ApiCacheKeys.RADIO_YOU_MAY_LIKE_CACHE
import com.rizwansayyed.zene.data.api.ApiCache.getAPICache
import com.rizwansayyed.zene.data.api.ApiCache.getAPICacheMood
import com.rizwansayyed.zene.data.api.ApiCache.setAPICache
import com.rizwansayyed.zene.data.api.ApiCache.setAPICacheMood
import com.rizwansayyed.zene.data.api.ip.IpAPIService
import com.rizwansayyed.zene.data.api.zene.ZeneAPIService
import com.rizwansayyed.zene.data.api.zene.ZeneRadioAPIInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject


class ZeneRadioAPIImpl @Inject constructor(
    private val zeneAPI: ZeneAPIService, private val ipAPI: IpAPIService
) : ZeneRadioAPIInterface {

    override suspend fun topRadio() = flow {
        val cache = getAPICacheMood(RADIO_MOOD_LIST_CACHE)
        if (cache != null) {
            emit(cache)
            return@flow
        }

        val ip = ipAPI.ip()

        val json = JSONObject().apply {
            put("countryID", ip.countryCode)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val response = zeneAPI.topRadio(body)
        setAPICacheMood(RADIO_MOOD_LIST_CACHE, response)
        emit(response)
    }.flowOn(Dispatchers.IO)

    override suspend fun radiosYouMayLike() = flow {
        val cache = getAPICache(RADIO_YOU_MAY_LIKE_CACHE)
        if (cache != null) {
            emit(cache)
            return@flow
        }

        val ip = ipAPI.ip()

        val json = JSONObject().apply {
            put("countryID", ip.countryCode)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val response = zeneAPI.radiosYouMayLike(body)
        setAPICache(RADIO_YOU_MAY_LIKE_CACHE, response)
        emit(response)
    }.flowOn(Dispatchers.IO)

    override suspend fun radioLanguages() = flow {
        val cache = getAPICache(RADIO_LANGUAGES_CACHE)
        if (cache != null) {
            emit(cache)
            return@flow
        }

        val response = zeneAPI.radioLanguages()
        setAPICache(RADIO_LANGUAGES_CACHE, response)
        emit(response)
    }.flowOn(Dispatchers.IO)

    override suspend fun radiosViaLanguages(language: String) = flow {
        val ip = ipAPI.ip()

        val json = JSONObject().apply {
            put("countryID", ip.countryCode)
            put("language", language)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.radioViaLanguages(body))
    }.flowOn(Dispatchers.IO)


    override suspend fun radioInfo(id: String) = flow {
        emit(zeneAPI.radioInfo(id))
    }.flowOn(Dispatchers.IO)

    override suspend fun radioViaCountries(country: String, page: Int) = flow {
        val ip = ipAPI.ip()

        val json = JSONObject().apply {
            put("countryID", ip.countryCode)
            put("page", page)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.radioViaCountries(body))
    }.flowOn(Dispatchers.IO)

    override suspend fun radioCountries() = flow {
        val cache = getAPICache(RADIO_COUNTRIES_CACHE)
        if (cache != null) {
            emit(cache)
            return@flow
        }

        val response = zeneAPI.radioCountries()
        setAPICache(RADIO_COUNTRIES_CACHE, response)
        emit(response)
    }.flowOn(Dispatchers.IO)

}