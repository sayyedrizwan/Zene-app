package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataResponse
import com.rizwansayyed.zene.data.api.zene.ZeneAPIInterface
import com.rizwansayyed.zene.ui.login.flow.LoginFlow
import com.rizwansayyed.zene.utils.Utils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    private val zeneAPI: ZeneAPIInterface
) : ViewModel() {

    private var songID = ""

    var lyrics by mutableStateOf<APIResponse<String>>(APIResponse.Empty)
    var similarSongs by mutableStateOf<APIResponse<ZeneMusicDataResponse>>(APIResponse.Empty)

    fun similarSongs(songId: String) = viewModelScope.launch(Dispatchers.IO) {
        if (songId == songID) return@launch

        zeneAPI.suggestedSongs(songId).onStart {
            similarSongs = APIResponse.Loading
        }.catch {
            similarSongs = APIResponse.Error(it)
        }.collectLatest {
            songID = songId
            similarSongs = APIResponse.Success(it)
        }
    }
}