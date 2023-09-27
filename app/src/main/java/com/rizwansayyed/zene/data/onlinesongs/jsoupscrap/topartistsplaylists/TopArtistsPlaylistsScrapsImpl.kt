package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.topartistsplaylists


import com.rizwansayyed.zene.data.onlinesongs.cache.responseCache
import com.rizwansayyed.zene.data.onlinesongs.cache.returnFromCache2Days
import com.rizwansayyed.zene.data.onlinesongs.cache.writeToCacheFile
import com.rizwansayyed.zene.data.onlinesongs.instagram.InstagramInfoService
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.jsoupResponseData
import com.rizwansayyed.zene.data.utils.CacheFiles.topArtistsList
import com.rizwansayyed.zene.data.utils.ScrapURL.TOP_ARTISTS
import com.rizwansayyed.zene.data.utils.SearchEngine.searchEngineDataURL
import com.rizwansayyed.zene.data.utils.config.RemoteConfigInterface
import com.rizwansayyed.zene.data.utils.getInstagramUsername
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicDataCache
import com.rizwansayyed.zene.domain.toTxtCache
import com.rizwansayyed.zene.domain.yt.YoutubeReleaseChannelResponse
import com.rizwansayyed.zene.presenter.util.UiUtils.ContentTypes.THE_ARTISTS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jsoup.Jsoup
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class TopArtistsPlaylistsScrapsImpl @Inject constructor(
    private val instagramInfo: InstagramInfoService,
    private val remoteConfig: RemoteConfigInterface
) : TopArtistsPlaylistsScrapsInterface {

    override suspend fun topArtistsOfWeeks() = flow {
        val list = mutableListOf<MusicData>()
        val cache = responseCache(topArtistsList, MusicDataCache::class.java)

        if (cache != null) {
            if (returnFromCache2Days(cache.cacheTime) && cache.list.isNotEmpty()) {
                emit(cache.list.toMutableList())
                return@flow
            }
        }

        val response = jsoupResponseData(TOP_ARTISTS)
        val jsoup = Jsoup.parse(response!!)

        jsoup.select("div.o-chart-results-list-row-container").forEach {
            var img =
                it.selectFirst("img.c-lazy-image__img.lrv-u-background-color-grey-lightest.lrv-u-width-100p.lrv-u-display-block.lrv-u-height-auto")
                    ?.attr("data-lazy-src")?.replace("-180x180.jpg", "-344x344.jpg") ?: ""
            val name = it.selectFirst("h3#title-of-a-story")?.text()

            if (name != null) {
                if (img.contains("fallback.gif")) {
                    img = ""
                    delay(1.seconds)
                    val (instagram, twitter) = searchEngineData(name).first()
                    if (instagram.isNotEmpty()) {
                        val appId = remoteConfig.instagramAppID()
                        val i = instagramInfo.instagramInfo(appId, getInstagramUsername(instagram))
                        img = i.data?.user?.profile_pic_url_hd ?: ""
                    }
                }
                if (img.isNotEmpty() && name.isNotEmpty())
                    list.add(MusicData(img, name, name, THE_ARTISTS))
            }
        }
        for (i in 0 until 9) list.shuffle()

        list.toTxtCache()?.let { writeToCacheFile(topArtistsList, it) }
        emit(list)
    }.flowOn(Dispatchers.IO)


    override suspend fun searchEngineData(name: String) = flow {
        var instagram = ""
        var twitter = ""
        val response = jsoupResponseData(searchEngineDataURL(name))
        if (response != null) {
            val jsoup = Jsoup.parse(response)
            jsoup.select("li.b_algo").forEach {
                val url = it.selectFirst("a.tilk")?.attr("href")

                if (url?.contains("twitter.com") == true && twitter.isEmpty())
                    twitter = url

                if (url?.contains("instagram.com") == true && instagram.isEmpty())
                    instagram = url
            }

            emit(Pair(instagram, twitter))
        } else
            emit(Pair(instagram, twitter))
    }.flowOn(Dispatchers.IO)


    override suspend fun ytChannelJson(path: String) = flow {
        var response = jsoupResponseData(path)
        val jsoup = Jsoup.parse(response!!)
        jsoup.select("script").forEach {
            if (it.html().contains("var ytInitialData ="))
                response = it.html()
        }

        val pattern = Regex("ytInitialData\\s*=\\s*([^;]+)")
        val ytInitialDataJson = pattern.find(response ?: "")?.groups?.get(1)?.value
        val getReleasePlaylist = moshi.adapter(YoutubeReleaseChannelResponse::class.java)
            .fromJson(ytInitialDataJson ?: "")


        emit(getReleasePlaylist)
    }.flowOn(Dispatchers.IO)


    override suspend fun ytPlaylistItems(path: String) = flow {
        var response = jsoupResponseData(path)
        val jsoup = Jsoup.parse(response!!)

        jsoup.select("script").forEach {
            if (it.html().contains("var ytInitialData ="))
                response = it.html()
        }
        val pattern = Regex("ytInitialData\\s*=\\s*([^;]+)")
        val ytInitialDataJson = pattern.find(response ?: "")?.groups?.get(1)?.value

        val getReleasePlaylist = moshi.adapter(YoutubeReleaseChannelResponse::class.java)
            .fromJson(ytInitialDataJson ?: "")

        emit(getReleasePlaylist)
    }.flowOn(Dispatchers.IO)
}