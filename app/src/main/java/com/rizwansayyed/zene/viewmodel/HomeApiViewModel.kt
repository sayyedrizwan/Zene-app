package com.rizwansayyed.zene.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.onlinesongs.radio.implementation.OnlineRadioImplInterface
import com.rizwansayyed.zene.data.onlinesongs.spotify.implementation.SpotifyAPIImplInterface
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.OnlineRadioResponse
import com.rizwansayyed.zene.domain.spotify.SpotifyItem
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.measureTimeMillis

@HiltViewModel
class HomeApiViewModel @Inject constructor(
    private val onlineRadiosAPI: OnlineRadioImplInterface,
    private val spotifyAPI: SpotifyAPIImplInterface,
    private val youtubeAPI: YoutubeAPIImplInterface,
) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            onlineRadiosInCity()
            globalTrendingSongs()
            countryTrendingSongs()
            newReleaseMusic()
        }
    }

    var onlineRadio by mutableStateOf<DataResponse<OnlineRadioResponse>>(DataResponse.Empty)
        private set


    var topGlobalTrendingSongs by mutableStateOf<DataResponse<List<MusicData?>>>(DataResponse.Empty)
        private set

    var topCountryTrendingSongs by mutableStateOf<DataResponse<List<MusicData?>>>(DataResponse.Empty)
        private set


    var freshAddedSongs by mutableStateOf<List<MusicData?>>(emptyList())
        private set

    var topCountryArtists by mutableStateOf<List<String>>(emptyList())
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
        val start = System.currentTimeMillis()
        spotifyAPI.topSongsInCountry().onStart {
            topCountryTrendingSongs = DataResponse.Loading
        }.catch {
            topCountryTrendingSongs = DataResponse.Error(it)
        }.collectLatest {
            topArtists(it)
            topCountryTrendingSongs = DataResponse.Success(it)
        }
    }

    private fun topArtists(items: List<MusicData>) = viewModelScope.launch(Dispatchers.IO) {
        val lists = mutableListOf<String>()

        items.forEach { a ->
            for (a in a.artists?.split(",", "&")!!) {
                lists.add(a)
            }
        }
        val newList = ArrayList(lists.subList(0, lists.size / 2))
        lists.clear()
        lists.addAll(newList)
        lists.shuffle()
        newList.clear()
        topCountryArtists = lists
    }

    private fun newReleaseMusic() = viewModelScope.launch(Dispatchers.IO) {
        youtubeAPI.newReleaseMusic().catch {}.collectLatest {
            freshAddedSongs = it
        }
    }

}