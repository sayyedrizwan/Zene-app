package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.implementation.ZeneAPIInterface
import com.rizwansayyed.zene.data.model.MusicDataTypes
import com.rizwansayyed.zene.data.model.NewPlaylistResponse
import com.rizwansayyed.zene.data.model.PlayerLyricsInfoResponse
import com.rizwansayyed.zene.data.model.PlayerRadioResponse
import com.rizwansayyed.zene.data.model.PodcastEposideResponse
import com.rizwansayyed.zene.data.model.SearchDataResponse
import com.rizwansayyed.zene.data.model.UserPlaylistResponse
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.data.model.ZeneMusicDataList
import com.rizwansayyed.zene.datastore.DataStorageManager.musicPlayerDB
import com.rizwansayyed.zene.datastore.model.MusicPlayerData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class PlayerViewModel @Inject constructor(private val zeneAPI: ZeneAPIInterface) : ViewModel() {

    var videoSimilarVideos by mutableStateOf<ResponseResult<Pair<String, ZeneMusicDataList>>>(
        ResponseResult.Empty
    )

    var itemAddedToPlaylists = mutableStateMapOf<String, Boolean>()
    var createPlaylist by mutableStateOf<ResponseResult<NewPlaylistResponse>>(ResponseResult.Empty)
    var playerLyrics by mutableStateOf<ResponseResult<PlayerLyricsInfoResponse>>(ResponseResult.Empty)
    var playerPodcastInfo by mutableStateOf<ResponseResult<PodcastEposideResponse>>(ResponseResult.Empty)
    var playerRadioInfo by mutableStateOf<ResponseResult<PlayerRadioResponse>>(ResponseResult.Empty)
    var similarSongs by mutableStateOf<ResponseResult<SearchDataResponse>>(ResponseResult.Empty)
    var checksPlaylistsSongLists = mutableListOf<UserPlaylistResponse>()
    var checksPlaylistsSongListsLoading by mutableStateOf(false)
    var isItemLiked by mutableStateOf(false)
    private var lyricsJob by mutableStateOf<Job?>(null)

    fun similarVideos(id: String) = viewModelScope.launch(Dispatchers.IO) {
        when (val v = videoSimilarVideos) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> {}
            is ResponseResult.Success -> {
                if (v.data.first == id && v.data.second.isNotEmpty()) return@launch
            }
        }

        zeneAPI.similarVideos(id).onStart {
            videoSimilarVideos = ResponseResult.Loading
        }.catch {
            videoSimilarVideos = ResponseResult.Error(it)
        }.collectLatest {
            videoSimilarVideos = ResponseResult.Success(Pair(id, it))
        }
    }

    fun createNewPlaylists(name: String, info: ZeneMusicData?) =
        viewModelScope.launch(Dispatchers.IO) {
            zeneAPI.createNewPlaylists(name, info).onStart {
                createPlaylist = ResponseResult.Loading
            }.catch {
                createPlaylist = ResponseResult.Error(it)
            }.collectLatest {
                createPlaylist = ResponseResult.Success(it)
                delay(1.seconds)
                createPlaylist = ResponseResult.Empty
            }
        }

    fun playlistSongCheckList(page: Int, songId: String) = viewModelScope.launch(Dispatchers.IO) {
        if (page == 0) checksPlaylistsSongLists.clear()
        zeneAPI.playlistSongCheck(songId, page).onStart {
            checksPlaylistsSongListsLoading = true
        }.catch {
            checksPlaylistsSongListsLoading = false
        }.collectLatest {
            checksPlaylistsSongListsLoading = false
            checksPlaylistsSongLists.addAll(it)
        }
    }

    fun likedMediaItem(id: String?, type: MusicDataTypes) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.likedStatus(id, type).onStart {
            isItemLiked = false
        }.catch {
            isItemLiked = false
        }.collectLatest {
            isItemLiked = it.isLiked ?: false
        }
    }

    fun likeAItem(data: ZeneMusicData?, doLike: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        isItemLiked = doLike
        zeneAPI.addRemoveLikeItem(data, doLike).catch { }.collectLatest { }
    }

    fun addMediaToPlaylist(id: String, state: Boolean, info: ZeneMusicData?) =
        viewModelScope.launch(Dispatchers.IO) {
            itemAddedToPlaylists[id] = state
            zeneAPI.addItemToPlaylists(info, id, state).catch { }.collectLatest { }
        }


    fun getSongLyrics() {
        var p: MusicPlayerData? = null
        playerLyrics = ResponseResult.Loading
        lyricsJob?.cancel()
        lyricsJob = viewModelScope.launch(Dispatchers.IO) {
            while (p == null) {
                delay(1.seconds)
                if (musicPlayerDB.firstOrNull()?.totalDuration != "0") p =
                    musicPlayerDB.firstOrNull()
            }
            zeneAPI.playerLyrics(p).catch {
                playerLyrics = ResponseResult.Error(it)
            }.collectLatest {
                playerLyrics = ResponseResult.Success(it)
            }
        }
    }

    fun getAISongLyrics(id: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.lyricsAIMusic(id).onStart {
            playerLyrics = ResponseResult.Loading
        }.catch {
            playerLyrics = ResponseResult.Error(it)
        }.collectLatest {
            playerLyrics = ResponseResult.Success(it)
        }
    }

    fun playerPodcastInfo(id: String, path: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.playerPodcastInfo(id, path).onStart {
            playerPodcastInfo = ResponseResult.Loading
        }.catch {
            playerPodcastInfo = ResponseResult.Error(it)
        }.collectLatest {
            playerPodcastInfo = ResponseResult.Success(it)
        }
    }

    fun playerRadioInfo(id: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.playerRadioInfo(id).onStart {
            playerRadioInfo = ResponseResult.Loading
        }.catch {
            playerRadioInfo = ResponseResult.Error(it)
        }.collectLatest {
            playerRadioInfo = ResponseResult.Success(it)
        }
    }

    fun playerSimilarSongs(id: String?) = viewModelScope.launch(Dispatchers.IO) {
        id ?: return@launch
        zeneAPI.similarSongs(id).onStart {
            similarSongs = ResponseResult.Loading
        }.catch {
            similarSongs = ResponseResult.Error(it)
        }.collectLatest {
            similarSongs = ResponseResult.Success(it)
        }
    }
}