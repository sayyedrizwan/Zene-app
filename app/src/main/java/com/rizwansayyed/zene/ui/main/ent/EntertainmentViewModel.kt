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
import com.rizwansayyed.zene.ui.main.ent.utils.LiveReadersCounter
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
}