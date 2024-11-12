package com.rizwansayyed.zene.viewmodel

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.api.model.ZeneArtistsDataResponse
import com.rizwansayyed.zene.data.api.model.ZeneArtistsInfoResponse
import com.rizwansayyed.zene.data.api.model.ZeneArtistsPostsResponse
import com.rizwansayyed.zene.data.api.model.ZeneBooleanResponse
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataResponse
import com.rizwansayyed.zene.data.api.model.ZeneMusicHistoryItem
import com.rizwansayyed.zene.data.api.model.ZeneSavedPlaylistsResponseItem
import com.rizwansayyed.zene.data.api.zene.ZeneAPIInterface
import com.rizwansayyed.zene.data.db.DataStoreManager.pinnedArtistsList
import com.rizwansayyed.zene.data.db.DataStoreManager.userInfoDB
import com.rizwansayyed.zene.data.roomdb.updates.implementation.UpdatesRoomDBInterface
import com.rizwansayyed.zene.data.roomdb.updates.model.UpdateData
import com.rizwansayyed.zene.utils.Utils.internetIsConnected
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
import javax.inject.Inject


@HiltViewModel
class ZeneViewModel @Inject constructor(
    private val zeneAPI: ZeneAPIInterface, private val updateDB: UpdatesRoomDBInterface
) : ViewModel() {

    private var lastSyncedA by mutableStateOf<String?>(null)
    var artistsInfo by mutableStateOf<APIResponse<ZeneArtistsInfoResponse>>(APIResponse.Empty)
    var artistsData by mutableStateOf<APIResponse<ZeneArtistsDataResponse>>(APIResponse.Empty)
    var searchImg by mutableStateOf<APIResponse<List<String>>>(APIResponse.Empty)
    var createPlaylistInfo by mutableStateOf<APIResponse<ZeneBooleanResponse>>(APIResponse.Empty)
    var songHistory = mutableStateListOf<ZeneMusicHistoryItem>()
    var zeneSavedPlaylists = mutableStateListOf<ZeneSavedPlaylistsResponseItem>()
    var saveSongPlaylists = mutableStateListOf<ZeneMusicDataItems>()
    var songHistoryIsLoading by mutableStateOf(true)
    var doShowMoreLoading by mutableStateOf(false)
    var relatedVideos by mutableStateOf<APIResponse<ZeneMusicDataResponse>>(APIResponse.Empty)
    var searchFindSong by mutableStateOf<APIResponse<ZeneMusicDataItems>>(APIResponse.Empty)
    var feedItems by mutableStateOf<APIResponse<ZeneArtistsPostsResponse>>(APIResponse.Empty)
    var isSongLiked by mutableStateOf<APIResponse<Boolean>>(APIResponse.Empty)

    fun artistsInfo(name: String) = viewModelScope.launch(Dispatchers.IO) {
        if (lastSyncedA == name && artistsInfo is APIResponse.Success) return@launch

        if (!internetIsConnected()) {
            artistsInfo = APIResponse.Empty
            return@launch
        }

        zeneAPI.artistsInfo(name).onStart {
            artistsInfo = APIResponse.Loading
        }.catch {
            artistsInfo = APIResponse.Error(it)
        }.collectLatest {
            lastSyncedA = name
            artistsInfo = APIResponse.Success(it)
        }
    }

    fun artistsData(name: String) = viewModelScope.launch(Dispatchers.IO) {
        if (lastSyncedA == name && artistsData is APIResponse.Success) return@launch

        if (!internetIsConnected()) {
            artistsData = APIResponse.Empty
            return@launch
        }

        zeneAPI.artistsData(name).onStart {
            artistsData = APIResponse.Loading
        }.catch {
            artistsData = APIResponse.Error(it)
        }.collectLatest {
            artistsData = APIResponse.Success(it)
        }
    }

    fun followArtists(name: String?, b: Boolean, isMore: (Boolean) -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
            if (!internetIsConnected()) {
                isMore(false)
                return@launch
            }

            val email = userInfoDB.firstOrNull()?.email ?: ""
            val user = zeneAPI.getUser(email).firstOrNull()

            if ((user?.pinned_artists?.size ?: 0) >= 40) {
                isMore(true)
                return@launch
            }

            val list = ArrayList<String>()
            if (b && name != null) list.add(name)

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

        if (!internetIsConnected()) {
            songHistoryIsLoading = false
            return@launch
        }


        zeneAPI.getMusicHistory(page).onStart {
            songHistoryIsLoading = true
        }.catch {
            songHistoryIsLoading = false
        }.collectLatest {
            if (page == 0) {
                songHistory.clear()
                zeneSavedPlaylists.clear()
            }

            songHistoryIsLoading = false
            it.forEach { songHistory.add(it) }

            doShowMoreLoading = it.size >= 20
        }
    }

    fun playlists(page: Int) = viewModelScope.launch(Dispatchers.IO) {
        if (page == 0) {
            songHistory.clear()
            zeneSavedPlaylists.clear()
        }

        if (!internetIsConnected()) {
            songHistoryIsLoading = false
            return@launch
        }

        zeneAPI.savedPlaylists(page).onStart {
            songHistoryIsLoading = true
        }.catch {
            songHistoryIsLoading = false
        }.collectLatest {
            if (page == 0) {
                songHistory.clear()
                zeneSavedPlaylists.clear()
            }
            songHistoryIsLoading = false
            it.forEach { m -> zeneSavedPlaylists.add(m) }

            doShowMoreLoading = it.size >= 20
        }
    }

    fun searchImages(q: String) = viewModelScope.launch(Dispatchers.IO) {
        if (!internetIsConnected()) {
            searchImg = APIResponse.Empty
            return@launch
        }

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
            if (!internetIsConnected()) {
                createPlaylistInfo = APIResponse.Empty
                return@launch
            }

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
        if (!internetIsConnected()) return@launch

        zeneAPI.deletePlaylists(id).catch {}.collectLatest {
            if (it.isSuccess()) APIResponse.Success(it)
            else APIResponse.Error(Exception(""))
        }
    }


    fun checkIfSongPresentInPlaylists(id: String, page: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            if (page == 0) saveSongPlaylists.clear()

            if (!internetIsConnected()) {
                songHistoryIsLoading = false
                return@launch
            }


            zeneAPI.checkIfSongPresentInPlaylists(id, page).onStart {
                songHistoryIsLoading = true
            }.catch {
                songHistoryIsLoading = false
            }.collectLatest {
                if (page == 0) saveSongPlaylists.clear()
                songHistoryIsLoading = false
                saveSongPlaylists.addAll(it)

                doShowMoreLoading = it.size >= 20
            }
        }


    fun addRemoveSongFromPlaylists(pID: String, sID: String, add: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            if (!internetIsConnected()) return@launch
            zeneAPI.addRemoveSongFromPlaylists(sID, pID, add).catch {}.collectLatest {}
        }

    fun searchFindSong(q: String) = viewModelScope.launch(Dispatchers.IO) {
        if (!internetIsConnected()) {
            searchFindSong = APIResponse.Empty
            return@launch
        }

        zeneAPI.searchData(q).onStart {
            searchFindSong = APIResponse.Loading
        }.catch {
            searchFindSong = APIResponse.Error(it)
        }.collectLatest {
            val d = it.songs.first()
            searchFindSong = APIResponse.Success(d)
        }
    }

    fun startGettingFeed() = viewModelScope.launch(Dispatchers.IO) {
        if (!internetIsConnected()) {
            feedItems = APIResponse.Empty
            return@launch
        }

        zeneAPI.artistsPosts().onStart {
            feedItems = APIResponse.Loading
        }.catch {
            feedItems = APIResponse.Empty
        }.collectLatest {
            feedItems = APIResponse.Success(it)
        }
    }


    fun isSongLiked(songID: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.isSongLiked(songID).onStart {
            isSongLiked = APIResponse.Loading
        }.catch {
            isSongLiked = APIResponse.Empty
        }.collectLatest {
            isSongLiked = APIResponse.Success(it.isLiked)
        }
    }

    fun relatedVideos(songID: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.relatedVideos(songID).onStart {
            relatedVideos = APIResponse.Loading
        }.catch {
            relatedVideos = APIResponse.Empty
        }.collectLatest {
            relatedVideos = APIResponse.Success(it)
        }
    }
}