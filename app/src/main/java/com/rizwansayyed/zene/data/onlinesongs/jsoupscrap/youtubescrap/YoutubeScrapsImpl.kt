package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.youtubescrap

import android.util.Log
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.jsoupResponseData
import com.rizwansayyed.zene.data.utils.YoutubeAPI.generateYTMusicPlaylistURL
import com.rizwansayyed.zene.data.utils.YoutubeAPI.ytThisYearVideoSearch
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.domain.yt.YoutubeLatestSearchVideoResponse
import com.rizwansayyed.zene.domain.yt.YoutubePlaylistItemsResponse
import com.rizwansayyed.zene.domain.yt.YoutubeReleaseChannelResponse
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.utils.Utils.forSearchTxt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jsoup.Jsoup
import javax.inject.Inject

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
                    if (list.size <= 18) list.add(forSearchTxt(it))
                }
            }
        }

        emit(list)
    }.flowOn(Dispatchers.IO)


    override suspend fun ytThisYearArtistOfficialVideos(name: String) = flow {
        var videoId = ""
        var response = jsoupResponseData(ytThisYearVideoSearch(name))
        val jsoup = Jsoup.parse(response!!)

        jsoup.select("script").forEach {
            if (it.html().contains("var ytInitialData ="))
                response = it.html()
        }

        val pattern = Regex("ytInitialData\\s*=\\s*([^;]+)")
        val ytInitialDataJson = pattern.find(response ?: "")?.groups?.get(1)?.value
        val vidList = moshi.adapter(YoutubeLatestSearchVideoResponse::class.java)
            .fromJson(ytInitialDataJson ?: "")

        vidList?.contents?.twoColumnSearchResultsRenderer?.primaryContents?.sectionListRenderer?.contents?.first()?.itemSectionRenderer?.contents?.forEachIndexed { index, content ->
            if (index <= 3 && videoId.isEmpty() &&
                content?.videoRenderer?.title?.runs?.first()?.text?.lowercase()
                    ?.contains("official") == true &&
                content.videoRenderer.title.runs.first()?.text?.lowercase()
                    ?.contains("video") == true
            ) videoId = content.videoRenderer.videoId ?: ""

        }

        emit(videoId)
    }.flowOn(Dispatchers.IO)

}