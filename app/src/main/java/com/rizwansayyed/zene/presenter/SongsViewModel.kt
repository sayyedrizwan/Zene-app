package com.rizwansayyed.zene.presenter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import com.rizwansayyed.zene.domain.ApiInterfaceImpl
import com.rizwansayyed.zene.roomdb.RoomDBImpl
import com.rizwansayyed.zene.roomdb.recentplayed.RecentPlayedEntity
import com.rizwansayyed.zene.utils.DateTime.is1DayOlderNeedCache
import com.rizwansayyed.zene.utils.DateTime.is2DayOlderNeedCache
import com.rizwansayyed.zene.utils.DateTime.isOlderNeedCache
import com.rizwansayyed.zene.utils.Utils.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import javax.inject.Inject

@HiltViewModel
class SongsViewModel @Inject constructor(
    private val apiImpl: ApiInterfaceImpl,
    private val roomDBImpl: RoomDBImpl
) : ViewModel() {

    fun run() {
        albumsWithHeaders()
        topWeekArtists()
        topGlobalSongsThisWeek()
        recentPlaySongs()
        topCountrySong()
        songsSuggestions()
        trendingSongsTop50()
    }

    private fun albumsWithHeaders() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.albumHeaderTimestamp.first().isOlderNeedCache() &&
            dataStoreManager.albumHeaderData.first() != null
        ) return@launch

        val channelUrl = try {
            apiImpl.albumsWithHeaders().first().url
        } catch (e: Exception) {
            ""
        }
        if (channelUrl.isEmpty()) return@launch

        apiImpl.albumsWithYTHeaders(channelUrl).catch {}.collectLatest {
            dataStoreManager.albumHeaderTimestamp = flowOf(System.currentTimeMillis())
            dataStoreManager.albumHeaderData = flowOf(it)
        }
    }

    private fun topWeekArtists() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.topArtistsOfWeekTimestamp.first().is2DayOlderNeedCache() &&
            dataStoreManager.topArtistsOfWeekData.first() != null
        ) return@launch

        apiImpl.topArtistOfWeek().catch {}.collectLatest {
            dataStoreManager.topArtistsOfWeekTimestamp = flowOf(System.currentTimeMillis())
            dataStoreManager.topArtistsOfWeekData = flowOf(it.toTypedArray())
        }
    }

    private fun topGlobalSongsThisWeek() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.topGlobalSongsTimestamp.first().is1DayOlderNeedCache() &&
            dataStoreManager.topGlobalSongsData.first() != null
        ) return@launch

        apiImpl.topGlobalSongsThisWeek().catch {}.collectLatest {
            dataStoreManager.topGlobalSongsTimestamp = flowOf(System.currentTimeMillis())
            dataStoreManager.topGlobalSongsData = flowOf(it.toTypedArray())
        }
    }

    var recentPlayedSongs by mutableStateOf<Flow<List<RecentPlayedEntity>>?>(null)
        private set

    private fun recentPlaySongs() = viewModelScope.launch(Dispatchers.IO) {
        roomDBImpl.recentPlayed().catch {}.collectLatest {
            recentPlayedSongs = it
        }
    }

    private fun topCountrySong() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.topCountrySongsTimestamp.first().isOlderNeedCache() &&
            dataStoreManager.topCountrySongsData.first() != null
        ) return@launch

        apiImpl.topCountrySongs().catch {}.collectLatest {
            dataStoreManager.topCountrySongsTimestamp = flowOf(System.currentTimeMillis())
            dataStoreManager.topCountrySongsData = flowOf(it.toTypedArray())
        }
    }


    private fun trendingSongsTop50() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.trendingSongsTop50Timestamp.first().is2DayOlderNeedCache() &&
            dataStoreManager.trendingSongsTop50Data.first() != null
        ) return@launch

        apiImpl.trendingSongsTop50().catch {}.collectLatest {
            dataStoreManager.trendingSongsTop50Timestamp = flowOf(System.currentTimeMillis())
            dataStoreManager.trendingSongsTop50Data = flowOf(it.toTypedArray())
        }
    }


    private fun songsSuggestions() = viewModelScope.launch(Dispatchers.IO) {
        if (!dataStoreManager.songsSuggestionsTimestamp.first().is1DayOlderNeedCache() &&
            dataStoreManager.songsSuggestionsData.first() != null
        ) return@launch

        roomDBImpl.songsSuggestionsUsingSongsHistory().catch {}.collectLatest {
            dataStoreManager.songsSuggestionsTimestamp = flowOf(System.currentTimeMillis())
            dataStoreManager.songsSuggestionsData = flowOf(it.toTypedArray())
        }
    }


    fun insert() = viewModelScope.launch(Dispatchers.IO) {
        val insert = RecentPlayedEntity(
            id = null,
            name = "Last Night",
            "Morgan Wallen",
            1,
            "sfJDnua1cB4",
            "https://charts-static.billboard.com/img/2016/08/morgan-wallen-nlu-344x344.jpg",
            System.currentTimeMillis(),
            233
        )
        roomDBImpl.insert(insert).catch { }.collect()
    }
}