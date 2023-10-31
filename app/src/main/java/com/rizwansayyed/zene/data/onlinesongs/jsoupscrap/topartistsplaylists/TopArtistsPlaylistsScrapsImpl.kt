package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.topartistsplaylists


import android.util.Log
import com.rizwansayyed.zene.data.onlinesongs.cache.responseCache
import com.rizwansayyed.zene.data.onlinesongs.cache.returnFromCache2Days
import com.rizwansayyed.zene.data.onlinesongs.cache.writeToCacheFile
import com.rizwansayyed.zene.data.onlinesongs.instagram.InstagramInfoService
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.jsoupResponseData
import com.rizwansayyed.zene.data.onlinesongs.lastfm.implementation.LastFMImplInterface
import com.rizwansayyed.zene.data.utils.CacheFiles.topArtistsList
import com.rizwansayyed.zene.data.utils.ScrapURL.TOP_ARTISTS
import com.rizwansayyed.zene.data.utils.SearchEngine.searchEngineDataURL
import com.rizwansayyed.zene.data.utils.config.RemoteConfigInterface
import com.rizwansayyed.zene.data.utils.getInstagramUsername
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicDataCache
import com.rizwansayyed.zene.domain.MusicType
import com.rizwansayyed.zene.domain.toTxtCache
import com.rizwansayyed.zene.domain.yt.YoutubeReleaseChannelResponse
import com.rizwansayyed.zene.presenter.util.UiUtils.ContentTypes.THE_ARTISTS
import com.rizwansayyed.zene.presenter.util.UiUtils.isImagePresent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jsoup.Jsoup
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class TopArtistsPlaylistsScrapsImpl @Inject constructor(
    private val lastFM: LastFMImplInterface
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
                    ?.attr("data-lazy-src") ?: ""
            val name = it.selectFirst("h3#title-of-a-story")?.text()

            if (name != null) {
                if (img.contains("fallback.gif") || img.isEmpty()) {
                    img = lastFM.artistsImages(name, 1).first().first()

                }
                if (img.isNotEmpty() && name.isNotEmpty())
                    list.add(MusicData(img, name, name, THE_ARTISTS, MusicType.ARTISTS))
            }
        }

        list.toTxtCache()?.let { writeToCacheFile(topArtistsList, it) }
        emit(list)
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