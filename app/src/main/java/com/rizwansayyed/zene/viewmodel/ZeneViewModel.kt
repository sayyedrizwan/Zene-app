package com.rizwansayyed.zene.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.api.model.ZeneArtistsDataResponse
import com.rizwansayyed.zene.data.api.model.ZeneArtistsInfoResponse
import com.rizwansayyed.zene.data.api.model.ZeneBooleanResponse
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataResponse
import com.rizwansayyed.zene.data.api.model.ZeneMusicHistoryItem
import com.rizwansayyed.zene.data.api.model.ZeneMusicHistoryResponse
import com.rizwansayyed.zene.data.api.model.ZeneSavedPlaylistsResponseItem
import com.rizwansayyed.zene.data.api.zene.ZeneAPIInterface
import com.rizwansayyed.zene.data.db.DataStoreManager.pinnedArtistsList
import com.rizwansayyed.zene.data.db.DataStoreManager.userInfoDB
import com.rizwansayyed.zene.utils.Utils.saveBitmap
import com.rizwansayyed.zene.utils.Utils.savePlaylistFilePath
import com.rizwansayyed.zene.utils.Utils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject


@HiltViewModel
class ZeneViewModel @Inject constructor(private val zeneAPI: ZeneAPIInterface) : ViewModel() {

    var artistsInfo by mutableStateOf<APIResponse<ZeneArtistsInfoResponse>>(APIResponse.Empty)
    var artistsData by mutableStateOf<APIResponse<ZeneArtistsDataResponse>>(APIResponse.Empty)
    var searchImg by mutableStateOf<APIResponse<List<String>>>(APIResponse.Empty)
    var createPlaylistInfo by mutableStateOf<APIResponse<ZeneBooleanResponse>>(APIResponse.Empty)
    var songHistory = mutableStateListOf<ZeneMusicHistoryItem>()
    var zeneSavedPlaylists = mutableStateListOf<ZeneSavedPlaylistsResponseItem>()
    var saveSongPlaylists = mutableStateListOf<ZeneMusicDataItems>()
    var songHistoryIsLoading by mutableStateOf(true)
    var doShowMoreLoading by mutableStateOf(false)
    var searchFindSong by mutableStateOf<APIResponse<ZeneMusicDataItems>>(APIResponse.Empty)

    fun artistsInfo(name: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.artistsInfo(name).onStart {
            artistsInfo = APIResponse.Loading
        }.catch {
            artistsInfo = APIResponse.Error(it)
        }.collectLatest {
            artistsInfo = APIResponse.Success(it)
        }
    }

    fun artistsData(name: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.artistsData(name).onStart {
            artistsData = APIResponse.Loading
        }.catch {
            artistsData = APIResponse.Error(it)
        }.collectLatest {
            artistsData = APIResponse.Success(it)
        }
    }

    fun followArtists(name: String?, b: Boolean, isMore: () -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
            val email = userInfoDB.firstOrNull()?.email ?: ""
            val user = zeneAPI.getUser(email).firstOrNull()

            if ((user?.pinned_artists?.size ?: 0) >= 30) {
                isMore()
                return@launch
            }

            val list = ArrayList<String>()
            if (b && name != null)
                list.add(name)

            user?.pinned_artists?.forEach {
                if (it?.lowercase() != name?.lowercase()) it?.let { it1 -> list.add(it1) }
            }

            userInfoDB = flowOf(user?.toUserInfo(email))
            pinnedArtistsList = flowOf(list.toTypedArray())
            zeneAPI.updateArtists(list.toTypedArray()).firstOrNull()
        }

    fun songHistory(page: Int) = viewModelScope.launch(Dispatchers.IO) {
        if (page == 0) {
            songHistory.clear()
            zeneSavedPlaylists.clear()
        }

        zeneAPI.getMusicHistory(page).onStart {
            songHistoryIsLoading = true
        }.catch {
            songHistoryIsLoading = false
        }.collectLatest {
            songHistoryIsLoading = false
            it.forEach { songHistory.add(it) }

            doShowMoreLoading = it.size >= 24
        }
    }

