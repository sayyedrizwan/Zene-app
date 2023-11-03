package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.onlinesongs.lastfm.implementation.LastFMImplInterface
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ArtistsViewModel @Inject constructor(private val lastFMImpl: LastFMImplInterface) :
    ViewModel() {

    var artistsImages by mutableStateOf<DataResponse<List<String>>>(DataResponse.Empty)
        private set


    fun init(a: String) = viewModelScope.launch(Dispatchers.IO) {
        searchImg(a)
    }


    private fun searchImg(a: String) = viewModelScope.launch(Dispatchers.IO) {
        lastFMImpl.artistsImages(a, 80).onStart {
            artistsImages = DataResponse.Loading
        }.catch {
            it.message?.toast()
            artistsImages = DataResponse.Error(it)
        }.collectLatest {
            artistsImages = DataResponse.Success(it)
        }
    }

}