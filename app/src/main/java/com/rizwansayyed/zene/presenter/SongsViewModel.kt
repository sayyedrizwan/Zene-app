package com.rizwansayyed.zene.presenter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import com.rizwansayyed.zene.domain.ApiInterfaceImpl
import com.rizwansayyed.zene.domain.model.toLocal
import com.rizwansayyed.zene.domain.roomdb.RoomDBImpl
import com.rizwansayyed.zene.domain.roomdb.collections.items.PlaylistSongsEntity
import com.rizwansayyed.zene.domain.roomdb.collections.playlist.PlaylistEntity
import com.rizwansayyed.zene.domain.roomdb.offlinesongs.OfflineSongsEntity
import com.rizwansayyed.zene.domain.roomdb.offlinesongs.OfflineStatusTypes
import com.rizwansayyed.zene.domain.roomdb.recentplayed.RecentPlayedEntity
import com.rizwansayyed.zene.domain.roomdb.recentplayed.toRecentPlay
import com.rizwansayyed.zene.presenter.jsoup.SongsDataJsoup
import com.rizwansayyed.zene.presenter.model.ApiResponse
import com.rizwansayyed.zene.presenter.model.MusicPlayerDetails
import com.rizwansayyed.zene.presenter.model.MusicPlayerState
import com.rizwansayyed.zene.presenter.model.TopArtistsSongs
import com.rizwansayyed.zene.presenter.model.TopArtistsSongsResponse
import com.rizwansayyed.zene.presenter.model.toMusicPlayerData
import com.rizwansayyed.zene.service.musicplayer.MediaPlayerObjects
import com.rizwansayyed.zene.service.musicplayer.MediaPlayerService.Companion.isMusicPlayerServiceIsRunning
import com.rizwansayyed.zene.service.musicplayer.MediaPlayerService.Companion.startMedaPlayerService
import com.rizwansayyed.zene.service.workmanager.startDownloadSongsWorkManager
import com.rizwansayyed.zene.ui.musicplay.video.VideoPlayerResponse
import com.rizwansayyed.zene.ui.musicplay.video.VideoPlayerStatus
import com.rizwansayyed.zene.utils.DateTime.is1DayOlderNeedCache
import com.rizwansayyed.zene.utils.DateTime.is2DayOlderNeedCache
import com.rizwansayyed.zene.utils.DateTime.is5DayOlderNeedCache
import com.rizwansayyed.zene.utils.DateTime.isOlderNeedCache
import com.rizwansayyed.zene.utils.RemoteConfigReader
import com.rizwansayyed.zene.utils.Utils.ifLyricsFileExistReturn
import com.rizwansayyed.zene.utils.Utils.saveCaptionsFileTXT
import com.rizwansayyed.zene.utils.Utils.updateStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SongsViewModel @Inject constructor(
    private val apiImpl: ApiInterfaceImpl,
    private val roomDBImpl: RoomDBImpl,
    private val mediaPlayerObjects: MediaPlayerObjects,
    private val songsDataJsoup: SongsDataJsoup
) : ViewModel() {

    private var footerDataTried = 0

    fun run() {
        footerDataTried = 0
        recentPlaySongs()
        collectionsLists()
        allOfflineSongs()
        topCountrySongYT()

        viewModelScope.launch(Dispatchers.IO) {
            topWeekArtists()
            topGlobalSongsThisWeek()
        }

        viewModelScope.launch(Dispatchers.IO) {
            delay(2.seconds)
            topArtists()
            topCountrySong()
            songsSuggestions()
        }

        viewModelScope.launch(Dispatchers.IO) {
            delay(4.seconds)
            songsSuggestionsForYou()
            trendingSongsTopKPop()
            similarArtistsSuggestionsForYou()
            topArtistsSongs()
            songsForYouAll()
        }
    }

    private fun albumsWithHeaders(songs: ArrayList<TopArtistsSongs>) =
        viewModelScope.launch(Dispatchers.IO) {
            val newList = songs.take(4)
            apiImpl.songPlayDetails(newList).catch {}.collectLatest {
                dataStoreManager.albumHeaderData = flowOf(it.toTypedArray())
            }
        }

    private fun topWeekArtists() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.topArtistsOfWeekTimestamp.first().is2DayOlderNeedCache() &&
            dataStoreManager.topArtistsOfWeekData.first() != null &&
            dataStoreManager.topArtistsOfWeekData.first()?.isNotEmpty() == true
        ) return@launch

        apiImpl.topArtistOfWeek().catch {}.collectLatest {
            dataStoreManager.topArtistsOfWeekTimestamp = flowOf(System.currentTimeMillis())
            dataStoreManager.topArtistsOfWeekData = flowOf(it.toTypedArray())
        }
    }

    private fun topGlobalSongsThisWeek() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.topGlobalSongsTimestamp.first().is1DayOlderNeedCache() &&
            dataStoreManager.topGlobalSongsData.first() != null &&
            dataStoreManager.topGlobalSongsData.first()?.isNotEmpty() == true
        ) return@launch

        apiImpl.topGlobalSongsThisWeek().catch {}.collectLatest {
            dataStoreManager.topGlobalSongsTimestamp = flowOf(System.currentTimeMillis())
            dataStoreManager.topGlobalSongsData = flowOf(it.toTypedArray())
        }
    }

    var recentPlayedSongs by mutableStateOf<Flow<List<RecentPlayedEntity>>?>(null)
        private set

    private fun recentPlaySongs() = viewModelScope.launch(Dispatchers.IO) {
        roomDBImpl.recentPlayed().catch {}.collectLatest {
            recentPlayedSongs = it
        }
    }

    var topArtistsSuggestions by mutableStateOf<List<RecentPlayedEntity>?>(null)
        private set

    private fun topArtists() = viewModelScope.launch(Dispatchers.IO) {
        roomDBImpl.topArtistsSuggestions().catch {}.collectLatest {
            topArtistsSuggestions = it
        }
    }

    var searchSongs by mutableStateOf<TopArtistsSongsResponse?>(null)
        private set

    fun searchSongs(search: String) = viewModelScope.launch(Dispatchers.IO) {
        apiImpl.searchSongs(search).onStart {
            searchSongs = TopArtistsSongsResponse(emptyList(), ApiResponse.LOADING)
        }.catch {
            searchSongs = TopArtistsSongsResponse(emptyList(), ApiResponse.ERROR)
        }.collectLatest {
            searchSongs = TopArtistsSongsResponse(it, ApiResponse.SUCCESS)
        }
    }

    var searchArtists by mutableStateOf<TopArtistsSongsResponse?>(null)
        private set

    fun searchArtists(search: String) = viewModelScope.launch(Dispatchers.IO) {
        apiImpl.searchArtists(search).onStart {
            searchArtists = TopArtistsSongsResponse(emptyList(), ApiResponse.LOADING)
        }.catch {
            searchArtists = TopArtistsSongsResponse(emptyList(), ApiResponse.ERROR)
        }.collectLatest {
            searchArtists = TopArtistsSongsResponse(it, ApiResponse.SUCCESS)
        }
    }

    private fun topCountrySong() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.topCountrySongsTimestamp.first().isOlderNeedCache() &&
            dataStoreManager.topCountrySongsData.first() != null &&
            dataStoreManager.topCountrySongsData.first()?.isNotEmpty() == true
        ) {
            val s = dataStoreManager.topCountrySongsData.first()
            s?.shuffle()
            s?.shuffle()
            dataStoreManager.topCountrySongsData = flowOf(s)
            return@launch
        }

        val countryName = try {
            apiImpl.ipAddressDetails().first().country?.lowercase() ?: "united states"
        } catch (e: Exception) {
            "united states"
        }

        songsDataJsoup.appleMusicCountryTrends(countryName.lowercase()).catch { }.collectLatest {
            dataStoreManager.topCountrySongsTimestamp = flowOf(System.currentTimeMillis())
            dataStoreManager.topCountrySongsData = flowOf(it.toTypedArray())
        }
    }


    private fun topCountrySongYT() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.topCountrySongsYTTimestamp.first().isOlderNeedCache() &&
            dataStoreManager.topCountrySongsYTData.first() != null &&
            dataStoreManager.topCountrySongsYTData.first()?.isNotEmpty() == true
        ) {
            val s = dataStoreManager.topCountrySongsYTData.first()
            s?.shuffle()
            s?.shuffle()
            dataStoreManager.topCountrySongsYTData = flowOf(s)
            return@launch
        }

        val ip = try {
            apiImpl.ipAddressDetails().first().query ?: ""
        } catch (e: Exception) {
            ""
        }

        apiImpl.topTrendingSongsYT(RemoteConfigReader().getYTKey(), ip).catch {}.collectLatest {
            dataStoreManager.topCountrySongsYTTimestamp = flowOf(System.currentTimeMillis())
            dataStoreManager.topCountrySongsYTData = flowOf(it.toTypedArray())
            albumsWithHeaders(it)
        }
    }

    private fun songsSuggestions() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.songsSuggestionsTimestamp.first().is1DayOlderNeedCache() &&
            dataStoreManager.songsSuggestionsData.first() != null &&
            dataStoreManager.songsSuggestionsData.first()?.isNotEmpty() == true
        ) {
            val s = dataStoreManager.songsSuggestionsData.first()
            s?.shuffle()
            s?.shuffle()
            dataStoreManager.songsSuggestionsData = flowOf(s)
            return@launch
        }

        roomDBImpl.songsSuggestionsUsingSongsHistory().catch {}.collectLatest {
            dataStoreManager.songsSuggestionsTimestamp = flowOf(System.currentTimeMillis())
            dataStoreManager.songsSuggestionsData = flowOf(it.toTypedArray())
        }
    }

    private fun songsSuggestionsForYou() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.songsSuggestionsForYouTimestamp.first().is1DayOlderNeedCache() &&
            dataStoreManager.songsSuggestionsForYouData.first() != null &&
            dataStoreManager.songsSuggestionsForYouData.first()?.isNotEmpty() == true
        ) {
            val s = dataStoreManager.songsSuggestionsForYouData.first()
            s?.shuffle()
            s?.shuffle()
            dataStoreManager.songsSuggestionsForYouData = flowOf(s)
            return@launch
        }

        roomDBImpl.songSuggestionsForYouUsingHistory().catch {}.collectLatest {
            dataStoreManager.songsSuggestionsForYouTimestamp = flowOf(System.currentTimeMillis())
            dataStoreManager.songsSuggestionsForYouData = flowOf(it.toTypedArray())
        }
    }


    var musicOfflineSongs = mutableStateOf<Flow<List<OfflineSongsEntity>>?>(flowOf(emptyList()))
        private set


    fun offlineSongsData(pId: String) = viewModelScope.launch(Dispatchers.IO) {
        if (roomDBImpl.countOfflineSongs(pId).first() > 0) return@launch

        roomDBImpl.musicOfflineSongs(pId).catch {}.collectLatest {
            musicOfflineSongs.value = it
        }
    }

    var allOfflineSongs by mutableStateOf<Flow<List<OfflineSongsEntity>>?>(null)
        private set


    private fun allOfflineSongs() = viewModelScope.launch(Dispatchers.IO) {
        roomDBImpl.allOfflineSongs().catch {}.collectLatest {
            allOfflineSongs = it
        }
    }


    private fun trendingSongsTopKPop() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.trendingSongsTopKPopTimestamp.first().is2DayOlderNeedCache() &&
            dataStoreManager.trendingSongsTopKPopData.first() != null &&
            dataStoreManager.trendingSongsTopKPopData.first()?.isNotEmpty() == true
        ) {
            val s = dataStoreManager.trendingSongsTopKPopData.first()
            s?.shuffle()
            dataStoreManager.trendingSongsTopKPopData = flowOf(s)
            return@launch
        }


        songsDataJsoup.appleMusicKoreanSongs().catch { }.collectLatest {
            dataStoreManager.trendingSongsTopKPopTimestamp = flowOf(System.currentTimeMillis())
            dataStoreManager.trendingSongsTopKPopData = flowOf(it.toTypedArray())
        }
    }


    private fun similarArtistsSuggestionsForYou() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.artistsSuggestionsTimestamp.first().is2DayOlderNeedCache() &&
            dataStoreManager.artistsSuggestionsData.first() != null &&
            dataStoreManager.artistsSuggestionsData.first()?.isNotEmpty() == true
        ) {
            val s = dataStoreManager.artistsSuggestionsData.first()
            s?.shuffle()
            s?.shuffle()
            dataStoreManager.artistsSuggestionsData = flowOf(s)
            return@launch
        }

        roomDBImpl.artistsSuggestionsForYouUsingHistory().catch {}.collectLatest {
            dataStoreManager.artistsSuggestionsTimestamp = flowOf(System.currentTimeMillis())
            dataStoreManager.artistsSuggestionsData = flowOf(it.toTypedArray())
        }
    }


    private fun topArtistsSongs() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.topArtistsSongsDataTimestamp.first().is2DayOlderNeedCache() &&
            dataStoreManager.topArtistsSongsData.first() != null &&
            dataStoreManager.topArtistsSongsData.first()?.isNotEmpty() == true
        ) return@launch

        roomDBImpl.topArtistsSongs().catch {}.collectLatest {
            dataStoreManager.topArtistsSongsDataTimestamp = flowOf(System.currentTimeMillis())
            dataStoreManager.topArtistsSongsData = flowOf(it.toTypedArray())
        }
    }

    private fun songsForYouAll() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.songsAllForYouAllTimestamp.first().is5DayOlderNeedCache() &&
            dataStoreManager.songsAllForYouAllData.first() != null &&
            dataStoreManager.songsAllForYouAllData.first()?.isNotEmpty() == true
        ) {
            val s = dataStoreManager.songsAllForYouAllData.first()
            s?.shuffle()
            s?.shuffle()
            dataStoreManager.songsAllForYouAllData = flowOf(s)
            return@launch
        }

        roomDBImpl.allSongsForYouSongs().catch {}.collectLatest {
            dataStoreManager.songsAllForYouAllTimestamp = flowOf(System.currentTimeMillis())
            it.shuffle()
            it.shuffle()
            it.shuffle()
            dataStoreManager.songsAllForYouAllData = flowOf(it.toTypedArray())
        }
    }

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    fun songsPlayingOffline(songs: OfflineSongsEntity) = viewModelScope.launch(Dispatchers.IO) {
        if (!isMusicPlayerServiceIsRunning()) {
            startMedaPlayerService()
        }

        dataStoreManager.musicPlayerData =
            flowOf(toMusicPlayerData(songs.img, songs.songName, songs.songArtists))

        roomDBImpl.insert(toRecentPlay(songs)).collect()

        val mediaDetails = mediaPlayerObjects.mediaItems(
            songs.pid, File(songs.songPath), songs.songName, songs.songArtists, File(songs.img)
        )
        mediaPlayerObjects.playSong(mediaDetails, true)
    }

    fun songsDeleteOffline(songs: OfflineSongsEntity) = viewModelScope.launch(Dispatchers.IO) {
        roomDBImpl.deleteOfflineSong(songs.pid).collectLatest {
            if (File(songs.img).exists()) File(songs.img).deleteRecursively()
            if (File(songs.songPath).exists()) File(songs.songPath).deleteRecursively()
        }
    }

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    fun songsPlayingDetails(thumbnail: String, name: String, artists: String) =
        viewModelScope.launch(Dispatchers.IO) {
            if (!isMusicPlayerServiceIsRunning()) {
                startMedaPlayerService()
            }

            dataStoreManager.musicPlayerData = flowOf(toMusicPlayerData(thumbnail, name, artists))


            val searchName = ("${name.lowercase().replace("official video", "")} - " +
                    artists).lowercase()

            val songs = roomDBImpl.recentPlayedHome(searchName).first()
            if (songs.isNotEmpty()) {
                if (!songs.first().timestamp.is2DayOlderNeedCache()) {
                    val s = songs.first()

                    updateStatus(s.thumbnail, s.name, s.artists, s.songID, MusicPlayerState.LOADING)

                    val url = mediaPlayerObjects.mediaAudioPaths(s.songID)

                    roomDBImpl.insert(toRecentPlay(s)).collect()

                    val mediaDetails =
                        mediaPlayerObjects.mediaItems(s.songID, url, s.name, s.artists, s.thumbnail)
                    mediaPlayerObjects.playSong(mediaDetails, true)
                    return@launch
                }
            }


            apiImpl.songPlayDetails(searchName).catch {}.collectLatest {
                if (thumbnail.isNotEmpty()) {
                    it.thumbnail = thumbnail
                }
                roomDBImpl.removeSongDetails(it.songID ?: "").collect()
                it.toLocal(searchName)?.let { d -> roomDBImpl.insert(d).collect() }

                roomDBImpl.insert(toRecentPlay(it)).collect()

                updateStatus(
                    it.thumbnail,
                    it.songName ?: "",
                    artists,
                    it.songID ?: "",
                    MusicPlayerState.LOADING
                )

                val url = mediaPlayerObjects.mediaAudioPaths(it.songID ?: "")

                val mediaDetails = mediaPlayerObjects.mediaItems(
                    it.songID, url, it.songName, artists, it.thumbnail
                )
                mediaPlayerObjects.playSong(mediaDetails, true)
            }
        }

    var videoPlayingDetails by mutableStateOf(VideoPlayerResponse())
        private set

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    fun videoPlayingDetails(q: String) = viewModelScope.launch(Dispatchers.IO) {
        apiImpl.videoPlayDetails(q).onStart {
            videoPlayingDetails = VideoPlayerResponse(VideoPlayerStatus.LOADING)
        }.catch {
            videoPlayingDetails = VideoPlayerResponse(VideoPlayerStatus.ERROR, null)
        }.collectLatest {
            if (it.videoID == null) return@collectLatest
            videoPlayingDetails = VideoPlayerResponse(VideoPlayerStatus.SUCCESS, it.videoID)
        }
    }


    var videoLyricsDetails by mutableStateOf(VideoPlayerResponse())
        private set


    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    fun readLyrics(q: String, pID: String) = viewModelScope.launch(Dispatchers.IO) {
        videoLyricsDetails = VideoPlayerResponse(VideoPlayerStatus.LOADING, null)

        if (ifLyricsFileExistReturn("${q}_$pID").length > 10) {
            videoLyricsDetails =
                VideoPlayerResponse(VideoPlayerStatus.SUCCESS, ifLyricsFileExistReturn("${q}_$pID"))
            return@launch
        }

        val lyricsS = try {
            songsDataJsoup.searchLyrics(q).first()
        } catch (e: Exception) {
            ""
        }

        if (lyricsS.isNotEmpty()) {
            saveCaptionsFileTXT("${q}_$pID", lyricsS)
            videoLyricsDetails = VideoPlayerResponse(VideoPlayerStatus.SUCCESS, lyricsS)
            return@launch
        }


        val lyricsAZ = try {
            songsDataJsoup.searchLyricsAZ(q).first()
        } catch (e: Exception) {
            ""
        }

        videoLyricsDetails = if (lyricsAZ.isEmpty())
            VideoPlayerResponse(VideoPlayerStatus.ERROR, null)
        else {
            saveCaptionsFileTXT("${q}_$pID", lyricsAZ)
            VideoPlayerResponse(VideoPlayerStatus.SUCCESS, lyricsAZ)
        }
    }


    fun insertOfflineSongs(music: MusicPlayerDetails) = viewModelScope.launch(Dispatchers.IO) {
        val offlineSongsEntity = OfflineSongsEntity(
            null,
            music.songName!!,
            music.artists!!,
            music.pId!!,
            music.thumbnail!!,
            "",
            System.currentTimeMillis(),
            OfflineStatusTypes.DOWNLOADING,
            music.duration!!
        )

        roomDBImpl.insert(offlineSongsEntity).collect()

        delay(1.seconds)

        startDownloadSongsWorkManager()
    }


    var isSongInPlaylist by mutableIntStateOf(0)
        private set


    fun isSongInPlaylist(pID: String) = viewModelScope.launch(Dispatchers.IO) {
        isSongInPlaylist = 0
        roomDBImpl.isSongsAlreadyAvailable(pID).catch { }.collectLatest {
            isSongInPlaylist = it
        }
    }

    fun removeSongPlaylist(pID: String) = viewModelScope.launch(Dispatchers.IO) {
        roomDBImpl.deleteSongs(pID).catch { }.collectLatest {
            isSongInPlaylist(pID)
        }
    }


    var playlists by mutableStateOf<Flow<List<PlaylistEntity>>>(flowOf(emptyList()))
        private set


    private fun collectionsLists() = viewModelScope.launch(Dispatchers.IO) {
        roomDBImpl.allPlaylist().catch { }.collectLatest {
            playlists = it
        }
    }

    fun insertPlaylist(name: String, isAdd: (Boolean) -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
            val count = try {
                roomDBImpl.playlists(name.lowercase()).first().size
            } catch (e: Exception) {
                0
            }

            if (count > 0) {
                isAdd(false)
                return@launch
            }

            val p = PlaylistEntity(null, name)

            roomDBImpl.playlists(p).catch { }.collectLatest {
                isAdd(true)
            }
        }

    fun insertSongToPlaylist(music: MusicPlayerDetails, playlist: PlaylistEntity) =
        viewModelScope.launch(Dispatchers.IO) {

            if (roomDBImpl.isSongsAlreadyAvailable(music.pId ?: "").first() > 0) return@launch

            val p = PlaylistSongsEntity(
                null,
                playlist.id!!,
                music.songName!!,
                System.currentTimeMillis(),
                music.thumbnail!!,
                music.artists!!,
                music.pId!!
            )

            roomDBImpl.playlistItem(p).catch { }.collectLatest {
                val playlists = roomDBImpl.latest4playlistsItem(playlist.id).first()
                if (playlists.size > 4) {
                    roomDBImpl.playlistsWithId(playlist.id).first().forEach {
                        it.image1 = music.thumbnail
                        it.image2 = playlists[1].thumbnail
                        it.image3 = playlists[2].thumbnail
                        it.image4 = playlists[3].thumbnail
                        roomDBImpl.playlists(it).collect()
                    }
                } else if (playlists.isNotEmpty()) {
                    roomDBImpl.playlistsWithId(playlist.id).first().forEach {
                        it.image1 = music.thumbnail
                        roomDBImpl.playlists(it).collect()
                    }
                }

                isSongInPlaylist(music.pId!!)
            }
        }
}