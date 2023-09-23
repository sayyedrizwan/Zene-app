package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.db.offlinedownload.OfflineDownloadedEntity
import com.rizwansayyed.zene.data.onlinesongs.radio.OnlineRadioService
import com.rizwansayyed.zene.data.onlinesongs.radio.implementation.OnlineRadioImpl
import com.rizwansayyed.zene.data.onlinesongs.spotify.implementation.SpotifyAPIImpl
import com.rizwansayyed.zene.data.utils.CacheFiles.radioList
import com.rizwansayyed.zene.domain.OnlineRadioResponse
import com.rizwansayyed.zene.domain.spotify.SpotifyItem
import com.rizwansayyed.zene.domain.spotify.SpotifyPlaylistSongsResponse
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeApiViewModel @Inject constructor(
    private val onlineRadioImpl: OnlineRadioImpl,
    private val spotifyAPIImpl: SpotifyAPIImpl
) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            onlineRadiosInCity()
            globalTrendingSongs()
            countryTrendingSongs()
        }
    }

    var onlineRadio by mutableStateOf<DataResponse<OnlineRadioResponse>>(DataResponse.Empty)
        private set


    var topGlobalTrendingSongs by mutableStateOf<DataResponse<List<SpotifyItem?>>>(DataResponse.Empty)
        private set

    var topCountryTrendingSongs by mutableStateOf<DataResponse<List<SpotifyItem?>>>(DataResponse.Empty)
        private set


    private fun onlineRadiosInCity() = viewModelScope.launch(Dispatchers.IO) {
        onlineRadioImpl.onlineRadioSearch(false).onStart {
            onlineRadio = DataResponse.Loading
        }.catch {
            onlineRadio = DataResponse.Error(it)
        }.collectLatest {
            onlineRadio = DataResponse.Success(it)
        }
    }

    private fun globalTrendingSongs() = viewModelScope.launch(Dispatchers.IO) {
        spotifyAPIImpl.globalTrendingSongs().onStart {
            topGlobalTrendingSongs = DataResponse.Loading
        }.catch {
            topGlobalTrendingSongs = DataResponse.Error(it)
        }.collectLatest {
            topGlobalTrendingSongs = DataResponse.Success(it ?: emptyList())
        }
    }

    private fun countryTrendingSongs() = viewModelScope.launch(Dispatchers.IO) {
        spotifyAPIImpl.topSongsInCountry().onStart {
            topCountryTrendingSongs = DataResponse.Loading
        }.catch {
            topCountryTrendingSongs = DataResponse.Error(it)
        }.collectLatest {
            topCountryTrendingSongs = DataResponse.Success(it ?: emptyList())
        }
    }

}