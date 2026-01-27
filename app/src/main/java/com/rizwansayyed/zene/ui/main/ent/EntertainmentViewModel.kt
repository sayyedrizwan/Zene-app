package com.rizwansayyed.zene.ui.main.ent

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.cache.CacheHelper
import com.rizwansayyed.zene.data.implementation.ZeneAPIInterface
import com.rizwansayyed.zene.data.model.EntertainmentDiscoverResponse
import com.rizwansayyed.zene.data.model.EventsResponsesItems
import com.rizwansayyed.zene.data.model.StreamingTrendingList
import com.rizwansayyed.zene.data.model.UpcomingMoviesList
import com.rizwansayyed.zene.data.model.WhoDatedWhoData
import com.rizwansayyed.zene.data.model.ZeneMusicDataList
import com.rizwansayyed.zene.ui.main.ent.utils.LiveReadersCounter
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_ENT_ALL_TRAILERS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_ENT_BUZZ_NEWS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_ENT_DATING_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_ENT_DISCOVER_TRENDING_NEWS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_ENT_LIFESTYLES_EVENTS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_ENT_LIFESTYLE_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_ENT_STREAMING_TRENDING_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_ENT_TOP_BOX_OFFICE_MOVIES_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_ENT_UPCOMING_MOVIES_API
import com.rizwansayyed.zene.utils.safeLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject


