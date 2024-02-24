package com.rizwansayyed.zene.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.topartistsplaylists.TopArtistsPlaylistsScrapsInterface
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class JsoupScrapViewModel @Inject constructor(private val jsoupScrap: TopArtistsPlaylistsScrapsInterface) :
    ViewModel() {

    fun init() = viewModelScope.launch(Dispatchers.IO) {
        topArtistsList()
    }

    var topArtists by mutableStateOf<DataResponse<List<MusicData>>>(DataResponse.Empty)
        private set

    private fun topArtistsList() = viewModelScope.launch(Dispatchers.IO) {
        jsoupScrap.topArtistsOfWeeks().onStart {
            topArtists = DataResponse.Loading
        }.catch {
            topArtists = DataResponse.Error(it)
        }.collectLatest {
            topArtists = DataResponse.Success(it)
        }
    }
}