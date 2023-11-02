package com.rizwansayyed.zene.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.favouriteRadioList
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.searchHistoryList
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.userIpDetails
import com.rizwansayyed.zene.data.onlinesongs.ip.implementation.IpJsonImplInterface
import com.rizwansayyed.zene.data.onlinesongs.lastfm.implementation.LastFMImplInterface
import com.rizwansayyed.zene.data.onlinesongs.radio.implementation.OnlineRadioImplInterface
import com.rizwansayyed.zene.data.onlinesongs.spotify.implementation.SpotifyAPIImplInterface
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.data.utils.CacheFiles
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicDataWithArtists
import com.rizwansayyed.zene.domain.OnlineRadioResponse
import com.rizwansayyed.zene.domain.SearchData
import com.rizwansayyed.zene.domain.lastfm.TopRecentPlaySongsResponse
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
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
        favouriteRadios(false)
        fun runs() {
            onlineRadiosInCity()
            globalTrendingSongs()
            countryTrendingSongs()
            newReleaseMusic()
        }

        suspend fun runIp() {
            ip.ip().catch {
                runs()
            }.collectLatest {
                userIpDetails = flowOf(it)
                delay(1.seconds)
                runs()
            }
        }

        try {
            if (userIpDetails.first()?.query?.trim() == ip.awsIp().first().trim())
                runs()
            else
                runIp()
        } catch (e: Exception) {
            runIp()
        }
    }

    var onlineRadio by mutableStateOf<DataResponse<OnlineRadioResponse>>(DataResponse.Empty)
        private set

    var onlineRadioAll by mutableStateOf<DataResponse<OnlineRadioResponse>>(DataResponse.Empty)
        private set

    var favouriteRadio by mutableStateOf<DataResponse<OnlineRadioResponse>>(DataResponse.Empty)
        private set

    var topGlobalTrendingSongs by mutableStateOf<DataResponse<List<List<MusicData?>>>>(DataResponse.Empty)
        private set

    var topCountryTrendingSongs by mutableStateOf<DataResponse<List<MusicData?>>>(DataResponse.Empty)
        private set


    var freshAddedSongs by mutableStateOf<DataResponse<List<MusicData?>>>(DataResponse.Empty)
        private set

    var topCountryArtists by mutableStateOf<DataResponse<List<MusicData>?>>(DataResponse.Empty)
        private set


    var topArtistsSelect by mutableStateOf<DataResponse<List<MusicData>?>>(DataResponse.Empty)
        private set


    var mostPlayingSong by mutableStateOf<DataResponse<List<MusicDataWithArtists>?>>(DataResponse.Empty)
        private set


    var suggestionsSearchText by mutableStateOf<DataResponse<List<MusicData>?>>(DataResponse.Empty)
        private set


    var searchData by mutableStateOf<DataResponse<SearchData?>>(DataResponse.Empty)
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

    fun onlineRadiosInCountry() = viewModelScope.launch(Dispatchers.IO) {
        onlineRadiosAPI.onlineRadioSearch(true).onStart {
            onlineRadioAll = DataResponse.Loading
        }.catch {
            onlineRadioAll = DataResponse.Error(it)
        }.collectLatest {
            onlineRadioAll = DataResponse.Success(it)
        }
    }

    fun onlineRadiosSearch(q: String) = viewModelScope.launch(Dispatchers.IO) {
        onlineRadiosAPI.searchOnlineRadio(q).onStart {
            onlineRadioAll = DataResponse.Loading
        }.catch {
            onlineRadioAll = DataResponse.Error(it)
        }.collectLatest {
            onlineRadioAll = DataResponse.Success(it)
        }
    }

    fun favouriteRadios(clear: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        if (clear) CacheFiles.favRadio.deleteRecursively()

        if (favouriteRadioList.first()?.isEmpty() == true) {
            favouriteRadio = DataResponse.Success(emptyList())
            return@launch
        }

        val favLists = favouriteRadioList.first()?.joinToString(",") ?: return@launch

        onlineRadiosAPI.favouriteRadioLists(favLists).onStart {
            favouriteRadio = DataResponse.Loading
        }.catch {
            favouriteRadio = DataResponse.Error(it)
        }.collectLatest {
            favouriteRadio = DataResponse.Success(it)
        }
    }


    private fun globalTrendingSongs() = viewModelScope.launch(Dispatchers.IO) {
        spotifyAPI.globalTrendingSongs().onStart {
            topGlobalTrendingSongs = DataResponse.Loading
        }.catch {
            topGlobalTrendingSongs = DataResponse.Error(it)
        }.collectLatest {
            topGlobalTrendingSongs = DataResponse.Success(it.chunked(3))
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
        youtubeAPI.newReleaseMusic().onStart {
            freshAddedSongs = DataResponse.Loading
        }.catch {
            freshAddedSongs = DataResponse.Error(it)
        }.collectLatest {
            freshAddedSongs = DataResponse.Success(it)
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

    fun searchTextSuggestions(q: String) = viewModelScope.launch(Dispatchers.IO) {
        youtubeAPI.searchTextsSuggestions(q).onStart {
            val list = ArrayList<String>()
            searchHistoryList.first()?.forEach { s ->
                if (q.trim().lowercase() != s.trim().lowercase() && list.size < 30)
                    list.add(s)
            }
            list.add(0, q)
            searchHistoryList = flowOf(list.toTypedArray())
            suggestionsSearchText = DataResponse.Loading
        }.catch {
            suggestionsSearchText = DataResponse.Error(it)
        }.collectLatest {
            suggestionsSearchText = DataResponse.Success(it)
        }
    }

    fun emptySearchText() = viewModelScope.launch(Dispatchers.IO) {
        suggestionsSearchText = DataResponse.Empty
    }


    fun searchData(q: String) = viewModelScope.launch(Dispatchers.IO) {
        youtubeAPI.searchData(q).onStart {
            searchData = DataResponse.Loading
        }.catch {
            searchData = DataResponse.Error(it)
        }.collectLatest {
            searchData = DataResponse.Success(it)
        }
    }

}