@HiltViewModel
class EntertainmentViewModel @Inject constructor(private val zeneAPI: ZeneAPIInterface) :
    ViewModel() {

    private val cacheHelper = CacheHelper()

    private val breakingNewHandler = Handler(Looper.getMainLooper())

    var trendingNewsNumber by mutableIntStateOf(0)

    private val updateRunnable = object : Runnable {
        override fun run() {
            trendingNewsNumber = LiveReadersCounter().next()
            breakingNewHandler.postDelayed(this, 2000)
        }
    }

    fun startReadersCounter() {
        breakingNewHandler.post(updateRunnable)
    }

    fun stopReadersCounter() {
        breakingNewHandler.removeCallbacks(updateRunnable)
    }

    var discover by mutableStateOf<ResponseResult<EntertainmentDiscoverResponse>>(ResponseResult.Empty)
    var buzzNews by mutableStateOf<ResponseResult<ZeneMusicDataList>>(ResponseResult.Empty)
    var discoverLifeStyle by mutableStateOf<ResponseResult<ZeneMusicDataList>>(ResponseResult.Empty)
    var dating by mutableStateOf<ResponseResult<List<WhoDatedWhoData>>>(ResponseResult.Empty)
    var trailers by mutableStateOf<ResponseResult<ZeneMusicDataList>>(ResponseResult.Empty)
    var streaming by mutableStateOf<ResponseResult<StreamingTrendingList>>(ResponseResult.Empty)
    var boxOfficeMovie by mutableStateOf<ResponseResult<ZeneMusicDataList>>(ResponseResult.Empty)
    var upcomingMovies by mutableStateOf<ResponseResult<UpcomingMoviesList>>(ResponseResult.Empty)
    var lifeStylesEvents by mutableStateOf<ResponseResult<ZeneMusicDataList>>(ResponseResult.Empty)


    fun entDiscoverNews(expireToken: () -> Unit) = viewModelScope.safeLaunch {
        val data: EntertainmentDiscoverResponse? =
            cacheHelper.get(ZENE_ENT_DISCOVER_TRENDING_NEWS_API)
        if ((data?.news?.size ?: 0) > 0) {
            discover = ResponseResult.Success(data!!)
            return@safeLaunch
        }

        zeneAPI.entDiscoverNews().onStart {
            discover = ResponseResult.Loading
        }.catch {
            discover = ResponseResult.Error(it)
        }.collectLatest {
            if (it.isExpire == true) {
                expireToken()
                return@collectLatest
            }

            it.events?.all?.forEach {
                Log.d("TAG", "entDiscoverNews: ${it.name} == ${it.geo?.lat} == ${it.geo?.lng}")
            }

            it.events?.city?.forEach {
                Log.d("TAG", "entDiscoverNews: ${it.name} == ${it.geo?.lat} == ${it.geo?.lng}")
            }

            it.events?.thisWeek?.forEach {
                Log.d("TAG", "entDiscoverNews: ${it.name} == ${it.geo?.lat} == ${it.geo?.lng}")
            }

            cacheHelper.save(ZENE_ENT_DISCOVER_TRENDING_NEWS_API, it)
            discover = ResponseResult.Success(it)
        }
    }


    fun entBuzzNews() = viewModelScope.safeLaunch {
        val data: ZeneMusicDataList? = cacheHelper.get(ZENE_ENT_BUZZ_NEWS_API)
        if ((data?.size ?: 0) > 0) {
            buzzNews = ResponseResult.Success(data!!)
            return@safeLaunch
        }

        zeneAPI.entBuzzNews().onStart {
            buzzNews = ResponseResult.Loading
        }.catch {
            buzzNews = ResponseResult.Error(it)
        }.collectLatest {
            cacheHelper.save(ZENE_ENT_BUZZ_NEWS_API, it)
            buzzNews = ResponseResult.Success(it)
        }
    }

    fun entDiscoverLifeStyle() = viewModelScope.safeLaunch {
        val data: ZeneMusicDataList? = cacheHelper.get(ZENE_ENT_LIFESTYLE_API)
        if ((data?.size ?: 0) > 0) {
            discoverLifeStyle = ResponseResult.Success(data!!)
            return@safeLaunch
        }

        zeneAPI.entDiscoverLifeStyle().onStart {
            discoverLifeStyle = ResponseResult.Loading
        }.catch {
            discoverLifeStyle = ResponseResult.Error(it)
        }.collectLatest {
            cacheHelper.save(ZENE_ENT_LIFESTYLE_API, it)
            discoverLifeStyle = ResponseResult.Success(it)
        }
    }


    fun entDating() = viewModelScope.safeLaunch {
        val data: List<WhoDatedWhoData>? = cacheHelper.get(ZENE_ENT_DATING_API)
        if ((data?.size ?: 0) > 0) {
            dating = ResponseResult.Success(data!!)
            return@safeLaunch
        }

        zeneAPI.entDating().onStart {
            dating = ResponseResult.Loading
        }.catch {
            dating = ResponseResult.Error(it)
        }.collectLatest {
            cacheHelper.save(ZENE_ENT_DATING_API, it)
            dating = ResponseResult.Success(it ?: emptyList())
        }
    }

    fun entTrailers() = viewModelScope.safeLaunch {
        val data: ZeneMusicDataList? = cacheHelper.get(ZENE_ENT_ALL_TRAILERS_API)
        if ((data?.size ?: 0) > 0) {
            trailers = ResponseResult.Success(data!!)
            return@safeLaunch
        }

        zeneAPI.entAllTrailers().onStart {
            trailers = ResponseResult.Loading
        }.catch {
            trailers = ResponseResult.Error(it)
        }.collectLatest {
            cacheHelper.save(ZENE_ENT_ALL_TRAILERS_API, it)
            trailers = ResponseResult.Success(it)
        }
    }


    fun entStreamingTrending() = viewModelScope.safeLaunch {
        val data: StreamingTrendingList? = cacheHelper.get(ZENE_ENT_STREAMING_TRENDING_API)
        if ((data?.size ?: 0) > 0) {
            streaming = ResponseResult.Success(data!!)
            return@safeLaunch
        }

        zeneAPI.entStreamingTrending().onStart {
            streaming = ResponseResult.Loading
        }.catch {
            streaming = ResponseResult.Error(it)
        }.collectLatest {
            cacheHelper.save(ZENE_ENT_STREAMING_TRENDING_API, it)
            streaming = ResponseResult.Success(it)
        }
    }


    fun entBoxOfficeMovie() = viewModelScope.safeLaunch {
        val data: ZeneMusicDataList? = cacheHelper.get(ZENE_ENT_TOP_BOX_OFFICE_MOVIES_API)
        if ((data?.size ?: 0) > 0) {
            boxOfficeMovie = ResponseResult.Success(data!!)
            return@safeLaunch
        }

        zeneAPI.entBoxOfficeMovie().onStart {
            boxOfficeMovie = ResponseResult.Loading
        }.catch {
            boxOfficeMovie = ResponseResult.Error(it)
        }.collectLatest {
            cacheHelper.save(ZENE_ENT_TOP_BOX_OFFICE_MOVIES_API, it)
            boxOfficeMovie = ResponseResult.Success(it)
        }
    }

    fun entUpcomingMovie() = viewModelScope.safeLaunch {
        val data: UpcomingMoviesList? = cacheHelper.get(ZENE_ENT_UPCOMING_MOVIES_API)
        if ((data?.size ?: 0) > 0) {
            upcomingMovies = ResponseResult.Success(data!!)
            return@safeLaunch
        }

        zeneAPI.entUpcomingMovie().onStart {
            upcomingMovies = ResponseResult.Loading
        }.catch {
            upcomingMovies = ResponseResult.Error(it)
        }.collectLatest {
            cacheHelper.save(ZENE_ENT_UPCOMING_MOVIES_API, it)
            upcomingMovies = ResponseResult.Success(it)
        }
    }

    fun entLifeStyle() = viewModelScope.safeLaunch {
        val data: ZeneMusicDataList? = cacheHelper.get(ZENE_ENT_LIFESTYLES_EVENTS_API)
        if ((data?.size ?: 0) > 0) {
            lifeStylesEvents = ResponseResult.Success(data!!)
            return@safeLaunch
        }

        zeneAPI.entLifestyleEvents().onStart {
            lifeStylesEvents = ResponseResult.Loading
        }.catch {
            lifeStylesEvents = ResponseResult.Error(it)
        }.collectLatest {
            cacheHelper.save(ZENE_ENT_LIFESTYLES_EVENTS_API, it)
            lifeStylesEvents = ResponseResult.Success(it)
        }
    }

    private val _clusterItems = MutableStateFlow<List<EventsResponsesItems>>(emptyList())
    val clusterItems = _clusterItems.asStateFlow()

    fun loadMapsEvents(context: Context, events: List<EventsResponsesItems>) {
//        viewModelScope.launch {
//            val items = events.map { event ->
//                val bitmap = loadBitmapFromUrl(context, event.thumbnail.orEmpty())
//                event.copy(thumbnailBitmap = bitmap)
//            }
//            _clusterItems.value = items
//        }
    }
}