package com.rizwansayyed.zene.viewmodel

import android.util.Log
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
import com.rizwansayyed.zene.utils.safeLaunch
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
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
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

    fun similarVideos(id: String) = viewModelScope.safeLaunch  {
        when (val v = videoSimilarVideos) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> {}
            is ResponseResult.Success -> {
                if (v.data.first == id && v.data.second.isNotEmpty()) return@safeLaunch
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
        viewModelScope.safeLaunch  {
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

    fun likedMediaItem(id: String?, type: MusicDataTypes) = viewModelScope.safeLaunch  {
        if (isItemLiked.contains(id)) return@safeLaunch

        zeneAPI.likedStatus(id, type).onStart {
            isItemLiked[id] = LikeItemType.LOADING
        }.catch {
            isItemLiked[id] = LikeItemType.NONE
        }.collectLatest {
            isItemLiked[id] = if (it.isLiked == true) LikeItemType.LIKE else LikeItemType.NONE
        }
    }

    fun likeAItem(data: ZeneMusicData?, doLike: Boolean) = viewModelScope.safeLaunch  {
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
        playlistSongCheckJob = viewModelScope.safeLaunch {
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
        viewModelScope.safeLaunch  {
            itemAddedToPlaylists[id] = state
            zeneAPI.addItemToPlaylists(info, id, state).catch { }.collectLatest {}
        }

    fun getSongLyrics() {
        var p: MusicPlayerData? = null
        playerLyrics = ResponseResult.Loading
        lyricsJob?.cancel()
        lyricsJob = viewModelScope.safeLaunch  {
            try {
                withTimeout(15.seconds) {
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
            } catch (e: CancellationException) {
                playerLyrics = ResponseResult.Error(e)
            } catch (e: Exception) {
                playerLyrics = ResponseResult.Error(e)
            }
        }
    }

    fun getAISongLyrics(id: String) = viewModelScope.safeLaunch  {
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
    ) = viewModelScope.safeLaunch  {
        zeneAPI.similarArtistsAlbumOfSong(id, name, artists).onStart {}.catch {}.collectLatest {
            response(it)
        }
    }


    fun playerPodcastInfo(id: String) = viewModelScope.safeLaunch  {
        zeneAPI.playerPodcastInfo(id).onStart {
            playerPodcastInfo = ResponseResult.Loading
        }.catch {
            playerPodcastInfo = ResponseResult.Error(it)
        }.collectLatest {
            playerPodcastInfo = ResponseResult.Success(it)
        }
    }

    fun playerRadioInfo(id: String) = viewModelScope.safeLaunch  {
        zeneAPI.playerRadioInfo(id).onStart {
            playerRadioInfo = ResponseResult.Loading
        }.catch {
            playerRadioInfo = ResponseResult.Error(it)
        }.collectLatest {
            playerRadioInfo = ResponseResult.Success(it)
        }
    }

    fun playerSimilarSongs(id: String?) = viewModelScope.safeLaunch  {
        id ?: return@safeLaunch
        zeneAPI.similarSongs(id).onStart {
            similarSongs = ResponseResult.Loading
        }.catch {
            similarSongs = ResponseResult.Error(it)
        }.collectLatest {
            similarSongs = ResponseResult.Success(it)
        }
    }

    fun playerVideoForSongs(data: ZeneMusicData?) = viewModelScope.safeLaunch  {
        zeneAPI.playerVideoForSongs(data).onStart {
            videoForSongs = ResponseResult.Loading
        }.catch {
            videoForSongs = ResponseResult.Error(it)
        }.collectLatest {
            videoForSongs = ResponseResult.Success(it)
        }
    }

    fun similarPodcasts(data: ZeneMusicData?) = viewModelScope.safeLaunch  {
        zeneAPI.similarPodcasts(data?.secId).onStart {
            similarPodcast = ResponseResult.Loading
        }.catch {
            similarPodcast = ResponseResult.Error(it)
        }.collectLatest {
            similarPodcast = ResponseResult.Success(it)
        }
    }

    fun similarRadio(data: ZeneMusicData?) = viewModelScope.safeLaunch  {
        zeneAPI.similarRadio(data?.artists).onStart {
            similarRadio = ResponseResult.Loading
        }.catch {
            similarRadio = ResponseResult.Error(it)
        }.collectLatest {
            similarRadio = ResponseResult.Success(it)
        }
    }

    fun similarAIMusic(data: ZeneMusicData?) = viewModelScope.safeLaunch  {
        zeneAPI.similarAISongs(data?.artists).onStart {
            similarAIMusic = ResponseResult.Loading
        }.catch {
            similarAIMusic = ResponseResult.Error(it)
        }.collectLatest {
            similarAIMusic = ResponseResult.Success(it)
        }
    }

    fun songInfoPlay(id: String) = viewModelScope.safeLaunch  {
        zeneAPI.songInfo(id).onStart {}.catch {}.collectLatest {
            startMedia(it)
        }
    }

    fun radioInfoPlay(id: String) = viewModelScope.safeLaunch  {
        zeneAPI.playerRadioInfo(id).onStart {}.catch {}.collectLatest {
            startMedia(it.toMusicData())
        }
    }

    fun podcastInfoPlay(id: String) = viewModelScope.safeLaunch  {
        zeneAPI.playerPodcastInfo(id).onStart {}.catch {}.collectLatest {
            startMedia(it.getAsMusicData())
        }
    }

    fun aiMusicInfoPlay(id: String) = viewModelScope.safeLaunch  {
        zeneAPI.aiMusicInfo(id).onStart {}.catch {}.collectLatest {
            startMedia(it)
        }
    }
}