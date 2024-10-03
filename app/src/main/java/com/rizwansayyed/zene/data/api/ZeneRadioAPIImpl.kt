package com.rizwansayyed.zene.data.api

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
        val ip = ipAPI.ip()

        val json = JSONObject().apply {
            put("countryID", ip.countryCode)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.topRadio(body))
    }.flowOn(Dispatchers.IO)

    override suspend fun radiosYouMayLike() = flow {
        val ip = ipAPI.ip()

        val json = JSONObject().apply {
            put("countryID", ip.countryCode)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.radiosYouMayLike(body))
    }.flowOn(Dispatchers.IO)

    override suspend fun radioLanguages() = flow {
        emit(zeneAPI.radioLanguages())
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
        emit(zeneAPI.radioCountries())
    }.flowOn(Dispatchers.IO)

}