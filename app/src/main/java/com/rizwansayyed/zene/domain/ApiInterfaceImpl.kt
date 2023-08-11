package com.rizwansayyed.zene.domain

import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import com.rizwansayyed.zene.presenter.converter.SongsAlbumsHeaderConverter
import com.rizwansayyed.zene.presenter.model.IpJSONResponse
import com.rizwansayyed.zene.utils.Utils.URL.IP_JSON
import com.rizwansayyed.zene.utils.Utils.moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import javax.inject.Inject


class ApiInterfaceImpl @Inject constructor(
    private val apiInterface: ApiInterface,
    private val ipApiInterface: IPApiInterface
) : ApiInterfaceImplInterface {

    override suspend fun albumsWithHeaders() = flow {
        emit(apiInterface.albumsWithHeaders())
    }.flowOn(Dispatchers.IO)


    override suspend fun albumsWithYTHeaders(url: String) = flow {
        val document = Jsoup.connect(url).get()
        var scripts = ""
        document.getElementsByTag("script").forEach { s ->
            if (s.html().contains("var ytInitialData = ")) {
                scripts = s.html().replace("var ytInitialData = ", "")
                    .replace("\\s", "").replace(";", "").trim()
            }
        }
        emit(SongsAlbumsHeaderConverter(scripts).get())
    }.flowOn(Dispatchers.IO)


    override suspend fun topArtistOfWeek() = flow {
        emit(apiInterface.topArtistOfWeek())
    }.flowOn(Dispatchers.IO)


    override suspend fun topGlobalSongsThisWeek() = flow {
        emit(apiInterface.topGlobalSongsThisWeek())
    }.flowOn(Dispatchers.IO)

    override suspend fun topCountrySongs() = flow {
        val ip = ipApiInterface.ip()
        dataStoreManager.ipData = flowOf(ip)
        emit(apiInterface.topCountrySongs(ip.country?.lowercase() ?: ""))
    }.flowOn(Dispatchers.IO)


    override suspend fun trendingSongsTop50() = flow {
        val ip = ipApiInterface.ip()
        dataStoreManager.ipData = flowOf(ip)
        emit(apiInterface.trendingSongsTop50(ip.country?.lowercase() ?: ""))
    }.flowOn(Dispatchers.IO)

    override suspend fun trendingSongsTopKPop() = flow {
        emit(apiInterface.trendingSongsTopKPop())
    }.flowOn(Dispatchers.IO)


    override suspend fun trendingSongsTop50KPop() = flow {
        emit(apiInterface.trendingSongsTop50KPop())
    }.flowOn(Dispatchers.IO)


    override suspend fun ipAddressDetails() = flow {
        val ip = ipApiInterface.ip()
        dataStoreManager.ipData = flowOf(ip)
        emit(ip)
    }.flowOn(Dispatchers.IO)

}