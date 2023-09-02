package com.rizwansayyed.zene.domain


import android.util.Log
import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import com.rizwansayyed.zene.domain.model.UrlResponse
import com.rizwansayyed.zene.presenter.model.SocialMediaCombine
import com.rizwansayyed.zene.presenter.model.SongLyricsResponse
import com.rizwansayyed.zene.presenter.jsoup.ArtistsDataJsoup
import com.rizwansayyed.zene.presenter.jsoup.model.YTTrendingResponse
import com.rizwansayyed.zene.presenter.model.MusicsHeader
import com.rizwansayyed.zene.presenter.model.TopArtistsResponseApi
import com.rizwansayyed.zene.presenter.model.TopArtistsSongs
import com.rizwansayyed.zene.utils.Utils.URL.ytBrowse
import com.rizwansayyed.zene.utils.Utils.USER_AGENT
import com.rizwansayyed.zene.utils.Utils.moshi
import com.rizwansayyed.zene.utils.Utils.saveCaptionsFileTXT
import com.rizwansayyed.zene.utils.Utils.showToast
import com.rizwansayyed.zene.utils.postOkHttps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import org.jsoup.Jsoup
import javax.inject.Inject


class ApiInterfaceImpl @Inject constructor(
    private val apiInterface: ApiInterface,
    private val ipApiInterface: IPApiInterface,
    private val jsoup: ArtistsDataJsoup
) : ApiInterfaceImplInterface {

    suspend fun topTrendingSongsYT(key: String, ip: String) = flow {
        val lists = ArrayList<TopArtistsSongs>(50)

        val clientObject = JSONObject().apply {
            put("remoteHost", ip)
            put("userAgent", USER_AGENT)
            put("clientName", "WEB_REMIX")
            put("clientVersion", "1.20230828.00.00")
        }
        val contextObject = JSONObject().apply {
            put("client", clientObject)
        }
        val jsonObject = JSONObject().apply {
            put("context", contextObject)
            put("browseId", "FEmusic_explore")
        }
        val response = postOkHttps(ytBrowse(key), jsonObject.toString())

        if (response == null) {
            emit(lists)
            return@flow
        }

        val json = moshi.adapter(YTTrendingResponse::class.java).fromJson(response)

        json?.contents?.singleColumnBrowseResultsRenderer?.tabs?.get(0)?.tabRenderer?.content?.sectionListRenderer?.contents?.forEach { music ->
            val text = music?.musicCarouselShelfRenderer?.header
                ?.musicCarouselShelfBasicHeaderRenderer?.title?.runs?.get(0)?.text ?: ""
            if (text == "Trending") {
                music?.musicCarouselShelfRenderer?.contents?.forEach { items ->
                    val title = items?.musicResponsiveListItemRenderer?.flexColumns?.get(0)
                        ?.musicResponsiveListItemFlexColumnRenderer?.text?.runs?.get(0)?.text

                    val thumbnail = items?.musicResponsiveListItemRenderer?.thumbnail
                        ?.musicThumbnailRenderer?.thumbnail?.thumbnails?.get(0)
                        ?.url?.replace("sddefault", "maxresdefault")

                    var artists = ""

                    items?.musicResponsiveListItemRenderer?.flexColumns?.get(1)?.musicResponsiveListItemFlexColumnRenderer?.text?.runs?.forEach { a ->
                        artists += if (a?.text?.contains("•") == true) {
                            ""
                        } else if (a?.text?.contains("views") == true)
                            ""
                        else
                            a?.text
                    }

                    if (title != null) {
                        lists.add(TopArtistsSongs(title, thumbnail ?: "", artists))
                    }
                }
            }
        }

        emit(lists)
    }.flowOn(Dispatchers.IO)


    override suspend fun topArtistOfWeek() = flow {
        emit(apiInterface.topArtistOfWeek())
    }.flowOn(Dispatchers.IO)


    override suspend fun topGlobalSongsThisWeek() = flow {
        emit(apiInterface.topGlobalSongsThisWeek())
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

    override suspend fun songPlayDetails(list: List<TopArtistsSongs>) = flow {
        val lists = ArrayList<MusicsHeader>(15)

        val ip = ipApiInterface.ip()
        dataStoreManager.ipData = flowOf(ip)

        for (s in list) {
            val response = apiInterface.songPlayDetails(ip.query ?: "", "${s.name} - ${s.artist}")
            lists.add(MusicsHeader(response.songName, response.thumbnail, response.artistName))
        }
        emit(lists)
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

    override suspend fun searchArtists(q: String) = flow {
        val ip = ipApiInterface.ip()
        dataStoreManager.ipData = flowOf(ip)
        emit(apiInterface.searchArtists(ip.query ?: "", q.replace(" ", "+")))
    }

    override suspend fun artistsInstagramPosts(name: String) = flow {
        val (instagramURL, twitterURL) = jsoup.instagramTwitterAccounts(name).first()

        val instagram = apiInterface.artistsInstagramPosts(instagramURL)
        val twitter = apiInterface.artistsTwitterTweets(twitterURL)

        val response = SocialMediaCombine(instagram, twitter)
        emit(response)
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