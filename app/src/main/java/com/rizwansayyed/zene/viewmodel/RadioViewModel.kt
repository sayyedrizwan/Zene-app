package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.api.model.MoodLists
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

    fun init() {
        topRadios()
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

}