package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.db.offlinedownload.OfflineDownloadedEntity
import com.rizwansayyed.zene.data.onlinesongs.radio.OnlineRadioService
import com.rizwansayyed.zene.data.onlinesongs.radio.implementation.OnlineRadioImpl
import com.rizwansayyed.zene.data.utils.CacheFiles.radioList
import com.rizwansayyed.zene.domain.OnlineRadioResponse
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeApiViewModel @Inject constructor(private val onlineRadioImpl: OnlineRadioImpl) :
    ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            onlineRadios()
        }
    }

    var onlineRadio by mutableStateOf<OnlineRadioResponse>(emptyList())
        private set


    private fun onlineRadios() = viewModelScope.launch(Dispatchers.IO) {
        onlineRadioImpl.onlineRadioSearch().onStart {
            onlineRadio = emptyList()
        }.catch {
            radioList.deleteRecursively()
            onlineRadio = emptyList()
        }.collectLatest {
            onlineRadio = it
        }
    }

}