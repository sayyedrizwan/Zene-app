package com.rizwansayyed.zene.ui.artists.artistviewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.domain.ApiInterfaceImpl
import com.rizwansayyed.zene.presenter.model.ArtistsInstagramPostResponse
import com.rizwansayyed.zene.presenter.model.ArtistsTwitterInfoResponse
import com.rizwansayyed.zene.presenter.model.SocialMediaCombine
import com.rizwansayyed.zene.presenter.model.TopArtistsResponseApi
import com.rizwansayyed.zene.presenter.model.TopArtistsSongs
import com.rizwansayyed.zene.ui.artists.artistviewmodel.model.ListenersNumberValue
import com.rizwansayyed.zene.ui.artists.artistviewmodel.model.NewsResponse
import com.rizwansayyed.zene.ui.artists.model.ArtistsAlbumsData
import com.rizwansayyed.zene.ui.artists.model.ArtistsSongsData
import com.rizwansayyed.zene.utils.Utils.clearUrlForArtistsInfo
import com.rizwansayyed.zene.utils.Utils.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


@HiltViewModel
class ArtistsViewModel @Inject constructor(
    private val apiImpl: ApiInterfaceImpl,
    private val artistsDataJsoup: ArtistsDataJsoup
) : ViewModel() {


    var artistName by mutableStateOf("")
        private set

    var bio by mutableStateOf<String?>(null)
        private set

    var listeners by mutableStateOf<ListenersNumberValue?>(null)
        private set

    var artistsImages by mutableStateOf<List<String>>(emptyList())
        private set

    var artistsMainImages by mutableStateOf("")
        private set

    var artistsTopSongs by mutableStateOf<List<TopArtistsSongs>>(emptyList())
        private set

    var artistsAllTimeSongs by mutableStateOf<TopArtistsResponseApi>(emptyList())
        private set

    var artistsTopAlbums by mutableStateOf<List<ArtistsAlbumsData>>(emptyList())
        private set

    var artistsSimilar by mutableStateOf<List<TopArtistsSongs>>(emptyList())
        private set


    var artistsInstagramPosts by mutableStateOf<SocialMediaCombine?>(null)
        private set

    var artistsNews by mutableStateOf<List<NewsResponse>>(emptyList())
        private set


    var readNewsList by mutableStateOf<List<NewsResponse>>(emptyList())
        private set


    var artistsTwitterInfo by mutableStateOf<ArtistsTwitterInfoResponse?>(null)
        private set


    fun toDefault() {
        bio = null
        listeners = null
        artistsImages = emptyList()
        artistsTopSongs = emptyList()
        artistsAllTimeSongs = emptyList()
        artistsTopAlbums = emptyList()
        artistsSimilar = emptyList()
        artistsInstagramPosts = null
        artistsTwitterInfo = null
    }

    fun searchArtists(artists: String) = viewModelScope.launch(Dispatchers.IO) {
        artistName = artists
        apiImpl.searchSongs("${artists}+latest+songs").catch {}.collectLatest {
            artistsAllTimeSongs = it
        }

        apiImpl.artistsInstagramPosts(artists).catch {}.collectLatest {
            artistsInstagramPosts = it
        }

        artistsDataJsoup.artistsNews(artistName).catch {}.collectLatest {
            artistsNews = it
        }


        artistsDataJsoup.searchArtists(artists).catch {}.collectLatest {
            if (it?.isNotEmpty() == true) {
                val url = clearUrlForArtistsInfo(it)
                artistsListeners(url)
                similarArtists(url)
                artistsBio(url)
                artistsImage(url)
                artistsTopSongs(url)
                artistsTopAlbums(url)

            } else
                Log.d("TAG", "searchArtists: Error")

        }
    }

    private fun artistsListeners(url: String) = viewModelScope.launch(Dispatchers.IO) {
        val data = try {
            artistsDataJsoup.artistsListeners(url).first()
        } catch (e: Exception) {
            null
        }

        listeners = data
    }

    private fun similarArtists(url: String) = viewModelScope.launch(Dispatchers.IO) {
        val data = try {
            artistsDataJsoup.artistsSimilar(url).first()
        } catch (e: Exception) {
            null
        }

        data?.toList()?.let { artistsSimilar = it }
    }

    private fun artistsBio(url: String) = viewModelScope.launch(Dispatchers.IO) {
        val data = try {
            artistsDataJsoup.artistsBio(url).first()
        } catch (e: Exception) {
            null
        }
        bio = data
    }


    private fun artistsImage(url: String) = viewModelScope.launch(Dispatchers.IO) {
        val data = try {
            artistsDataJsoup.artistsImages(url).first()
        } catch (e: Exception) {
            null
        }

        if (data != null) {
            artistsImages = data
            artistsMainImages = data.random()
        }
    }


    private fun artistsTopSongs(url: String) = viewModelScope.launch(Dispatchers.IO) {
        val data = try {
            artistsDataJsoup.artistsTopSongs(url).first()
        } catch (e: Exception) {
            null
        }

        if (data != null) {
            artistsTopSongs = data
        }
    }

    private fun artistsTopAlbums(url: String) = viewModelScope.launch(Dispatchers.IO) {
        val data = try {
            artistsDataJsoup.artistsTopAlbums(url).first()
        } catch (e: Exception) {
            null
        }

        if (data != null) {
            artistsTopAlbums = data
        }
    }
}