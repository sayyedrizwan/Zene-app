package com.rizwansayyed.zene.data.implementation

import com.google.firebase.messaging.FirebaseMessaging
import com.rizwansayyed.zene.data.IPAPIService
import com.rizwansayyed.zene.data.ZeneAPIService
import com.rizwansayyed.zene.data.model.ConnectFeedDataResponse
import com.rizwansayyed.zene.data.model.ZeneMusicData
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

    override suspend fun addHistory(data: ZeneMusicData) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("id", data.id)
            put("name", data.name)
            put("artists", data.artists)
            put("deviceInfo", "Android ${getDeviceInfo()}")
            put("email", email)
            put("thumbnail", data.thumbnail)
            put("type", data.type)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.addHistory(token, body))
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

    override suspend fun trendingData() = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val country = ipDB.firstOrNull()?.countryCode

        val json = JSONObject().apply {
            put("email", email)
            put("country", country)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.trendingData(token, body))
    }

    override suspend fun searchKeywords(q: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val country = ipDB.firstOrNull()?.countryCode

        val json = JSONObject().apply {
            put("email", email)
            put("country", country)
            put("q", q)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.searchKeywords(token, body))
    }

    override suspend fun trendingAIMusic() = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.trendingAIMusic(token, body))
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

    override suspend fun searchASong(q: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val ip = ipAPI.get()

        val json = JSONObject().apply {
            put("q", q)
            put("email", email)
            put("country", ip.countryCode)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.searchASong(token, body))
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
        emit(zeneAPI.connectUsersSearchViaPhoneNumber(token, body))
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

    override suspend fun connectFriendsRequestList() = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.connectFriendsRequestList(token, body))
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

    override suspend fun shareConnectVibe(
        d: ConnectFeedDataResponse,
        file: String?,
        thumbnail: String?
    ) = flow {
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

        if (thumbnail != null) {
            val f = File(thumbnail)
            if (f.exists()) {
                val uploader =
                    File(thumbnail).asRequestBody("application/octet-stream".toMediaTypeOrNull())
                body.addFormDataPart("file_thumbnail", f.name, uploader)
            }
        }
        body.addFormDataPart("email", email)
        d.jazz_type?.let { body.addFormDataPart("jazz_type", it) }
        d.jazz_artists?.let { body.addFormDataPart("jazz_artists", it) }
        d.jazz_thumbnail?.let { body.addFormDataPart("jazz_thumbnail", it) }
        d.jazz_name?.let { body.addFormDataPart("jazz_name", it) }
        d.jazz_id?.let { body.addFormDataPart("jazz_id", it) }
        d.is_vibing?.let { body.addFormDataPart("is_vibing", it.toString()) }
        d.longitude?.let { body.addFormDataPart("longitude", it) }
        d.latitude?.let { body.addFormDataPart("latitude", it) }
        d.location_address?.let { body.addFormDataPart("location_address", it) }
        d.location_name?.let { body.addFormDataPart("location_name", it) }
        d.emoji?.let { body.addFormDataPart("emoji", it) }
        d.caption?.let { body.addFormDataPart("caption", it) }


        emit(zeneAPI.shareConnectVibe(token, body.build()))
    }

    override suspend fun connectFriendsVibesList(page: Int) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("page", page)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.connectFriendsVibes(token, body))
    }

    override suspend fun trendingGIF() = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.trendingGif(token, body))
    }

    override suspend fun searchGif(q: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("q", q)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.searchGif(token, body))
    }

    override suspend fun updateConnectStatus(status: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("status", status)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.updateConnectStatus(token, body))
    }

    override suspend fun postCommentOnVibes(gif: String, id: Int?) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("gif", gif)
            put("vibes_id", id)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.postCommentOnVibes(token, body))
    }

    override suspend fun getCommentOfVibes(id: Int?, page: Int) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("vibes_id", id)
            put("offset", page)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.getCommentOfVibes(token, body))
    }

    override suspend fun similarVideos(id: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("id", id)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.similarVideos(token, body))
    }

    override suspend fun createNewPlaylists(name: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("name", name)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.createNewPlaylists(token, body))
    }
}