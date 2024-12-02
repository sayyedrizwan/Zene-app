package com.rizwansayyed.zene.data.api

import android.os.Build
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.rizwansayyed.zene.data.api.ApiCache.ApiCacheKeys.LATEST_RELEASES_CACHE
import com.rizwansayyed.zene.data.api.ApiCache.ApiCacheKeys.MOOD_LIST_CACHE
import com.rizwansayyed.zene.data.api.ApiCache.ApiCacheKeys.RECOMMENDED_ALBUMS_CACHE
import com.rizwansayyed.zene.data.api.ApiCache.ApiCacheKeys.RECOMMENDED_PLAYLISTS_CACHE
import com.rizwansayyed.zene.data.api.ApiCache.ApiCacheKeys.RECOMMENDED_VIDEOS_CACHE
import com.rizwansayyed.zene.data.api.ApiCache.ApiCacheKeys.SPONSORS_ADS_CACHE
import com.rizwansayyed.zene.data.api.ApiCache.ApiCacheKeys.SUGGESTED_SONGS_CACHE
import com.rizwansayyed.zene.data.api.ApiCache.ApiCacheKeys.SUGGESTED_TOP_SONGS_CACHE
import com.rizwansayyed.zene.data.api.ApiCache.ApiCacheKeys.TOP_MOST_LISTENING_ARTISTS_CACHE
import com.rizwansayyed.zene.data.api.ApiCache.ApiCacheKeys.TOP_MOST_LISTENING_SONG_CACHE
import com.rizwansayyed.zene.data.api.ApiCache.ApiCacheKeys.UPDATE_AVAILABILITY_CACHE
import com.rizwansayyed.zene.data.api.ApiCache.getAPICache
import com.rizwansayyed.zene.data.api.ApiCache.setAPICache
import com.rizwansayyed.zene.data.api.ip.IpAPIService
import com.rizwansayyed.zene.data.api.zene.ZeneAPIInterface
import com.rizwansayyed.zene.data.api.zene.ZeneAPIService
import com.rizwansayyed.zene.data.db.DataStoreManager.artistsHistoryListDB
import com.rizwansayyed.zene.data.db.DataStoreManager.getCustomTimestamp
import com.rizwansayyed.zene.data.db.DataStoreManager.ipDB
import com.rizwansayyed.zene.data.db.DataStoreManager.setCustomTimestamp
import com.rizwansayyed.zene.data.db.DataStoreManager.songHistoryListDB
import com.rizwansayyed.zene.data.db.DataStoreManager.sponsorsAdsDB
import com.rizwansayyed.zene.data.db.DataStoreManager.updateAvailabilityDB
import com.rizwansayyed.zene.data.db.DataStoreManager.userInfoDB
import com.rizwansayyed.zene.utils.Utils.addArtistsHistoryLists
import com.rizwansayyed.zene.utils.Utils.addSongHistoryLists
import com.rizwansayyed.zene.utils.Utils.getDeviceName
import com.rizwansayyed.zene.utils.Utils.moshi
import com.rizwansayyed.zene.utils.Utils.timeDifferenceInMinutes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours


