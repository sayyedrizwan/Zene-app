package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.jsoupscrap.JsoupScrapTopArtistsTopArtistsImpl
import com.rizwansayyed.zene.domain.TopArtistsResult
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JsoupScrapViewModel @Inject constructor(private val jsoupScrap: JsoupScrapTopArtistsTopArtistsImpl) :
    ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            topArtistsList()
        }
    }

    var showGlobalArtistInfo by mutableStateOf(true)
        private set

    var topArtists by mutableStateOf<DataResponse<List<TopArtistsResult>>>(DataResponse.Empty)
        private set

    private fun topArtistsList() = viewModelScope.launch(Dispatchers.IO) {
        jsoupScrap.topArtistsOfWeeks().onStart {
            topArtists = DataResponse.Loading
        }.catch {
            showGlobalArtistInfo = false
            topArtists = DataResponse.Error(it)
        }.collectLatest {
            topArtists = DataResponse.Success(it)
        }
    }
}