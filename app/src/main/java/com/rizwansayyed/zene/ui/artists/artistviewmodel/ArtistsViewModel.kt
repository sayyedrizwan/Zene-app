package com.rizwansayyed.zene.ui.artists.artistviewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.domain.ApiInterfaceImpl
import com.rizwansayyed.zene.presenter.model.TopArtistsResponseApi
import com.rizwansayyed.zene.ui.artists.model.ArtistsAlbumsData
import com.rizwansayyed.zene.ui.artists.model.ArtistsSongsData
import com.rizwansayyed.zene.utils.Utils.clearUrlForArtistsInfo
import com.rizwansayyed.zene.utils.Utils.showToast
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

    var artistsTopSongs = mutableStateOf<List<ArtistsSongsData>>(emptyList())
        private set

    var artistsAllTimeSongs = mutableStateOf<TopArtistsResponseApi>(emptyList())
        private set

    var artistsTopAlbums = mutableStateOf<List<ArtistsAlbumsData>>(emptyList())
        private set

    var artistsSimilar = mutableStateOf<TopArtistsResponseApi>(emptyList())
        private set


    fun toDefault() {
        topInfo.value = null
        artistsImages.value = emptyList()
        artistsTopSongs.value = emptyList()
        artistsAllTimeSongs.value = emptyList()
        artistsTopAlbums.value = emptyList()
        artistsSimilar.value = emptyList()
    }

    fun searchArtists(artists: String) = viewModelScope.launch(Dispatchers.IO) {

        apiImpl.searchSongs("${artists}+latest+songs").catch { }.collectLatest {
            artistsAllTimeSongs.value = it
        }

        apiImpl.similarArtists(artists).catch { }.collectLatest {
            artistsSimilar.value = it
        }

        apiImpl.artistsData(artists).catch {}.collectLatest {
            if (it.url?.isNotEmpty() == true) {
                val url = clearUrlForArtistsInfo(it.url)

                artistsBio(url)
                artistsImage(url)
                artistsTopSongs(url)
                artistsTopAlbums(url)
            } else
                Log.d("TAG", "searchArtists: Error")

        }
    }

    private fun artistsBio(url: String) = viewModelScope.launch(Dispatchers.IO) {
        val data = try {
            artistsDataJsoup.artistsBio(url).first()
        } catch (e: Exception) {
            null
        }

        topInfo.value = data
    }


    private fun artistsImage(url: String) = viewModelScope.launch(Dispatchers.IO) {
        val data = try {
            artistsDataJsoup.artistsImages(url).first()
        } catch (e: Exception) {
            null
        }

        if (data != null) {
            artistsImages.value = data
        }
    }


    private fun artistsTopSongs(url: String) = viewModelScope.launch(Dispatchers.IO) {
        val data = try {
            artistsDataJsoup.artistsTopSongs(url).first()
        } catch (e: Exception) {
            null
        }

        if (data != null) {
            artistsTopSongs.value = data
        }
    }

    private fun artistsTopAlbums(url: String) = viewModelScope.launch(Dispatchers.IO) {
        val data = try {
            artistsDataJsoup.artistsTopAlbums(url).first()
        } catch (e: Exception) {
            null
        }

        if (data != null) {
            artistsTopAlbums.value = data
        }
    }
}