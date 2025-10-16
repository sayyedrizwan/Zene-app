package com.rizwansayyed.zene.data.implementation

import android.net.Uri
import com.google.firebase.messaging.FirebaseMessaging
import com.rizwansayyed.zene.data.IPAPIService
import com.rizwansayyed.zene.data.ZeneAPIService
import com.rizwansayyed.zene.data.model.ConnectFeedDataResponse
import com.rizwansayyed.zene.data.model.MusicDataTypes
import com.rizwansayyed.zene.data.model.PodcastPlaylistResponse
import com.rizwansayyed.zene.data.model.StatusTypeResponse
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.datastore.DataStorageManager.ipDB
import com.rizwansayyed.zene.datastore.DataStorageManager.lastNotificationGeneratedTSDB
import com.rizwansayyed.zene.datastore.DataStorageManager.lastNotificationSuggestedType
import com.rizwansayyed.zene.datastore.DataStorageManager.pauseHistorySettings
import com.rizwansayyed.zene.datastore.DataStorageManager.sortMyPlaylistTypeDB
import com.rizwansayyed.zene.datastore.DataStorageManager.userInfo
import com.rizwansayyed.zene.datastore.model.MusicPlayerData
import com.rizwansayyed.zene.service.FirebaseAppMessagingService.Companion.getDeviceFcmToken
import com.rizwansayyed.zene.service.location.BackgroundLocationTracking
import com.rizwansayyed.zene.ui.connect_status.view.saveFileToAppDirectory
import com.rizwansayyed.zene.ui.view.playlist.PlaylistsType
import com.rizwansayyed.zene.utils.ContactData
import com.rizwansayyed.zene.utils.MainUtils.getAddressFromLatLong
import com.rizwansayyed.zene.utils.MainUtils.getDeviceInfo
import com.rizwansayyed.zene.utils.MainUtils.moshi
import com.rizwansayyed.zene.utils.safeLaunch
import com.rizwansayyed.zene.utils.share.ShareContentUtils.getUniqueDeviceId
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

    override suspend fun loginUser(tokenID: String, type: String) = flow {
        val ip = ipAPI.get()
        CoroutineScope(Dispatchers.IO).safeLaunch {
            ipDB = flowOf(ip)
        }

        val json = JSONObject().apply {
            put("token", tokenID)
            put("device_id", getUniqueDeviceId())
            put("type", type)
            put("fcm_token", getDeviceFcmToken())
            put("ip", ip.query)
            put("device", "Android ${getDeviceInfo()}")
            put("country", "${ip.city}, ${ip.regionName}, ${ip.country}")
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.loginUser(body))
    }

    override suspend fun updateUser() = flow {
        val ip = ipAPI.get()
        CoroutineScope(Dispatchers.IO).safeLaunch {
            ipDB = flowOf(ip)
        }
        val info = userInfo.firstOrNull()
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val location = BackgroundLocationTracking.getLatestLocation()

        val json = JSONObject().apply {
            put("email", info?.email ?: "")
            put("name", info?.name ?: "")
            put("device_id", getUniqueDeviceId())
            put("fcm_token", getDeviceFcmToken())
            put("ip", ip.query)
            put("device", "Android ${getDeviceInfo()}")
            put("country", "${ip.city}, ${ip.regionName}, ${ip.country}")
            put("location", "${location?.latitude}, ${location?.longitude}")
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.updateUser(token, body))
    }

    override suspend fun updateSubscription(purchaseToken: String, subscriptionId: String?) = flow {
        val ip = ipAPI.get()
        CoroutineScope(Dispatchers.IO).safeLaunch {
            ipDB = flowOf(ip)
        }
        val info = userInfo.firstOrNull()
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val location = BackgroundLocationTracking.getLatestLocation()

        val json = JSONObject().apply {
            put("email", info?.email ?: "")
            put("name", info?.name ?: "")
            put("device_id", getUniqueDeviceId())
            put("fcm_token", getDeviceFcmToken())
            put("token", purchaseToken)
            put("sub_token", subscriptionId)
            put("ip", ip.query)
            put("device", "Android ${getDeviceInfo()}")
            put("country", "${ip.city}, ${ip.regionName}, ${ip.country}")
            put("location", "${location?.latitude}, ${location?.longitude}")
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.updateSubscription(token, body))
    }

    override suspend fun isUserPremium() = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.isUserPremium(token, body))
    }

    override suspend fun addHistory(data: ZeneMusicData) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val pauseHistory = pauseHistorySettings.firstOrNull() == true

        if (pauseHistory) {
            emit(StatusTypeResponse(false, ""))
            return@flow
        }

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

    override suspend fun checkUsername(username: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("username", username)
            put("email", email)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.checkUsername(token, body))
    }

    override suspend fun updateUsername(username: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("username", username)
            put("email", email)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.updateUsername(token, body))
    }

    override suspend fun updateName(name: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("name", name)
            put("email", email)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.updateName(token, body))
    }


    override suspend fun updatePhoto(file: Uri?) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val body = MultipartBody.Builder().setType(MultipartBody.FORM)

        if (file != null && !file.toString().contains("https://")) {
            val savedFile = saveFileToAppDirectory(file, true)
            if (savedFile.exists()) {
                val upload = savedFile.asRequestBody("application/octet-stream".toMediaTypeOrNull())
                body.addFormDataPart("file", savedFile.name, upload)
            }
        }
        body.addFormDataPart("email", email)

        emit(zeneAPI.updateProfilePhoto(token, body.build()))
    }


    override suspend fun getHistory(page: Int) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("page", page)
            put("email", email)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.getHistory(token, body))
    }

    override suspend fun getSavePlaylists(page: Int) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("page", page)
            put("email", email)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.getSavePlaylists(token, body))
    }

    override suspend fun myPlaylists(page: Int) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("page", page)
            put("email", email)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.myPlaylists(token, body))
    }

    override suspend fun myAllPlaylists() = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.myAllPlaylists(token, body))
    }

    override suspend fun deleteMyPlaylists(id: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("id", id)
            put("email", email)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.deleteMyPlaylists(token, body))
    }

    override suspend fun myPlaylistInfo(id: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("id", id)
            put("email", email)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.myPlaylistInfo(token, body))
    }

    override suspend fun nameUserPlaylist(id: String, name: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("id", id)
            put("email", email)
            put("title", name)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.nameUserPlaylist(token, body))
    }

    override suspend fun updateImageUserPlaylist(id: String?, file: Uri?) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val body = MultipartBody.Builder().setType(MultipartBody.FORM)

        if (file != null && !file.toString().contains("https://")) {
            val savedFile = saveFileToAppDirectory(file, true)
            if (savedFile.exists()) {
                val upload = savedFile.asRequestBody("application/octet-stream".toMediaTypeOrNull())
                body.addFormDataPart("file", savedFile.name, upload)
            }
        }

        body.addFormDataPart("image", file.toString())
        body.addFormDataPart("email", email)
        if (id != null) body.addFormDataPart("id", id)

        emit(zeneAPI.updateUserPlaylistImage(token, body.build()))
    }


    override suspend fun myPlaylistsSongs(playlistId: String, page: Int) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val country = ipDB.firstOrNull()?.countryCode

        val json = JSONObject().apply {
            put("page", page)
            put("playlist_id", playlistId)
            put("email", email)
            put("country", country)
            put("sort", sortMyPlaylistTypeDB.firstOrNull()?.name)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.myPlaylistsSongs(token, body))
    }

    override suspend fun myPlaylistsSongsReorder(v: ZeneMusicData, pId: String, position: Int) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("songId", v.id)
            put("playlist_id", pId)
            put("type", v.type)
            put("ts", v.extra)
            put("email", email)
            put("position", position)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.myPlaylistsSongsReorder(token, body))
    }

    override suspend fun removeMyPlaylistsSongs(
        playlistId: String, songID: String, type: String?
    ) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("song_id", songID)
            put("playlist_id", playlistId)
            put("type", type)
            put("email", email)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.removeMyPlaylistsSongs(token, body))
    }

    override suspend fun likeSongsCount() = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.likeSongsCount(token, body))
    }

    override suspend fun importSongsToLike(
        name: List<String>,
        isLike: Boolean,
        playlistId: String
    ) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val country = ipDB.firstOrNull()?.countryCode

        val list = moshi.adapter(Array<String>::class.java).toJson(name.toTypedArray())
        val json = JSONObject().apply {
            put("email", email)
            put("list", list)
            put("isLiked", isLike)
            put("playlisID", playlistId)
            put("country", country)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.importSongsToLike(token, body))
    }

    override suspend fun recentHome() = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val ip = ipDB.firstOrNull()

        val json = JSONObject().apply {
            put("email", email)
            put("country", ip?.countryCode)
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

    override suspend fun searchImages(q: ZeneMusicData) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val country = ipDB.firstOrNull()?.countryCode

        if (q.type() == MusicDataTypes.PLAYLISTS || q.type() == MusicDataTypes.PODCAST_AUDIO || q.type() == MusicDataTypes.AI_MUSIC) {
            q.thumbnail?.let { emit(listOf(q.thumbnail)) }
            return@flow
        }

        val json = JSONObject().apply {
            put("email", email)
            put("country", country)
            put("type", q.type)
            put("artists", q.artists)
            put("name", q.name)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.searchImages(token, body))
    }

    override suspend fun searchImages(q: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val country = ipDB.firstOrNull()?.countryCode

        val json = JSONObject().apply {
            put("email", email)
            put("country", country)
            put("name", q)
            put("artists", q)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.searchImages(token, body))
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
        val country = ipDB.firstOrNull()?.countryCode

        val json = JSONObject().apply {
            put("email", email)
            put("country", country)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.homeRadio(token, body))
    }

    override suspend fun homeVideos() = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val country = ipDB.firstOrNull()?.countryCode

        val json = JSONObject().apply {
            put("email", email)
            put("country", country)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.homeVideos(token, body))
    }

    override suspend fun entertainmentNews() = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val country = ipDB.firstOrNull()?.countryCode

        val json = JSONObject().apply {
            put("email", email)
            put("country", country)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.entertainmentNews(token, body))
    }

    override suspend fun search(q: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val country = ipDB.firstOrNull()?.countryCode

        val json = JSONObject().apply {
            put("q", q)
            put("email", email)
            put("country", country)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.search(token, body))
    }

    override suspend fun searchASong(q: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val country = ipDB.firstOrNull()?.countryCode

        val json = JSONObject().apply {
            put("q", q)
            put("email", email)
            put("country", country)
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
        val country = ipDB.firstOrNull()?.countryCode

        val json = JSONObject().apply {
            put("email", email)
            put("country", country)
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

    override suspend fun checkNumberVerified() = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.checkNumberVerified(token, body))
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

    override suspend fun connectCreatePlaylists(otherEmail: String, name: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("otherEmail", otherEmail)
            put("name", name)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.connectCreatePlaylists(token, body))
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
        silentNotification: Boolean,
        expire: Int?
    ) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("toEmail", toEmail)
            put("lastListenSongs", lastListenSongs)
            put("locationSharing", locationSharing)
            put("silentNotification", silentNotification)
            put("expire", expire)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.updateConnectSettings(token, body))
    }

    override suspend fun sendConnectMessage(toEmail: String, message: String, gif: String?) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("toEmail", toEmail)
            put("message", message)
            put("gif", gif)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.sendConnectMessage(token, body))
    }

    override suspend fun deleteConnectMessage(toEmail: String?, id: String?) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("toEmail", toEmail)
            put("id", id)
        }
        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.deleteConnectMessage(token, body))
    }

    override suspend fun sendConnectFileMessage(toEmail: String?, file: File?) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val body = MultipartBody.Builder().setType(MultipartBody.FORM)

        if (file?.exists() == true) {
            val uploader =
                file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
            body.addFormDataPart("file", file.name, uploader)
        }

        body.addFormDataPart("email", email)
        body.addFormDataPart("fileName", file?.name ?: "")
        body.addFormDataPart("fileSize", file?.length().toString())
        toEmail?.let { body.addFormDataPart("toEmail", it) }

        emit(zeneAPI.sendConnectFileMessage(token, body.build()))
    }

    override suspend fun sendConnectJamMessage(toEmail: String, data: ZeneMusicData?) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("toEmail", toEmail)
            put("name", data?.name)
            put("artists", data?.artists)
            put("type", data?.type)
            put("thumbnail", data?.thumbnail)
            put("id", data?.id)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.sendConnectJamMessage(token, body))
    }

    override suspend fun getChatConnectRecentMessage(toEmail: String, lastId: String?) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("toEmail", toEmail)
            put("lastId", lastId)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.getChatConnectRecentMessage(token, body))
    }


    override suspend fun sendConnectMediaMessage(
        userEmail: String?, file: String?, thumbnail: String?
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
        userEmail?.let { body.addFormDataPart("toEmail", it) }

        emit(zeneAPI.sendConnectMediaMessage(token, body.build()))
    }

    override suspend fun markConnectMessageToRead(toEmail: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("toEmail", toEmail)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.markMessageAsRead(token, body))
    }

    override suspend fun sendConnectLocation(toEmail: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val location = BackgroundLocationTracking.getLatestLocation()
        val address =
            if (location != null) getAddressFromLatLong(location.latitude, location.longitude)
                ?: "" else ""

        val json = JSONObject().apply {
            put("email", email)
            put("toEmail", toEmail)
            put("address", address)
            put("lat", location?.latitude)
            put("lon", location?.longitude)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.sendConnectLocation(token, body))
    }

    override suspend fun sendPartyCall(toEmail: String, code: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("toEmail", toEmail)
            put("code", code)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.sendPartyCall(token, body))
    }

    override suspend fun declinePartyCall(toEmail: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("toEmail", toEmail)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.declinePartyCall(token, body))
    }

    override suspend fun shareConnectVibe(
        d: ConnectFeedDataResponse, file: String?, thumbnail: String?
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
        d.jam_type?.let { body.addFormDataPart("jam_type", it) }
        d.jam_artists?.let { body.addFormDataPart("jam_artists", it) }
        d.jam_thumbnail?.let { body.addFormDataPart("jam_thumbnail", it) }
        d.jam_name?.let { body.addFormDataPart("jam_name", it) }
        d.jam_id?.let { body.addFormDataPart("jam_id", it) }
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

    override suspend fun getConnectPlaylists(otherEmail: String?, page: Int) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("otherEmail", otherEmail)
            put("page", page)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.getConnectPlaylists(token, body))
    }

    override suspend fun similarArtistsAlbumOfSong(
        id: String, name: String?, artists: String?
    ) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val country = ipDB.firstOrNull()?.countryCode

        val json = JSONObject().apply {
            put("email", email)
            put("id", id)
            put("name", id)
            put("artist", artists)
            put("country", country)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.similarArtistsAlbumOfSong(token, body))
    }

    override suspend fun songInfo(id: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val country = ipDB.firstOrNull()?.countryCode

        val json = JSONObject().apply {
            put("email", email)
            put("id", id)
            put("country", country)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.songInfo(token, body))
    }

    override suspend fun isPlaylistAdded(id: String, type: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("id", id)
            put("type", type)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.isPlaylistAdded(token, body))
    }

    override suspend fun similarVideos(id: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val country = ipDB.firstOrNull()?.countryCode

        val json = JSONObject().apply {
            put("email", email)
            put("country", country)
            put("id", id)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.similarVideos(token, body))
    }


    override suspend fun similarSongs(id: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val country = ipDB.firstOrNull()?.countryCode

        val json = JSONObject().apply {
            put("email", email)
            put("id", id)
            put("country", country)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.similarSongs(token, body))
    }

    override suspend fun playerPodcastInfo(id: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("id", id.replace("_", "/"))
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.playerPodcastInfo(token, body))
    }

    override suspend fun playerRadioInfo(id: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("id", id)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.playerRadioInfo(token, body))
    }

    override suspend fun radioByCountry(name: String) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("country", name)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.radioByCountry(token, body))
    }

    override suspend fun createNewPlaylists(name: String, info: ZeneMusicData?) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("name", name)

            if (info != null) {
                put("media_name", info.name ?: "")
                put("media_artists", info.artists ?: "")
                put("type", info.type ?: "")
                put("media_id", info.id ?: "")
                put("media_thumbnail", info.thumbnail ?: "")
            }
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.createNewPlaylists(token, body))
    }

    override suspend fun playlistSongCheck(songId: String, page: Int) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("page", page)
            put("songID", songId)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.playlistSongCheck(token, body))
    }

    override suspend fun addItemToPlaylists(
        info: ZeneMusicData?, playlistID: String, state: Boolean
    ) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("status", state)
            put("playlist_id", playlistID)
            put("media_name", info?.name ?: "")
            put("media_artists", info?.artists ?: "")
            put("type", info?.type ?: "")
            put("media_id", info?.id ?: "")
            put("media_thumbnail", info?.thumbnail ?: "")
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.addItemToPlaylists(token, body))
    }

    override suspend fun addRemoveLikeItem(info: ZeneMusicData?, state: Boolean) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("status", state)
            put("media_name", info?.name ?: "")
            put("media_artists", info?.artists ?: "")
            put("type", info?.type ?: "")
            put("media_id", info?.id ?: "")
            put("media_thumbnail", info?.thumbnail ?: "")
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.likeItems(token, body))
    }

    override suspend fun likedStatus(id: String?, type: MusicDataTypes) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("media_id", id)
            put("type", type.name)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.likedStatus(token, body))
    }

    override suspend fun similarPlaylistsSongs(id: String?) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val country = ipDB.firstOrNull()?.countryCode

        val json = JSONObject().apply {
            put("email", email)
            put("id", id)
            put("country", country)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.similarPlaylistsSongs(token, body))
    }

    override suspend fun playerLyrics(p: MusicPlayerData?) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val country = ipDB.firstOrNull()?.countryCode

        val json = JSONObject().apply {
            put("email", email)
            put("name", p?.data?.name)
            put("artists", p?.data?.artists)
            put("duration", p?.totalDuration)
            put("id", p?.data?.id)
            put("type", p?.data?.type)
            put("country", country)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.playerLyrics(token, body))
    }

    override suspend fun playerVideoForSongs(p: ZeneMusicData?) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val country = ipDB.firstOrNull()?.countryCode

        val json = JSONObject().apply {
            put("email", email)
            put("country", country)
            put("name", p?.name)
            put("artists", p?.artists)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.playerVideoForSongs(token, body))
    }

    override suspend fun playlistsInfo(id: String?) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val country = ipDB.firstOrNull()?.countryCode

        val json = JSONObject().apply {
            put("email", email)
            put("id", id)
            put("country", country)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.playlistsInfo(token, body))
    }

    override suspend fun savePlaylists(
        data: PodcastPlaylistResponse, status: Boolean, type: PlaylistsType
    ) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("doAdd", status)
            put("id", data.info?.id)
            put("img", data.info?.thumbnail)
            put("name", data.info?.name)
            put("artist", data.info?.artists)
            put("type", type.type)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.savePlaylists(token, body))
    }

    override suspend fun artistsInfo(artistsID: String?) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val country = ipDB.firstOrNull()?.countryCode

        val json = JSONObject().apply {
            put("email", email)
            put("id", artistsID)
            put("country", country)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.artistsInfo(token, body))
    }

    override suspend fun followArtists(name: String?, doAdd: Boolean?) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("name", name)
            put("add", doAdd)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.followArtists(token, body))
    }

    override suspend fun podcastInfo(id: String?) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("id", id)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.podcastInfo(token, body))
    }

    override suspend fun podcastMediaURL(id: String?) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("id", id)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.podcastMediaURL(token, body))
    }

    override suspend fun podcastCategories(name: String?) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("id", name)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.podcastCategories(token, body))
    }

    override suspend fun radioMediaURL(id: String?) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("id", id)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.radioMediaURL(token, body))
    }

    override suspend fun aiMusicMediaURL(id: String?) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("id", id)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.aiMusicMediaURL(token, body))
    }

    override suspend fun similarAISongs(tags: String?) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("tags", tags)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.similarAISongs(token, body))
    }

    override suspend fun lyricsAIMusic(id: String?) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("id", id)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.lyricsAIMusic(token, body))
    }

    override suspend fun aiMusicInfo(id: String?) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("id", id)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.aiMusicInfo(token, body))
    }

    override suspend fun similarPodcasts(id: String?) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("id", id)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.similarPodcasts(token, body))
    }


    override suspend fun similarRadio(tags: String?) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val country = ipDB.firstOrNull()?.countryCode

        val json = JSONObject().apply {
            put("email", email)
            put("tags", tags)
            put("country", country)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.similarRadio(token, body))
    }

    override suspend fun moviesTvShowsInfo(id: String?) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val country = ipDB.firstOrNull()?.countryCode

        val json = JSONObject().apply {
            put("email", email)
            put("id", id)
            put("country", country)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.moviesTvShowsInfo(token, body))
    }

    override suspend fun seasonMoviesTvShowsInfo(id: String?) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val country = ipDB.firstOrNull()?.countryCode

        val json = JSONObject().apply {
            put("email", email)
            put("id", id)
            put("country", country)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.seasonMoviesTvShowsInfo(token, body))
    }

    override suspend fun updateCoupon(code: String?) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("code", code)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.updateCoupon(token, body))
    }

    override suspend fun notificationRecommendation() = flow {
        val info = userInfo.firstOrNull()
        val lastTS = lastNotificationGeneratedTSDB.firstOrNull()
        val lastType = lastNotificationSuggestedType.firstOrNull()
        val ip = ipAPI.get()
        CoroutineScope(Dispatchers.IO).safeLaunch {
            ipDB = flowOf(ip)
        }

        val json = JSONObject().apply {
            put("email", info?.email ?: "")
            put("name", info?.name ?: "")
            put("last_ts", lastTS)
            put("type", lastType)
            put("country", ip.countryCode)
            put("timezone", ip.timezone)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.notificationRecommendation(body))
    }

    override suspend fun sponsorAds() = flow {
        emit(zeneAPI.sponsorAds())
    }

    override suspend fun feedFollowedArtists() = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""
        val country = ipDB.firstOrNull()?.countryCode

        val json = JSONObject().apply {
            put("email", email)
            put("country", country)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.feedFollowedArtists(token, body))
    }

    override suspend fun feedUpdatesArtists() = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.feedUpdatesArtists(token, body))
    }

    override suspend fun deleteAccount() = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.deleteAccount(token, body))
    }

    override suspend fun deleteAccountInfo() = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.deleteAccountInfo(token, body))
    }

    override suspend fun cancelDeleteAccount() = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.cancelDeleteAccount(token, body))
    }

    override suspend fun updateEmailSubscription(value: Boolean) = flow {
        val email = userInfo.firstOrNull()?.email ?: ""
        val token = userInfo.firstOrNull()?.authToken ?: ""

        val json = JSONObject().apply {
            put("email", email)
            put("value", value)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.updateEmailSubscription(token, body))
    }
}