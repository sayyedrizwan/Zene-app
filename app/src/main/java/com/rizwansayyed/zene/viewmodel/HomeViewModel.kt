package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.cache.CacheHelper
import com.rizwansayyed.zene.data.implementation.ZeneAPIInterface
import com.rizwansayyed.zene.data.model.MusicDataResponse
import com.rizwansayyed.zene.data.model.PodcastDataResponse
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.ui.login.utils.LoginUtils
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_MUSIC_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_PODCAST_API
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

    val cacheHelper = CacheHelper()
    var homeRecent by mutableStateOf<ResponseResult<MusicDataResponse>>(ResponseResult.Empty)
    var homePodcast by mutableStateOf<ResponseResult<PodcastDataResponse>>(ResponseResult.Empty)

    fun homeRecentData() = viewModelScope.launch(Dispatchers.IO) {
        val data: MusicDataResponse? = cacheHelper.get(ZENE_RECENT_HOME_MUSIC_API)
        if (data != null) {
            homeRecent = ResponseResult.Success(data)
            return@launch
        }

        zeneAPI.recentHome().onStart {
            homeRecent = ResponseResult.Loading
        }.catch {
            homeRecent = ResponseResult.Error(it)
        }.collectLatest {
            if (it.isExpire == true) {
                homeRecent = ResponseResult.Loading
                return@collectLatest
            }
            cacheHelper.save(ZENE_RECENT_HOME_MUSIC_API, it)
            homeRecent = ResponseResult.Success(it)
        }
    }

    fun homePodcastData() = viewModelScope.launch(Dispatchers.IO) {
        val data: PodcastDataResponse? = cacheHelper.get(ZENE_RECENT_HOME_PODCAST_API)
        if (data != null) {
            homePodcast = ResponseResult.Success(data)
            return@launch
        }

        zeneAPI.recentPodcast().onStart {
            homePodcast = ResponseResult.Loading
        }.catch {
            homePodcast = ResponseResult.Error(it)
        }.collectLatest {
            cacheHelper.save(ZENE_RECENT_HOME_PODCAST_API, it)
            homePodcast = ResponseResult.Success(it)
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