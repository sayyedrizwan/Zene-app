package com.rizwansayyed.zene.data.implementation

import com.google.firebase.messaging.FirebaseMessaging
import com.rizwansayyed.zene.data.IPAPIService
import com.rizwansayyed.zene.data.ZeneAPIService
import com.rizwansayyed.zene.data.model.ConnectFeedDataResponse
import com.rizwansayyed.zene.datastore.DataStorageManager.ipDB
import com.rizwansayyed.zene.datastore.DataStorageManager.userInfo
import com.rizwansayyed.zene.service.location.BackgroundLocationTracking
import com.rizwansayyed.zene.utils.ContactData
import com.rizwansayyed.zene.utils.MainUtils.getAddressFromLatLong
import com.rizwansayyed.zene.utils.MainUtils.getDeviceInfo
import com.rizwansayyed.zene.utils.MainUtils.moshi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
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

    override suspend fun search(q: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val ip = ipAPI.get()

        val json = JSONObject().apply {
            put("q", q)
            put("email", email)
            put("country", ip.countryCode)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.search(token, body))
    }

    override suspend fun searchPlaces(q: String?, lon: Double?, lat: Double?) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("q", q)
            put("lon", lon)
            put("lat", lat)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.searchPlaces(token, body))
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

    override suspend fun connectUsersSearch(contacts: List<ContactData>) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val numbers = contacts.map { n -> n.number }
        val lists = moshi.adapter(Array<String?>::class.java).toJson(numbers.toTypedArray())

        val json = JSONObject().apply {
            put("email", email)
            put("contacts", lists)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.connectUsersSearch(token, body))
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

    override suspend fun searchConnect(query: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("q", query)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.connectSearch(token, body))
    }

    override suspend fun connectUserInfo(toEmail: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("toEmail", toEmail)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.connectUserInfo(token, body))
    }

    override suspend fun connectFriendsList() = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.connectFriendsList(token, body))
    }

    override suspend fun connectSendRequest(toEmail: String, remove: Boolean) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("toEmail", toEmail)
            put("remove", remove)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.connectSendRequest(token, body))
    }

    override suspend fun connectAcceptRequest(toEmail: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("toEmail", toEmail)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.connectAcceptRequest(token, body))
    }

    override suspend fun updateConnectSettings(
        toEmail: String,
        lastListenSongs: Boolean,
        locationSharing: Boolean,
        silentNotification: Boolean
    ) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("toEmail", toEmail)
            put("lastListenSongs", lastListenSongs)
            put("locationSharing", locationSharing)
            put("silentNotification", silentNotification)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.updateConnectSettings(token, body))
    }

    override suspend fun sendConnectMessage(toEmail: String, message: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("toEmail", toEmail)
            put("message", message)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.sendConnectMessage(token, body))
    }

    override suspend fun sendConnectLocation(toEmail: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val lat = BackgroundLocationTracking.updateLocationLat
        val lon = BackgroundLocationTracking.updateLocationLon
        val address = getAddressFromLatLong(lat, lon) ?: ""
        val json = JSONObject().apply {
            put("email", email)
            put("toEmail", toEmail)
            put("address", address)
            put("lat", lat)
            put("lon", lon)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.sendConnectLocation(token, body))
    }

    override suspend fun shareConnectVibe(d: ConnectFeedDataResponse, file: String?) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val body = MultipartBody.Builder().setType(MultipartBody.FORM)

        if (file != null) {
            val f = File(file)
            if (f.exists()) {
                val uploader =
                    File(file).asRequestBody("application/octet-stream".toMediaTypeOrNull())
                body.addFormDataPart("file", f.name, uploader)
            }
        }
        body.addFormDataPart("email", email)
        d.jazzType?.let { body.addFormDataPart("jazz_type", it) }
        d.jazzArtists?.let { body.addFormDataPart("jazz_artists", it) }
        d.jazzName?.let { body.addFormDataPart("jazz_name", it) }
        d.jazzId?.let { body.addFormDataPart("jazz_id", it) }
        d.isVibing?.let { body.addFormDataPart("is_vibing", it.toString()) }
        d.locationLongitude?.let { body.addFormDataPart("longitude", it) }
        d.locationLatitude?.let { body.addFormDataPart("latitude", it) }
        d.locationAddress?.let { body.addFormDataPart("location_address", it) }
        d.locationName?.let { body.addFormDataPart("location_name", it) }
        d.emoji?.let { body.addFormDataPart("emoji", it) }


        emit(zeneAPI.shareConnectVibe(token, body.build()))
    }
}