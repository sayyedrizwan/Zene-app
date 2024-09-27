package com.rizwansayyed.zene.data.api

import com.rizwansayyed.zene.data.api.ip.IpAPIService
import com.rizwansayyed.zene.data.api.zene.ZeneAPIService
import com.rizwansayyed.zene.data.api.zene.ZeneRadioAPIInterface
import kotlinx.coroutines.flow.flow
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
    }

    override suspend fun radiosYouMayLike() = flow {
        val ip = ipAPI.ip()

        val json = JSONObject().apply {
            put("countryID", ip.countryCode)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.radiosYouMayLike(body))
    }

    override suspend fun radioLanguages() = flow {
        emit(zeneAPI.radioLanguages())
    }

    override suspend fun radiosViaLanguages(language: String) = flow {
        val ip = ipAPI.ip()

        val json = JSONObject().apply {
            put("countryID", ip.countryCode)
            put("language", language)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.radioViaLanguages(body))
    }


    override suspend fun radioInfo(id: String) = flow {
        emit(zeneAPI.radioInfo(id))
    }

    override suspend fun radioViaCountries(country: String, page: Int) = flow {
        val ip = ipAPI.ip()

        val json = JSONObject().apply {
            put("countryID", ip.countryCode)
            put("page", page)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.radioViaCountries(body))
    }

    override suspend fun radioCountries() = flow {
        emit(zeneAPI.radioCountries())
    }

}