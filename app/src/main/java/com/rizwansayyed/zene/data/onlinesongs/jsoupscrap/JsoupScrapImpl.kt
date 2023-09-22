package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap


import com.rizwansayyed.zene.data.onlinesongs.cache.responseCache
import com.rizwansayyed.zene.data.onlinesongs.cache.returnFromCache2Days
import com.rizwansayyed.zene.data.onlinesongs.cache.writeToCacheFile
import com.rizwansayyed.zene.data.utils.CacheFiles
import com.rizwansayyed.zene.data.utils.CacheFiles.topArtistsList
import com.rizwansayyed.zene.data.utils.RadioOnlineAPI
import com.rizwansayyed.zene.data.utils.ScrapURL.TOP_ARTISTS
import com.rizwansayyed.zene.data.utils.SearchEngine.searchEngineDataURL
import com.rizwansayyed.zene.domain.TopArtistsCacheResponse
import com.rizwansayyed.zene.domain.TopArtistsResult
import com.rizwansayyed.zene.domain.toTxtCache
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import org.jsoup.Jsoup
import javax.inject.Inject

class JsoupScrapImpl @Inject constructor() : JsoupScrapInterface {

    override suspend fun topArtistsOfWeeks() = flow {
        val list = mutableListOf<TopArtistsResult>()
        val cache = responseCache(topArtistsList, TopArtistsCacheResponse::class.java)

        if (cache != null) {
            if (returnFromCache2Days(cache.cacheTime) && cache.list.isNotEmpty()) {
                emit(cache.list.toMutableList())
                return@flow
            }
        }

        val response = jsoupResponseData(TOP_ARTISTS)
        val jsoup = Jsoup.parse(response!!)
        jsoup.select("div.o-chart-results-list-row-container").forEach {
            val img =
                it.selectFirst("img.c-lazy-image__img.lrv-u-background-color-grey-lightest.lrv-u-width-100p.lrv-u-display-block.lrv-u-height-auto")
                    ?.attr("data-lazy-src")?.replace("-180x180.jpg", "-344x344.jpg") ?: ""
            val name = it.selectFirst("h3#title-of-a-story")?.text()

            if (name != null) {
                val (instagram, twitter) = searchEngineData(name).first()
                list.add(TopArtistsResult(img, name, instagram, twitter))
            }
        }
        list.toTxtCache()?.let { writeToCacheFile(topArtistsList, it) }
        emit(list)
    }


    override suspend fun searchEngineData(name: String) = flow {
        var instagram = ""
        var twitter = ""
        val response = jsoupResponseData(searchEngineDataURL(name))
        val jsoup = Jsoup.parse(response!!)
        jsoup.select("li.b_algo").forEach {
            val url = it.selectFirst("a.tilk")?.attr("href")
            if (url?.contains("instagram.com") == true && instagram.isEmpty())
                instagram = url

            if (url?.contains("twitter.com") == true && twitter.isEmpty())
                twitter = url
        }

        emit(Pair(instagram, twitter))
    }

}