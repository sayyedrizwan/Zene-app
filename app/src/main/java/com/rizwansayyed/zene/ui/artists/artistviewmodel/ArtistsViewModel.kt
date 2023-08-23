package com.rizwansayyed.zene.ui.artists.artistviewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.domain.ApiInterfaceImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistsViewModel @Inject constructor(
    private val apiImpl: ApiInterfaceImpl,
    private val artistsDataJsoup: ArtistsDataJsoup
) : ViewModel() {

    fun searchArtists(artists: String) = viewModelScope.launch(Dispatchers.IO) {
        apiImpl.artistsData(artists).catch { }.collectLatest {
            if (it.url?.isNotEmpty() == true) {
                artistsDataJsoup.artistsBio()
            } else {
                Log.d("TAG", "searchArtists: Error")
            }
        }
    }
}