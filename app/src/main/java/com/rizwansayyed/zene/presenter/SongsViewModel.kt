package com.rizwansayyed.zene.presenter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import com.rizwansayyed.zene.domain.ApiInterfaceImpl
import com.rizwansayyed.zene.presenter.model.MusicAlbumsItem
import com.rizwansayyed.zene.presenter.model.MusicsHeader
import com.rizwansayyed.zene.presenter.model.MusicHeaderAlbumResponse
import com.rizwansayyed.zene.presenter.model.MusicsAlbum
import com.rizwansayyed.zene.utils.DateTime.isOlderNeedCache
import com.rizwansayyed.zene.utils.Utils.moshi
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

}