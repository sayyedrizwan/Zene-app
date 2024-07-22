package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.api.model.ZeneArtistsInfoResponse
import com.rizwansayyed.zene.data.api.model.ZeneMoodPlaylistData
import com.rizwansayyed.zene.data.api.zene.ZeneAPIInterface
import com.rizwansayyed.zene.ui.login.flow.LoginFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ZeneViewModel @Inject constructor(private val zeneAPI: ZeneAPIInterface) : ViewModel() {

    var artistsInfo by mutableStateOf<APIResponse<ZeneArtistsInfoResponse>>(APIResponse.Empty)

    fun artistsInfo(name: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.artistsInfo(name).onStart {
            artistsInfo = APIResponse.Loading
        }.catch {
            artistsInfo = APIResponse.Error(it)
        }.collectLatest {
            artistsInfo = APIResponse.Success(it)
        }
    }
}