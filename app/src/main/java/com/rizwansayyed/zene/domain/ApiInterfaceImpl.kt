package com.rizwansayyed.zene.domain

import com.rizwansayyed.zene.presenter.ApiInterfaceImplInterface
import com.rizwansayyed.zene.presenter.converter.SongsAlbumsHeaderConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jsoup.Jsoup
import javax.inject.Inject


class ApiInterfaceImpl @Inject constructor(private val apiInterface: ApiInterface) :
    ApiInterfaceImplInterface {

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

}