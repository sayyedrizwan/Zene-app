package com.rizwansayyed.zene.viewmodel

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.cache.CacheHelper
import com.rizwansayyed.zene.data.implementation.ZeneAPIInterface
import com.rizwansayyed.zene.data.model.AIDataResponse
import com.rizwansayyed.zene.data.model.EntertainmentDataResponse
import com.rizwansayyed.zene.data.model.MoviesDataResponse
import com.rizwansayyed.zene.data.model.MusicDataResponse
import com.rizwansayyed.zene.data.model.PodcastDataResponse
import com.rizwansayyed.zene.data.model.RadioDataResponse
import com.rizwansayyed.zene.data.model.SearchDataResponse
import com.rizwansayyed.zene.data.model.SearchPlacesDataResponse
import com.rizwansayyed.zene.data.model.VideoDataResponse
import com.rizwansayyed.zene.data.model.ZeneMusicDataList
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.ui.login.utils.LoginUtils
import com.rizwansayyed.zene.ui.phoneverification.view.TrueCallerUtils
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_AI_MUSIC_LIST_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_NEAR_MUSIC_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_ENTERTAINMENT_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_ENTERTAINMENT_MOVIES_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_MUSIC_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_PODCAST_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_RADIO_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_VIDEOS_API
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val loginUtils: LoginUtils,
    val trueCallerUtils: TrueCallerUtils,
    private val zeneAPI: ZeneAPIInterface
) : ViewModel() {

    private val cacheHelper = CacheHelper()
    var homeRecent by mutableStateOf<ResponseResult<MusicDataResponse>>(ResponseResult.Empty)
    var aiMusicList by mutableStateOf<ResponseResult<AIDataResponse>>(ResponseResult.Empty)
    var homePodcast by mutableStateOf<ResponseResult<PodcastDataResponse>>(ResponseResult.Empty)
    var homeVideos by mutableStateOf<ResponseResult<VideoDataResponse>>(ResponseResult.Empty)
    var homeRadio by mutableStateOf<ResponseResult<RadioDataResponse>>(ResponseResult.Empty)
    var entertainmentData by mutableStateOf<ResponseResult<EntertainmentDataResponse>>(
        ResponseResult.Empty
    )
    var entertainmentMoviesData by mutableStateOf<ResponseResult<MoviesDataResponse>>(
        ResponseResult.Empty
    )

    var nearMusic by mutableStateOf<ResponseResult<ZeneMusicDataList>>(ResponseResult.Empty)
    var searchData by mutableStateOf<ResponseResult<SearchDataResponse>>(ResponseResult.Empty)
    var searchPlaces by mutableStateOf<ResponseResult<List<SearchPlacesDataResponse>>>(
        ResponseResult.Empty
    )

    fun homeRecentData(expireToken: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        val data: MusicDataResponse? = cacheHelper.get(ZENE_RECENT_HOME_MUSIC_API)
        if ((data?.topSongs?.size ?: 0) > 0) {
            homeRecent = ResponseResult.Success(data!!)
            return@launch
        }

        zeneAPI.recentHome().onStart {
            homeRecent = ResponseResult.Loading
        }.catch {
            homeRecent = ResponseResult.Error(it)
        }.collectLatest {
            if (it.isExpire == true) {
                expireToken()
                return@collectLatest
            }

            cacheHelper.save(ZENE_RECENT_HOME_MUSIC_API, it)
            homeRecent = ResponseResult.Success(it)
        }
    }

    fun trendingAIMusic(expireToken: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        val data: AIDataResponse? = cacheHelper.get(ZENE_AI_MUSIC_LIST_API)
        if ((data?.trending?.size ?: 0) > 0) {
            aiMusicList = ResponseResult.Success(data!!)
            return@launch
        }

        zeneAPI.trendingAIMusic().onStart {
            aiMusicList = ResponseResult.Loading
        }.catch {
            aiMusicList = ResponseResult.Error(it)
        }.collectLatest {
            if (it.isExpire == true) {
                expireToken()
                return@collectLatest
            }
            cacheHelper.save(ZENE_AI_MUSIC_LIST_API, it)
            aiMusicList = ResponseResult.Success(it)
        }
    }

    fun homePodcastData() = viewModelScope.launch(Dispatchers.IO) {
        val data: PodcastDataResponse? = cacheHelper.get(ZENE_RECENT_HOME_PODCAST_API)
        if ((data?.latest?.size ?: 0) > 0) {
            homePodcast = ResponseResult.Success(data!!)
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

    fun homeVideosData() = viewModelScope.launch(Dispatchers.IO) {
        val data: VideoDataResponse? = cacheHelper.get(ZENE_RECENT_HOME_VIDEOS_API)
        if ((data?.recommended?.size ?: 0) > 0) {
            homeVideos = ResponseResult.Success(data!!)
            return@launch
        }

        zeneAPI.homeVideos().onStart {
            homeVideos = ResponseResult.Loading
        }.catch {
            homeVideos = ResponseResult.Error(it)
        }.collectLatest {
            cacheHelper.save(ZENE_RECENT_HOME_VIDEOS_API, it)
            homeVideos = ResponseResult.Success(it)
        }
    }

    fun homeRadioData() = viewModelScope.launch(Dispatchers.IO) {
        val data: RadioDataResponse? = cacheHelper.get(ZENE_RECENT_HOME_RADIO_API)
        if ((data?.recent?.size ?: 0) > 0) {
            homeRadio = ResponseResult.Success(data!!)
            return@launch
        }

        zeneAPI.recentRadio().onStart {
            homeRadio = ResponseResult.Loading
        }.catch {
            homeRadio = ResponseResult.Error(it)
        }.collectLatest {
            cacheHelper.save(ZENE_RECENT_HOME_RADIO_API, it)
            homeRadio = ResponseResult.Success(it)
        }
    }


    fun entertainmentNewsData() = viewModelScope.launch(Dispatchers.IO) {
        val data: EntertainmentDataResponse? = cacheHelper.get(ZENE_RECENT_HOME_ENTERTAINMENT_API)
        if ((data?.topNews?.size ?: 0) > 0) {
            entertainmentData = ResponseResult.Success(data!!)
            return@launch
        }

        zeneAPI.entertainmentNews().onStart {
            entertainmentData = ResponseResult.Loading
        }.catch {
            entertainmentData = ResponseResult.Error(it)
        }.collectLatest {
            cacheHelper.save(ZENE_RECENT_HOME_ENTERTAINMENT_API, it)
            entertainmentData = ResponseResult.Success(it)
        }
    }

    fun entertainmentMovies() = viewModelScope.launch(Dispatchers.IO) {
        val data: MoviesDataResponse? = cacheHelper.get(ZENE_RECENT_HOME_ENTERTAINMENT_MOVIES_API)
        if ((data?.trendingMovies?.size ?: 0) > 0) {
            entertainmentMoviesData = ResponseResult.Success(data!!)
            return@launch
        }

        zeneAPI.entertainmentMovies().onStart {
            entertainmentMoviesData = ResponseResult.Loading
        }.catch {
            entertainmentMoviesData = ResponseResult.Error(it)
        }.collectLatest {
            cacheHelper.save(ZENE_RECENT_HOME_ENTERTAINMENT_MOVIES_API, it)
            entertainmentMoviesData = ResponseResult.Success(it)
        }
    }

    fun searchZene(q: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.search(q).onStart {
            searchData = ResponseResult.Loading
        }.catch {
            searchData = ResponseResult.Error(it)
        }.collectLatest {
            searchData = ResponseResult.Success(it)
        }
    }

    @SuppressLint("MissingPermission")
    fun searchPlaces(q: String?) = viewModelScope.launch(Dispatchers.IO) {
        searchPlaces = ResponseResult.Loading
        val l = if (q == null) {
            val l = LocationServices.getFusedLocationProviderClient(context)
            val token = CancellationTokenSource().token
            l.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, token).await()
        } else null

        zeneAPI.searchPlaces(q, l?.longitude, l?.latitude).onStart {
            searchPlaces = ResponseResult.Loading
        }.catch {
            searchPlaces = ResponseResult.Error(it)
        }.collectLatest {
            searchPlaces = ResponseResult.Success(it)
        }
    }


    fun connectNearMusic() = viewModelScope.launch(Dispatchers.IO) {
        val data: ZeneMusicDataList? = cacheHelper.get(ZENE_CONNECT_NEAR_MUSIC_API)
        if ((data?.size ?: 0) > 0) {
            nearMusic = ResponseResult.Success(data!!)
            return@launch
        }

        zeneAPI.connectNearMusic().onStart {
            nearMusic = ResponseResult.Loading
        }.catch {
            nearMusic = ResponseResult.Error(it)
        }.collectLatest {
            cacheHelper.save(ZENE_CONNECT_NEAR_MUSIC_API, it)
            nearMusic = ResponseResult.Success(it)
        }
    }


    fun userInfo() = viewModelScope.launch(Dispatchers.IO) {
        val data = DataStorageManager.userInfo.firstOrNull()
        if (data?.isLoggedIn() == false) return@launch

        zeneAPI.updateUser(data?.email ?: "", data?.name ?: "", data?.photo ?: "").catch {}
            .collectLatest {
                DataStorageManager.userInfo = flowOf(it)
            }
    }

    fun updateConnectInfo(connectStatus: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.updateConnectStatus(connectStatus).catch {}.collectLatest {
            if (it.status == true) {
                val info = DataStorageManager.userInfo.firstOrNull()
                info?.status = connectStatus
                DataStorageManager.userInfo = flowOf(info)
            }
        }
    }
}