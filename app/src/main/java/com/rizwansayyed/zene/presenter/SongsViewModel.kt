package com.rizwansayyed.zene.presenter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import com.rizwansayyed.zene.domain.ApiInterfaceImpl
import com.rizwansayyed.zene.domain.model.toLocal
import com.rizwansayyed.zene.domain.roomdb.RoomDBImpl
import com.rizwansayyed.zene.domain.roomdb.recentplayed.RecentPlayedEntity
import com.rizwansayyed.zene.service.musicplayer.MediaPlayerObjects
import com.rizwansayyed.zene.service.musicplayer.MediaPlayerService.Companion.isMusicPlayerServiceIsRunning
import com.rizwansayyed.zene.service.musicplayer.MediaPlayerService.Companion.startMedaPlayerService
import com.rizwansayyed.zene.utils.DateTime.is1DayOlderNeedCache
import com.rizwansayyed.zene.utils.DateTime.is2DayOlderNeedCache
import com.rizwansayyed.zene.utils.DateTime.is5DayOlderNeedCache
import com.rizwansayyed.zene.utils.DateTime.isOlderNeedCache
import com.rizwansayyed.zene.utils.Utils.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SongsViewModel @Inject constructor(
    private val apiImpl: ApiInterfaceImpl,
    private val roomDBImpl: RoomDBImpl,
    private val mediaPlayerObjects: MediaPlayerObjects
) : ViewModel() {

    private var footerDataTried = 0

    fun run() {
        footerDataTried = 0
        recentPlaySongs()

        viewModelScope.launch(Dispatchers.IO) {
            albumsWithHeaders()
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
            trendingSongsTop50()
            trendingSongsTopKPop()
            trendingSongsTop50KPop()
            similarArtistsSuggestionsForYou()
            topArtistsSongs()
            songsForYouAll()
        }
    }

    private fun albumsWithHeaders() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.albumHeaderTimestamp.first().isOlderNeedCache() &&
            dataStoreManager.albumHeaderData.first() != null &&
            dataStoreManager.albumHeaderData.first()?.isNotEmpty() == true
        ) return@launch

        val channelUrl = try {
            apiImpl.albumsWithHeaders().first().url
        } catch (e: Exception) {
            ""
        }
        if (channelUrl.isEmpty()) return@launch

        apiImpl.albumsWithYTHeaders(channelUrl).catch {}.collectLatest {
            dataStoreManager.albumHeaderTimestamp = flowOf(System.currentTimeMillis())
            it.header?.let { h ->
                dataStoreManager.albumHeaderData = flowOf(h.toTypedArray())
            }
            it.albums?.let { a ->
                dataStoreManager.footerAlbumsData = flowOf(a.toTypedArray())
            }

            if (it.albums?.isEmpty() == true && footerDataTried < 3) {
                footerDataTried += 1
                delay(3.seconds)
                reRunFooter()
            }
        }
    }

    private fun reRunFooter() {
        albumsWithHeaders()
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

    private fun topCountrySong() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.topCountrySongsTimestamp.first().isOlderNeedCache() &&
            dataStoreManager.topCountrySongsData.first() != null &&
            dataStoreManager.topCountrySongsData.first()?.isNotEmpty() == true
        ) return@launch

        apiImpl.topCountrySongs().catch {}.collectLatest {
            dataStoreManager.topCountrySongsTimestamp = flowOf(System.currentTimeMillis())
            dataStoreManager.topCountrySongsData = flowOf(it.toTypedArray())
        }
    }


    private fun trendingSongsTop50() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.trendingSongsTop50Timestamp.first().is2DayOlderNeedCache() &&
            dataStoreManager.trendingSongsTop50Data.first() != null &&
            dataStoreManager.trendingSongsTop50Data.first()?.isNotEmpty() == true
        ) return@launch

        apiImpl.trendingSongsTop50().catch {}.collectLatest {
            dataStoreManager.trendingSongsTop50Timestamp = flowOf(System.currentTimeMillis())
            it.shuffled()
            dataStoreManager.trendingSongsTop50Data = flowOf(it.toTypedArray())
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


    private fun trendingSongsTopKPop() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.trendingSongsTopKPopTimestamp.first().is2DayOlderNeedCache() &&
            dataStoreManager.trendingSongsTopKPopData.first() != null &&
            dataStoreManager.trendingSongsTopKPopData.first()?.isNotEmpty() == true
        ) return@launch

        apiImpl.trendingSongsTopKPop().catch {}.collectLatest {
            dataStoreManager.trendingSongsTopKPopTimestamp = flowOf(System.currentTimeMillis())
            dataStoreManager.trendingSongsTopKPopData = flowOf(it.toTypedArray())
        }
    }


    private fun trendingSongsTop50KPop() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.trendingSongsTop50KPopTimestamp.first().is2DayOlderNeedCache() &&
            dataStoreManager.trendingSongsTop50KPopData.first() != null &&
            dataStoreManager.trendingSongsTop50KPopData.first()?.isNotEmpty() == true
        ) {
            val s = dataStoreManager.trendingSongsTop50KPopData.first()
            s?.shuffle()
            s?.shuffle()
            s?.shuffle()
            dataStoreManager.trendingSongsTop50KPopData = flowOf(s)
            return@launch
        }

        apiImpl.trendingSongsTop50KPop().catch {}.collectLatest {
            dataStoreManager.trendingSongsTop50KPopTimestamp = flowOf(System.currentTimeMillis())
            dataStoreManager.trendingSongsTop50KPopData = flowOf(it.toTypedArray())
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

    fun songsPlayingDetails(name: String, artists: String) = viewModelScope.launch(Dispatchers.IO) {
        if (!isMusicPlayerServiceIsRunning()) {
            startMedaPlayerService()
        }

        val songs = roomDBImpl.recentPlayedHome(name, artists).first()
        if (songs.isNotEmpty()) {
            if (!songs.first().timestamp.is2DayOlderNeedCache()) {

                val song = songs.first()
                val url = mediaPlayerObjects.mediaAudioPaths(song.songID)

                val mediaDetails = mediaPlayerObjects.mediaItems(
                    song.songID, url, song.name, song.artists, song.thumbnail
                )
                mediaPlayerObjects.playSong(mediaDetails, true)
                return@launch
            }
        }

        val searchName = "$name - $artists".lowercase()
        apiImpl.songPlayDetails(searchName).catch {}.collectLatest {
            roomDBImpl.removeSongDetails(it.songID ?: "").collect()
            it.toLocal()?.let { d -> roomDBImpl.insert(d).collect() }

            val url = mediaPlayerObjects.mediaAudioPaths(it.songID ?: "")

            val mediaDetails = mediaPlayerObjects.mediaItems(
                it.songID, url, it.songName, it.artistName, it.thumbnail
            )
            mediaPlayerObjects.playSong(mediaDetails, true)
        }
    }
}