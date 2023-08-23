package com.rizwansayyed.zene.ui.artists.artistviewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.domain.ApiInterfaceImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ArtistsViewModel @Inject constructor(
    private val apiImpl: ApiInterfaceImpl,
    private val artistsDataJsoup: ArtistsDataJsoup
) : ViewModel() {

    var topInfo = mutableStateOf<String?>(null)
        private set

    var artistsImages = mutableStateOf<List<String>>(emptyList())
        private set

    fun toDefault() {
        topInfo.value = null
        artistsImages.value = emptyList()
    }

    fun searchArtists(artists: String) = viewModelScope.launch(Dispatchers.IO) {
        apiImpl.artistsData(artists).catch {
        }.collectLatest {
            if (it.url?.isNotEmpty() == true) {
                val url = it.url.substringBefore("?")

                artistsBio(url)
                artistsImage(url)
            } else
                Log.d("TAG", "searchArtists: Error")

        }
    }

    private fun artistsBio(url: String) = viewModelScope.launch(Dispatchers.IO) {
        val bio = try {
            artistsDataJsoup.artistsBio(url).first()
        } catch (e: Exception) {
            null
        }

        topInfo.value = bio
    }


    private fun artistsImage(url: String) = viewModelScope.launch(Dispatchers.IO) {
        val image = try {
            artistsDataJsoup.artistsImages(url).first()
        } catch (e: Exception) {
            null
        }

        if (image != null) {
            artistsImages.value = image
        }
    }
}