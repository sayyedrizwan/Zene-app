package com.rizwansayyed.zene.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.userIpDetails
import com.rizwansayyed.zene.data.onlinesongs.downloader.implementation.SongDownloaderInterface
import com.rizwansayyed.zene.data.onlinesongs.giphy.implementation.GiphyImplInterface
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.bing.BingScrapsInterface
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.songkick.SongKickScrapsImplInterface
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.youtubescrap.YoutubeScrapInterface
import com.rizwansayyed.zene.data.onlinesongs.lastfm.implementation.LastFMImplInterface
import com.rizwansayyed.zene.data.onlinesongs.news.implementation.GoogleNewsInterface
import com.rizwansayyed.zene.data.onlinesongs.pinterest.implementation.PinterestAPIImplInterface
import com.rizwansayyed.zene.data.onlinesongs.soundcloud.implementation.SoundCloudImplInterface
import com.rizwansayyed.zene.data.onlinesongs.spotify.music.implementation.SpotifyAPIImpl
import com.rizwansayyed.zene.data.onlinesongs.spotify.music.implementation.SpotifyAPIImplInterface
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.domain.ArtistsEvents
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.SearchData
import com.rizwansayyed.zene.domain.lastfm.LastFMArtist
import com.rizwansayyed.zene.domain.news.GoogleNewsResponse
import com.rizwansayyed.zene.domain.soundcloud.SoundCloudProfileInfo
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.service.player.utils.Utils.addAllPlayer
import com.rizwansayyed.zene.utils.Utils.printStack
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MoodViewModel @Inject constructor(
    private val giphy: GiphyImplInterface,
    private val youtube: YoutubeAPIImplInterface,
    private val spotify: SpotifyAPIImplInterface,
) : ViewModel() {

    var gifMood by mutableStateOf<DataResponse<String?>>(DataResponse.Empty)
        private set

    var songMood by mutableStateOf<DataResponse<List<MusicData>>>(DataResponse.Empty)
        private set

    var topSongs by mutableStateOf<DataResponse<List<List<MusicData>>>>(DataResponse.Empty)
        private set


    fun init(a: String) = viewModelScope.launch(Dispatchers.IO) {
        gifOfMood(a)
        searchMoodPlaylists(a)
        moodTopSongs(a)
    }


    private fun gifOfMood(q: String) = viewModelScope.launch(Dispatchers.IO) {
        giphy.search(q).onStart {
            gifMood = DataResponse.Loading
        }.catch {
            gifMood = DataResponse.Empty
        }.collectLatest {
            gifMood = DataResponse.Success(it)
        }
    }


    private fun searchMoodPlaylists(q: String) = viewModelScope.launch(Dispatchers.IO) {
        songMood = DataResponse.Loading
        songMood = try {
            val ip = userIpDetails.firstOrNull()
            val country = youtube.searchMoodPlaylists("$q mood songs ${ip?.country}").firstOrNull()
            val hollywood = youtube.searchMoodPlaylists("$q mood songs hollywood").firstOrNull()
            val list = ArrayList<MusicData>()
            country?.toTypedArray()?.let { list.addAll(it) }
            hollywood?.toTypedArray()?.let { list.addAll(it) }
            DataResponse.Success(list.shuffled())
        } catch (e: Exception) {
            DataResponse.Error(e)
        }
    }


    private fun moodTopSongs(q: String) = viewModelScope.launch(Dispatchers.IO) {
        spotify.searchTopSongsMoodPlaylists(q).onStart {
            topSongs = DataResponse.Loading
        }.catch {
            topSongs = DataResponse.Error(it)
        }.collectLatest {
            topSongs = DataResponse.Success(it.chunked(4))
        }
    }
}