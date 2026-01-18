package com.rizwansayyed.zene.ui.main.ent

import android.os.Handler
import android.os.Looper
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
import com.rizwansayyed.zene.data.model.WhoDatedWhoData
import com.rizwansayyed.zene.data.model.ZeneMusicDataList
import com.rizwansayyed.zene.ui.main.ent.utils.LiveReadersCounter
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_ENT_BUZZ_NEWS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_ENT_DATING_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_ENT_DISCOVER_TRENDING_NEWS_API
import com.rizwansayyed.zene.utils.safeLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
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
    var dating by mutableStateOf<ResponseResult<List<WhoDatedWhoData>>>(ResponseResult.Empty)


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
}