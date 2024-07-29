package com.rizwansayyed.zene.data.api

import android.os.Build
import com.google.firebase.messaging.FirebaseMessaging
import com.rizwansayyed.zene.data.api.ip.IpAPIService
import com.rizwansayyed.zene.data.api.zene.ZeneAPIInterface
import com.rizwansayyed.zene.data.api.zene.ZeneAPIService
import com.rizwansayyed.zene.data.db.DataStoreManager.ipDB
import com.rizwansayyed.zene.data.db.DataStoreManager.userInfoDB
import com.rizwansayyed.zene.utils.Utils.getDeviceName
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import javax.inject.Inject


class ZeneAPIImpl @Inject constructor(
    private val zeneAPI: ZeneAPIService, private val ipAPI: IpAPIService
) : ZeneAPIInterface {
    override suspend fun ip() = flow {
        val ip = ipAPI.ip()
        ipDB = flowOf(ip)
        emit(ip)
    }

    override suspend fun updateUser() = flow {
        val ip = ip().firstOrNull()

        val users = userInfoDB.firstOrNull()
        val fcm = FirebaseMessaging.getInstance().token.await()

        val json = JSONObject().apply {
            put("name", users?.name)
            put("email", users?.email)
            put("photo", users?.getProfilePicture())
            put("playtime", users?.totalPlayTime ?: 0)
            put("ip", ip?.query)
            put("fcm", fcm)
            put("device", "${Build.MANUFACTURER} ${Build.MODEL} ${Build.VERSION.RELEASE}")
            put("country", "${ip?.city}, ${ip?.regionName}, ${ip?.country}")
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

    override suspend fun playlistAlbums(id: String) = flow {
        val email = userInfoDB.firstOrNull()?.email ?: ""
        emit(zeneAPI.playlistAlbums(id, email))
    }

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
    }

    override suspend fun artistsData(name: String) = flow {
        val json = JSONObject().apply {
            put("name", name)
        }

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        emit(zeneAPI.artistsData(body))
    }

    override suspend fun getMusicHistory(page: Int) = flow {
        val email = userInfoDB.firstOrNull()?.email ?: ""
        emit(zeneAPI.getSongHistory(email, page))
    }

    override suspend fun savedPlaylists(page: Int) = flow {
        val email = userInfoDB.firstOrNull()?.email ?: return@flow
        emit(zeneAPI.savedPlaylists(email, page))
    }

    override suspend fun createNewPlaylists(name: String, file: File?, id: String?) = flow {
        val email = userInfoDB.firstOrNull()?.email ?: return@flow

        val requestFile = file?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val fileForm = if (requestFile == null) null
        else MultipartBody.Part.createFormData("image", file.name, requestFile)

        val nameForm = name.trim().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val emailForm = email.trim().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val idForm = id?.trim()?.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        emit(zeneAPI.playlistCreate(fileForm, nameForm, emailForm, idForm))
    }

    override suspend fun searchImg(q: String) = flow {
        emit(zeneAPI.searchImg(q))
    }

    override suspend fun deletePlaylists(id: String) = flow {
        val email = userInfoDB.firstOrNull()?.email ?: return@flow
        emit(zeneAPI.deletePlaylists(email, id))
    }

    override suspend fun checkIfSongPresentInPlaylists(songId: String, page: Int) = flow {
        val email = userInfoDB.firstOrNull()?.email ?: return@flow
        emit(zeneAPI.checkIfSongPresentInPlaylists(email, page, songId))
    }

    override suspend fun addRemoveSongFromPlaylists(songId: String, pID: String, doAdd: Boolean) =
        flow {
            emit(zeneAPI.addRemoveSongFromPlaylists(pID, songId, doAdd))
        }
}