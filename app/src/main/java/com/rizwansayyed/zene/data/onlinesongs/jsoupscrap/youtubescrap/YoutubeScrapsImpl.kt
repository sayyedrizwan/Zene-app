package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.youtubescrap


import android.util.Log
import com.rizwansayyed.zene.data.onlinesongs.cache.responseCache
import com.rizwansayyed.zene.data.onlinesongs.cache.returnFromCache2Days
import com.rizwansayyed.zene.data.onlinesongs.cache.writeToCacheFile
import com.rizwansayyed.zene.data.onlinesongs.instagram.InstagramInfoService
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.jsoupResponseData
import com.rizwansayyed.zene.data.utils.CacheFiles.topArtistsList
import com.rizwansayyed.zene.data.utils.ScrapURL.TOP_ARTISTS
import com.rizwansayyed.zene.data.utils.SearchEngine.searchEngineDataURL
import com.rizwansayyed.zene.data.utils.YoutubeAPI.generateYTMusicPlaylistURL
import com.rizwansayyed.zene.data.utils.config.RemoteConfigInterface
import com.rizwansayyed.zene.data.utils.getInstagramUsername
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicDataCache
import com.rizwansayyed.zene.domain.MusicType
import com.rizwansayyed.zene.domain.toTxtCache
import com.rizwansayyed.zene.domain.yt.YoutubeMusicMainSearchResponse
import com.rizwansayyed.zene.domain.yt.YoutubeMusicReleaseResponse
import com.rizwansayyed.zene.domain.yt.YoutubePlaylistItemsResponse
import com.rizwansayyed.zene.domain.yt.YoutubeReleaseChannelResponse
import com.rizwansayyed.zene.domain.yt.YoutubeSearchSuggestionResponse
import com.rizwansayyed.zene.presenter.util.UiUtils.ContentTypes.THE_ARTISTS
import com.rizwansayyed.zene.presenter.util.UiUtils.isImagePresent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jsoup.Jsoup
import java.io.File
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class YoutubeScrapsImpl @Inject constructor() : YoutubeScrapInterface {
    override suspend fun ytReleaseItems(pId: String) = flow {
        var response = jsoupResponseData(generateYTMusicPlaylistURL(pId))
        val jsoup = Jsoup.parse(response!!)


        jsoup.select("script").forEach {
            if (it.html().contains("const initialData = []"))
                response = it.html()
        }

        response = response?.substringAfterLast("params: JSON.parse(")
            ?.substringAfterLast("), data: '")?.substringBeforeLast("'});")
            ?.replace("\\x7b", "{")?.replace("\\x7d", "}")
            ?.replace("\\x22", "\"")?.replace("\\x3d", "=")
            ?.replace("\\x5b", "[")?.replace("\\x5d", "]")
            ?.replace("\\x27", "'")?.replace("\\\\\"", "'")

        val list = mutableListOf<String>()
        val playlist = moshi.adapter(YoutubePlaylistItemsResponse::class.java)
            .fromJson(response ?: "")

        playlist?.contents?.singleColumnBrowseResultsRenderer?.tabs?.first()?.tabRenderer?.content?.sectionListRenderer?.contents?.forEach { content ->
            content?.musicPlaylistShelfRenderer?.contents?.forEach { music ->
                music?.musicResponsiveListItemRenderer?.flexColumns?.first()?.musicResponsiveListItemFlexColumnRenderer?.text?.runs?.first()?.text?.let {
                    if (list.size <= 18) list.add(it.substringBefore("|"))
                }
            }
        }

        emit(list)
    }.flowOn(Dispatchers.IO)
}