package com.rizwansayyed.zene.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.bing.BingScrapsInterface
import com.rizwansayyed.zene.data.onlinesongs.lastfm.implementation.LastFMImplInterface
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.domain.lastfm.ArtistsSearchResponse
import com.rizwansayyed.zene.domain.lastfm.LastFMArtist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ArtistsViewModel @Inject constructor(
    private val lastFMImpl: LastFMImplInterface,
    private val bingScraps: BingScrapsInterface
) : ViewModel() {

    var artistsImages by mutableStateOf<DataResponse<List<String>>>(DataResponse.Empty)
        private set

    var artistsDesc by mutableStateOf<DataResponse<String>>(DataResponse.Empty)
        private set

    var artistsImage by mutableStateOf<DataResponse<String>>(DataResponse.Empty)
        private set

    var artistsVideoId by mutableStateOf("")
        private set


    fun init(a: String) = viewModelScope.launch(Dispatchers.IO) {
        latestVideo(a)

        lastFMImpl.artistsUsername(a).onStart {
            artistsImage = DataResponse.Loading
        }.catch {
            artistsImage = DataResponse.Error(it)
        }.collectLatest {
            artistsImage = DataResponse.Success(it?.image ?: "")
            it?.let { i ->
                searchImg(i)
                artistsDesc(i)
            }
        }
    }

    private fun searchImg(a: LastFMArtist) = viewModelScope.launch(Dispatchers.IO) {
        lastFMImpl.artistsImages(a, 80).onStart {
            artistsImages = DataResponse.Loading
        }.catch {
            artistsImages = DataResponse.Error(it)
        }.collectLatest {
            artistsImages = DataResponse.Success(it)
        }
    }

    private fun artistsDesc(a: LastFMArtist) = viewModelScope.launch(Dispatchers.IO) {
        lastFMImpl.artistsDescription(a).onStart {
            artistsDesc = DataResponse.Loading
        }.catch {
            artistsDesc = DataResponse.Error(it)
        }.collectLatest {
            artistsDesc = DataResponse.Success(it)
        }
    }

    private fun latestVideo(a: String) = viewModelScope.launch(Dispatchers.IO) {
        bingScraps.bingOfficialVideo(a).onStart {
            artistsVideoId = ""
        }.catch {
            artistsVideoId = ""
        }.collectLatest {
            artistsVideoId = it
        }
    }

}