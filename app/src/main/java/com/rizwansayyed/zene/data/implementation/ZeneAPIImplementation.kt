package com.rizwansayyed.zene.data.implementation

import com.google.firebase.messaging.FirebaseMessaging
import com.rizwansayyed.zene.data.IPAPIService
import com.rizwansayyed.zene.data.ZeneAPIService
import com.rizwansayyed.zene.datastore.DataStorageManager.ipDB
import com.rizwansayyed.zene.datastore.DataStorageManager.userInfo
import com.rizwansayyed.zene.utils.MainUtils.getDeviceInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject

class ZeneAPIImplementation @Inject constructor(
    private val zeneAPI: ZeneAPIService, private val ipAPI: IPAPIService
) : ZeneAPIInterface {

    override suspend fun updateUser(email: String, name: String, photo: String) = flow {
        val ip = ipAPI.get()
        CoroutineScope(Dispatchers.IO).launch {
            ipDB = flowOf(ip)
        }

        val fcm = FirebaseMessaging.getInstance().token.await() ?: ""
        val json = JSONObject().apply {
            put("email", email)
            put("name", name)
            put("photo", photo)
            put("fcm_token", fcm)
            put("ip", ip.query)
            put("device", "Android ${getDeviceInfo()}")
            put("country", "${ip.city}, ${ip.regionName}, ${ip.country}")
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.updateUser(body))
    }

    override suspend fun recentHome() = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val ip = ipAPI.get()

        val json = JSONObject().apply {
            put("email", email)
            put("country", ip.countryCode)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.recentHome(token, body))
    }

    override suspend fun recentPodcast() = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val json = JSONObject().apply {
            put("email", email)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.homePodcast(token, body))
    }

    override suspend fun recentRadio() = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val ip = ipAPI.get()

        val json = JSONObject().apply {
            put("email", email)
            put("country", ip.countryCode)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.homeRadio(token, body))
    }

    override suspend fun homeVideos() = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val ip = ipAPI.get()

        val json = JSONObject().apply {
            put("email", email)
            put("country", ip.countryCode)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.homeVideos(token, body))
    }

    override suspend fun entertainmentNews() = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val ip = ipAPI.get()

        val json = JSONObject().apply {
            put("email", email)
            put("country", ip.countryCode)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.entertainmentNews(token, body))
    }

    override suspend fun entertainmentMovies() = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val ip = ipAPI.get()

        val json = JSONObject().apply {
            put("email", email)
            put("country", ip.countryCode)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.entertainmentMovies(token, body))
    }

    override suspend fun updateTrueCallerNumber(codeVerifier: String, code: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("codeVerifier", codeVerifier)
            put("code", code)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.updateTrueCallerPhone(token, body))
    }

    override suspend fun connectNearMusic() = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val ip = ipAPI.get()

        val json = JSONObject().apply {
            put("email", email)
            put("country", ip.countryCode)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.connectNearMusic(token, body))
    }

    override suspend fun sendVerifyPhoneNumber(number: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("number", number)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.sendVerifyPhoneNumber(token, body))
    }

    override suspend fun verifyPhoneNumber(code: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("code", code)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.verifyPhoneNumber(token, body))
    }

}