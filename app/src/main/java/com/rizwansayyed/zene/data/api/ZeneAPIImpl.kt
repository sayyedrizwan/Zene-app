package com.rizwansayyed.zene.data.api

import android.os.Build
import com.google.firebase.messaging.FirebaseMessaging
import com.rizwansayyed.zene.data.api.ip.IpAPIService
import com.rizwansayyed.zene.data.api.zene.ZeneAPIInterface
import com.rizwansayyed.zene.data.api.zene.ZeneAPIService
import com.rizwansayyed.zene.data.db.DataStoreManager.userInfoDB
import com.rizwansayyed.zene.utils.Utils.getDeviceName
import com.rizwansayyed.zene.utils.Utils.toast
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject


class ZeneAPIImpl @Inject constructor(
    private val zeneAPI: ZeneAPIService, private val ipAPI: IpAPIService
) : ZeneAPIInterface {

    override suspend fun updateUser() = flow {
        val ip = ipAPI.ip()
        val users = userInfoDB.firstOrNull()
        val fcm = FirebaseMessaging.getInstance().token.await()

        val json = JSONObject().apply {
            put("name", users?.name)
            put("email", users?.email)
            put("photo", users?.profilePhoto)
            put("playtime", users?.totalPlayTime ?: 0)
            put("ip", ip.query)
            put("fcm", fcm)
            put("device", "${Build.MANUFACTURER} ${Build.MODEL} ${Build.VERSION.RELEASE}")
            put("country", "${ip.city}, ${ip.regionName}, ${ip.country}")
            put("review", users?.isReviewDone ?: false)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.updateUser(body))
    }

    override suspend fun getUser(email: String) = flow {
        emit(zeneAPI.getUser(email))
    }

    override suspend fun moodLists() = flow {
        emit(zeneAPI.moodLists())
    }

    override suspend fun moodLists(id: String) = flow {
        val json = JSONObject().apply {
            put("id", id)
        }
        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.moodLists(body))
    }

    override suspend fun latestReleases(id: String) = flow {
        emit(zeneAPI.latestReleases(id))
    }

    override suspend fun lyrics(id: String, name: String, artists: String) = flow {
        emit(zeneAPI.lyrics(id, name, artists))
    }

    override suspend fun playerVideoData(name: String, artists: String) = flow {
        emit(zeneAPI.playerVideoData(name, artists))
    }

    override suspend fun merchandise(name: String, artists: String) = flow {
        emit(zeneAPI.merchandise("${artists.substringBefore("-").substringBefore("&")} - ${name}"))
    }

    override suspend fun topMostListeningSong() = flow {
        emit(zeneAPI.topMostListeningSong())
    }

    override suspend fun topMostListeningArtists() = flow {
        emit(zeneAPI.topMostListeningArtists())
    }

    override suspend fun searchData(s: String) = flow {
        emit(zeneAPI.searchData(s))
    }

    override suspend fun searchSuggestions(s: String) = flow {
        emit(zeneAPI.searchSuggestions(s))
    }

    override suspend fun favArtistsData() = flow {
        val email = userInfoDB.firstOrNull()?.email ?: ""
        val json = JSONObject().apply {
            put("email", email)
        }
        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.favArtistsData(body))
    }

    override suspend fun suggestedSongs() = flow {
        val email = userInfoDB.firstOrNull()?.email ?: ""
        val json = JSONObject().apply {
            put("email", email)
        }
        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.suggestedSongs(body))
    }

    override suspend fun suggestedSongs(id: String) = flow {
        emit(zeneAPI.suggestedSongs(id))
    }

    override suspend fun recommendedPlaylists() = flow {
        val email = userInfoDB.firstOrNull()?.email ?: ""
        val json = JSONObject().apply {
            put("email", email)
        }
        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.recommendedPlaylists(body))
    }

    override suspend fun recommendedAlbums() = flow {
        val email = userInfoDB.firstOrNull()?.email ?: ""
        val json = JSONObject().apply {
            put("email", email)
        }
        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.recommendedAlbums(body))
    }

    override suspend fun recommendedVideo() = flow {
        val email = userInfoDB.firstOrNull()?.email ?: ""
        val json = JSONObject().apply {
            put("email", email)
        }
        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.recommendedVideo(body))
    }

    override suspend fun suggestTopSongs() = flow {
        val email = userInfoDB.firstOrNull()?.email ?: ""
        val json = JSONObject().apply {
            put("email", email)
        }
        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.suggestTopSongs(body))
    }

    override suspend fun addMusicHistory(songID: String) = flow {
        val email = userInfoDB.firstOrNull()?.email ?: ""

        val json = JSONObject().apply {
            put("songID", songID)
            put("email", email)
            put("device", getDeviceName())
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.addSongHistory(body))
    }

    override suspend fun artistsInfo(name: String) = flow {
        val json = JSONObject().apply {
            put("name", name)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.artistsInfo(body))
    }

    override suspend fun getMusicHistory(page: Int) = flow {
        val email = userInfoDB.firstOrNull()?.email ?: ""
        emit(zeneAPI.getSongHistory(email, page))
    }

    override suspend fun playlistAlbums(id: String) = flow {
        emit(zeneAPI.playlistAlbums(id))
    }
}