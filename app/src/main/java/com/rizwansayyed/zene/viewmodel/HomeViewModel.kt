package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.implementation.ZeneAPIInterface
import com.rizwansayyed.zene.data.model.MusicDataResponse
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.ui.login.utils.LoginUtils
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
class HomeViewModel @Inject constructor(
    val loginUtils: LoginUtils, private val zeneAPI: ZeneAPIInterface
) : ViewModel() {

    var homeRecent by mutableStateOf<ResponseResult<MusicDataResponse>>(ResponseResult.Empty)

    fun homeRecentData() = viewModelScope.launch(Dispatchers.IO) {
//        val data = CacheHelper().getData(ZENE_RECENT_HOME_API, MusicDataResponse::class.java)
//        if ((data?.topSongs?.size ?: 0) > 2) {
//            homeRecent = ResponseResult.Success(data!!)
//            return@launch
//        }

        zeneAPI.recentHome().onStart {
            homeRecent = ResponseResult.Loading
        }.catch {
            homeRecent = ResponseResult.Error(it)
        }.collectLatest {
            if (it.isExpire == true) {
                homeRecent = ResponseResult.Loading
                return@collectLatest
            }
//            CacheHelper().saveData(ZENE_RECENT_HOME_API, MusicDataResponse::class.java)
            homeRecent = ResponseResult.Success(it)
        }
    }

    fun userInfo() = viewModelScope.launch(Dispatchers.IO) {
        val data = DataStorageManager.userInfo.firstOrNull()
        if (data?.isLoggedIn() == false) return@launch
        data?.email ?: return@launch
        data.name ?: return@launch
        data.photo ?: return@launch
        zeneAPI.updateUser(data.email, data.name, data.photo).catch {}.collectLatest {
            DataStorageManager.userInfo = flowOf(it)
        }
    }
}