    fun playlists(page: Int) = viewModelScope.launch(Dispatchers.IO) {
        if (page == 0) {
            songHistory.clear()
            zeneSavedPlaylists.clear()
        }

        zeneAPI.savedPlaylists(page).onStart {
            songHistoryIsLoading = true
        }.catch {
            songHistoryIsLoading = false
        }.collectLatest {
            songHistoryIsLoading = false
            it.forEach { zeneSavedPlaylists.add(it) }

            doShowMoreLoading = it.size >= 24
        }
    }

    fun searchImages(q: String) = viewModelScope.launch(Dispatchers.IO) {
        if (q == "") {
            searchImg = APIResponse.Empty
            return@launch
        }
        zeneAPI.searchImg(q.trim()).onStart {
            searchImg = APIResponse.Loading
        }.catch {
            searchImg = APIResponse.Error(it)
        }.collectLatest {
            val result = zeneAPI.searchData(q).firstOrNull()
            val list = ArrayList<String>()
            it.forEach { list.add(it) }
            result?.songs?.forEach { m -> m.thumbnail?.let { it1 -> list.add(it1) } }
            result?.albums?.forEach { m -> m.thumbnail?.let { it1 -> list.add(it1) } }
            result?.playlists?.forEach { m -> m.thumbnail?.let { it1 -> list.add(it1) } }

            searchImg = APIResponse.Success(list)
        }
    }

    fun createNewPlaylist(name: String, bitmap: Bitmap?, id: String?) =
        viewModelScope.launch(Dispatchers.IO) {
            val savedPath = saveBitmap(savePlaylistFilePath, bitmap)

            zeneAPI.createNewPlaylists(name, savedPath, id).onStart {
                createPlaylistInfo = APIResponse.Loading
            }.catch {
                createPlaylistInfo = APIResponse.Error(it)
            }.collectLatest {
                createPlaylistInfo = if (it.isSuccess()) APIResponse.Success(it)
                else APIResponse.Error(Exception(""))
            }
        }


    fun deletePlaylists(id: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.deletePlaylists(id).catch {}.collectLatest {
            if (it.isSuccess()) APIResponse.Success(it)
            else APIResponse.Error(Exception(""))
        }
    }


    fun checkIfSongPresentInPlaylists(id: String, page: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            if (page == 0) saveSongPlaylists.clear()

            zeneAPI.checkIfSongPresentInPlaylists(id, page).onStart {
                songHistoryIsLoading = true
            }.catch {
                songHistoryIsLoading = false
            }.collectLatest {
                songHistoryIsLoading = false
                saveSongPlaylists.addAll(it)

                doShowMoreLoading = it.size >= 24
            }
        }


    fun addRemoveSongFromPlaylists(pID: String, sID: String, add: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            zeneAPI.addRemoveSongFromPlaylists(sID, pID, add).catch {}.collectLatest {}
        }

    fun searchFindSong(q: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.searchData(q).onStart {
            searchFindSong = APIResponse.Loading
        }.catch {
            searchFindSong = APIResponse.Error(it)
        }.collectLatest {
            val d = it.songs.first()
            searchFindSong = APIResponse.Success(d)
        }
    }


    fun uploadFile(file: File) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val client = OkHttpClient()
            val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(
                    "file", file.name, file.asRequestBody("application/octet-stream".toMediaType())
                )
                .addFormDataPart("sample_size","155648")
                .build()
            val request = Request.Builder()
                .url("https://api.doreso.com/identify")
                .post(body)
                .addHeader("accept", "application/json, text/plain, */*")
                .addHeader("accept-language", "en-US,en;q=0.9")
                .addHeader("origin", "https://www.aha-music.com")
                .addHeader("priority", "u=1, i")
                .addHeader("referer", "https://www.aha-music.com/")
                .addHeader("sec-ch-ua", "\"Not/A)Brand\";v=\"8\", \"Chromium\";v=\"126\", \"Google Chrome\";v=\"126\"")
                .addHeader("sec-ch-ua-mobile", "?1")
                .addHeader("sec-ch-ua-platform", "\"Android\"")
                .addHeader("sec-fetch-dest", "empty")
                .addHeader("sec-fetch-mode", "cors")
                .addHeader("sec-fetch-site", "cross-site")
                .addHeader("user-agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Mobile Safari/537.36")
                .build()

            val response = client.newCall(request).execute()

            Log.d("TAG", "uploadFile: ${response.body?.string()}")
        } catch (e: Exception) {
            Log.d("TAG", "uploadFile: on ${e.message}")
        }
    }
}