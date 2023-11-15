package com.rizwansayyed.zene.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val youtubeAPI: YoutubeAPIImplInterface
) : ViewModel() {

    fun init(id: String) = viewModelScope.launch(Dispatchers.IO) {

    }
}