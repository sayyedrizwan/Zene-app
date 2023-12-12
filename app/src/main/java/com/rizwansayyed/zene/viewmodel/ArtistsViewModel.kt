package com.rizwansayyed.zene.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.onlinesongs.downloader.implementation.SongDownloaderInterface
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.bing.BingScrapsInterface
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.songkick.SongKickScrapsImplInterface
import com.rizwansayyed.zene.data.onlinesongs.lastfm.implementation.LastFMImplInterface
import com.rizwansayyed.zene.data.onlinesongs.news.implementation.GoogleNewsInterface
import com.rizwansayyed.zene.data.onlinesongs.pinterest.implementation.PinterestAPIImplInterface
import com.rizwansayyed.zene.data.onlinesongs.soundcloud.implementation.SoundCloudImplInterface
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.domain.ArtistsEvents
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.SearchData
import com.rizwansayyed.zene.domain.lastfm.LastFMArtist
import com.rizwansayyed.zene.domain.news.GoogleNewsResponse
import com.rizwansayyed.zene.domain.soundcloud.SoundCloudProfileInfo
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.service.player.utils.Utils.addAllPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ArtistsViewModel @Inject constructor(
    private val lastFMImpl: LastFMImplInterface,
    private val youtubeAPI: YoutubeAPIImplInterface,
    private val bingScraps: BingScrapsInterface,
    private val soundCloud: SoundCloudImplInterface,
    private val songKick: SongKickScrapsImplInterface,
    private val songDownloader: SongDownloaderInterface,
    private val googleNews: GoogleNewsInterface,
    private val pinterestAPI: PinterestAPIImplInterface
) : ViewModel() {

    var artistsImages = mutableStateListOf("loading")
        private set

    var radioStatus by mutableStateOf<DataResponse<List<String>>>(DataResponse.Empty)
        private set

    var artistsDesc by mutableStateOf<DataResponse<String>>(DataResponse.Empty)
        private set

    var artistsImage by mutableStateOf<DataResponse<String>>(DataResponse.Empty)
        private set

    var artistSocialProfile by mutableStateOf<DataResponse<SoundCloudProfileInfo>>(DataResponse.Empty)
        private set

    var artistsEvents by mutableStateOf<DataResponse<List<ArtistsEvents>?>>(DataResponse.Empty)
        private set

    var artistsTopSongs by mutableStateOf<DataResponse<List<MusicData>>>(DataResponse.Empty)
        private set

    var searchData by mutableStateOf<DataResponse<SearchData?>>(DataResponse.Empty)
        private set

    var artistsNews by mutableStateOf<DataResponse<List<List<GoogleNewsResponse.Channel.Item>>>>(
        DataResponse.Empty
    )
        private set

    var artistsVideoURL by mutableStateOf("")
        private set

    fun init(a: String) = viewModelScope.launch(Dispatchers.IO) {
        latestVideo(a)
        artistsNews(a)
        socialProfile(a)
        searchData(a)
        radioStatus = DataResponse.Empty
        lastFMImpl.artistsUsername(a).onStart {
            artistsDesc = DataResponse.Loading
            artistsImage = DataResponse.Loading
            artistsEvents = DataResponse.Loading
        }.catch {
            artistsImage = DataResponse.Error(it)
        }.collectLatest {
            artistsImage = DataResponse.Success(it?.image ?: "")
            it?.let { i ->
                searchImg(i)
                artistsDesc(i)
                artistsEvents(i)
                topSongs(i)
            }
        }
    }

    fun startArtistsRadioPlaylist(artistsName: String) = viewModelScope.launch(Dispatchers.IO) {
        if (radioStatus == DataResponse.Loading) return@launch

        youtubeAPI.searchArtistsPlaylistsForRadio(artistsName).onStart {
            radioStatus = DataResponse.Loading
        }.catch {
            radioStatus = DataResponse.Empty
        }.collectLatest {
            addAllPlayer(listOf(it).toTypedArray(), 0)
            radioStatus = DataResponse.Empty
        }
    }

    private fun searchImg(a: LastFMArtist) = viewModelScope.launch(Dispatchers.IO) {
        if (artistsImages.size > 1) artistsImages.clear()

        fun clearIfOne() {
            if (artistsImages.size == 1) artistsImages.clear()
        }

        lastFMImpl.artistsImages(a, 60).catch {
            clearIfOne()
        }.collectLatest {
            clearIfOne()
            artistsImages.addAll(it)
        }

        pinterestAPI.search(a.name ?: "").catch {
            clearIfOne()
        }.collectLatest {
            clearIfOne()
            artistsImages.addAll(it)

        }

        pinterestAPI.search("${a.name} photoshoot").catch {
            clearIfOne()
        }.collectLatest {
            clearIfOne()
            artistsImages.addAll(it)
        }

        pinterestAPI.search("${a.name} latest images").catch {
            clearIfOne()
        }.collectLatest {
            clearIfOne()
            artistsImages.addAll(it)
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
        artistsVideoURL = ""

        val vId = try {
            bingScraps.bingOfficialVideo(a).first()
        } catch (e: Exception) {
            null
        }

        if (vId == null) {
            artistsVideoURL = ""
            return@launch
        }


        songDownloader.downloadVideo(vId).catch {
            artistsVideoURL = ""
        }.collectLatest {
            artistsVideoURL = it ?: ""
        }
    }

    private fun socialProfile(a: String) = viewModelScope.launch(Dispatchers.IO) {
        soundCloud.artistsProfileDetails(a).onStart {
            artistSocialProfile = DataResponse.Loading
        }.catch {
            artistSocialProfile = DataResponse.Error(it)
        }.collectLatest {
            artistSocialProfile = DataResponse.Success(it)
        }
    }

    private fun topSongs(a: LastFMArtist) = viewModelScope.launch(Dispatchers.IO) {
        lastFMImpl.artistsTopSongs(a).onStart {
            artistsTopSongs = DataResponse.Loading
        }.catch {
            artistsTopSongs = DataResponse.Error(it)
        }.collectLatest {
            artistsTopSongs = DataResponse.Success(it)
        }
    }

    private fun artistsEvents(a: LastFMArtist) = viewModelScope.launch(Dispatchers.IO) {
        artistsEvents = DataResponse.Loading
        var list = try {
            lastFMImpl.artistsEvent(a).first()
        } catch (e: Exception) {
            null
        }

        if (list != null) {
            artistsEvents = DataResponse.Success(list)
            return@launch
        }

        if (a.name?.isEmpty() == true) {
            artistsEvents = DataResponse.Error(Exception("No Name Found"))
            return@launch
        }

        list = try {
            songKick.artistsEvents(a.name!!).first()
        } catch (e: Exception) {
            null
        }

        if (list == null) {
            artistsEvents = DataResponse.Error(Exception("No Found"))
            return@launch
        }

        artistsEvents = DataResponse.Success(list)
    }


    private fun searchData(q: String) = viewModelScope.launch(Dispatchers.IO) {
        youtubeAPI.searchData(q).onStart {
            searchData = DataResponse.Loading
        }.catch {
            searchData = DataResponse.Error(it)
        }.collectLatest {
            searchData = DataResponse.Success(it)
        }
    }

    private fun artistsNews(q: String) = viewModelScope.launch(Dispatchers.IO) {
        googleNews.searchNews(q).onStart {
            artistsNews = DataResponse.Loading
        }.catch {
            artistsNews = DataResponse.Error(it)
        }.collectLatest {
            val list = it.channel?.items?.sortedByDescending { i -> i.convertToMilliseconds() }
            artistsNews = DataResponse.Success(list?.chunked(5) ?: emptyList())
        }
    }
}