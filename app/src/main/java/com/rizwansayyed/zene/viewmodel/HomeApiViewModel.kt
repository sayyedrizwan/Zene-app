package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.userIpDetails
import com.rizwansayyed.zene.data.onlinesongs.ip.implementation.IpJsonImplInterface
import com.rizwansayyed.zene.data.onlinesongs.lastfm.implementation.LastFMImplInterface
import com.rizwansayyed.zene.data.onlinesongs.radio.implementation.OnlineRadioImplInterface
import com.rizwansayyed.zene.data.onlinesongs.spotify.implementation.SpotifyAPIImplInterface
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.OnlineRadioResponse
import com.rizwansayyed.zene.domain.lastfm.TopRecentPlaySongsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class HomeApiViewModel @Inject constructor(
    private val onlineRadiosAPI: OnlineRadioImplInterface,
    private val ip: IpJsonImplInterface,
    private val spotifyAPI: SpotifyAPIImplInterface,
    private val youtubeAPI: YoutubeAPIImplInterface,
    private val lastFMAPI: LastFMImplInterface,
) : ViewModel() {

    fun init() = viewModelScope.launch(Dispatchers.IO) {
        currentMostPlayingSong()

        try {
            userIpDetails = flowOf(ip.ip().first())
        } catch (e: Exception) {
            e.message
        }

        delay(2.seconds)
        onlineRadiosInCity()
        globalTrendingSongs()
        countryTrendingSongs()
        newReleaseMusic()
    }

    var onlineRadio by mutableStateOf<DataResponse<OnlineRadioResponse>>(DataResponse.Empty)
        private set


    var topArtistsList = mutableStateListOf<MusicData>()
        private set

    var topGlobalTrendingSongs by mutableStateOf<DataResponse<List<MusicData?>>>(DataResponse.Empty)
        private set

    var topCountryTrendingSongs by mutableStateOf<DataResponse<List<MusicData?>>>(DataResponse.Empty)
        private set


    var freshAddedSongs by mutableStateOf<List<MusicData?>>(emptyList())
        private set

    var topCountryArtists by mutableStateOf<DataResponse<List<MusicData>?>>(DataResponse.Empty)
        private set


    var topArtistsSelect by mutableStateOf<DataResponse<List<MusicData>?>>(DataResponse.Empty)
        private set


    var mostPlayingSong by mutableStateOf<DataResponse<List<MusicData>?>>(DataResponse.Empty)
        private set


    private fun onlineRadiosInCity() = viewModelScope.launch(Dispatchers.IO) {
        onlineRadiosAPI.onlineRadioSearch(false).onStart {
            onlineRadio = DataResponse.Loading
        }.catch {
            onlineRadio = DataResponse.Error(it)
        }.collectLatest {
            onlineRadio = DataResponse.Success(it)
        }
    }

    private fun globalTrendingSongs() = viewModelScope.launch(Dispatchers.IO) {
        spotifyAPI.globalTrendingSongs().onStart {
            topGlobalTrendingSongs = DataResponse.Loading
        }.catch {
            topGlobalTrendingSongs = DataResponse.Error(it)
        }.collectLatest {
            topGlobalTrendingSongs = DataResponse.Success(it)
        }
    }

    private fun countryTrendingSongs() = viewModelScope.launch(Dispatchers.IO) {
        spotifyAPI.topSongsInCountry().onStart {
            topCountryTrendingSongs = DataResponse.Loading
        }.catch {
            topCountryTrendingSongs = DataResponse.Error(it)
        }.collectLatest {
            topArtists(it)
            topCountryTrendingSongs = DataResponse.Success(it)
        }
    }

    private fun topArtists(artists: List<MusicData>) = viewModelScope.launch(Dispatchers.IO) {
        youtubeAPI.artistsInfo(artists).onStart {
            topCountryArtists = DataResponse.Loading
        }.catch {
            topCountryArtists = DataResponse.Error(it)
        }.collectLatest {
            topArtistsList.addAll(it)
            topCountryArtists = DataResponse.Success(it)
        }
    }

    fun topArtistsSelects() = viewModelScope.launch(Dispatchers.IO) {
        val ip = userIpDetails.first()
        topArtistsSelect = DataResponse.Loading
        topArtistsSelect = try {
            val b = youtubeAPI.searchArtistsInfo("top 100").first()
            val c = youtubeAPI.searchArtistsInfo("top ${ip?.country}").first()
            val h = youtubeAPI.searchArtistsInfo("top hollywood").first()
            val l = ArrayList<MusicData>(c + h + b)
            l.random()
            DataResponse.Success(l)
        } catch (e: Exception) {
            DataResponse.Error(e)
        }
    }

    fun topArtistsSearch(artists: String) = viewModelScope.launch(Dispatchers.IO) {
        youtubeAPI.searchArtistsInfo(artists).catch {}.collectLatest {
            topArtistsSelect = DataResponse.Success(it)
        }
    }

    private fun newReleaseMusic() = viewModelScope.launch(Dispatchers.IO) {
        youtubeAPI.newReleaseMusic().catch {}.collectLatest {
            freshAddedSongs = it
        }
    }

    private fun currentMostPlayingSong() = viewModelScope.launch(Dispatchers.IO) {
        lastFMAPI.topRecentPlayingSongs().onStart {
            mostPlayingSong = DataResponse.Loading
        }.catch {
            mostPlayingSong = DataResponse.Error(it)
        }.collectLatest {
            mostPlayingSong = DataResponse.Success(it)
        }
    }
}