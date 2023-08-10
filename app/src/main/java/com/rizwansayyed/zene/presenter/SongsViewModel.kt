package com.rizwansayyed.zene.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import com.rizwansayyed.zene.domain.ApiInterfaceImpl
import com.rizwansayyed.zene.utils.DateTime.is2DayOlderNeedCache
import com.rizwansayyed.zene.utils.DateTime.isOlderNeedCache
import com.rizwansayyed.zene.utils.Utils.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import javax.inject.Inject

@HiltViewModel
class SongsViewModel @Inject constructor(private val apiImpl: ApiInterfaceImpl) : ViewModel() {

    fun run() {
        albumsWithHeaders()

        topWeekArtists()
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

}