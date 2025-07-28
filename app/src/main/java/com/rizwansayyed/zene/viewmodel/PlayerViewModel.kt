package com.rizwansayyed.zene.viewmodel

import android.os.Build
import android.text.Html
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.implementation.ZeneAPIInterface
import com.rizwansayyed.zene.data.model.LikeItemType
import com.rizwansayyed.zene.data.model.MusicDataTypes
import com.rizwansayyed.zene.data.model.NewPlaylistResponse
import com.rizwansayyed.zene.data.model.PlayerLyricsInfoResponse
import com.rizwansayyed.zene.data.model.PlayerRadioResponse
import com.rizwansayyed.zene.data.model.PlayerVideoForSongsResponse
import com.rizwansayyed.zene.data.model.PodcastEpisodeResponse
import com.rizwansayyed.zene.data.model.SearchDataResponse
import com.rizwansayyed.zene.data.model.UserPlaylistResponse
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.data.model.ZeneMusicDataList
import com.rizwansayyed.zene.datastore.DataStorageManager.musicPlayerDB
import com.rizwansayyed.zene.datastore.model.MusicPlayerData
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.utils.share.MediaContentUtils.startMedia
import com.rizwansayyed.zene.widgets.likedsongs.LikedMediaWidgets
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
import kotlin.collections.set
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class PlayerViewModel @Inject constructor(private val zeneAPI: ZeneAPIInterface) : ViewModel() {

    var videoSimilarVideos by mutableStateOf<ResponseResult<Pair<String, ZeneMusicDataList>>>(
        ResponseResult.Empty
    )

    var itemAddedToPlaylists = mutableStateMapOf<String, Boolean>()
    var createPlaylist by mutableStateOf<ResponseResult<NewPlaylistResponse>>(ResponseResult.Empty)
    var playerLyrics by mutableStateOf<ResponseResult<PlayerLyricsInfoResponse>>(ResponseResult.Empty)
    var playerPodcastInfo by mutableStateOf<ResponseResult<PodcastEpisodeResponse>>(ResponseResult.Empty)
    var playerRadioInfo by mutableStateOf<ResponseResult<PlayerRadioResponse>>(ResponseResult.Empty)
    var similarSongs by mutableStateOf<ResponseResult<SearchDataResponse>>(ResponseResult.Empty)
    var similarPodcast by mutableStateOf<ResponseResult<ZeneMusicDataList>>(ResponseResult.Empty)
    var similarRadio by mutableStateOf<ResponseResult<ZeneMusicDataList>>(ResponseResult.Empty)
    var similarAIMusic by mutableStateOf<ResponseResult<ZeneMusicDataList>>(ResponseResult.Empty)
    var videoForSongs by mutableStateOf<ResponseResult<PlayerVideoForSongsResponse>>(ResponseResult.Empty)
    var checksPlaylistsSongLists = mutableListOf<UserPlaylistResponse>()
    var checksPlaylistsSongListsLoading by mutableStateOf(false)
    private var lyricsJob by mutableStateOf<Job?>(null)
    var isItemLiked = mutableStateMapOf<String?, LikeItemType>()

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

    fun likedMediaItem(id: String?, type: MusicDataTypes) = viewModelScope.launch(Dispatchers.IO) {
        if (isItemLiked.contains(id)) return@launch

        zeneAPI.likedStatus(id, type).onStart {
            isItemLiked[id] = LikeItemType.LOADING
        }.catch {
            isItemLiked[id] = LikeItemType.NONE
        }.collectLatest {
            isItemLiked[id] = if (it.isLiked == true) LikeItemType.LIKE else LikeItemType.NONE
        }
    }

    fun likeAItem(data: ZeneMusicData?, doLike: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        isItemLiked[data?.id] = if (doLike) LikeItemType.LIKE else LikeItemType.NONE

        zeneAPI.addRemoveLikeItem(data, doLike).catch {
            LikedMediaWidgets().updateAll(context)
        }.collectLatest {
            LikedMediaWidgets().updateAll(context)
        }
    }

    private var playlistSongCheckJob: Job? = null
    private var playlistPage: Int = 0

    fun clearPlaylistInfo() {
        playlistSongCheckJob?.cancel()
        playlistPage = 0
        itemAddedToPlaylists.clear()
        checksPlaylistsSongLists.clear()
    }

    fun playlistSongCheckList(songId: String) {
        playlistSongCheckJob?.cancel()
        playlistSongCheckJob = viewModelScope.launch(Dispatchers.IO) {
            if (playlistPage == 0) {
                itemAddedToPlaylists.clear()
                checksPlaylistsSongLists.clear()
            }

            zeneAPI.playlistSongCheck(songId, playlistPage).onStart {
                checksPlaylistsSongListsLoading = true
            }.catch {
                checksPlaylistsSongListsLoading = false
            }.collectLatest {
                checksPlaylistsSongListsLoading = false
                checksPlaylistsSongLists.addAll(it)
                playlistPage += 1
            }
        }
    }

    fun addMediaToPlaylist(id: String, state: Boolean, info: ZeneMusicData?) =
        viewModelScope.launch(Dispatchers.IO) {
            itemAddedToPlaylists[id] = state
            zeneAPI.addItemToPlaylists(info, id, state).catch { }.collectLatest {}
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

    fun similarArtistsAlbumOfSong(
        id: String, name: String?, artists: String?, response: (ZeneMusicData) -> Unit
    ) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.similarArtistsAlbumOfSong(id, name, artists).onStart {}.catch {}.collectLatest {
            response(it)
        }
    }


    fun playerPodcastInfo(id: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.playerPodcastInfo(id).onStart {
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

    fun playerVideoForSongs(data: ZeneMusicData?) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.playerVideoForSongs(data).onStart {
            videoForSongs = ResponseResult.Loading
        }.catch {
            videoForSongs = ResponseResult.Error(it)
        }.collectLatest {
            videoForSongs = ResponseResult.Success(it)
        }
    }

    fun similarPodcasts(data: ZeneMusicData?) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.similarPodcasts(data?.secId).onStart {
            similarPodcast = ResponseResult.Loading
        }.catch {
            similarPodcast = ResponseResult.Error(it)
        }.collectLatest {
            similarPodcast = ResponseResult.Success(it)
        }
    }

    fun similarRadio(data: ZeneMusicData?) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.similarRadio(data?.artists).onStart {
            similarRadio = ResponseResult.Loading
        }.catch {
            similarRadio = ResponseResult.Error(it)
        }.collectLatest {
            similarRadio = ResponseResult.Success(it)
        }
    }

    fun similarAIMusic(data: ZeneMusicData?) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.similarAISongs(data?.artists).onStart {
            similarAIMusic = ResponseResult.Loading
        }.catch {
            similarAIMusic = ResponseResult.Error(it)
        }.collectLatest {
            similarAIMusic = ResponseResult.Success(it)
        }
    }

    fun songInfoPlay(id: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.songInfo(id).onStart {}.catch {}.collectLatest {
            startMedia(it)
        }
    }

    fun radioInfoPlay(id: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.playerRadioInfo(id).onStart {}.catch {}.collectLatest {
            startMedia(it.toMusicData())
        }
    }

    fun podcastInfoPlay(id: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.playerPodcastInfo(id).onStart {}.catch {}.collectLatest {
            val artists = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(it.description, Html.FROM_HTML_MODE_LEGACY).toString()
            } else {
                Html.fromHtml(it.description).toString()
            }
            val t = it.image?.url ?: it.series?.imageURL
            val data =
                ZeneMusicData(
                    artists,
                    id,
                    it.title,
                    it.lookup,
                    t,
                    MusicDataTypes.PODCAST_AUDIO.name
                )
            startMedia(data)
        }
    }

    fun aiMusicInfoPlay(id: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.aiMusicInfo(id).onStart {}.catch {}.collectLatest {
            startMedia(it)
        }
    }
}