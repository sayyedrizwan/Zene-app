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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val loginFlow: LoginFlow, private val zeneAPI: ZeneAPIInterface
) : ViewModel() {

    var recommendedPlaylists by mutableStateOf<APIResponse<ZeneMusicDataResponse>>(APIResponse.Empty)

    var recommendedAlbums by mutableStateOf<APIResponse<ZeneMusicDataResponse>>(APIResponse.Empty)

    fun recommendedPlaylists() = viewModelScope.launch(Dispatchers.IO) {
        val list = listOf(
            "qqywIKDTK6o",
            "9Gduk7Zjem4",
            "0siKyXL_h08",
            "81oCkIJko5s",
            "ffqliB42Nh4",
            "gnN_jiesIkM",
            "DcDbKDAb7go",
            "sEPXrepgujY",
            "KPM_BYl-EaQ",
            "k3smYB3Nfqc"
        )
        zeneAPI.recommendedPlaylists(list.toTypedArray()).onStart {
            recommendedPlaylists = APIResponse.Loading
        }.catch {
            recommendedPlaylists = APIResponse.Error(it)
        }.collectLatest {
            recommendedPlaylists = APIResponse.Success(it)
        }
    }


    fun recommendedAlbums() = viewModelScope.launch(Dispatchers.IO) {
        val list = listOf(
            "qqywIKDTK6o",
            "9Gduk7Zjem4",
            "0siKyXL_h08",
            "81oCkIJko5s",
            "ffqliB42Nh4",
            "gnN_jiesIkM",
            "DcDbKDAb7go",
            "sEPXrepgujY",
            "KPM_BYl-EaQ",
            "k3smYB3Nfqc"
        )
        zeneAPI.recommendedAlbums(list.toTypedArray()).onStart {
            recommendedAlbums = APIResponse.Loading
        }.catch {
            recommendedAlbums = APIResponse.Error(it)
        }.collectLatest {
            recommendedAlbums = APIResponse.Success(it)
        }
    }
}