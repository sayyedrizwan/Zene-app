package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import com.rizwansayyed.zene.data.db.impl.RoomDBInterface
import com.rizwansayyed.zene.data.db.offlinedownload.OfflineDownloadedEntity
import com.rizwansayyed.zene.data.db.savedplaylist.playlist.SavedPlaylistEntity
import com.rizwansayyed.zene.data.db.savedplaylist.playlistsongs.PlaylistSongsEntity
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.subtitles.SubtitlesScrapsImplInterface
import com.rizwansayyed.zene.data.onlinesongs.lastfm.implementation.LastFMImplInterface
import com.rizwansayyed.zene.data.onlinesongs.pinterest.implementation.PinterestAPIImplInterface
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicPlayerData
import com.rizwansayyed.zene.domain.MusicPlayerList
import com.rizwansayyed.zene.domain.lastfm.ArtistsShortInfo
import com.rizwansayyed.zene.domain.subtitles.GeniusLyricsWithInfo
import com.rizwansayyed.zene.domain.yt.MerchandiseItems
import com.rizwansayyed.zene.domain.yt.PlayerVideoDetailsData
import com.rizwansayyed.zene.domain.yt.RetryItems
import com.rizwansayyed.zene.presenter.ui.musicplayer.utils.Utils
import com.rizwansayyed.zene.presenter.ui.musicplayer.utils.Utils.areSongNamesEqual
import com.rizwansayyed.zene.service.workmanager.OfflineDownloadManager.Companion.songDownloadPath
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val subtitlesScraps: SubtitlesScrapsImplInterface,
    private val youtubeAPI: YoutubeAPIImplInterface,
    private val roomDb: RoomDBInterface,
    private val lastFMImpl: LastFMImplInterface,
    private val pinterestAPI: PinterestAPIImplInterface
) : ViewModel() {

    var lyricsInfo by mutableStateOf<GeniusLyricsWithInfo?>(null)
        private set

    var relatedSongs by mutableStateOf<DataResponse<List<MusicData>>>(DataResponse.Empty)
        private set

    var artistsMerchandise by mutableStateOf<DataResponse<List<MerchandiseItems>>>(DataResponse.Empty)
        private set

    var videoSongs by mutableStateOf<DataResponse<PlayerVideoDetailsData>>(DataResponse.Empty)
        private set

    var musicImages by mutableStateOf<DataResponse<List<String>>>(DataResponse.Empty)
        private set

    var offlineSongStatus by mutableStateOf<Flow<OfflineDownloadedEntity?>>(flowOf(null))
        private set

    var addingPlaylist by mutableStateOf<DataResponse<Boolean>>(DataResponse.Empty)
        private set

    var artistsInfo = mutableStateListOf<ArtistsShortInfo>()
        private set

    val playlistLists = mutableStateListOf<SavedPlaylistEntity>()
    var playlistSongsInfo by mutableStateOf<Flow<PlaylistSongsEntity?>>(flowOf(null))

    var showMusicType by mutableStateOf(Utils.MusicViewType.MUSIC)

    fun init(data: MusicPlayerData) = viewModelScope.launch(Dispatchers.IO) {
        relatedSongs = DataResponse.Empty
        songArtistsInfo(data.v?.artists?.split(",", "&") ?: emptyList())
        songsImages(data.v)
        showMusicType = Utils.MusicViewType.MUSIC
        videoSongs = DataResponse.Empty

        val d = musicPlayerData.first()
        musicPlayerData = flowOf(d?.apply { songID = data.v?.songID ?: "" })
    }

    fun setMusicType(t: Utils.MusicViewType) {
        showMusicType = t
    }

    fun searchLyrics(d: MusicPlayerData) = viewModelScope.launch(Dispatchers.IO) {
        if (lyricsInfo?.songId == d.v?.songID) return@launch
        d.v?.let { data ->
            subtitlesScraps.searchSubtitles(data).onStart {
                lyricsInfo = null
            }.catch {
                lyricsInfo = GeniusLyricsWithInfo(d.v?.songID ?: "", "", "", false)
            }.collectLatest {
                lyricsInfo = it
            }
        }
    }

    private fun songArtistsInfo(artists: List<String>) = viewModelScope.launch(Dispatchers.IO) {
        artistsInfo.clear()

        artists.forEach { a ->
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val info = lastFMImpl.artistsUsername(a).first() ?: return@launch
                    val desc = lastFMImpl.artistsDescription(info).first()
                    val data = ArtistsShortInfo(a.lowercase(), info, desc)
                    artistsInfo.add(data)
                } catch (e: Exception) {
                    e.message
                }
                if (isActive) cancel()
            }
        }
    }

    fun similarSongsArtists(id: String) = viewModelScope.launch(Dispatchers.IO) {
        youtubeAPI.songsSuggestionsForUsers(listOf(id)).onStart {
            relatedSongs = DataResponse.Loading
        }.catch {
            relatedSongs = DataResponse.Error(it)
        }.collectLatest {
            val list = (it.related + it.next).shuffled()
            relatedSongs = DataResponse.Success(list)
        }
    }


    private var artistsRetry = RetryItems("", 0)
    private fun artistsMerchandise(vId: String?) = viewModelScope.launch(Dispatchers.IO) {
        youtubeAPI.artistsShopMerchandise(vId ?: "").onStart {
            artistsMerchandise = DataResponse.Loading
        }.catch {
            artistsMerchandise = DataResponse.Error(it)
        }.collectLatest {
            if (it.isEmpty() && artistsRetry.retry!! <= 2 && artistsRetry.extra == vId) {
                reArtistsMerchandise(vId)
            }
            artistsMerchandise = DataResponse.Success(it)
        }
    }

    private fun reArtistsMerchandise(vId: String?) {
        artistsRetry = artistsRetry.apply { artistsRetry.retry = artistsRetry.retry?.plus(1) }
        artistsMerchandise(vId)
    }

    fun searchLyricsAndSongVideo(name: String?, artist: String?) =
        viewModelScope.launch(Dispatchers.IO) {
            videoSongs = DataResponse.Loading
            artistsMerchandise = DataResponse.Loading

            var searchQuery = "${name?.replace("(", "")?.replace(")", "")} - ${
                artist?.substringBefore(",")?.substringBefore("&")
            }"

            val videoList = try {
                youtubeAPI.youtubeVideoSearch(searchQuery).first()
            } catch (e: Exception) {
                null
            }
            var videoId: String? = null
            videoList?.forEachIndexed { i, musicData ->
                if (i == 0) if (areSongNamesEqual(musicData.name ?: "", searchQuery))
                    videoId = musicData.pId
            }
            artistsRetry = RetryItems(videoId, 0)
            artistsMerchandise(videoId)

            searchQuery += "Lyrics Video"

            val lyricsList = try {
                youtubeAPI.youtubeVideoSearch(searchQuery).first()
            } catch (e: Exception) {
                null
            }

            var lyricsVideoId: String? = null
            lyricsList?.forEachIndexed { i, musicData ->
                if (i <= 2 && lyricsVideoId == null)
                    if (areSongNamesEqual(musicData.name ?: "", searchQuery))
                        lyricsVideoId = musicData.pId
            }

            videoSongs = DataResponse.Success(PlayerVideoDetailsData(videoId, lyricsVideoId))
        }


    fun addOfflineSong(player: MusicPlayerList) = viewModelScope.launch(Dispatchers.IO) {
        if (roomDb.offlineSongInfo(player.songID ?: "").first() != null) return@launch

        val offline = OfflineDownloadedEntity(
            player.songID ?: "",
            player.songName ?: "",
            player.artists ?: "",
            player.thumbnail ?: "",
            "", System.currentTimeMillis(), 0
        )

        roomDb.addOfflineSongDownload(offline).collect()
    }


    fun addOfflineSong(player: MusicData) = viewModelScope.launch(Dispatchers.IO) {
        if (roomDb.offlineSongInfo(player.pId ?: "").first() != null) return@launch

        val offline = OfflineDownloadedEntity(
            player.pId ?: "",
            player.name ?: "",
            player.artists ?: "",
            player.thumbnail ?: "",
            "", System.currentTimeMillis(), 0
        )

        roomDb.addOfflineSongDownload(offline).collect()
    }


    fun offlineSongDetails(songId: String) = viewModelScope.launch(Dispatchers.IO) {
        roomDb.offlineSongInfoFlow(songId).catch { }.collectLatest {
            offlineSongStatus = it
        }
    }

    fun rmDownloadSongs(songId: String) = viewModelScope.launch(Dispatchers.IO) {
        roomDb.removeSong(songId).catch { }.collectLatest {
            File(songDownloadPath, "$songId.mp3").deleteRecursively()
            offlineSongDetails(songId)
        }
    }

    private var pageNumber = 0
    fun allPlaylists(doReset: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        if (doReset) {
            pageNumber = 0
            playlistLists.clear()
            delay(1.seconds)
        }
        roomDb.allPlaylists(pageNumber * 50).catch { }.collectLatest {
            pageNumber += 1
            it.forEach { i ->
                playlistLists.add(i)
            }

        }
    }

    fun addPlaylist(name: String) = viewModelScope.launch(Dispatchers.IO) {
        if (roomDb.playlistWithName(name.trim().lowercase()).first().isNotEmpty()) {
            addingPlaylist = DataResponse.Success(false)
            return@launch
        }
        val n = SavedPlaylistEntity(null, name)
        roomDb.insert(n).catch {
            addingPlaylist = DataResponse.Error(it)
        }.collectLatest {
            addingPlaylist = DataResponse.Success(true)
            delay(1.seconds)
            addingPlaylist = DataResponse.Empty
        }
    }

    fun playlistSongsInfo(songId: String) = viewModelScope.launch(Dispatchers.IO) {
        roomDb.playlistSongInfo(songId).onStart {
            playlistSongsInfo = flowOf(null)
        }.catch {
            playlistSongsInfo = flowOf(null)
        }.collectLatest {
            playlistSongsInfo = it
        }
    }

    private fun songsImages(v: MusicPlayerList?) = viewModelScope.launch(Dispatchers.IO) {
        pinterestAPI.search(v?.songName ?: "", v?.artists ?: "").onStart {
            musicImages = DataResponse.Loading
        }.catch {
            musicImages = DataResponse.Error(it)
        }.collectLatest {
            musicImages = DataResponse.Success(it)
        }
    }


    fun addRmSongToPlaylist(
        v: MusicPlayerList, doRemove: Boolean,
        playlistId: String, savedPlaylist: SavedPlaylistEntity?
    ) = viewModelScope.launch(Dispatchers.IO) {
        try {
            var info = roomDb.songInfo(v.songID ?: "").first()

            if (doRemove) {
                info?.addedPlaylistIds =
                    info?.addedPlaylistIds?.replace(playlistId, "")?.trim() ?: ""

                if (info?.addedPlaylistIds?.trim()?.isEmpty() == true)
                    roomDb.rmSongs(v.songID ?: "").collect()
                else
                    info?.let { roomDb.insert(it).collect() }

            } else {
                if (info == null) {
                    info = PlaylistSongsEntity(
                        v.songID ?: "", playlistId, v.songName, v.artists,
                        v.thumbnail, System.currentTimeMillis()
                    )
                    roomDb.insert(info).collect()
                } else {
                    info.addedPlaylistIds = "${info.addedPlaylistIds} $playlistId"
                    roomDb.insert(info).collect()
                }
                savedPlaylist?.thumbnail = v.thumbnail
                savedPlaylist?.let { roomDb.insert(it).collect() }
            }
        } catch (e: Exception) {
            e.message
        }
        delay(1.seconds)
        playlistSongsInfo(v.songID ?: "")
    }

}