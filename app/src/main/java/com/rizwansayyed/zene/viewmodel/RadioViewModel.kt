package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.api.model.MoodLists
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataResponse
import com.rizwansayyed.zene.data.api.zene.ZeneAPIInterface
import com.rizwansayyed.zene.data.api.zene.ZeneRadioAPIInterface
import com.rizwansayyed.zene.data.db.DataStoreManager.pinnedArtistsList
import com.rizwansayyed.zene.data.db.DataStoreManager.userInfoDB
import com.rizwansayyed.zene.utils.Utils.internetIsConnected
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RadioViewModel @Inject constructor(private val zeneAPI: ZeneRadioAPIInterface) : ViewModel() {

    var topRadios by mutableStateOf<APIResponse<List<MoodLists>>>(APIResponse.Empty)
    var radioLanguages by mutableStateOf<APIResponse<ZeneMusicDataResponse>>(APIResponse.Empty)
    var radioCountries by mutableStateOf<APIResponse<ZeneMusicDataResponse>>(APIResponse.Empty)
    var radiosYouMayLike by mutableStateOf<APIResponse<ZeneMusicDataResponse>>(APIResponse.Empty)

    fun init() {
        topRadios()
        radioLanguages()
        radioCountries()
        radiosYouMayLike()
    }

    private fun topRadios() = viewModelScope.launch(Dispatchers.IO) {
        if (!internetIsConnected()) return@launch

        zeneAPI.topRadio().onStart {
            topRadios = APIResponse.Loading
        }.catch {
            topRadios = APIResponse.Error(it)
        }.collectLatest {
            topRadios = APIResponse.Success(it)
        }
    }

    private fun radioLanguages() = viewModelScope.launch(Dispatchers.IO) {
        if (!internetIsConnected()) return@launch

        zeneAPI.radioLanguages().onStart {
            radioLanguages = APIResponse.Loading
        }.catch {
            radioLanguages = APIResponse.Error(it)
        }.collectLatest {
            radioLanguages = APIResponse.Success(it)
        }
    }

    private fun radioCountries() = viewModelScope.launch(Dispatchers.IO) {
        if (!internetIsConnected()) return@launch

        zeneAPI.radioCountries().onStart {
            radioCountries = APIResponse.Loading
        }.catch {
            radioCountries = APIResponse.Error(it)
        }.collectLatest {
            radioCountries = APIResponse.Success(it)
        }
    }

    private fun radiosYouMayLike() = viewModelScope.launch(Dispatchers.IO) {
        if (!internetIsConnected()) return@launch

        zeneAPI.radiosYouMayLike().onStart {
            radiosYouMayLike = APIResponse.Loading
        }.catch {
            radiosYouMayLike = APIResponse.Error(it)
        }.collectLatest {
            radiosYouMayLike = APIResponse.Success(it)
        }
    }

}