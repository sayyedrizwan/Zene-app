package com.rizwansayyed.zene.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.BaseApplication
import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import com.rizwansayyed.zene.api.ApiInterfaceImpl
import com.rizwansayyed.zene.utils.DateTime.isOlderNeedCache
import com.rizwansayyed.zene.utils.Utils.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
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

        apiImpl.albumsWithHeaders().catch {}.collectLatest {
            dataStoreManager.albumHeaderData = flowOf(it)
            dataStoreManager.albumHeaderTimestamp = flowOf(System.currentTimeMillis())
        }
    }
}