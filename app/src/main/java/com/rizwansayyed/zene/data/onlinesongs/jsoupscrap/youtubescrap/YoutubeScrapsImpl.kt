package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.youtubescrap

import android.util.Log
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.jsoupResponseData
import com.rizwansayyed.zene.data.utils.YoutubeAPI.generateYTMusicPlaylistURL
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.domain.yt.YoutubePlaylistItemsResponse
import com.rizwansayyed.zene.utils.Utils.forSearchTxt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
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
}