class ZeneAPIImpl @Inject constructor(
    private val zeneAPI: ZeneAPIService, private val ipAPI: IpAPIService
) : ZeneAPIInterface {

    override suspend fun ip() = flow {
        val ip = ipAPI.ip()
        ipDB = flowOf(ip)
        emit(ip)
    }.flowOn(Dispatchers.IO)

    override suspend fun updateUser() = flow {
        val ip = ip().firstOrNull()

        Firebase.messaging.subscribeToTopic("country_${ip?.country?.lowercase()}")
            .addOnCompleteListener { }

        val users = userInfoDB.firstOrNull()
        val fcm = FirebaseMessaging.getInstance().token.await()

        val json = JSONObject().apply {
            put("name", users?.name)
            put("email", users?.email)
            put("photo", users?.getProfilePicture())
            put("playtime", users?.totalPlayTime ?: 0)
            put("ip", ip?.query)
            put("fcm", fcm)
            put("device", "Android ${Build.MANUFACTURER} ${Build.MODEL} ${Build.VERSION.RELEASE}")
            put("country", "${ip?.city}, ${ip?.regionName}, ${ip?.country}")
            put("review", users?.isReviewDone ?: false)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.updateUser(body))
    }.flowOn(Dispatchers.IO)

    override suspend fun updateUserSubscription(orderId: String, purchaseToken: String) = flow {
        val email = userInfoDB.firstOrNull()?.email ?: ""
        val json = JSONObject().apply {
            put("email", email)
            put("orderID", orderId)
            put("purchaseToken", purchaseToken)
        }
        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.updateUserSubscription(body))
    }.flowOn(Dispatchers.IO)

    override suspend fun isUserPremium() = flow {
        val email = userInfoDB.firstOrNull()?.email ?: ""
        emit(zeneAPI.isUserSubscribe(email))
    }.flowOn(Dispatchers.IO)

    override suspend fun getUser(email: String) = flow {
        emit(zeneAPI.getUser(email))
    }.flowOn(Dispatchers.IO)

    override suspend fun updateAvailability() = flow {
        val ts = getCustomTimestamp(UPDATE_AVAILABILITY_CACHE)
        if (timeDifferenceInMinutes(ts) > 10.hours.inWholeMinutes) {
            val response = zeneAPI.updateAvailability()
            updateAvailabilityDB = flowOf(response)
            setCustomTimestamp(UPDATE_AVAILABILITY_CACHE)
            emit(response)
        } else updateAvailabilityDB.first()?.let { emit(it) }
    }.flowOn(Dispatchers.IO)

    override suspend fun sponsorsAds() {
        val ts = getCustomTimestamp(SPONSORS_ADS_CACHE)
        if (timeDifferenceInMinutes(ts) > 720) {
            try {
                val ads = zeneAPI.sponsors().android
                setCustomTimestamp(SPONSORS_ADS_CACHE)
                sponsorsAdsDB = flowOf(ads)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun moodLists() = flow {
        val cache = getAPICache(MOOD_LIST_CACHE)
        if (cache != null) {
            emit(cache)
            return@flow
        }

        val response = zeneAPI.moodLists()
        setAPICache(MOOD_LIST_CACHE, response)
        emit(response)
    }.flowOn(Dispatchers.IO)

    override suspend fun moodLists(id: String) = flow {
        val json = JSONObject().apply {
            put("id", id)
        }
        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.moodLists(body))
    }.flowOn(Dispatchers.IO)

    override suspend fun latestReleases(id: String) = flow {
        val cache = getAPICache(LATEST_RELEASES_CACHE)
        if (cache != null) {
            emit(cache)
            return@flow
        }

        val response = zeneAPI.latestReleases(id)
        setAPICache(LATEST_RELEASES_CACHE, response)
        emit(response)
    }

    override suspend fun lyrics(id: String, name: String, artists: String) = flow {
        emit(zeneAPI.lyrics(id, name, artists))
    }.flowOn(Dispatchers.IO)

    override suspend fun playerVideoData(name: String, artists: String) = flow {
        emit(zeneAPI.playerVideoData(name, artists))
    }.flowOn(Dispatchers.IO)

    override suspend fun merchandise(name: String, artists: String) = flow {
        emit(zeneAPI.merchandise("${artists.substringBefore("-").substringBefore("&")} - $name"))
    }.flowOn(Dispatchers.IO)

    override suspend fun topMostListeningSong() = flow {
        val cache = getAPICache(TOP_MOST_LISTENING_SONG_CACHE)
        if (cache != null) {
            emit(cache)
            return@flow
        }

        val response = zeneAPI.topMostListeningSong()
        setAPICache(TOP_MOST_LISTENING_SONG_CACHE, response)
        emit(response)
    }.flowOn(Dispatchers.IO)

    override suspend fun topMostListeningArtists() = flow {
        val cache = getAPICache(TOP_MOST_LISTENING_ARTISTS_CACHE)
        if (cache != null) {
            emit(cache)
            return@flow
        }
        val response = zeneAPI.topMostListeningArtists()
        setAPICache(TOP_MOST_LISTENING_ARTISTS_CACHE, response)
        emit(response)
    }.flowOn(Dispatchers.IO)

    override suspend fun searchData(s: String) = flow {
        emit(zeneAPI.searchData(s))
    }.flowOn(Dispatchers.IO)

    override suspend fun searchSuggestions(s: String) = flow {
        emit(zeneAPI.searchSuggestions(s))
    }.flowOn(Dispatchers.IO)

    override suspend fun favArtistsData() = flow {
        val list = artistsHistoryListDB.firstOrNull() ?: emptyArray()
        val email = userInfoDB.firstOrNull()?.email ?: ""
        val json = JSONObject().apply {
            put("email", email)
            put("list", moshi.adapter(Array<String>::class.java).toJson(list))
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.favArtistsData(body))
    }.flowOn(Dispatchers.IO)

    override suspend fun suggestedSongs() = flow {
        val cache = getAPICache(SUGGESTED_SONGS_CACHE)
        if (cache != null) {
            emit(cache)
            return@flow
        }

        val list = songHistoryListDB.firstOrNull() ?: emptyArray()
        val email = userInfoDB.firstOrNull()?.email ?: ""
        val json = JSONObject().apply {
            put("email", email)
            put("list", moshi.adapter(Array<String>::class.java).toJson(list))
        }
        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val response = zeneAPI.suggestedSongs(body)
        setAPICache(SUGGESTED_SONGS_CACHE, response)
        emit(response)
    }.flowOn(Dispatchers.IO)

    override suspend fun suggestedSongs(id: String) = flow {
        emit(zeneAPI.suggestedSongs(id))
    }.flowOn(Dispatchers.IO)

    override suspend fun recommendedPlaylists() = flow {
        val cache = getAPICache(RECOMMENDED_PLAYLISTS_CACHE)
        if (cache != null) {
            emit(cache)
            return@flow
        }
        val list = songHistoryListDB.firstOrNull() ?: emptyArray()
        val email = userInfoDB.firstOrNull()?.email ?: ""
        val json = JSONObject().apply {
            put("email", email)
            put("list", moshi.adapter(Array<String>::class.java).toJson(list))
        }
        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val response = zeneAPI.recommendedPlaylists(body)
        setAPICache(RECOMMENDED_PLAYLISTS_CACHE, response)
        emit(response)
    }.flowOn(Dispatchers.IO)

    override suspend fun recommendedAlbums() = flow {
        val cache = getAPICache(RECOMMENDED_ALBUMS_CACHE)
        if (cache != null) {
            emit(cache)
            return@flow
        }
        val list = songHistoryListDB.firstOrNull() ?: emptyArray()
        val email = userInfoDB.firstOrNull()?.email ?: ""
        val json = JSONObject().apply {
            put("email", email)
            put("list", moshi.adapter(Array<String>::class.java).toJson(list))
        }
        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val response = zeneAPI.recommendedAlbums(body)
        setAPICache(RECOMMENDED_ALBUMS_CACHE, response)
        emit(response)
    }.flowOn(Dispatchers.IO)

    override suspend fun recommendedVideo() = flow {
        val cache = getAPICache(RECOMMENDED_VIDEOS_CACHE)
        if (cache != null) {
            emit(cache)
            return@flow
        }

        val list = songHistoryListDB.firstOrNull() ?: emptyArray()
        val email = userInfoDB.firstOrNull()?.email ?: ""
        val json = JSONObject().apply {
            put("email", email)
            put("list", moshi.adapter(Array<String>::class.java).toJson(list))
        }
        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val response = zeneAPI.recommendedVideo(body)
        setAPICache(RECOMMENDED_VIDEOS_CACHE, response)
        emit(response)
    }.flowOn(Dispatchers.IO)

    override suspend fun suggestTopSongs() = flow {
        val cache = getAPICache(SUGGESTED_TOP_SONGS_CACHE)
        if (cache != null) {
            emit(cache)
            return@flow
        }
        val list = songHistoryListDB.firstOrNull() ?: emptyArray()
        val email = userInfoDB.firstOrNull()?.email ?: ""
        val json = JSONObject().apply {
            put("email", email)
            put("list", moshi.adapter(Array<String>::class.java).toJson(list))
        }
        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val response = zeneAPI.suggestTopSongs(body)
        setAPICache(SUGGESTED_TOP_SONGS_CACHE, response)
        emit(response)
    }.flowOn(Dispatchers.IO)

    override suspend fun addMusicHistory(songID: String, artists: String?) = flow {
        val email = userInfoDB.firstOrNull()?.email ?: ""

        val json = JSONObject().apply {
            put("songID", songID)
            put("email", email)
            put("device", getDeviceName())
        }
        addSongHistoryLists(songID)
        artists?.let { addArtistsHistoryLists(it) }
        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.addSongHistory(body))
    }.flowOn(Dispatchers.IO)

    override suspend fun artistsInfo(name: String) = flow {
        val json = JSONObject().apply {
            put("name", name)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.artistsInfo(body))
    }.flowOn(Dispatchers.IO)

    override suspend fun playlistAlbums(id: String) = flow {
        val email = userInfoDB.firstOrNull()?.email ?: ""
        emit(zeneAPI.playlistAlbums(id, email))
    }.flowOn(Dispatchers.IO)

    override suspend fun updateArtists(list: Array<String>) = flow {
        val email = userInfoDB.firstOrNull()?.email ?: ""

        val artists = JSONArray().also { j ->
            list.forEach { j.put(it) }
        }
        val json = JSONObject().apply {
            put("email", email)
            put("artists", artists)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.updateArtists(body))
    }.flowOn(Dispatchers.IO)

    override suspend fun artistsData(name: String) = flow {
        val json = JSONObject().apply {
            put("name", name)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.artistsData(body))
    }.flowOn(Dispatchers.IO)

    override suspend fun getMusicHistory(page: Int) = flow {
        val email = userInfoDB.firstOrNull()?.email ?: ""
        emit(zeneAPI.getSongHistory(email, page))
    }.flowOn(Dispatchers.IO)

    override suspend fun savedPlaylists(page: Int) = flow {
        val email = userInfoDB.firstOrNull()?.email ?: return@flow
        emit(zeneAPI.savedPlaylists(email, page))
    }.flowOn(Dispatchers.IO)

    override suspend fun createNewPlaylists(name: String, file: File?, id: String?) = flow {
        val email = userInfoDB.firstOrNull()?.email ?: return@flow

        val requestFile = file?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val fileForm = if (requestFile == null) null
        else MultipartBody.Part.createFormData("image", file.name, requestFile)

        val nameForm = name.trim().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val emailForm = email.trim().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val idForm = id?.trim()?.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        emit(zeneAPI.playlistCreate(fileForm, nameForm, emailForm, idForm))
    }.flowOn(Dispatchers.IO)

    override suspend fun searchImg(q: String) = flow {
        emit(zeneAPI.searchImg(q))
    }.flowOn(Dispatchers.IO)

    override suspend fun deletePlaylists(id: String) = flow {
        val email = userInfoDB.firstOrNull()?.email ?: return@flow
        emit(zeneAPI.deletePlaylists(email, id))
    }.flowOn(Dispatchers.IO)

    override suspend fun checkIfSongPresentInPlaylists(songId: String, page: Int) = flow {
        val email = userInfoDB.firstOrNull()?.email ?: return@flow
        emit(zeneAPI.checkIfSongPresentInPlaylists(email, page, songId))
    }.flowOn(Dispatchers.IO)

    override suspend fun addRemoveSongFromPlaylists(songId: String, pID: String, doAdd: Boolean) =
        flow {
            val email = userInfoDB.firstOrNull()?.email ?: return@flow
            emit(zeneAPI.addRemoveSongFromPlaylists(pID, songId, doAdd, email))
        }.flowOn(Dispatchers.IO)


    override suspend fun isSongLiked(songID: String) = flow {
        val email = userInfoDB.firstOrNull()?.email ?: return@flow
        val json = JSONObject().apply {
            put("songID", songID)
            put("email", email)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.isSongLiked(body))
    }.flowOn(Dispatchers.IO)

    override suspend fun userPlaylistData(playlistID: String) = flow {
        val json = JSONObject().apply {
            put("playlistId", playlistID)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.userPlaylistData(body))
    }.flowOn(Dispatchers.IO)

    override suspend fun artistsPosts() = flow {
        val email = userInfoDB.firstOrNull()?.email ?: return@flow
        val json = JSONObject().apply {
            put("email", email)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.artistsPosts(body))
    }.flowOn(Dispatchers.IO)

    override suspend fun topCacheSongsAndArtists() = flow {
        val email = userInfoDB.firstOrNull()?.email ?: return@flow
        val json = JSONObject().apply {
            put("email", email)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.topCacheSongsAndArtists(body))
    }.flowOn(Dispatchers.IO)

    override suspend fun userPlaylistSongs(playlistID: String, page: Int) = flow {
        emit(zeneAPI.userPlaylistSongs(playlistID, page))
    }.flowOn(Dispatchers.IO)

    override suspend fun importSpotifyPlaylists(token: String, path: String?) = flow {
        emit(zeneAPI.importSpotifyPlaylists(token, path))
    }.flowOn(Dispatchers.IO)

    override suspend fun relatedVideos(id: String) = flow {
        emit(zeneAPI.relatedVideos(id))
    }.flowOn(Dispatchers.IO)


    override suspend fun songInfo(id: String) = flow {
        emit(zeneAPI.songInfo(id))
    }.flowOn(Dispatchers.IO)
}