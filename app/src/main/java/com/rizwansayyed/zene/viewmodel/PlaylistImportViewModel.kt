package com.rizwansayyed.zene.viewmodel


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import com.rizwansayyed.zene.data.db.impl.RoomDBInterface
import com.rizwansayyed.zene.data.db.savedplaylist.playlistsongs.PlaylistSongsEntity
import com.rizwansayyed.zene.data.onlinesongs.applemusic.implementation.AppleMusicAPIImplInterface
import com.rizwansayyed.zene.data.onlinesongs.spotify.users.implementation.SpotifyUsersAPIImplInterface
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.userplaylist.YoutubeMusicPlaylistImplInterface
import com.rizwansayyed.zene.domain.ImportPlaylistInfoData
import com.rizwansayyed.zene.domain.ImportPlaylistTrackInfoData
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.toPlaylistInfo
import com.rizwansayyed.zene.domain.toPlaylists
import com.rizwansayyed.zene.domain.toTrack
import com.rizwansayyed.zene.presenter.ui.home.mymusic.playlistimport.PlaylistImportersType
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.service.player.utils.Utils.addAllPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


@HiltViewModel
class PlaylistImportViewModel @Inject constructor(
    private val spotifyUserAPI: SpotifyUsersAPIImplInterface,
    private val youtubeAPIImpl: YoutubeAPIImplInterface,
    private val youtubeAPI: YoutubeMusicPlaylistImplInterface,
    private val appleMusicAPI: AppleMusicAPIImplInterface,
    private val roomDb: RoomDBInterface
) : ViewModel() {

    private var playlistTrackJob: Job? = null

    var selectedPlaylist by mutableStateOf<ImportPlaylistInfoData?>(null)

    var songMenu by mutableStateOf<DataResponse<MusicData?>>(DataResponse.Empty)

    var playlistTrackers = mutableStateListOf<ImportPlaylistTrackInfoData>()

    var usersPlaylists by mutableStateOf<DataResponse<List<ImportPlaylistInfoData>>>(DataResponse.Empty)
        private set


    fun spotifyPlaylistInfo() = viewModelScope.launch(Dispatchers.IO) {
        spotifyUserAPI.usersPlaylist().onStart {
            usersPlaylists = DataResponse.Loading
        }.catch {
            usersPlaylists = DataResponse.Error(it)
        }.collectLatest {
            playlistTrackers.clear()

            val list = it.toPlaylistInfo(PlaylistImportersType.SPOTIFY) ?: emptyList()
            usersPlaylists = DataResponse.Success(list)

            list.forEachIndexed { i, item ->
                if (i == 0) {
                    spotifyPlaylistTrack(item)
                }
            }
        }
    }

    fun youtubeMusicPlaylistInfo() = viewModelScope.launch(Dispatchers.IO) {
        youtubeAPI.usersPlaylists().onStart {
            usersPlaylists = DataResponse.Loading
        }.catch {
            usersPlaylists = DataResponse.Error(it)
        }.collectLatest {
            usersPlaylists = DataResponse.Success(it)

            it.forEachIndexed { i, item ->
                if (i == 0) {
                    youtubeMusicPlaylistTracks(item)
                }
            }
        }
    }

    fun appleMusicPlaylistInfo() = viewModelScope.launch(Dispatchers.IO) {
        appleMusicAPI.userPlaylists().onStart {
            usersPlaylists = DataResponse.Loading
        }.catch {
            usersPlaylists = DataResponse.Error(it)
        }.collectLatest {
            val list = it?.toPlaylists() ?: emptyList()
            usersPlaylists = DataResponse.Success(list)

            list.forEachIndexed { i, item ->
                if (i == 0) {
                    appleMusicPlaylistTracks(item)
                }
            }
        }
    }

    fun appleMusicPlaylistTracks(item: ImportPlaylistInfoData) =
        viewModelScope.launch(Dispatchers.IO) {
            if (selectedPlaylist == item) return@launch
            selectedPlaylist = item

            playlistTrackers.clear()
            appleMusicAPI.playlistsTracks(item.id ?: "").catch {}.collectLatest {
                playlistTrackers.addAll(it)
            }
        }

    fun youtubeMusicPlaylistTracks(item: ImportPlaylistInfoData) =
        viewModelScope.launch(Dispatchers.IO) {
            if (selectedPlaylist == item) return@launch
            selectedPlaylist = item

            playlistTrackers.clear()
            youtubeAPI.playlistsTracker(item.id ?: "").catch {}.collectLatest {
                playlistTrackers.addAll(it)
            }
        }

    fun spotifyPlaylistTrack(item: ImportPlaylistInfoData) = viewModelScope.launch(Dispatchers.IO) {
        if (item.id == null) return@launch
        if (selectedPlaylist == item) return@launch
        selectedPlaylist = item
        playlistTrackJob?.cancel()
        playlistTrackJob = viewModelScope.launch(Dispatchers.IO) {
            playlistTrackers.clear()

            try {
                val lists = spotifyUserAPI.playlistTrack(item.id, 0).first()
                lists.items?.forEach {
                    it?.toTrack()?.let { t -> playlistTrackers.add(t) }
                }

                var page = 0

                repeat(((lists.total ?: 0) / 50)) {
                    if (page > 0) {
                        val listsOffset =
                            spotifyUserAPI.playlistTrack(item.id ?: "", page * 50).first()
                        listsOffset.items?.forEach {
                            it?.toTrack()?.let { t -> playlistTrackers.add(t) }
                        }
                    }
                    page += 1
                }

            } catch (e: Exception) {
                e.message
            }
        }
    }

    fun searchSongForPlaylist(n: String, menu: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        youtubeAPIImpl.musicInfoSearch(n).onStart {
            songMenu = DataResponse.Loading
        }.catch {
            songMenu = DataResponse.Error(it)
        }.collectLatest {
            songMenu = if (menu) DataResponse.Success(it)
            else {
                addAllPlayer(listOf(it).toTypedArray(), 0)
                DataResponse.Empty
            }
            viewModelScope.launch(Dispatchers.IO) {
                delay(3.seconds)
                val mpd = musicPlayerData.first()?.apply { show = false }
                musicPlayerData = flowOf(mpd)
            }

        }
    }

    fun clear() = viewModelScope.launch(Dispatchers.IO) {
        songMenu = DataResponse.Empty
    }


    fun addSongsToPlaylistWithInfo(v: Array<ImportPlaylistTrackInfoData>, playlist: String) =
        viewModelScope.launch(Dispatchers.IO) {
            songMenu = DataResponse.Loading
            delay(1.seconds)
            var done = 0
            val saved = roomDb.searchName(playlist.lowercase()).firstOrNull()
            if (saved != null) {
                v.forEachIndexed { i, m ->
                    viewModelScope.launch(Dispatchers.IO) {
                        val s = "${m.songName} - ${m.artistsName?.substringBefore(",")}"
                        youtubeAPIImpl.musicInfoSearch(s).catch {
                            done += 1
                            songMenu = if (done == v.size) DataResponse.Empty
                            else DataResponse.Loading

                        }.collectLatest {
                            var info = roomDb.songInfo(it?.songId ?: "").first()
                            if (info == null) {
                                info = PlaylistSongsEntity(
                                    it?.songId ?: "", "${saved.id},", it?.name, it?.artists,
                                    it?.thumbnail, System.currentTimeMillis()
                                )
                                roomDb.insert(info).collect()
                            } else {
                                info.addedPlaylistIds = "${info.addedPlaylistIds} ${saved.id},"
                                roomDb.insert(info).collect()
                            }
                            saved.items = saved.items.plus(1)
                            saved.thumbnail = it?.thumbnail
                            roomDb.insert(saved).collect()

                            done += 1
                            songMenu = if (done == v.size)
                                DataResponse.Empty
                            else
                                DataResponse.Loading
                        }
                    }
                }
            }
        }
}