package com.rizwansayyed.zene.domain

import android.content.Context
import android.util.Log
import com.prof18.rssparser.RssParser
import com.rizwansayyed.zene.BaseApplication.Companion.context
import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import com.rizwansayyed.zene.domain.model.UrlResponse
import com.rizwansayyed.zene.presenter.converter.SongsAlbumsHeaderConverter
import com.rizwansayyed.zene.presenter.model.ArtistsInstagramPostResponse
import com.rizwansayyed.zene.presenter.model.SocialMediaCombine
import com.rizwansayyed.zene.presenter.model.SongLyricsResponse
import com.rizwansayyed.zene.presenter.model.TopArtistsResponseApi
import com.rizwansayyed.zene.ui.artists.artistviewmodel.ArtistsDataJsoup
import com.rizwansayyed.zene.ui.artists.artistviewmodel.model.NewsResponse
import com.rizwansayyed.zene.utils.Utils.URL.readNewsUrl
import com.rizwansayyed.zene.utils.Utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import org.jsoup.Jsoup
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import javax.inject.Inject


class ApiInterfaceImpl @Inject constructor(
    private val apiInterface: ApiInterface,
    private val ipApiInterface: IPApiInterface,
    private val jsoup: ArtistsDataJsoup
) : ApiInterfaceImplInterface {

    override suspend fun albumsWithHeaders() = flow {
        val url = jsoup.albumsWithHeaders().first()

        emit(UrlResponse(url ?: ""))
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

        val url = jsoup.topCountrySongs(ip.country ?: "america").first()
        if (url == null) {
            emit(emptyList())
            return@flow
        }
        emit(apiInterface.topCountrySongs(url))
    }.flowOn(Dispatchers.IO)


    override suspend fun trendingSongsTop50() = flow {
        val ip = ipApiInterface.ip()
        dataStoreManager.ipData = flowOf(ip)
        val url = jsoup.trendingSongsApple(ip.country ?: "america").first()
        if (url == null) {
            emit(emptyList())
            return@flow
        }

        emit(apiInterface.trendingSongsTop50(url))
    }.flowOn(Dispatchers.IO)

    override suspend fun trendingSongsTopKPop() = flow {
        val url = jsoup.trendingSongsTopKPop().first()
        if (url == null) {
            emit(emptyList())
            return@flow
        }

        emit(apiInterface.trendingSongsTopKPop(url))
    }.flowOn(Dispatchers.IO)


    override suspend fun trendingSongsTop50KPop() = flow {
        val url = jsoup.trendingSongsTop50KPop().first()
        if (url == null) {
            emit(emptyList())
            return@flow
        }

        emit(apiInterface.trendingSongsTop50KPop(url))
    }.flowOn(Dispatchers.IO)


    override suspend fun ipAddressDetails() = flow {
        val ip = ipApiInterface.ip()
        dataStoreManager.ipData = flowOf(ip)
        emit(ip)
    }.flowOn(Dispatchers.IO)


    override suspend fun songPlayDetails(name: String) = flow {
        val ip = ipApiInterface.ip()
        dataStoreManager.ipData = flowOf(ip)
        emit(apiInterface.songPlayDetails(ip.query ?: "", name))
    }

    override suspend fun videoPlayDetails(name: String) = flow {
        val ip = ipApiInterface.ip()
        dataStoreManager.ipData = flowOf(ip)
        emit(apiInterface.videoPlayDetails(ip.query ?: "", name))
    }

    override suspend fun searchSongs(q: String) = flow {
        val ip = ipApiInterface.ip()
        dataStoreManager.ipData = flowOf(ip)
        emit(apiInterface.searchSongs(ip.query ?: "", q.replace(" ", "+")))
    }

    override suspend fun artistsInstagramPosts(name: String) = flow {
        val (instagramURL, twitterURL) = jsoup.instagramTwitterAccounts(name).first()

        val instagram = apiInterface.artistsInstagramPosts(instagramURL)
        val twitter = apiInterface.artistsTwitterTweets(twitterURL)

        val response = SocialMediaCombine(instagram, twitter)
        emit(response)
    }

   override suspend fun readNewsList(name: String) = flow {
        val lists = ArrayList<NewsResponse>(100)
        val rssParser = RssParser()
        val rssChannel = rssParser.getRssChannel(readNewsUrl(name))
        rssChannel.items.forEach {
            if (it.title != null)
                lists.add(NewsResponse(it.title ?: "", it.link ?: "", it.pubDate ?: ""))
        }
        emit(lists)
    }


    override suspend fun songLyrics(name: String) = flow {
        val url = jsoup.songLyrics(name).first()
        if (url == null) {
            emit(SongLyricsResponse(""))
            return@flow
        }
        emit(apiInterface.songLyrics(url))
    }


    override suspend fun artistsData(name: String) = flow {
        val url = jsoup.artistData(name).first()

        emit(UrlResponse(url ?: ""))
    }
}